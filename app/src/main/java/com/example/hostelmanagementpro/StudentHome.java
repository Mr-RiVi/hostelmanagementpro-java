package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StudentHome extends AppCompatActivity {

    private CardView resBtn;
    private CardView myAttBtn;
    private CardView scanBtn;
    private CardView payBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        //assigning My Attendance button
        myAttBtn = findViewById(R.id.myAttBtn);
        myAttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStuAtt();
            }
        });

        //assigning QR code scanner button
        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openQrScan();
            }
        });

        //assigning Payments button
        payBtn = findViewById(R.id.payBtn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPayments();
            }
        });

        //assigning My Residence button
        resBtn = findViewById(R.id.resBtn);
        resBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyRes();
            }
        });

        //assigning toolbar
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        //Enabling back button and setting tittle
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Student Home");
    }
        //creating toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
        return true;
    }

    //assigning on click items in menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myProfile:
                Intent intent = new Intent(this, MyProfile.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                Intent intents = new Intent(this, MainActivity.class);
                startActivity(intents);
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //redirecting to My Attendance activity
    public void openStuAtt() {
        Intent intent = new Intent(this, MyAttendance.class);
        startActivity(intent);
    }

    //redirecting to QR code scanner activity
    //change destination.......................................................
    public void openQrScan() {
        Intent in = new Intent(this, MyProfile.class);
        startActivity(in);
    }

    //redirecting to Payments activity
    //change destination.......................................................
    public void openPayments() {
        Intent intents = new Intent(this, MyProfile.class);
        startActivity(intents);
    }

    //redirecting to My Residence activity
    public void openMyRes() {
        Intent i = new Intent(this, MyResidence.class);
        startActivity(i);
    }
}