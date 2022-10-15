package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class UpdateProfile extends AppCompatActivity {

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Update Profile");
    }
}