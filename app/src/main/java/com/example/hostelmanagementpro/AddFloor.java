package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFloor extends AppCompatActivity {

    TextView txt;
    private EditText numberText;
    private EditText numberText2;
    private Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_floor);

        //     import tool bar
        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Add Floor");
        numberText = findViewById(R.id.F_add_no);
        numberText2 = findViewById(R.id.F_add_roomCount);
        save_btn = findViewById(R.id.F_add_btn);

        save_btn.setOnClickListener(v -> {
            String number = numberText.getText().toString();
            String roomCount = numberText2.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("floors").child(number);

            myRef.child("FloorNo").setValue(number);
            myRef.child("RoomCount").setValue(roomCount);

            Intent intent=new Intent(AddFloor.this,Floor.class);
            startActivity(intent);
            Toast.makeText(this,"Data inserted",Toast.LENGTH_LONG).show();

        });

    }

    //    Back Button
    public void onclickBbtn(View view){
        Intent in=new Intent(this,Floor.class);
        startActivity(in);
    }
}