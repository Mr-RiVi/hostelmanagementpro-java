package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    EditText username,password;
    TextView rgOrg,rgAdmin;
    Button login;
    DatabaseReference dbCred,dbAdmin;
    String credId,role,userId,orgID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbCred= FirebaseDatabase.getInstance().getReference("credentials");
        dbAdmin=FirebaseDatabase.getInstance().getReference("administrators");

        username=findViewById(R.id.edtTxtUserName);
        password=findViewById(R.id.edtTxtPassword);

        rgOrg=findViewById(R.id.reg_as_org);
        rgAdmin=findViewById(R.id.reg_as_admin);

        login=findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsername(username.getText().toString().trim());
            }
        });

        rgOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,OrganizationRegistration.class);
                startActivity(intent);
            }
        });

        rgAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AdministratorRegistration.class);
                startActivity(intent);
            }
        });
    }

    //checking username availability
    public void checkUsername(String usrEnterUsername){
        dbCred.orderByChild("Username").equalTo(usrEnterUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        credId=ds.getKey().toString();
                        System.out.println("credential id is: "+credId);
                    }
                    checkCredential(username.getText().toString().trim(),password.getText().toString().trim());
                }
                else{
                    username.setError("Username incorrect");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //check user credentials
    public void checkCredential(String usrEnterUsername,String userEnterPassword){
        dbCred.child(credId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dbUsername=snapshot.child("Username").getValue().toString();
                String dbPassword=snapshot.child("Password").getValue().toString();
                System.out.println("db pass is:"+dbPassword);
                System.out.println("db username is:"+dbUsername);
                if (usrEnterUsername.equals(dbUsername)&&userEnterPassword.equals(dbPassword)){
                    role=snapshot.child("Role").getValue().toString();
                    userId=snapshot.child("UserId").getValue().toString();
                    System.out.println("use role is: "+role);
                    synchronized (this){
                        getOrganizationID();
                    }
                    password.setError(null);
                    switch (role){
                        case "admin":
                            openAdminHomeActivity(orgID);
                            break;
                        case "student":
                            //redirect to student home page
                            break;
                    }
                }
                else {
                    password.setError("Password incorrect");
                    System.out.println("credentials incorrect");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //get organization id in that user
    public void getOrganizationID(){
        String arr[]=userId.split("_");
        System.out.println("array 0 th element is:"+arr[0]);
        System.out.println("user id is:"+userId);
        switch (arr[0]){
            case "ADMIN":
                dbAdmin.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orgID=snapshot.child("orgID").getValue().toString();
                        System.out.println("Org id is:"+orgID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "STU":
                //check student collection
                break;
        }
    }

    //admin authenticated
    public void openAdminHomeActivity(String o){
        Intent intent=new Intent(this,FunctionsAdministrator.class);
        System.out.println("organization id is and this is next activity func: "+o);
        intent.putExtra(EXTRA_ORGID,o);
        startActivity(intent);
    }

    //student authenticated
    public void openStudentHomeActivity(){
        Intent intent=new Intent();
        startActivity(intent);
    }
}