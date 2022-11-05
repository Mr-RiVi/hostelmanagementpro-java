package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FunctionsStuManagement extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    String orgId;
    Button regStudent,stuProfile,Yes,No;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions_stu_management);

        regStudent=findViewById(R.id.regStudent);
        stuProfile=findViewById(R.id.stuProfiles);
        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.student_management);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        orgId=intent.getStringExtra(MainActivity.EXTRA_ORGID);



        regStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FunctionsStuManagement.this,StuRegister.class);
                intent.putExtra(EXTRA_ORGID,orgId);
                startActivity(intent);
            }
        });

        stuProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FunctionsStuManagement.this,StudentProfiles.class);
                intent.putExtra(EXTRA_ORGID,orgId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                Intent intent1 =new Intent(FunctionsStuManagement.this,FunctionsAdministrator.class);
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
                Intent intent=new Intent(FunctionsStuManagement.this,MainActivity.class);
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