package com.example.hostelmanagementpro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostelmanagementpro.model.Payment;
import com.example.hostelmanagementpro.model.UsersRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Add_payment extends AppCompatActivity {

    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    ArrayList<Payment> paymentArrayList;
    UsersRecyclerAdapter adapter;

    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);


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

            EditText txtName = dialog.findViewById(R.id.textName2);
            EditText txtMonth = dialog.findViewById(R.id.txtMonth2);
            EditText txtPrice = dialog.findViewById(R.id.txtPrice2);

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

}