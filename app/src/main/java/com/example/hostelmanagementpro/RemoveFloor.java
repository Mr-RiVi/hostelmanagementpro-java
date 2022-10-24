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

public class RemoveFloor extends AppCompatActivity {

    Toolbar toolbar;
    EditText numberText;
    Button remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_floor);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Remove Floor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                Intent intent =new Intent(RemoveFloor.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}