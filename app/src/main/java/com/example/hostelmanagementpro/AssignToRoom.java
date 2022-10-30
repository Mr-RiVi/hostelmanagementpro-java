package com.example.hostelmanagementpro;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.BuildingModel;
import com.example.hostelmanagementpro.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AssignToRoom extends AppCompatActivity {

    ArrayList<String> dropdownItems;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter arrayAdapter;
    DatabaseReference dbBuildings,dbStudents;
    long buildingCount;
    String TAG="rivindu";
    String name,stuGender,stuID;
    Intent intent;
    Toolbar toolbar;

    PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_to_room);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.chk_avlblty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbBuildings= FirebaseDatabase.getInstance().getReference("buildings");
        dbStudents= FirebaseDatabase.getInstance().getReference("students");
        dropdownItems=new ArrayList<>();

        autoCompleteTextView=findViewById(R.id.dropdown_menu);
        chart=findViewById(R.id.pieChart);

        intent=getIntent();
        if (intent.getStringExtra(StuRegister.EXTRA_STUDENTID)!=null){
            stuID=intent.getStringExtra(StuRegister.EXTRA_STUDENTID);
            System.out.println("student id is "+stuID);
        }
//        else if(intent.getStringExtra(MainActivity.EXTRA_STUDENTID)!=null){
//            stuID=intent.getStringExtra(MainActivity.EXTRA_STUDENTID);
//        }
        else {
            System.out.println("No Student id retrieve");
        }

        arrayAdapter=new ArrayAdapter(AssignToRoom.this,R.layout.dropdown_list,dropdownItems);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item=dropdownItems.get(i);
                generatePieChart();
                Toast.makeText(AssignToRoom.this, "Item is "+item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        readStudentGender();
        getBuildingCount();
    }

    public void getBuildingCount(){
        dbBuildings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    buildingCount=snapshot.getChildrenCount();
                    System.out.println("building count is "+buildingCount);
                    System.out.println("building count is "+(int)buildingCount);
                    addBuildingToArrList();
                }
                else
                    buildingCount=0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //showing building names in ArrayList according to the student gender
    public void addBuildingToArrList(){
        System.out.println("this is add building function and gender is "+stuGender);
        dbBuildings.orderByChild("Gender").equalTo(stuGender).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String bName=ds.child("BuildingName").getValue().toString();
                        dropdownItems.add(bName);
                    }
                }
                else {
                    autoCompleteTextView.setHint("No building Available");
                    System.out.println("no data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generatePieChart(){
        chart.addPieSlice(new PieModel("labe2",25, Color.parseColor("#FFBB86FC")));
        chart.addPieSlice(new PieModel("labe2",75, Color.parseColor("#FF9800")));
        chart.setInnerValueString("58%");
        chart.startAnimation();
    }

    public void readStudentGender(){
        dbStudents.child("STU_"+stuID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    stuGender=snapshot.child("gender").getValue().toString();
                    System.out.println("student gender is "+stuGender);
                }
                else {
                    System.out.println("no data available on that id");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
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
            case R.id.mnuHome:
                Intent intent1 =new Intent(AssignToRoom.this,FunctionsAdministrator.class);
                startActivity(intent1);
                return true;
            case R.id.mnuMyProfile:
                //go to profile
                return true;
            case R.id.mnuLogout:
                Intent intent =new Intent(AssignToRoom.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}