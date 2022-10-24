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

    Toolbar toolbar;
    CardView mngStudentBtn;
    String orgId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions_administrator);

        mngStudentBtn=findViewById(R.id.btnMngStudents);
        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name
        getSupportActionBar().setTitle(R.string.home);

        Intent intent=getIntent();
        orgId=intent.getStringExtra(MainActivity.EXTRA_ORGID);
        System.out.println("This is admin home page and org id is:"+orgId);

        mngStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity();
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

    public void openNextActivity(){
        Intent intent =new Intent(FunctionsAdministrator.this,FunctionsStuManagement.class);
        intent.putExtra(EXTRA_ORGID,orgId);
        startActivity(intent);
    }

    public void logout(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}