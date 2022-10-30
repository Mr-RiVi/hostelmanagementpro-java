package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AdministratorRegistration extends AppCompatActivity {

    Toolbar toolbar;
    EditText orgUsername,orgPassword,adminName,adminUsername,adminPassword,contNo,nic,email,address;
    Button crtAcc;
    TextView welcomeMsg;
    ImageButton gen;
    DatabaseReference dbOrg,dbAdmin,dbCredentials;
    String orgID,dbOrgName;
    Admin admin;
    long maxAdminId=0,maxCredID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_registration);

        dbOrg= FirebaseDatabase.getInstance().getReference("organizations");
        dbAdmin= FirebaseDatabase.getInstance().getReference("administrators");

        admin=new Admin();

        toolbar=findViewById(R.id.toolbar);

        orgUsername=findViewById(R.id.edtTxtOrgName);
        orgPassword=findViewById(R.id.edtTxtOrgPass);
        adminName=findViewById(R.id.edtTxtAdminName);
        adminUsername=findViewById(R.id.edtTxtUserName);
        adminPassword=findViewById(R.id.edtTxtPassword);
        contNo=findViewById(R.id.edtTxtPhone);
        nic=findViewById(R.id.edtTxtNIC);
        email=findViewById(R.id.edtTxtEmail);
        address=findViewById(R.id.edtTxtAddress);
        crtAcc=findViewById(R.id.btnCrtAcc);
        gen=findViewById(R.id.imgBtnusNameGen);

        welcomeMsg=findViewById(R.id.txtWelcome);

        //catch toolbar and set it as default actionbar
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.admin_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        String orgUsName=intent.getStringExtra(OrganizationDetails.EXTRA_ORGUSERNAME);
        if (orgUsName==null){
            //check Username validity while user entering username
            orgUsername.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    checkOrgUsernameAvailability();
                    //checkOrganizationCredentials();
                }
            });
        }else{
            orgUsername.setText(orgUsName);
        }

        //check credentials while user entering password
        orgPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkOrgUsernameAvailability();
                checkOrganizationCredentials();
            }
        });

        //check username isEmpty or not
        if(TextUtils.isEmpty(orgUsername.getText().toString())){
            orgUsername.setError("Enter Organization Username");
        }
        else{
            checkOrgUsernameAvailability();
        }

        //insert admin to database by clicking create account button
        crtAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAdminDetails();
            }
        });

        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organizationUsernameGenerator();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        administratorIdGenerator();
        getAndUpdateMaxCredentialID();
    }

    //check organization is available or not, using username
    public void checkOrgUsernameAvailability(){
        dbOrg.orderByChild("orgUsername").equalTo(orgUsername.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        orgID=ds.getKey().toString();
                        System.out.println("orgId accoding to the username: "+orgID);
                    }
                    orgPassword.setEnabled(true);
                    checkOrganizationCredentials();
                }else {
                    orgUsername.setError("Organization invalid");
                    orgPassword.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //check organization credentials
    public void checkOrganizationCredentials(){
        System.out.println("org id is:"+orgID);
        dbOrg.child(orgID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userEnterUsName=orgUsername.getText().toString().trim();
                String userEnterPass=orgPassword.getText().toString().trim();
                String dbUsName=snapshot.child("orgUsername").getValue().toString().trim();
                dbOrgName=snapshot.child("orgName").getValue().toString().trim();
                String dbPass=snapshot.child("orgPassword").getValue().toString().trim();
                if(userEnterUsName.equals(dbUsName)&&userEnterPass.equals(dbPass)){
                    changeWelcomeMsgIsSuccess();
                    enableTextBoxes();
                    orgPassword.setError(null);
                }
                else {
                    changeWelcomeMsgIsFailed();
                    orgPassword.setError("Password incorrect");
                    disableTextBoxes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //insert admin details to db
    public void saveAdminDetails(){
        if(TextUtils.isEmpty(adminName.getText().toString())){
            Toast.makeText(this,"Enter Your Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(adminUsername.getText().toString())){
            Toast.makeText(this,"Enter Username",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(adminPassword.getText().toString())){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(contNo.getText().toString())){
            Toast.makeText(this,"Enter Phone",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(nic.getText().toString())){
            Toast.makeText(this,"Enter NIC",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(this,"Enter address",Toast.LENGTH_SHORT).show();
        }
        else{
            saveCredentialDetails(adminUsername.getText().toString(),adminPassword.getText().toString(),"admin","ADMIN_"+String.valueOf(maxAdminId+1));

            admin.setOrgID(orgID);
            admin.setCredentialID("CRED_"+String.valueOf(maxCredID+1));
            admin.setName(adminName.getText().toString().trim());
            admin.setContactNo(contNo.getText().toString().trim());
            admin.setNic(nic.getText().toString().trim());
            admin.setEmail(email.getText().toString().trim());
            admin.setAddress(address.getText().toString().trim());

            dbAdmin.child("ADMIN_"+String.valueOf(maxAdminId+1)).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        adminName.setText("");
                        adminUsername.setText("");
                        adminPassword.setText("");
                        contNo.setText("");
                        nic.setText("");
                        email.setText("");
                        address.setText("");

                        Toast.makeText(AdministratorRegistration.this,"admin registered",Toast.LENGTH_SHORT).show();
                        openNextActivity();
                    }
                }
            });
        }
    }

    //go to next activity and passing essential data
    public void openNextActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    //admin id(primary key) generator
    public void administratorIdGenerator(){
        dbAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxAdminId=snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //username generator for organization
    public void organizationUsernameGenerator(){
        adminUsername.setText("ADMIN@"+String.format("%07d",maxAdminId+1));
    }

//credential------------------------------
    public void saveCredentialDetails(String Username,String Password,String role,String userid){
        dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");

        HashMap<String,String> a=new HashMap<>();
        a.put("Username",Username);
        a.put("Password",Password);
        a.put("Role",role);
        a.put("UserId",userid);

        getAndUpdateMaxCredentialID();

        dbCredentials.child("CRED_"+String.valueOf(maxCredID+1)).setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    System.out.println("credential insertion successfully");
                }
                else
                    System.out.println("credential insertion failed");
            }
        });
    }

    private void getAndUpdateMaxCredentialID(){
        dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");
        ArrayList<Integer> list=new ArrayList<>();
        dbCredentials.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String orgId=ds.getKey().toString();
                        String arr[]=orgId.split("_");
                        list.add(Integer.parseInt(arr[1]));
                    }
                    maxCredID= Collections.max(list);
                }
                else
                    maxCredID=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //enable Input fields
    public void enableTextBoxes(){
        adminName.setEnabled(true);
        adminUsername.setEnabled(true);
        adminPassword.setEnabled(true);
        contNo.setEnabled(true);
        nic.setEnabled(true);
        email.setEnabled(true);
        address.setEnabled(true);
        crtAcc.setEnabled(true);
        gen.setClickable(true);
    }

    //disable Input fields
    public void disableTextBoxes(){
        adminName.setEnabled(false);
        adminUsername.setEnabled(false);
        adminPassword.setEnabled(false);
        contNo.setEnabled(false);
        nic.setEnabled(false);
        email.setEnabled(false);
        address.setEnabled(false);
        crtAcc.setEnabled(false);
        gen.setClickable(false);
    }

    //changing welcome message to org name
    public void changeWelcomeMsgIsSuccess(){
        welcomeMsg.setText("Welcome to "+dbOrgName);
    }
    //changing welcome message by default
    public void changeWelcomeMsgIsFailed(){
        welcomeMsg.setText("Welcome");
    }
}