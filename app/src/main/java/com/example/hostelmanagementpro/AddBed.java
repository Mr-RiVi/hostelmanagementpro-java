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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBed extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    private EditText numberText1;
    private EditText numberText2;
    private Button saveBtn;
    private String roomNumber = "";
    private String floorNumber = "";
    private String buildingNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bed);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Add Bed");
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

        numberText1 = findViewById(R.id.Bed_add_id);
        numberText2 = findViewById(R.id.Bed_add_stuCount);
        saveBtn = findViewById(R.id.Bed_add_btn);

        saveBtn.setOnClickListener(v -> {
            String bedNo = numberText1.getText().toString();
            String stuId = numberText2.getText().toString();;

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("buildings").child(buildingNumber).child("floors").child(floorNumber).child("rooms").child(roomNumber).child("beds").child(bedNo);

            myRef.child("BedNo").setValue(bedNo);
            myRef.child("StuId").setValue(stuId);
            myRef.child("RoomNo").setValue(roomNumber);

            Toast.makeText(this,"Data inserted",Toast.LENGTH_LONG).show();
            finish();

        });

    }

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
                Intent intent =new Intent(AddBed.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}