package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyResidence extends AppCompatActivity {

    private Button myProfBtn;

    private TextView stuName, stuId, orgID;

    String studentID,credentialsID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_residence);

        Intent intent=getIntent();
        studentID=intent.getStringExtra(StudentHome.EXTRA_USERID);
        credentialsID=intent.getStringExtra(StudentHome.EXTRA_CREDID);

        stuName = findViewById(R.id.stuName);
        stuId = findViewById(R.id.stuId);
        orgID = findViewById(R.id.orgId);

        myProfBtn = (Button) findViewById(R.id.button);
        myProfBtn.setText("These details are uneditable");
        myProfBtn.setClickable(false);
        myProfBtn.setTextColor(getResources().getColor(R.color.blue));
        myProfBtn.setBackgroundColor(getResources().getColor(R.color.white));

        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Residence");

        DatabaseReference getRef = FirebaseDatabase.getInstance().getReference().child("students").child(studentID);
        getRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    stuName.setText(snapshot.child("name").getValue().toString());
                    orgID.setText(snapshot.child("organizationID").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(), "No Name to Display", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference readID = FirebaseDatabase.getInstance().getReference().child("credentials").child(credentialsID);
        readID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    stuId.setText(dataSnapshot.child("UserId").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(), "No ID", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                Intent intents = new Intent(this, MainActivity.class);
                startActivity(intents);
                Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}