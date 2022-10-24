package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ManageAccommodation extends AppCompatActivity {

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accommodation);

        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Manage Accommodation");
    }

//    Btn for Building Page
    public void onclickMA(View view){
        Intent in=new Intent(this,Building.class);
        startActivity(in);
    }
}