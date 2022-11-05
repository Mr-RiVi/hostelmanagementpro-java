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
    public static final String EXTRA_USERID="com.example.hostelmanagementpro.EXTRA_USERID";
    public static final String EXTRA_CREDID="com.example.hostelmanagementpro.EXTRA_CREDID";

    private Button updateProfileBtn;

    private TextView stuName, stuId, stuAddress, stuContact, stuEmerContact, stuEmail;
    private EditText stuPassword;
    String studentID,credentialsID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //Assigning toolbar
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        //getting studentID, credentialID from Student Home
        Intent intent=getIntent();
        if(intent.getStringExtra(StudentHome.EXTRA_ACTIVITYID).equals("StudentHome")){
            studentID=intent.getStringExtra(StudentHome.EXTRA_USERID);
            credentialsID=intent.getStringExtra(StudentHome.EXTRA_CREDID);
        }
//        else if (intent.getStringExtra(StudentProfiles.EXTRA_ACTIVITYID).equals("StudentProfiles")){
//            studentID=intent.getStringExtra(StudentProfiles.EXTRA_STUDENTID);
//            credentialsID=intent.getStringExtra(StudentProfiles.EXTRA_CREDID);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        }


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



        //Changing name on toolbar and enabling back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        //Getting student details from database
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

        //getting studentID and password from database
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
        intent.putExtra(EXTRA_USERID,studentID);
        intent.putExtra(EXTRA_CREDID,credentialsID);
        startActivity(intent);
    }


}