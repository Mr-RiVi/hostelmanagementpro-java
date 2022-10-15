package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyProfile extends AppCompatActivity {
    private Button updateProfileBtn;

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        txt=findViewById(R.id.toolbarTitle);

        txt.setText("My Profile");

        updateProfileBtn = (Button) findViewById(R.id.button);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateProfile();
            }
        });
    }
    public void openUpdateProfile() {
        Intent intent = new Intent(this, UpdateProfile.class);
        startActivity(intent);
    }
}