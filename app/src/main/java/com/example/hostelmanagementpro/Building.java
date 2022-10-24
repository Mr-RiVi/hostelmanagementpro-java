package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelmanagementpro.model.BuildingModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Building extends AppCompatActivity {

    Toolbar toolbar;
    ImageView btn;
    DatabaseReference myRef;
    ValueEventListener postListener;
    private List<BuildingModel> buildings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Model");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // setting recycler view adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BuildingAdapter adapter = new BuildingAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            BuildingModel clickedBuilding = buildings.get(position);

            Intent in=new Intent(Building.this,Floor.class);
            startActivity(in);

            Toast.makeText(this,"Building Clicked - No: "+clickedBuilding.getNumber() +", Gender: "+clickedBuilding.getGender(),Toast.LENGTH_LONG).show();

        });


        // getting and listening data from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("buildings");

        // Child event listner ekak use krnna ona. lesi handa meka demme. hariyata krnawa nam eka use krnna
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.exists()){
                    buildings = new ArrayList<>();
                    for (DataSnapshot buildingSnapshot: dataSnapshot.getChildren()) {
                        BuildingModel model = new BuildingModel(buildingSnapshot.child("BuildingNo").getValue(String.class),
                                buildingSnapshot.child("BuildingName").getValue(String.class),
                                buildingSnapshot.child("Gender").getValue(String.class));
                        buildings.add(model);
                    }
                    adapter.setBuildings(buildings);
                }
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Building", "loadBuildings:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(postListener);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener(postListener);
    }

    //    Back Button
    public void onclickBbtn(View view){
        Intent in=new Intent(this,ManageAccommodation.class);
        startActivity(in);
    }

//    Btn for Add Building Page
    public void onclickAB(View view){
        Intent in=new Intent(this,AddBuilding.class);
        startActivity(in);
    }

//    Btn for Remove Building Page
    public void onclickRB(View view){
        Intent in=new Intent(this,RemoveBuilding.class);
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
                Intent intent =new Intent(Building.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}