package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    TextView txt;
    private RadioGroup radioGroup;
    private EditText numberText;
    private EditText numberText2;
    private Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

//        import tool bar
        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Add Building");
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

}