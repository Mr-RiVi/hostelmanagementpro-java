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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProfile extends AppCompatActivity {
    private Button confirmUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        confirmUpdateBtn=findViewById(R.id.button);
        confirmUpdateBtn.setText("Confirm Update");

        confirmUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UpdateProfile.this, "My Profile updated", Toast.LENGTH_SHORT).show();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Profile");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu, menu);
        menu.removeItem(R.id.myProfile);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myProfile:
                Intent intent = new Intent(this, MyProfile.class);
                startActivity(intent);
                return true;

            case R.id.logout:
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void openMyProfileActivity() {
        Intent intent = new Intent(this, MyProfile.class);
        startActivity(intent);
    }
}