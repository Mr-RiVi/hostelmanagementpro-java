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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFloor extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    private EditText numberText;
    private EditText numberText2;
    private Button save_btn;
    private  String buildingNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_floor);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Add Floor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            buildingNumber = extras.getString("building_number");
        } else  {
            Toast.makeText(this,"Error - Building ID not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        numberText = findViewById(R.id.F_add_no);
        numberText2 = findViewById(R.id.F_add_roomCount);
        save_btn = findViewById(R.id.F_add_btn);

        save_btn.setOnClickListener(v -> {
            String number = numberText.getText().toString();
            String roomCount = numberText2.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("buildings").child(buildingNumber).child("floors").child(number);

            myRef.child("FloorNo").setValue(number);
            myRef.child("RoomCount").setValue(roomCount);

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
                Intent intent =new Intent(AddFloor.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}