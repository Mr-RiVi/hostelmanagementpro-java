package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RemoveBuilding extends AppCompatActivity {

    TextView txt;
    EditText numberText;
    Button remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_building);

        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Remove Building");
        numberText = findViewById(R.id.B_no_del);
        remove = findViewById(R.id.removeBuildingBtn);

        remove.setOnClickListener(v -> {
            String number = numberText.getText().toString();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("buildings");
            if(!number.isEmpty()) myRef.child(number).removeValue();

            Intent intent=new Intent(RemoveBuilding.this,Building.class);
            startActivity(intent);

            Toast.makeText(this,"Data removed",Toast.LENGTH_LONG).show();
        });

    }

//    Back Button
    public void onclickBbtn(View view){
        Intent in=new Intent(this,Building.class);
        startActivity(in);
    }

}