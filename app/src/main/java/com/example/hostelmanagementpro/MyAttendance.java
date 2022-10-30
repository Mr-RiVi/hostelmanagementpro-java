package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MyAttendance extends AppCompatActivity {
    public static final String EXTRA_USERID="com.example.hostelmanagementpro.EXTRA_USERID";

    TextView selectDate;
    ImageView dateBtn;
    DatePickerDialog.OnDateSetListener setListener;

    private Button deleteHis;

    private TextView attTime, attType;
    private CardView attCardView;
    private LinearLayout attParent;

    DatabaseReference dbDel;

    String dbDate;

    String studentID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attendance);

        Intent intent=getIntent();
        studentID=intent.getStringExtra(MainActivity.EXTRA_USERID);

        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Attendance");

        selectDate = findViewById(R.id.selectDate);
        dateBtn = findViewById(R.id.dateBtn);

        attTime = findViewById(R.id.attTime);
        attType = findViewById(R.id.attType);
        attCardView = findViewById(R.id.attCardView);
        attParent = findViewById(R.id.attParent);

        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyAttendance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"-"+month+"-"+year;
                        selectDate.setText(date);
                        dbDate = selectDate.toString();//catch date from this variable

                        DatabaseReference dbOldAtt = FirebaseDatabase.getInstance().getReference("attendance").child(studentID).child(date);
                        dbOldAtt.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    attTime.setText(dataSnapshot.child("Time").getValue().toString());
                                    attType.setText(dataSnapshot.child("Type").getValue().toString());

                                }
                                else {
                                    attTime.setText("");
                                    attType.setText("");
                                    Toast.makeText(getApplicationContext(), "No Attendance Activity", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                },year,month,day);
                datePickerDialog.show();

            }

        });


        deleteHis = (Button) findViewById(R.id.delHis);
        deleteHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference refDel = FirebaseDatabase.getInstance().getReference("attendance").child(studentID);
                refDel.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(QRScanner.date)) {
                            dbDel = FirebaseDatabase.getInstance().getReference("attendance").child(studentID).child(QRScanner.date);
                            dbDel.removeValue();
                            attTime.setText("");
                            attType.setText("");
                            Toast.makeText(getApplicationContext(), "Attendance Record Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "No Record to Delete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        DatabaseReference dbAtt = FirebaseDatabase.getInstance().getReference("attendance").child(studentID).child(QRScanner.date);
        dbAtt.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    selectDate.setText(QRScanner.date);
                    attTime.setText(dataSnapshot.child("Time").getValue().toString());
                    attType.setText(dataSnapshot.child("Type").getValue().toString());

                }
                else {
                    selectDate.setText(QRScanner.date);
                    attTime.setText("");
                    attType.setText("");
                    Toast.makeText(getApplicationContext(), "No Attendance Activity", Toast.LENGTH_SHORT).show();
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