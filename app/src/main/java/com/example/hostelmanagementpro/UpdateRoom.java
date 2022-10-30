package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.BufferUnderflowException;
import java.util.HashMap;
import java.util.Map;

public class UpdateRoom extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    private String roomNumber = "";
    private String floorNumber = "";
    private String buildingNumber = "";

    private EditText roomNoET;
    private RadioGroup radioGroupRoomType;
    private RadioGroup radioGroupRoomStatus;
    private EditText studentCount;
    private EditText bedCount;
    private Button updateRoomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_room);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Update Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomNumber = extras.getString("room_number");
            floorNumber = extras.getString("floor_number");
            buildingNumber = extras.getString("building_number");
        } else  {
            Toast.makeText(this,"Error - Room ID not found", Toast.LENGTH_LONG).show();
            finish();
        }

        //ui
        roomNoET = findViewById(R.id.R_update_id);
        radioGroupRoomType = findViewById(R.id.R_update_room_type);
        radioGroupRoomStatus = findViewById(R.id.R_update_room_status);
        studentCount = findViewById(R.id.R_update_stu_count);
        bedCount = findViewById(R.id.R_update_bed_count);
        updateRoomBtn = findViewById(R.id.R_update_btn_room);


        roomNoET.setText("Room ID - "+roomNumber);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("buildings").child(buildingNumber).child("floors").child(floorNumber).child("rooms").child(roomNumber);
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(UpdateRoom.this,"Error getting data", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                DataSnapshot snapshot = task.getResult();
                Log.i("kkkk", snapshot.toString());
                    studentCount.setText(snapshot.child("StudentCount").getValue(String.class));
                    bedCount.setText( snapshot.child("BedCount").getValue(String.class));
                    String roomtype = snapshot.child("RoomType").getValue(String.class);
                    String roomstatus = snapshot.child("RoomStatus").getValue(String.class);
                    if(roomtype.equals("AC")){
                        radioGroupRoomType.check(R.id.radioUpdateBtnAC);
                    }else{
                        radioGroupRoomType.check(R.id.radioUpdateBtnNonAC);
                    }
                    if(roomstatus.equals("Available")){
                        radioGroupRoomStatus.check(R.id.radioUpdateBtnAvailable);
                    }else{
                        radioGroupRoomStatus.check(R.id.radioUpdateBtnBooked);
                    }

            }
        });

        updateRoomBtn.setOnClickListener(v -> {

                myRef.child("StudentCount").setValue(studentCount.getText().toString());
                myRef.child("BedCount").setValue(bedCount.getText().toString());

                int selectedStatus = radioGroupRoomStatus.getCheckedRadioButtonId();
                int selectedType = radioGroupRoomType.getCheckedRadioButtonId();
                if(selectedStatus == R.id.radioUpdateBtnAvailable) {
                    myRef.child("RoomStatus").setValue("Available");
                }else{
                    myRef.child("RoomStatus").setValue("Booked");
                }
                if(selectedType == R.id.radioUpdateBtnAC) {
                    myRef.child("RoomType").setValue("AC");
                }else{
                    myRef.child("RoomType").setValue("Non AC");
                }

                Toast.makeText(this,"Data updated",Toast.LENGTH_LONG).show();
                finish();

        });


    }

//    //    Back Button
//    public void onclickBbtn(View view){
//        Intent in=new Intent(this,Floor.class);
//        startActivity(in);
//    }

    //actionbar menu implementation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbarmenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuMyProfile:
                //go to Admin profile
                return true;
            case R.id.mnuLogout:
                Intent intent =new Intent(UpdateRoom.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}