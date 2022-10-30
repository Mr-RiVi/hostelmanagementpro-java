package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.StuProfiles;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class StudentProfiles extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";

    Toolbar toolbar;
    RecyclerView recyclerView;
    StudentProfilesFetchingAdapter studentProfilesFetchingAdapter;
    ArrayList<StuProfiles> list;
    DatabaseReference dbStudents,dbCredentials,dbBuildings;
    String orgID;
    SearchView searchView;
    Button Yes,No;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profiles);

        searchView=findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle(R.string.student_profiles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        orgID=intent.getStringExtra(MainActivity.EXTRA_ORGID);

        recyclerView=findViewById(R.id.recyclerActivity);
        dbStudents= FirebaseDatabase.getInstance().getReference("students");
        dbCredentials= FirebaseDatabase.getInstance().getReference("credentials");
        dbBuildings= FirebaseDatabase.getInstance().getReference("buildings");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<>();

        System.out.println("This is student profiles page and org id is:"+orgID);
        dbStudents.orderByChild("organizationID").equalTo(orgID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("This is student profiles page and org id is:"+orgID);
                if (snapshot.exists()){
                    for (DataSnapshot ds: snapshot.getChildren()){
                        String stuID=ds.getKey();
                        String name=ds.child("name").getValue().toString();
                        String mobile=ds.child("studentContactNo").getValue().toString();
                        String emMobile=ds.child("emergencyContactNo").getValue().toString();
                        String credID=ds.child("credentialID").getValue().toString();

                        StuProfiles stuProfiles=new StuProfiles(stuID,name,mobile,emMobile,credID);
                        System.out.println("Datasnapshot stuProfile object look like:"+stuProfiles.getStuName());
                        list.add(stuProfiles);
                    }
                    studentProfilesFetchingAdapter=new StudentProfilesFetchingAdapter(list, StudentProfiles.this, new StudentProfilesFetchingAdapter.ItemClickListener() {
                        @Override
                        public void onItemRoomInfoClicked(StuProfiles profiles) {
                            //
                        }
                        @Override
                        public void onItemEditClicked(StuProfiles profiles) {
                            Toast.makeText(StudentProfiles.this, "student is is: "+profiles.getStuName(), Toast.LENGTH_SHORT).show();
                            //you have to implement student edit api
                        }
                        @Override
                        public void onItemDeleteClicked(StuProfiles profiles) {
                            Toast.makeText(StudentProfiles.this, "student is is: "+profiles.getStuID(), Toast.LENGTH_SHORT).show();
                            deleteStudent(profiles.getStuID(),profiles.getCredID());
                        }
                    });
                    recyclerView.setAdapter(studentProfilesFetchingAdapter);
                    studentProfilesFetchingAdapter.notifyDataSetChanged();
                }
                else{
                    System.out.println("No student profiles found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String newText) {
        ArrayList<StuProfiles> filteredList=new ArrayList<>();
        for (StuProfiles sp:list){
            if(sp.getStuID().toLowerCase(Locale.ROOT).contains(newText.toLowerCase(Locale.ROOT))){
                filteredList.add(sp);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No student found", Toast.LENGTH_SHORT).show();
        }
        else {
            studentProfilesFetchingAdapter.setFilteredList(filteredList);
        }
    }

    public void getStudentRoomInfo(String stuID,String orgID){
        dbBuildings.child("floors/rooms/beds").orderByChild("StuId").equalTo(stuID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteStudent(String stuID,String credID){
        //Create the Dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_student_dialogbox);
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
                dbStudents.child(stuID).removeValue();
                dbCredentials.child(credID).removeValue();
                dialog.dismiss();
                startActivity(getIntent());
                finish();
                overridePendingTransition(0, 0);
            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
                Intent intent1 =new Intent(StudentProfiles.this,FunctionsAdministrator.class);
                intent1.putExtra(EXTRA_ORGID,orgID);
                startActivity(intent1);
                return true;
            case R.id.mnuMyProfile:
                //go to Admin profile
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
                Intent intent=new Intent(StudentProfiles.this,MainActivity.class);
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