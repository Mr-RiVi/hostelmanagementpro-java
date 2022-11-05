package com.example.hostelmanagementpro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostelmanagementpro.model.Payment;
import com.example.hostelmanagementpro.model.UsersRecyclerAdapter;
import com.example.hostelmanagementpro.Add_payment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Add_payment extends AppCompatActivity {

    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    ArrayList<Payment> paymentArrayList;
    UsersRecyclerAdapter adapter;

    Toolbar toolbar;


    Button btnAdd;

    EditText etToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Payments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        etToken = findViewById(R.id.etToken);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println( "Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast

//                        System.out.println(token);
//                        Toast.makeText(Add_payment.this, "Your device registration token is" + token, Toast.LENGTH_SHORT).show();
//
//                        etToken.setText(token);

                    }
                });


        //Objects.requireNonNull(getSupportActionBar()).hide();

        databaseReference = FirebaseDatabase.getInstance().getReference("payments");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        paymentArrayList = new ArrayList<>();

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(view -> {
            ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
            viewDialogAdd.showDialog(Add_payment.this);
        });

        readData();

    }

    private void readData() {
        databaseReference.child("PAYMENT_TYPES").orderByChild("userName").addValueEventListener(new ValueEventListener(){
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                paymentArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Payment payment = dataSnapshot.getValue(Payment.class);
                    paymentArrayList.add(payment);
                }
                adapter = new UsersRecyclerAdapter(Add_payment.this, paymentArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });

    }

    public class ViewDialogAdd {
        @SuppressLint("SetTextI18n")
        public void showDialog(Context context){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alert_add_new_payment);

            EditText txtName = dialog.findViewById(R.id.txtName);
            EditText txtMonth = dialog.findViewById(R.id.txtMonth);
            EditText txtPrice = dialog.findViewById(R.id.txtPrice);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

            buttonAdd.setText("ADD");
            buttonCancel.setOnClickListener(view -> dialog.dismiss());

            buttonAdd.setOnClickListener(view -> {
                String id = "user" + new Date().getTime();
                String Name = txtName.getText().toString();
                String Month = txtMonth.getText().toString();
                String Price = txtPrice.getText().toString();

                if (Name.isEmpty() || Month.isEmpty() || Price.isEmpty()){
                    Toast.makeText(context, "Please Enter all data", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("PAYMENT_TYPES").child(id).setValue(new Payment(id, Name, Month, Price));
                    Toast.makeText(context, "Done !", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
    //actionbar menu implementation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbarmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuMyProfile:
                //go to Admin profile
                return true;
            case R.id.mnuLogout:
                Intent intent =new Intent(Add_payment.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}