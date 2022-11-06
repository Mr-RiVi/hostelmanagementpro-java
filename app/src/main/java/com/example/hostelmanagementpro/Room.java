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

import com.example.hostelmanagementpro.model.BuildingModel;
import com.example.hostelmanagementpro.model.RoomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Room extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    ImageView btn;
    Query myQuery;
    ValueEventListener postListener;
    private List<RoomModel> rooms;
    private String floorNumber = "";
    private String buildingNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            floorNumber = extras.getString("floor_number");
            buildingNumber = extras.getString("building_number");
            System.out.println(buildingNumber);
        } else  {
            Toast.makeText(this,"Error - Building ID not found", Toast.LENGTH_LONG).show();
            finish();
        }

        // setting recycler view adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RoomAdapter adapter = new RoomAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
            @Override
            public void onHolderClick(int position) {
                RoomModel clickedRoom = rooms.get(position);

                Intent intent =new Intent(Room.this,Bed.class);
                intent.putExtra("room_number",clickedRoom.getRoomNo());
                intent.putExtra("floor_number",floorNumber);
                intent.putExtra("building_number",buildingNumber);
                startActivity(intent);

                Toast.makeText(Room.this,"Room Clicked - No: "+clickedRoom.getRoomNo(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUpdateClick(int position) {
                RoomModel clickedRoom = rooms.get(position);

                Intent intent =new Intent(Room.this,UpdateRoom.class);
                intent.putExtra("room_number",clickedRoom.getRoomNo());
                intent.putExtra("floor_number",floorNumber);
                intent.putExtra("building_number",buildingNumber);
                startActivity(intent);
            }
        });

        // getting and listening data from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myQuery = database.getReference("buildings").child(buildingNumber).child("floors").child(floorNumber).child("rooms").orderByChild("RoomNo");

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.exists()){
                    rooms = new ArrayList<>();
                    for (DataSnapshot roomSnapshot: dataSnapshot.getChildren()) {
                        int st_count = 0;
                        for(DataSnapshot snap: roomSnapshot.child("beds").getChildren()){
                            String stid = snap.child("StuId").getValue(String.class);
                            if(stid!=null && !stid.isEmpty()){
                                st_count++;
                            }
                        }

                        RoomModel  model= new RoomModel(
                                roomSnapshot.child("RoomNo").getValue(String.class),
                                roomSnapshot.child("RoomStatus").getValue(String.class),
                                roomSnapshot.child("RoomType").getValue(String.class),
                                st_count+"",
                                roomSnapshot.child("beds").getChildrenCount()+""
                        );
                        rooms.add(model);
                    }
                    adapter.setRooms(rooms);
                }
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Room", "loadRooms:onCancelled", databaseError.toException());
            }
        };
        myQuery.addValueEventListener(postListener);
    }

    //    Btn for Add Room Page
    public void onclickAR(View view){
        Intent in=new Intent(this,AddRoom.class);
        in.putExtra("floor_number",floorNumber);
        in.putExtra("building_number",buildingNumber);
        startActivity(in);
    }
    //    Btn for Remove Room Page
    public void onclickRR(View view){
        Intent in=new Intent(this,RemoveRoom.class);
        in.putExtra("floor_number",floorNumber);
        in.putExtra("building_number",buildingNumber);
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
                Intent intent =new Intent(Room.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}