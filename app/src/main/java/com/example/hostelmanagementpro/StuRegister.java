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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StuRegister extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_STUDENTID="com.example.hostelmanagementpro.EXTRA_STUDENTID";
    public static final String EXTRA_ACTIVITYID="com.example.hostelmanagementpro.EXTRA_ACTIVITYID";

    Toolbar toolbar;
    EditText studentName,studentUsername,studentPassword,emgContactNo,contactNo,email,address;
    RadioGroup radioGroup;
    String gender;
    Button crtAcc,Yes,No;
    ImageButton gen;
    Student stu;
    String orgID;
    DatabaseReference dbStudent,dbCredentials;
    int maxStuId=0,credID=0;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_register);

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
        crtAcc=findViewById(R.id.btnCrtAcc);
        gen=findViewById(R.id.imgBtnGen);


        gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organizationUsernameGenerator();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rdoBtnMale:
                        gender="Male";
                        break;
                    case R.id.rdoBtnFemale:
                        gender="Female";
                        break;
                }
            }
        });
        System.out.println("gender is:"+gender);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.student_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stu=new Student();

        Intent intent=getIntent();
        orgID=intent.getStringExtra(FunctionsAdministrator.EXTRA_ORGID);

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_student_custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Yes = dialog.findViewById(R.id.btn_yes);
        No = dialog.findViewById(R.id.btn_No);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextActivity();
                dialog.dismiss();
            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StuRegister.this,FunctionsStuManagement.class);
                intent.putExtra(EXTRA_ORGID,orgID);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        crtAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  saveStudentDetails();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAndUpdateMaxStudentID();
        getAndUpdateMaxCredentialID();
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
            saveCredentialDetails();

            stu.setCredentialID("CRED_"+String.valueOf(credID+1));//credID+1
            stu.setOrganizationID(orgID); //log unama admin admin inna org ekt adala id eka intent ekakin gnna ona
            stu.setName(studentName.getText().toString().trim());
            stu.setGender(gender);
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
                    }
                }
            });

            dialog.show();
        }
    }

    //credential------------------------------
    public void saveCredentialDetails(){
        //dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");

        String Username=studentUsername.getText().toString();
        String Password=studentPassword.getText().toString();
        String role="student";
        String userid=("STU_"+String.valueOf(maxStuId+1));

        HashMap<String,String> a=new HashMap<>();
        a.put("Username",Username);
        a.put("Password",Password);
        a.put("Role",role);
        a.put("UserId",userid);

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
                    credID=Collections.max(list);
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
                        String arr[]=stuID.split("_");
                        list.add(Integer.parseInt(arr[1]));
                    }
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
            case R.id.mnuHome:
                Intent intent1 =new Intent(StuRegister.this,FunctionsAdministrator.class);
                intent1.putExtra(EXTRA_ORGID,orgID);
                startActivity(intent1);
                return true;
            case R.id.mnuMyProfile:
                //go to profile
                return true;
            case R.id.mnuLogout:
                logoutFunction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //go to next activity and passing essential data
    public void openNextActivity(){
        Intent intent=new Intent(StuRegister.this,AssignToRoom.class); //next activity eke name eka denna ona
        intent.putExtra(EXTRA_ORGID,orgID);
        intent.putExtra(EXTRA_ACTIVITYID,"StuRegister");
        intent.putExtra(EXTRA_STUDENTID,"STU_"+Long.toString(maxStuId));
        startActivity(intent);
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
                Intent intent=new Intent(StuRegister.this,MainActivity.class);
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

    //username generator for organization
    public void organizationUsernameGenerator(){
        studentUsername.setText("STU@"+String.format("%07d",maxStuId+1));
    }
}