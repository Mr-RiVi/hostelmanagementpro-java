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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRoom extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private EditText numberText1;
    private EditText numberText2;
    private EditText numberText3;
    private Button saveBtn;
    private String floorNumber = "";
    private String buildingNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Add Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            floorNumber = extras.getString("floor_number");
            buildingNumber = extras.getString("building_number");
        } else  {
            Toast.makeText(this,"Error - Building ID not found", Toast.LENGTH_LONG).show();
            finish();
        }


        radioGroup1 = findViewById(R.id.R_add_radio1);
        radioGroup2 = findViewById(R.id.R_add_radio2);
        numberText1 = findViewById(R.id.R_add_id);
        numberText2 = findViewById(R.id.R_add_stu_count);
        numberText3 = findViewById(R.id.R_add_bed_count);
        saveBtn = findViewById(R.id.R_add_btn);

        saveBtn.setOnClickListener(v -> {
            int selectedId = radioGroup1.getCheckedRadioButtonId();
            String roomType;
            if (selectedId == R.id.radioBtnAC) {
                roomType = "AC";
            } else {
                roomType = "Non AC";
            }

            int selectedId1 = radioGroup2.getCheckedRadioButtonId();
            String roomStatus;
            if (selectedId1 == R.id.radioBtnAvailable) {
                roomStatus = "Available";
            } else {
                roomStatus = "Booked";
            }

            String roomNo = numberText1.getText().toString();
            String stuCount = numberText2.getText().toString();
            String bedCount = numberText3.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("buildings").child(buildingNumber).child("floors").child(floorNumber).child("rooms").child(roomNo);

            myRef.child("RoomType").setValue(roomType);
            myRef.child("RoomStatus").setValue(roomStatus);
            myRef.child("RoomNo").setValue(roomNo);
            myRef.child("StudentCount").setValue(stuCount);
            myRef.child("BedCount").setValue(bedCount);
            myRef.child("FloorNo").setValue(floorNumber);

            Toast.makeText(this,"Data inserted",Toast.LENGTH_LONG).show();
            finish();
        });
    }

//    //    Back Button
//    public void onclickBbtn(View view){
//        Intent in=new Intent(this,Building.class);
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
                Intent intent =new Intent(AddRoom.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}