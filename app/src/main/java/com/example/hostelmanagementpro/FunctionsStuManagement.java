package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class FunctionsStuManagement extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    String orgId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions_stu_management);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.student_management);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        orgId=intent.getStringExtra(MainActivity.EXTRA_ORGID);
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

    public void onclickStudentRegistration(View view){
        Intent intent=new Intent(this,StuRegister.class);
        intent.putExtra(EXTRA_ORGID,orgId);
        startActivity(intent);
    }

    public void toolbar(String txt){

    }
}