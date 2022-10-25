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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {
    private Button confirmUpdateBtn;

    private TextView stuName, stuId;
    private EditText stuNewContact, stuNewEmerContact, stuNewEmail, stuCurPassword, stuNewPassword, stuConfirmPassword;
    DatabaseReference dbRef;

//    private void clearControls() {
//        stuNewContact.setText("");
//        stuNewEmerContact.setText("");
//        stuNewEmail.setText("");
//        stuCurPassword.setText("");
//        stuNewPassword.setText("");
//        stuConfirmPassword.setText("");
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        stuName = findViewById(R.id.stuName);
        stuId = findViewById(R.id.stuId);

        stuNewContact = findViewById(R.id.stuNewContact);
        stuNewEmerContact = findViewById(R.id.stuNewEmerContact);
        stuNewEmail = findViewById(R.id.stuNewEmail);
        stuCurPassword = findViewById(R.id.stuCurPassword);
        stuNewPassword = findViewById(R.id.stuNewPassword);
        stuConfirmPassword = findViewById(R.id.stuConfirmPassword);

//        std = new Student();

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

        //Get Name
        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("students").child("STU_2");
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    stuName.setText(snapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Get User ID
        DatabaseReference readPwd = FirebaseDatabase.getInstance().getReference().child("credentials").child("CRED_4");
        readPwd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    stuId.setText(dataSnapshot.child("UserId").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Update My Profile
//        DatabaseReference upRef = FirebaseDatabase.getInstance().getReference().child("students");
//        upRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.hasChild("STU_2")) {
//                    try {
//                        std.setStudentContactNo(stuNewContact.getText().toString().trim());
//                        std.setEmergencyContactNo(stuNewEmerContact.getText().toString().trim());
//                        std.setEmail(stuNewEmail.getText().toString().trim());
//
//                        dbRef = FirebaseDatabase.getInstance().getReference().child("students").child("STU_2");
//                        dbRef.setValue(std);
//                        //clearControls();
//                        //Toast.makeText(getApplicationContext(), "Details Updated Successfully", Toast.LENGTH_SHORT).show();
//                    }
//                    catch (NumberFormatException e) {
//                        Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else
//                    Toast.makeText(getApplicationContext(), "Nothing to Update", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //openMyProfileActivity();
//            }
//        });


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
    public void openMyProfileActivity() {
        Intent intent = new Intent(this, MyProfile.class);
        startActivity(intent);
    }
}