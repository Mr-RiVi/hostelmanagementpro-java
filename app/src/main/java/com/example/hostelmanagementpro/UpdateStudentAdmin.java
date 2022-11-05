package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class UpdateStudentAdmin extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_STUDENTID="com.example.hostelmanagementpro.EXTRA_STUDENTID";
    EditText studentName,studentUsername,studentPassword,emgContactNo,contactNo,email,address;
    RadioGroup radioGroup;
    Button btnUpdate;
    DatabaseReference dbStudent,dbCredentials;
    String orgID,stuID,credentialID,TAG="Kumarasiri";
    ImageButton gen;
    int credID=0,maxStuId=0;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_admin);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Update Student Credentials");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbStudent= FirebaseDatabase.getInstance().getReference("students");
        dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");

        studentName=findViewById(R.id.edtTxtStuName);
        studentUsername=findViewById(R.id.edtTxtUserName);
        studentPassword=findViewById(R.id.edtTxtPassword);
        emgContactNo=findViewById(R.id.edtTxtEmPhone);
        contactNo=findViewById(R.id.edtTxtPhone);
        email=findViewById(R.id.edtTxtEmail);
        address=findViewById(R.id.edtTxtStuAddress);
        radioGroup=findViewById(R.id.rdoGroup);
        gen=findViewById(R.id.imgBtnGen);
        btnUpdate=findViewById(R.id.btnUpdate);

        Intent intent=getIntent();
        if (intent.getStringExtra(StudentProfiles.EXTRA_ACTIVITYID).equals("StudentProfiles")){
            stuID=intent.getStringExtra(StudentProfiles.EXTRA_STUDENTID);
            orgID=intent.getStringExtra(StudentProfiles.EXTRA_ORGID);
            credentialID=intent.getStringExtra(StudentProfiles.EXTRA_CREDID);
            readAndDisplayStudentDetails(stuID);
            readAndDisplayStudentCredentialDetails(credentialID);
            btnUpdate.setVisibility(View.VISIBLE);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStudent();
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
        getAndUpdateMaxStudentID();
        getAndUpdateMaxCredentialID();
    }

    private void readAndDisplayStudentCredentialDetails(String stringExtra) {
        //getting studentID and password from database
        DatabaseReference readPwd = FirebaseDatabase.getInstance().getReference().child("credentials").child(stringExtra);
        readPwd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    studentUsername.setText(dataSnapshot.child("Username").getValue().toString());
                    studentPassword.setText(dataSnapshot.child("Password").getValue().toString());
                    studentUsername.setEnabled(false);
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
        dbCredentials.child(String.valueOf(credentialID)).child("Password").setValue(studentPassword.getText().toString());
        Intent intent=new Intent(UpdateStudentAdmin.this,StudentProfiles.class);
        intent.putExtra(EXTRA_STUDENTID,stuID);
        intent.putExtra(EXTRA_ORGID,orgID);
        startActivity(intent);
    }


    private void readAndDisplayStudentDetails(String studentid) {
        //Getting student details from database
        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("students").child(studentid);
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    studentName.setText(snapshot.child("name").getValue().toString());
                    address.setText(snapshot.child("address").getValue().toString());
                    contactNo.setText(snapshot.child("studentContactNo").getValue().toString());
                    emgContactNo.setText(snapshot.child("emergencyContactNo").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    studentName.setEnabled(false);
                    address.setEnabled(false);
                    contactNo.setEnabled(false);
                    emgContactNo.setEnabled(false);
                    email.setEnabled(false);
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        radioGroup.getChildAt(i).setEnabled(false);
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "No Details to Display", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getAndUpdateMaxCredentialID(){
        ArrayList<Integer> list=new ArrayList<>();
        dbCredentials.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String credenID=ds.getKey();
                        System.out.println("counting credentials id is:"+credenID);
                        String arr[]=credenID.split("_");
                        list.add(Integer.parseInt(arr[1]));
                    }
                    System.out.println("Largest in given cred array is " + Collections.max(list));
                    credID=Collections.max(list);
                    Log.d(TAG, "onDataChange: StuRegister max cred id is :"+credID);
                }
                else
                    credID=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getAndUpdateMaxStudentID(){
        ArrayList<Integer> list=new ArrayList<>();
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String stuID=ds.getKey().toString();
                        System.out.println("counting stu id is:"+stuID);
                        String arr[]=stuID.split("_");
                        list.add(Integer.parseInt(arr[1]));
                    }
                    System.out.println("Largest in given array is " + Collections.max(list));
                    maxStuId=Collections.max(list);
                }
                else
                    maxStuId=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //username generator for organization
    public void organizationUsernameGenerator(){
        studentUsername.setText("STU@"+String.format("%07d",maxStuId+1));
    }

}