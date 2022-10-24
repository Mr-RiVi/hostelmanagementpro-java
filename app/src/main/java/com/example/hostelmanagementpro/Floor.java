package com.example.hostelmanagementpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    TextView txt;
    ImageView btn;
    DatabaseReference myRef;
    ValueEventListener postListener;
    private List<FloorModel> floors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor);

        txt=findViewById(R.id.toolbarTitle);
        txt.setText("Floor");

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



}