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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateBed extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    private String bedNumber = "";
    private String roomNumber = "";
    private String floorNumber = "";
    private String buildingNumber = "";

    private EditText bedNoET;
    private EditText stuId;
    private Button updateBedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bed);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Update Bed");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bedNumber = extras.getString("bed_number");
            roomNumber = extras.getString("room_number");
            floorNumber = extras.getString("floor_number");
            buildingNumber = extras.getString("building_number");
        } else  {
            Toast.makeText(this,"Error - Bed ID not found", Toast.LENGTH_LONG).show();
            finish();
        }

        bedNoET = findViewById(R.id.B_update_bed_no);
        stuId = findViewById(R.id.B_update_student_id);
        updateBedBtn = findViewById(R.id.B_update_btn_bed);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("buildings").child(buildingNumber)
                .child("floors").child(floorNumber).child("rooms").child(roomNumber)
                .child("beds").child(bedNumber);
        myRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(UpdateBed.this,"Error getting data", Toast.LENGTH_LONG).show();
                finish();
            }
            else {
                DataSnapshot snapshot = task.getResult();
                Log.i("kkkk", snapshot.toString());
                bedNoET.setText(snapshot.child("BedNo").getValue(String.class));
                stuId.setText(snapshot.child("StuId").getValue(String.class));
            }
        });

        updateBedBtn.setOnClickListener(v -> {
            myRef.child("StuId").setValue(stuId.getText().toString());

            Toast.makeText(this,"Data updated",Toast.LENGTH_LONG).show();
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
                Intent intent =new Intent(UpdateBed.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}