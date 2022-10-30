package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FunctionsAdministrator extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_ADMINID="com.example.hostelmanagementpro.EXTRA_ADMINID";

    Toolbar toolbar;
    CardView mngStudentBtn,mngAccommodationBtn,mngPaymentBtn;
    String orgId,adminID;

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
        orgId=intent.getStringExtra(MainActivity.EXTRA_ORGID);
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
            case R.id.mnuMyProfile:
                //go to profile
                return true;
            case R.id.mnuLogout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}