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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {
    private Button confirmUpdateBtn;

    private TextView stuName, stuId;
    private EditText stuNewContact, stuNewEmerContact, stuNewEmail, stuCurPassword, stuNewPassword;
    DatabaseReference dbRef, pwdRef;

    Student std;

    String studentID,credentialsID;
    String getPwd;

    private void clearControls() {
        stuNewContact.setText("");
        stuNewEmerContact.setText("");
        stuNewEmail.setText("");
        stuNewPassword.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        //catching studentID, credentials ID from Student MyProfile
        Intent intent=getIntent();
        studentID=intent.getStringExtra(MyProfile.EXTRA_USERID);
        credentialsID=intent.getStringExtra(MyProfile.EXTRA_CREDID);

        stuName = findViewById(R.id.stuName);
        stuId = findViewById(R.id.stuId);

        stuNewContact = findViewById(R.id.stuNewContact);
        stuNewEmerContact = findViewById(R.id.stuNewEmerContact);
        stuNewEmail = findViewById(R.id.stuNewEmail);
        stuCurPassword = findViewById(R.id.stuCurPassword);
        stuNewPassword = findViewById(R.id.stuNewPassword);

        std = new Student();

        confirmUpdateBtn=findViewById(R.id.button);
        confirmUpdateBtn.setText("Confirm Update");

        //Update My Profile details on database

        confirmUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference upRef = FirebaseDatabase.getInstance().getReference("students");
                upRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(studentID)) {
                            try {
                                dbRef = FirebaseDatabase.getInstance().getReference().child("students").child(studentID);
                                dbRef.child("studentContactNo").setValue(stuNewContact.getText().toString().trim());
                                dbRef.child("emergencyContactNo").setValue(stuNewEmerContact.getText().toString().trim());
                                dbRef.child("email").setValue(stuNewEmail.getText().toString().trim());

                                clearControls();
                                Toast.makeText(getApplicationContext(), "Details Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            catch (NumberFormatException e) {
                                Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Nothing to Update", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //password change
                DatabaseReference newPwd = FirebaseDatabase.getInstance().getReference("credentials").child(credentialsID);
                getPwd = stuNewPassword.getText().toString();
                newPwd.child("Password").setValue(getPwd);

            }
        });


        //Assigning toolbar
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        //Changing name on toolbar and enabling back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Profile");

        //Get Student personal details from database
        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("students").child(studentID);
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    stuName.setText(snapshot.child("name").getValue().toString());
                    stuNewContact.setText(snapshot.child("studentContactNo").getValue().toString());
                    stuNewEmerContact.setText(snapshot.child("emergencyContactNo").getValue().toString());
                    stuNewEmail.setText(snapshot.child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get User login credentials from database
        DatabaseReference readPwd = FirebaseDatabase.getInstance().getReference().child("credentials").child(credentialsID);
        readPwd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    stuId.setText(dataSnapshot.child("UserId").getValue().toString());
                    stuCurPassword.setText(dataSnapshot.child("Password").getValue().toString());
                    stuNewPassword.setText(dataSnapshot.child("Password").getValue().toString());
                }
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