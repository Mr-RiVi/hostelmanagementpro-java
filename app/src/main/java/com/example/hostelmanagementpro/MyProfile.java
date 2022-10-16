package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MyProfile extends AppCompatActivity {
    private Button updateProfileBtn;
    private ImageView btnBack;
    private ImageView customBtn;

    TextView txt;
    EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        txt=findViewById(R.id.toolbarTitle);
        pwd = findViewById(R.id.stuPassword);
        txt.setText("My Profile");

        updateProfileBtn = (Button) findViewById(R.id.button);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateProfile();
            }
        });
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });


    }
    public void openUpdateProfile() {
        Intent intent = new Intent(this, UpdateProfile.class);
        startActivity(intent);
    }
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void passwordToText() {
        pwd.setInputType(InputType.TYPE_CLASS_TEXT);
        customBtn.setBackgroundResource(R.drawable.ic_closed_eye);

    }

    public void textToPassword() {
        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        customBtn.setBackgroundResource(R.drawable.ic_eye);
    }

}