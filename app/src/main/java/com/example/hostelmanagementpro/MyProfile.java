package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MyProfile extends AppCompatActivity {
    private Button updateProfileBtn;
    private ImageView btnBack;
    private ImageView customBtn;

    private TextView stuName, stuId, stuAddress, stuContact, stuEmerContact, stuEmail;
    private EditText stuPassword;
    String studentID,credentialsID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Intent intent=getIntent();
        studentID=intent.getStringExtra(StudentHome.EXTRA_USERID);
        credentialsID=intent.getStringExtra(StudentHome.EXTRA_CREDID);

        stuName = findViewById(R.id.stuName);
        stuId = findViewById(R.id.stuId);
        stuAddress = findViewById(R.id.stuAddress);
        stuContact = findViewById(R.id.stuContact);
        stuEmerContact = findViewById(R.id.stuEmerContact);
        stuEmail = findViewById(R.id.stuEmail);
        stuPassword = findViewById(R.id.stuPassword);

        updateProfileBtn = (Button) findViewById(R.id.button);
        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateProfile();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        DatabaseReference readRef = FirebaseDatabase.getInstance().getReference().child("students").child(studentID);
        readRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    stuName.setText(snapshot.child("name").getValue().toString());
                    stuAddress.setText(snapshot.child("address").getValue().toString());
                    stuContact.setText(snapshot.child("studentContactNo").getValue().toString());
                    stuEmerContact.setText(snapshot.child("emergencyContactNo").getValue().toString());
                    stuEmail.setText(snapshot.child("email").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(), "No Details to Display", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference readPwd = FirebaseDatabase.getInstance().getReference().child("credentials").child(credentialsID);
        readPwd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    stuId.setText(dataSnapshot.child("UserId").getValue().toString());
                    stuPassword.setText(dataSnapshot.child("Password").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(), "No Password", Toast.LENGTH_SHORT).show();
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

    public void openUpdateProfile() {
        Intent intent = new Intent(this, UpdateProfile.class);
        startActivity(intent);
    }
    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

//    public void passwordToText() {
//        pwd.setInputType(InputType.TYPE_CLASS_TEXT);
//        customBtn.setBackgroundResource(R.drawable.ic_closed_eye);
//
//    }
//
//    public void textToPassword() {
//        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        customBtn.setBackgroundResource(R.drawable.ic_eye);
//    }

}