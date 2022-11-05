package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentRoomDetails extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_STUDENTID="com.example.hostelmanagementpro.EXTRA_STUDENTID";
    public static final String EXTRA_ACTIVITYID="com.example.hostelmanagementpro.EXTRA_ACTIVITYID";
    Toolbar toolbar;
    Dialog dialog;
    Button Yes,No,btnbckToHome,btnAssign;
    String orgID,stuID,TAG="Ushan";
    TextView stuid,BuildingName,floorNo,roomNo,bedNo,notYetAsMsg;
    DatabaseReference dbBuildings,bedIDRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_room_details);

        dbBuildings= FirebaseDatabase.getInstance().getReference("buildings");

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.stu_room_details);

        stuid=findViewById(R.id.stuid);
        BuildingName=findViewById(R.id.BuildingName);
        floorNo=findViewById(R.id.floorNo);
        roomNo=findViewById(R.id.roomNo);
        bedNo=findViewById(R.id.bedNo);
        btnbckToHome=findViewById(R.id.btnbckToHome);
        notYetAsMsg=findViewById(R.id.notYetAsMsg);
        btnAssign=findViewById(R.id.btnAssign);

        Intent intent=getIntent();
        if (intent.getStringExtra(AssignToRoom.EXTRA_ORGID)!=null&&intent.getStringExtra(AssignToRoom.EXTRA_STUDENTID)!=null){
            orgID=intent.getStringExtra(AssignToRoom.EXTRA_ORGID);
            stuID=intent.getStringExtra(AssignToRoom.EXTRA_STUDENTID);
            Log.d(TAG, "Assign room: student id is"+stuID);
        }
        else if(intent.getStringExtra(StudentProfiles.EXTRA_ORGID)!=null&&intent.getStringExtra(StudentProfiles.EXTRA_STUDENTID)!=null){
            orgID=intent.getStringExtra(StudentProfiles.EXTRA_ORGID);
            stuID=intent.getStringExtra(StudentProfiles.EXTRA_STUDENTID);
            Log.d(TAG, "Student profiles: student id is"+stuID);
        }
        else{
            System.out.println("No id's retrieve");
        }
        stuid.setText(stuID);
        getRoomDetails(orgID,stuID);

        btnbckToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentRoomDetails.this,FunctionsStuManagement.class);
                intent.putExtra(EXTRA_ORGID,orgID);
                intent.putExtra(EXTRA_STUDENTID,stuID);
                startActivity(intent);
            }
        });
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentRoomDetails.this,AssignToRoom.class);
                intent.putExtra(EXTRA_ORGID,orgID);
                intent.putExtra(EXTRA_ACTIVITYID,"StudentRoomDetails");
                intent.putExtra(EXTRA_STUDENTID,stuID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void getRoomDetails(String orgid,String stuid){
        dbBuildings.orderByChild("organizationID").equalTo(orgid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds:snapshot.getChildren()){
                            for (DataSnapshot dsFloors:ds.child("floors").getChildren()){
                                for (DataSnapshot dsRooms:dsFloors.child("rooms").getChildren()){
                                    for (DataSnapshot dsBeds:dsRooms.child("beds").getChildren()){
                                        if (dsBeds.child("StuId").getValue().toString().equals(stuid)){
                                            Log.d(TAG, "onDataChange StudentRoomDetail: get room details");
                                            bedIDRef=dsBeds.getRef();
                                            Log.d(TAG, "onDataChange: according to the student that bed ref is: "+bedIDRef);
                                            catchRoomDetails(bedIDRef, stuid, new FirebaseCallback() {
                                                @Override
                                                public void onCallback(String bName,String id,String arr[]) {
                                                    displayRoomDetails(bName,id,arr);
                                                }
                                            });
                                            if (bedIDRef!=null){
                                                break;
                                            }
                                        }
                                        else{
                                            Log.d(TAG, "onDataChange: Room details not found");
                                        }
                                    }
                                }
                            }
                    }
                }
                else {
                    System.out.println("no data ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void catchRoomDetails(DatabaseReference bedRef,String stuID,FirebaseCallback firebaseCallback){
        Log.d(TAG, "catchRoomDetails: calling");
        final String[] buildingname = new String[1];
        String ref[]=bedRef.toString().split("/");
        for (int i=0;i<ref.length;i++){
            Log.d(TAG, "array element"+i+" "+ref[i]);
        }
        dbBuildings.child(ref[4]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buildingname[0] =snapshot.child("BuildingName").getValue().toString();
                System.out.println("Building name is :"+buildingname[0]);
                firebaseCallback.onCallback(buildingname[0],stuID,ref);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void displayRoomDetails(String bName,String sID,String id[]){
        btnbckToHome.setVisibility(View.VISIBLE);
        btnAssign.setVisibility(View.GONE);
        Log.d(TAG, "displayRoomDetails: building name is :"+bName);
        Log.d(TAG, "displayRoomDetails: floor no is is :"+id[6]);
        notYetAsMsg.setVisibility(View.GONE);
        BuildingName.setText(bName);
        floorNo.setText(id[6]);
        roomNo.setText(id[8]);
        bedNo.setText(id[10]);
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
                Intent intent1 =new Intent(StudentRoomDetails.this,FunctionsAdministrator.class);
                intent1.putExtra(EXTRA_ORGID,orgID);
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
                Intent intent=new Intent(StudentRoomDetails.this,MainActivity.class);
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
    private interface FirebaseCallback{
        public void onCallback(String bName,String id,String arr[]);
    }
}