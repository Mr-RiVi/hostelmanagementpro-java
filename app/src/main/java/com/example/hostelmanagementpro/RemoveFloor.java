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

public class RemoveFloor extends AppCompatActivity {

    TextView txt;
    EditText numberText;
    Button remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_floor);

        //     import tool bar
        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Remove Floor");
        numberText = findViewById(R.id.F_no_del);
        remove = findViewById(R.id.F_remove_btn);

        remove.setOnClickListener(v -> {
            String number = numberText.getText().toString();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("floors");
            if(!number.isEmpty()) myRef.child(number).removeValue();

            Intent intent=new Intent(RemoveFloor.this,Floor.class);
            startActivity(intent);
            Toast.makeText(this,"Data removed",Toast.LENGTH_LONG).show();
        });

    }

    //    Back Button
    public void onclickBbtn(View view){
        Intent in=new Intent(this,Floor.class);
        startActivity(in);
    }
}