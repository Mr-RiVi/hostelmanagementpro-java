package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBuilding extends AppCompatActivity {

    private RadioGroup radioGroup;
    private EditText numberText;
    private EditText numberText2;
    private Button save_btn;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Add Building");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        radioGroup = findViewById(R.id.B_add_radio);
        numberText = findViewById(R.id.B_add_no);
        numberText2 = findViewById(R.id.B_add_name);
        save_btn = findViewById(R.id.B_save_btn);

        save_btn.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String gender;
            if(selectedId == R.id.radioBtnMale) {
                gender = "Male";
            }else{
                gender = "Female";
            }

            String number = numberText.getText().toString();
            String name = numberText2.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("buildings").child(number);

            myRef.child("BuildingNo").setValue(number);
            myRef.child("BuildingName").setValue(name);
            myRef.child("Gender").setValue(gender);

            Intent intent=new Intent(AddBuilding.this,Building.class);
            startActivity(intent);

            Toast.makeText(this,"Data inserted",Toast.LENGTH_LONG).show();

        });

    }

//    Back Button
    public void onclickBbtn(View view){
        Intent in=new Intent(this,Building.class);
        startActivity(in);
    }
////    Btn for Building Page(After added building)
//    public void onclickB(View view){
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
                Intent intent =new Intent(AddBuilding.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}