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
    public static final String EXTRA_USERID="com.example.hostelmanagementpro.EXTRA_USERID";
    EditText username,password;
    TextView rgOrg,rgAdmin;
    Button login;
    DatabaseReference dbCred,dbAdmin,dbStu;
    String credId,role,userId,orgID,TAG="rivindu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbCred= FirebaseDatabase.getInstance().getReference("credentials");
        dbAdmin=FirebaseDatabase.getInstance().getReference("administrators");
        dbStu=FirebaseDatabase.getInstance().getReference("students");

        username=findViewById(R.id.edtTxtUserName);
        password=findViewById(R.id.edtTxtPassword);

        rgOrg=findViewById(R.id.reg_as_org);
        rgAdmin=findViewById(R.id.reg_as_admin);

        login=findViewById(R.id.btnLogin);

        // deal with the asynchronous behavior of Firebase API
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsername(new FirebaseCallback() {
                    @Override
                    public void onCallback(String id) {
                        credId=id;
                        Log.d(TAG, "cred id is: "+credId);
                        checkCredential(new FirebaseCallback() {
                            @Override
                            public void onCallback(String id) {
                                userId=id;
                                Log.d(TAG, "user id is: "+userId);
                                getOrganizationID(new FirebaseCallback() {
                                    @Override
                                    public void onCallback(String id) {
                                        orgID=id;
                                        Log.d(TAG, "org id is: "+orgID);
                                        Log.d(TAG, "user role is: "+role);
                                        switch (role){
                                            case "admin":
                                                openAdminHomeActivity(orgID,userId);
                                                break;
                                            case "student":
                                                openStudentHomeActivity(orgID,userId);
                                                break;
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
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
    public void checkUsername(FirebaseCallback firebaseCallback){
        String usrEnterUsername=username.getText().toString().trim();
        dbCred.orderByChild("Username").equalTo(usrEnterUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        credId=ds.getKey().toString();
                        System.out.println("credential id is: "+credId);
                        firebaseCallback.onCallback(credId);
                    }
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
    public void checkCredential(FirebaseCallback firebaseCallback){
        dbCred.child(credId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usrEnterUsername= username.getText().toString().trim();
                String userEnterPassword  =  password.getText().toString().trim();

                String dbUsername=snapshot.child("Username").getValue().toString();
                String dbPassword=snapshot.child("Password").getValue().toString();
                System.out.println("db pass is:"+dbPassword);
                System.out.println("db username is:"+dbUsername);
                if (usrEnterUsername.equals(dbUsername)&&userEnterPassword.equals(dbPassword)){
                    role=snapshot.child("Role").getValue().toString();
                    userId=snapshot.child("UserId").getValue().toString();
                    System.out.println("use role is: "+role);
                    firebaseCallback.onCallback(userId);
                    password.setError(null);
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
    public void getOrganizationID(FirebaseCallback firebaseCallback){
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
                        firebaseCallback.onCallback(orgID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case "STU":
                System.out.println("This is Student user id is: "+userId);
                dbStu.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orgID=snapshot.child("organizationID").getValue().toString();
                        System.out.println("Student Org id is:"+orgID);
                        firebaseCallback.onCallback(orgID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }

    }

    //admin authenticated
    public void openAdminHomeActivity(String orgid,String userid){
        Intent intent=new Intent(this,FunctionsAdministrator.class);
        intent.putExtra(EXTRA_ORGID,orgid);
        intent.putExtra(EXTRA_USERID,userid);
        startActivity(intent);
    }

    //student authenticated
    public void openStudentHomeActivity(String orgid,String userId){
        Intent intent=new Intent(MainActivity.this,StudentHome.class);
        intent.putExtra(EXTRA_ORGID,orgid);
        intent.putExtra(EXTRA_USERID,userId);
        startActivity(intent);
    }

    private interface FirebaseCallback{
        public void onCallback(String id);
    }
}