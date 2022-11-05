package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateAdminAccount extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_ADMINID="com.example.hostelmanagementpro.EXTRA_ADMINID";
    public static final String EXTRA_CREDENTIALID="com.example.hostelmanagementpro.EXTRA_CREDENTIALID";

    Toolbar toolbar;
    DatabaseReference dbAdmin,dbCredentials;
    EditText adminName,adminUsername,adminPassword,contNo,nic,email,address;
    Button btnUpdate;
    ImageButton gen;
    String adminID,credentialID,orgID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_admin_account);


        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Update Admin");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAdmin= FirebaseDatabase.getInstance().getReference("administrators");
        dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");

        adminName=findViewById(R.id.edtTxtAdminName);
        adminUsername=findViewById(R.id.edtTxtUserName);
        adminPassword=findViewById(R.id.edtTxtPassword);
        contNo=findViewById(R.id.edtTxtPhone);
        nic=findViewById(R.id.edtTxtNIC);
        email=findViewById(R.id.edtTxtEmail);
        address=findViewById(R.id.edtTxtAddress);
        btnUpdate=findViewById(R.id.btnUpdate);
        gen=findViewById(R.id.imgBtnusNameGen);

        Intent intent=getIntent();
        adminID=intent.getStringExtra(FunctionsAdministrator.EXTRA_ADMINID);
        credentialID=intent.getStringExtra(FunctionsAdministrator.EXTRA_CREDENTIALID);
        orgID=intent.getStringExtra(FunctionsAdministrator.EXTRA_ORGID);
        readAndDisplayAdminDetails(adminID);
        readAndDisplayAdminCredentialDetails(credentialID);
        btnUpdate.setVisibility(View.VISIBLE);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStudent();
            }
        });

    }

    private void readAndDisplayAdminDetails(String adID) {
        //Getting student details from database
        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("administrators").child(adID);
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    adminName.setText(snapshot.child("name").getValue().toString());
                    address.setText(snapshot.child("address").getValue().toString());
                    nic.setText(snapshot.child("nic").getValue().toString());
                    contNo.setText(snapshot.child("contactNo").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(), "No Details to Display", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readAndDisplayAdminCredentialDetails(String credid) {
        //getting studentID and password from database
        DatabaseReference readPwd = FirebaseDatabase.getInstance().getReference().child("credentials").child(credid);
        readPwd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    adminUsername.setText(dataSnapshot.child("Username").getValue().toString());
                    adminPassword.setText(dataSnapshot.child("Password").getValue().toString());
                    adminUsername.setEnabled(false);
                    gen.setEnabled(false);
                }
                else
                    Toast.makeText(getApplicationContext(), "No Password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateStudent(){
        dbCredentials.child(String.valueOf(credentialID)).child("Password").setValue(adminPassword.getText().toString());
        dbAdmin.child(String.valueOf(adminID)).child("name").setValue(adminName.getText().toString());
        dbAdmin.child(String.valueOf(adminID)).child("contactNo").setValue(contNo.getText().toString());
        dbAdmin.child(String.valueOf(adminID)).child("nic").setValue(nic.getText().toString());
        dbAdmin.child(String.valueOf(adminID)).child("email").setValue(email.getText().toString());
        dbAdmin.child(String.valueOf(adminID)).child("address").setValue(address.getText().toString());
        Intent intent=new Intent(this,FunctionsAdministrator.class);
        intent.putExtra(EXTRA_ADMINID,adminID);
        intent.putExtra(EXTRA_CREDENTIALID,credentialID);
        intent.putExtra(EXTRA_ORGID,orgID);
        startActivity(intent);
    }
}