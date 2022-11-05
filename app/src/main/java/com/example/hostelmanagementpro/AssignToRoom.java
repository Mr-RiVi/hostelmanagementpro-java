package com.example.hostelmanagementpro;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.BuildingModel;
import com.example.hostelmanagementpro.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_STUDENTID="com.example.hostelmanagementpro.EXTRA_STUDENTID";

    ArrayList<String> dropdownItems;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter arrayAdapter;
    DatabaseReference dbBuildings,dbStudents,bedIDRef,dbRoomDetails;
    long buildingCount;
    String TAG="Milendra";
    String name,item,stuGender,stuID,orgID,buildingID;
    Intent intent;
    Toolbar toolbar;
    Button btnAssignToRoom,Yes,No,btn_chkacc,btn_tryagin;
    Dialog dialog;
    PieChart chart;
    int totBedCount=0,avBedCount=0;
    TextView txtTotalBeds,txtAvailableBeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_to_room);

        btnAssignToRoom=findViewById(R.id.btnAssignToRoom);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.chk_avlblty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTotalBeds=findViewById(R.id.txtTotalBeds);
        txtAvailableBeds=findViewById(R.id.txtAvailableBeds);

        dbBuildings= FirebaseDatabase.getInstance().getReference("buildings");
        dbStudents= FirebaseDatabase.getInstance().getReference("students");
        dbRoomDetails= FirebaseDatabase.getInstance().getReference("studentRoom");
        dropdownItems=new ArrayList<>();

        autoCompleteTextView=findViewById(R.id.dropdown_menu);
        chart=findViewById(R.id.pieChart);

        intent=getIntent();
        if (intent.getStringExtra(StudentRoomDetails.EXTRA_ACTIVITYID).equals("StudentRoomDetails")){
            Log.d(TAG, "onCreate: activity recognize");
            stuID=intent.getStringExtra(StudentRoomDetails.EXTRA_STUDENTID);
            orgID=intent.getStringExtra(StudentRoomDetails.EXTRA_ORGID);
            Log.d(TAG, "onCreate: Student id is"+stuID);
        }
        else if (intent.getStringExtra(StuRegister.EXTRA_ACTIVITYID).equals("StuRegister")){
            Log.d(TAG, "onCreate stuReg: activity recognize");
            stuID=intent.getStringExtra(StuRegister.EXTRA_STUDENTID);
            orgID=intent.getStringExtra(StuRegister.EXTRA_ORGID);
            Log.d(TAG, "onCreate: Student id is"+stuID);
        }
        else {
            System.out.println("No Student id retrieve");
        }

        arrayAdapter=new ArrayAdapter(AssignToRoom.this,R.layout.dropdown_list,dropdownItems);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=dropdownItems.get(i);
                Toast.makeText(AssignToRoom.this, "Item is "+item, Toast.LENGTH_SHORT).show();
                getAvailableStudentSeats(item);
                getTotalBedCount(item);
            }
        });

        btnAssignToRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignStudent(stuID);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: calling on start");
        readStudentGender(new FirebaseCallback() {
            @Override
            public void onCallback(String stuGender) {
                addBuildingToArrList(stuGender);
            }
            @Override
            public void onCallbackGetAvBedCount(int avCount){

            }
        });
    }

    public void addBuildingToArrList(String stuGen){
        System.out.println("this is add building function and gender is "+stuGender+"\n"+"orgID is :"+orgID);
        dbBuildings.orderByChild("organizationID").equalTo(orgID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        String bName=ds.child("BuildingName").getValue().toString();
                        if (ds.child("Gender").getValue().toString().equals(stuGen)){
                            dropdownItems.add(bName);
                        }
                        else{
                            Log.d(TAG, "onDataChange: Gender not matching");
                        }
                    }
                    for (String s:dropdownItems){
                        Log.d(TAG, "dropdown menu items :"+s);
                    }
                }
                else {
                    autoCompleteTextView.setHint("No building Available");
                    Log.d(TAG, "onDataChange: No building found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //showing building names in ArrayList according to the student gender
    public void getAvailableStudentSeats(String buildingName){
        System.out.println("this is add building function and gender is "+stuGender+"\n"+"orgID is :"+orgID);
        dbBuildings.orderByChild("organizationID").equalTo(orgID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        if (ds.child("Gender").getValue().toString().equals(stuGender)&&ds.child("BuildingName").getValue().toString().equals(buildingName)){
                            for (DataSnapshot dsFloors:ds.child("floors").getChildren()){
                                for (DataSnapshot dsRooms:dsFloors.child("rooms").getChildren()){
                                    for (DataSnapshot dsBeds:dsRooms.child("beds").getChildren()){
                                        if (dsBeds.child("StuId").getValue().toString().equals("")){
                                            Log.d(TAG, "onDataChange AssignToRoom: iterate1");
                                            bedIDRef=dsBeds.getRef();
                                            Log.d(TAG, "onDataChange AssignToRoom: Up to bed ref is :"+bedIDRef);
                                        }
                                        if (bedIDRef!=null){
                                            break;
                                        }
                                        else{
                                            Log.d(TAG, "onDataChange: Assign not found iterate");
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Log.d(TAG, "onDataChange: Gender and building name not matching");
                        }
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

    public void assignStudent(String stuId){
        try {
            bedIDRef.child("StuId").setValue(stuId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG, "onComplete: assignStudent "+bedIDRef);
                        String rmDetails[]=bedIDRef.orderByChild("StuId").equalTo(stuId).toString().split("/");
                        for (int i=0;i<rmDetails.length;i++){
                            Log.d(TAG, "array element is:"+i+" "+rmDetails[i]);
                        }
                        HashMap<String,String> details=new HashMap<>();
                        details.put("BuildingNo",rmDetails[4]);
                        details.put("FloorNo",rmDetails[6]);
                        details.put("RoomNo",rmDetails[8]);
                        details.put("BedNo",rmDetails[10]);
                        dbRoomDetails.child(stuId).setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                System.out.println("Room details add successfully");
                            }
                        });
                        Toast.makeText(AssignToRoom.this, "Student Assign successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AssignToRoom.this,StudentRoomDetails.class);
                        intent.putExtra(EXTRA_STUDENTID,stuId);
                        intent.putExtra(EXTRA_ORGID,orgID);
                        startActivity(intent);
                    }
                    else{
                        accommodationError();
                    }
                }
            });
        }catch (NullPointerException e){
            accommodationError();
        }

    }

    public void getTotalBedCount(String builName){
        dbBuildings.orderByChild("organizationID").equalTo(orgID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        if (ds.child("BuildingName").getValue().toString().equals(builName)){
                            for (DataSnapshot dsFloors:ds.child("floors").getChildren()){
                                for (DataSnapshot dsRooms:dsFloors.child("rooms").getChildren()){
                                    totBedCount+=dsRooms.child("beds").getChildrenCount();
                                    Log.d(TAG, "tot bed count is : "+dsRooms.child("beds").getChildrenCount());
                                }
                            }
                        }
                        else{
                            Log.d(TAG, "onDataChange: building name not matching");
                        }
                    }
                    getAvailableBedCount(builName, new FirebaseCallback() {
                        @Override
                        public void onCallback(String stuGender) {

                        }

                        @Override
                        public void onCallbackGetAvBedCount(int avCount) {
                            generatePieChart(totBedCount,avCount);
                        }
                    });
                }
                else {
                    System.out.println("no data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAvailableBedCount(String builName,FirebaseCallback firebaseCallback){
        dbBuildings.orderByChild("organizationID").equalTo(orgID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                        if (ds.child("BuildingName").getValue().toString().equals(builName)){
                            for (DataSnapshot dsFloors:ds.child("floors").getChildren()){
                                for (DataSnapshot dsRooms:dsFloors.child("rooms").getChildren()){
                                    for (DataSnapshot dsBed:dsRooms.child("beds").getChildren()){
                                        if (dsBed.child("StuId").getValue().toString().equals("")){
                                            avBedCount+=1;
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Log.d(TAG, "onDataChange: building name not matching");
                        }
                    }
                    firebaseCallback.onCallbackGetAvBedCount(avBedCount);
                    Log.d(TAG, "Available bed count is: "+avBedCount);
                }
                else {
                    System.out.println("no data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void generatePieChart(int totCount,int availableCount){
        Log.d(TAG, "generatePieChart: total bed count is:"+ totCount);
        txtTotalBeds.setText(String.valueOf(totCount));
        Log.d(TAG, "generatePieChart: Available bed count is:"+ availableCount);
        txtAvailableBeds.setText(String.valueOf(availableCount));
        double percentage=((double)availableCount/(double)totCount)*100;
        chart.addPieSlice(new PieModel("labe2",(float) percentage, Color.parseColor("#2E2252")));
        chart.addPieSlice(new PieModel("labe2",100-(float) percentage, Color.parseColor("#808080")));
        Log.d(TAG, "generatePieChart: percentage is:"+percentage);
        chart.setInnerValueString(String.valueOf(percentage)+"%");
        chart.startAnimation();
        totBedCount=0;
        avBedCount=0;
    }

    public void readStudentGender(FirebaseCallback firebaseCallback){
        dbStudents.child(stuID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    stuGender=snapshot.child("gender").getValue().toString();
                    Log.d(TAG, "onDataChange readStudentGender : student gender is "+stuGender);
                    firebaseCallback.onCallback(stuGender);
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

    private interface FirebaseCallback{
        void onCallback(String stuGender);
        void onCallbackGetAvBedCount(int avCount);
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
                intent.putExtra(EXTRA_STUDENTID,stuID);
                intent.putExtra(EXTRA_ORGID,orgID);
                startActivity(intent1);
                return true;
            case R.id.mnuMyProfile:
                //go to profile
                return true;
            case R.id.mnuLogout:
                logoutFunction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //accommodation error
    @SuppressLint("ResourceType")
    public void accommodationError(){
        //Create the Dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.accommodation_error_dialogbox);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialog.show();
        btn_tryagin = dialog.findViewById(R.id.btn_tryagin);
        btn_chkacc = dialog.findViewById(R.id.btn_chkacc);
        btn_tryagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_chkacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AssignToRoom.this,Building.class);
                intent.putExtra(EXTRA_STUDENTID,stuID);
                intent.putExtra(EXTRA_ORGID,orgID);
                startActivity(intent);
            }
        });
    }

    //logout
    @SuppressLint("ResourceType")
    public void logoutFunction(){
        //Create the Dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_custom_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialog.show();
        Yes = dialog.findViewById(R.id.btn_yes);
        No = dialog.findViewById(R.id.btn_No);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AssignToRoom.this,MainActivity.class);
                startActivity(intent);
            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}