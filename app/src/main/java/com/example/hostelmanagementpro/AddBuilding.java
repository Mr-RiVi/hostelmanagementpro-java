package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBuilding extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    private RadioGroup radioGroup;
    private EditText numberText;
    private EditText numberText2;
    private Button save_btn;
    String orgID;
    Dialog dialog;
    Button Yes,No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Add Building");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        orgID=intent.getStringExtra(Building.EXTRA_ORGID);

        radioGroup = findViewById(R.id.B_add_radio);
        numberText = findViewById(R.id.B_add_no);
        numberText2 = findViewById(R.id.B_add_name);
        save_btn = findViewById(R.id.B_save_btn);

        save_btn.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String gender;
            if(selectedId == R.id.radioBtnMale) {
                gender = "Male";
            }else{
                gender = "Female";
            }

            String number = numberText.getText().toString();
            String name = numberText2.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("buildings").child(number);

            myRef.child("BuildingNo").setValue(number);
            myRef.child("BuildingName").setValue(name);
            myRef.child("Gender").setValue(gender);
            myRef.child("organizationID").setValue(orgID);

            Toast.makeText(this,"Data inserted",Toast.LENGTH_LONG).show();
            finish();
        });
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
                logoutFunction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //logout
    @SuppressLint("ResourceType")
    public void logoutFunction(){
        //Create the Dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialog.show();
        Yes = dialog.findViewById(R.id.btn_yes);
        No = dialog.findViewById(R.id.btn_No);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddBuilding.this,MainActivity.class);
                startActivity(intent);
            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}