package com.example.hostelmanagementpro;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.Organization;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class OrganizationRegistration extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";

    Toolbar toolbar;
    EditText organizationName;
    EditText userName;
    EditText password;
    EditText phone;
    EditText email;
    EditText address;
    Button crtAccount;
    ImageButton gen;
    DatabaseReference dbOrganization;
    Organization org;
    long maxOrgId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_registration);


        toolbar=findViewById(R.id.toolbar);

        organizationName=findViewById(R.id.edtTxtOrgName);
        userName=findViewById(R.id.edtTxtUserName);
        password=findViewById(R.id.edtTxtPassword);
        phone=findViewById(R.id.edtTxtPhone);
        email=findViewById(R.id.edtTxtEmail);
        address=findViewById(R.id.edtTxtAddress);

        crtAccount=findViewById(R.id.btnCrtAcc);
        gen=findViewById(R.id.imgBtnusNameGen);

        dbOrganization= FirebaseDatabase.getInstance().getReference().child("organizations");

        org=new Organization();

        //catch toolbar and set it as default actionbar
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.organization_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //insert organization to database by clicking create account button
        crtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrganizationDetails();
            }
        });

        //generate username by clicking imageButton
        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organizationUsernameGenerator();
            }
        });
    }

    //go to next activity and passing essential data
    public void openNextActivity(){
        Intent intent=new Intent(this,OrganizationDetails.class);
        intent.putExtra(EXTRA_ORGID,"ORG_"+String.valueOf(maxOrgId)); //maxid+1 kalin tbba
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAndUpdateMaxOrganizationId();
    }

    //inserting organization data to the database
    public void saveOrganizationDetails(){
        try {
            if(TextUtils.isEmpty(organizationName.getText().toString())){
                Toast.makeText(this,"Enter Organization Name",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(userName.getText().toString())){
                Toast.makeText(this,"Enter Username",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(password.getText().toString())){
                Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(phone.getText().toString())){
                Toast.makeText(this,"Enter Phone",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(email.getText().toString())){
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(address.getText().toString())){
                Toast.makeText(this,"Enter address",Toast.LENGTH_SHORT).show();
            }
            else{
                org.setOrgName(organizationName.getText().toString().trim());
                org.setOrgUsername(userName.getText().toString().trim());
                org.setOrgPassword(password.getText().toString().trim());
                org.setOrgPhone(phone.getText().toString().trim());
                org.setEmail(email.getText().toString().trim());
                org.setAddress(address.getText().toString().trim());

                dbOrganization.child("ORG_"+String.valueOf(maxOrgId+1)).setValue(org).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            organizationName.setText("");
                            userName.setText("");
                            password.setText("");
                            phone.setText("");
                            email.setText("");
                            address.setText("");

                            Toast.makeText(OrganizationRegistration.this,"Organization registered",Toast.LENGTH_SHORT).show();
                            openNextActivity();
                        }
                        else
                            Toast.makeText(OrganizationRegistration.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (NumberFormatException e){
            Toast.makeText(this,"Registration Failed",Toast.LENGTH_LONG).show();
        }
    }

    //organization id(primary key) generator
    public void getAndUpdateMaxOrganizationId(){
        ArrayList<Integer> list=new ArrayList<>();
        dbOrganization.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String orgId=ds.getKey().toString();
                        String arr[]=orgId.split("_");
                        list.add(Integer.parseInt(arr[1]));
                    }
                    maxOrgId=Collections.max(list);
                }
                else
                    maxOrgId=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //username generator for organization
    public void organizationUsernameGenerator(){
        userName.setText("ORG@"+String.format("%07d",maxOrgId+1));
    }
}