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
    public static final String EXTRA_STUDENTID="com.example.hostelmanagementpro.EXTRA_STUDENTID";

    Toolbar toolbar;
    EditText studentName,studentUsername,studentPassword,emgContactNo,contactNo,email,address;
    RadioGroup radioGroup;
    String gender;
    Button crtAcc;
    ImageButton gen;
    Student stu;
    String orgID;
    String TAG="rivindu";
    DatabaseReference dbStudent,dbCredentials;
    long maxStuId=0,credID=0;
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
                        gender="male";
                        break;
                    case R.id.rdoBtnFemale:
                        gender="female";
                        break;
                }
            }
        });
        //get gender in the radio button
        //int radioId=radioGroup.getCheckedRadioButtonId();
        //gender=findViewById(radioId);
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
        Log.d(TAG, "Admin org id is : "+orgID);

        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_student_custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Yes = dialog.findViewById(R.id.btn_yes);
        Button NotNow = dialog.findViewById(R.id.btn_notNow);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextActivity();
                dialog.dismiss();
            }
        });

        NotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StuRegister.this,FunctionsStuManagement.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        crtAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  saveStudentDetails();
                  dialog.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAndUpdateStudentNodeCount();
        getAndUpdateCredentialNodeCount();
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

            //getAndUpdateStudentNodeCount();
            System.out.println("student count is:"+maxStuId);
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

                        //Toast.makeText(StuRegister.this,"Student registered",Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

        //getAndUpdateCredentialNodeCount();
        System.out.println("cred count is:"+credID);

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
        dbCredentials.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    credID=snapshot.getChildrenCount();
                    System.out.println("cred count is"+credID);
                }
                else
                    credID=0;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAndUpdateStudentNodeCount(){
        dbStudent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxStuId=snapshot.getChildrenCount();
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
        Intent intent=new Intent(StuRegister.this,AssignToRoom.class); //next activity eke name eka denna ona
        intent.putExtra(EXTRA_ORGID,orgID);
        System.out.println("this is StuRegister and maxStuId is "+maxStuId);
        intent.putExtra(EXTRA_STUDENTID,Long.toString(maxStuId));
        startActivity(intent);
    }

    private interface FirebaseCallbackGetCount{
        public void onCallback(long count);
    }

    private interface FirebaseCallbackSaveDetails{
        public void onCallback();
    }

    //username generator for organization
    public void organizationUsernameGenerator(){
        studentUsername.setText("STU@"+String.format("%07d",maxStuId+1));
    }
}