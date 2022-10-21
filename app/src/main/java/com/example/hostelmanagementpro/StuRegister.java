package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StuRegister extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";

    Toolbar toolbar;
    EditText studentName,studentUsername,studentPassword,emgContactNo,contactNo,email,address;
    RadioGroup radioGroup;
    RadioButton gender;
    Student stu;
    String orgID;
    DatabaseReference dbStudent,dbCredentials;
    long maxStuId,credID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_register);

        dbStudent= FirebaseDatabase.getInstance().getReference("students");

        studentName=findViewById(R.id.edtTxtStuName);
        studentUsername=findViewById(R.id.edtTxtUserName);
        studentPassword=findViewById(R.id.edtTxtPassword);
        emgContactNo=findViewById(R.id.edtTxtEmPhone);
        contactNo=findViewById(R.id.edtTxtPhone);
        email=findViewById(R.id.edtTxtEmail);
        address=findViewById(R.id.edtTxtStuAddress);
        //get gender in the radio button
        int radioId=radioGroup.getCheckedRadioButtonId();
        gender=findViewById(radioId);
        //gender.getText().toString();

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.student_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stu=new Student();

        Intent intent=getIntent();
        orgID=intent.getStringExtra(FunctionsAdministrator.EXTRA_ORGID);
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
                //go to profile
                return true;
            case R.id.mnuLogout:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //go to next activity and passing essential data
    public void openNextActivity(){
        Intent intent=new Intent(); //next activity eke name eka denna ona
        intent.putExtra(EXTRA_ORGID,orgID);
        startActivity(intent);
    }

    //insert student details to db
    public void saveStudentDetails(){
        if(TextUtils.isEmpty(studentName.getText().toString())){
            Toast.makeText(this,"Enter Student Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(studentUsername.getText().toString())){
            Toast.makeText(this,"Enter Username",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(studentPassword.getText().toString())){
            Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(gender.getText().toString())){
            Toast.makeText(this,"Select Gender",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(emgContactNo.getText().toString())){
            Toast.makeText(this,"Enter Emergency contact number",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(contactNo.getText().toString())){
            Toast.makeText(this,"Enter Student contact number",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email.getText().toString())){
            Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(address.getText().toString())){
            Toast.makeText(this,"Enter address",Toast.LENGTH_SHORT).show();
        }
        else{
            saveCredentialDetails(studentUsername.getText().toString(),studentPassword.getText().toString(),"student","STU_"+String.valueOf(maxStuId+1));

            stu.setCredentialID("CRED_"+String.valueOf(credID+1));
            stu.setOrganizationID(orgID); //log unama admin admin inna org ekt adala id eka intent ekakin gnna ona
            stu.setName(studentName.getText().toString().trim());
            stu.setGender(gender.getText().toString().trim());
            stu.setEmergencyContactNo(emgContactNo.getText().toString().trim());
            stu.setStudentContactNo(contactNo.getText().toString().trim());
            stu.setEmail(email.getText().toString().trim());
            stu.setAddress(address.getText().toString().trim());

            dbStudent.child("STU_"+String.valueOf(maxStuId+1)).setValue(stu).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        studentName.setText("");
                        studentUsername.setText("");
                        studentPassword.setText("");
                        emgContactNo.setText("");
                        contactNo.setText("");
                        email.setText("");
                        address.setText("");

                        Toast.makeText(StuRegister.this,"Student registered",Toast.LENGTH_SHORT).show();
                        openNextActivity();
                    }
                }
            });
        }
    }

    //credential------------------------------
    public void saveCredentialDetails(String Username,String Password,String role,String userid){
        dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");

        HashMap<String,String> a=new HashMap<>();
        a.put("Username",Username);
        a.put("Password",Password);
        a.put("Role",role);
        a.put("UserId",userid);

        getAndUpdateCredentialNodeCount();

        dbCredentials.child("CRED_"+String.valueOf(credID+1)).setValue(a).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void getAndUpdateCredentialNodeCount(){
        dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");
        dbCredentials.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    credID=snapshot.getChildrenCount();
                }
                else
                    credID=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}