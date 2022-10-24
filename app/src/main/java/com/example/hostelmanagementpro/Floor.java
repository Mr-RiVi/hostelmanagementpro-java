package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
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

import com.example.hostelmanagementpro.model.FloorModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Floor extends AppCompatActivity {

    Toolbar toolbar;
    ImageView btn;
    DatabaseReference myRef;
    ValueEventListener postListener;
    private List<FloorModel> floors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Floor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setting recycler view adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final FloorAdapter adapter = new FloorAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            FloorModel clickedFloor = floors.get(position);
            //Pass data and start new activity here
            // uda tiyenne click karapu building eka. eken ona nam next activity ekata yana eka metana code krnna
            // click krma run wenne metana
            // TODO @ metana hadanna

            Toast.makeText(this,"Floor Clicked - No: "+clickedFloor.getNumber() +", Room Count: "+clickedFloor.getRoomCount(),Toast.LENGTH_LONG).show();
        });

        // getting and listening data from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("floors");

        // Child event listner ekak use krnna ona. lesi handa meka demme. hariyata krnawa nam eka use krnna
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.exists()){
                    floors = new ArrayList<>();
                    for (DataSnapshot floorSnapshot: dataSnapshot.getChildren()) {
                        FloorModel model = new FloorModel(floorSnapshot.child("FloorNo").getValue(String.class),
                                floorSnapshot.child("RoomCount").getValue(String.class));
                        floors.add(model);
                    }
                    adapter.setFloors(floors);
                }
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Floor", "loadFloors:onCancelled", databaseError.toException());
            }
        };
        myRef.addValueEventListener(postListener);
    }


    //   tool bar Back Button
    public void onclickBbtn(View view){
        Intent in=new Intent(this,Building.class);
        startActivity(in);
    }

    //    Btn for Add Floor Page
    public void onclickAF(View view){
        Intent in=new Intent(this,AddFloor.class);
        startActivity(in);
    }
    //    Btn for Remove Floor Page
    public void onclickRF(View view){
        Intent in=new Intent(this,RemoveFloor.class);
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
                Intent intent =new Intent(Floor.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}