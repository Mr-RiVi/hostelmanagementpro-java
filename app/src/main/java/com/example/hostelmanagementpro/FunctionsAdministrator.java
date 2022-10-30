package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FunctionsAdministrator extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_ADMINID="com.example.hostelmanagementpro.EXTRA_ADMINID";

    Toolbar toolbar;
    CardView mngStudentBtn,mngAccommodationBtn,mngPaymentBtn;
    String orgId,adminID;
    Dialog dialog;
    Button Yes,No;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions_administrator);

        mngStudentBtn=findViewById(R.id.btnMngStudents);
        mngAccommodationBtn=findViewById(R.id.btnMngAccommodation);
        mngPaymentBtn=findViewById(R.id.btn_payment);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name
        getSupportActionBar().setTitle(R.string.home);

        Intent intent=getIntent();
        if (intent.getStringExtra(MainActivity.EXTRA_ORGID)!=null){
            orgId=intent.getStringExtra(MainActivity.EXTRA_ORGID);
        }else if(intent.getStringExtra(StudentProfiles.EXTRA_ORGID)!=null){
            orgId=intent.getStringExtra(StudentProfiles.EXTRA_ORGID);
        }
        else{
            System.out.println("Admin Home page org id is null");
        }
        adminID=intent.getStringExtra(MainActivity.EXTRA_USERID);
        System.out.println("This is admin home page and org id is:"+orgId);

        mngStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(FunctionsAdministrator.this,FunctionsStuManagement.class);
                intent.putExtra(EXTRA_ORGID,orgId);
                intent.putExtra(EXTRA_ADMINID,adminID);
                startActivity(intent);
            }
        });

        mngAccommodationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(FunctionsAdministrator.this,ManageAccommodation.class);
                intent.putExtra(EXTRA_ORGID,orgId);
                intent.putExtra(EXTRA_ADMINID,adminID);
                startActivity(intent);
            }
        });

        mngPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FunctionsAdministrator.this,Add_payment.class);
                startActivity(intent);
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
                //hide this menu item
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
                Intent intent=new Intent(FunctionsAdministrator.this,MainActivity.class);
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