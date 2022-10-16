package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProfile extends AppCompatActivity {
    private ImageView btnBack;
    private Button confirmUpdateBtn;

    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Update Profile");

        confirmUpdateBtn=findViewById(R.id.button);
        confirmUpdateBtn.setText("Confirm Update");

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

        confirmUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UpdateProfile.this, "My Profile updated", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void openMainActivity() {
        Intent intent = new Intent(this, MyProfile.class);
        startActivity(intent);
    }
}