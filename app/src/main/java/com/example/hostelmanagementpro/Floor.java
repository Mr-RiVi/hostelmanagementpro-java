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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Floor extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    ImageView btn;
    Query myQuery;
    ValueEventListener postListener;
    private List<FloorModel> floors;
    private String buildingNumber = "";

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


        // get extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            buildingNumber = extras.getString("building_number");
        } else  {
            Toast.makeText(this,"Error - Building ID not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // setting recycler view adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final FloorAdapter adapter = new FloorAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            FloorModel clickedFloor = floors.get(position);

            Intent intent =new Intent(Floor.this,Room.class);
            intent.putExtra("floor_number",clickedFloor.getNumber());
            intent.putExtra("building_number",buildingNumber);
            startActivity(intent);

            Toast.makeText(this,"Floor Clicked - No: "+clickedFloor.getNumber(), Toast.LENGTH_LONG).show();
        });

        // getting and listening data from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myQuery = database.getReference("buildings").child(buildingNumber).child("floors").orderByChild("FloorNo");

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.exists()){
                    floors = new ArrayList<>();
                    for (DataSnapshot floorSnapshot: dataSnapshot.getChildren()) {
                        FloorModel model = new FloorModel(
                                floorSnapshot.child("FloorNo").getValue(String.class),
                                floorSnapshot.child("RoomCount").getValue(String.class));
                        floors.add(model);
                    }
                    adapter.setFloors(floors);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Floor", "loadFloors:onCancelled", databaseError.toException());
            }
        };
        myQuery.addValueEventListener(postListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myQuery.removeEventListener(postListener);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        myQuery.addValueEventListener(postListener);
//    }
//    @Override
//    protected void onStop() {
//        if (postListener != null && myQuery!=null) {
//            myQuery.removeEventListener(postListener);
//        }
//        super.onStop();
//    }
//    @Override
//    protected void onPause() {
//        if (postListener != null && myQuery!=null) {
//            myQuery.removeEventListener(postListener);
//        }
//        super.onPause();
//    }


    //    Btn for Add Floor Page
    public void onclickAF(View view){
        Intent in=new Intent(this,AddFloor.class);
        in.putExtra("building_number",buildingNumber);
        startActivity(in);
    }
    //    Btn for Remove Floor Page
    public void onclickRF(View view){
        Intent in=new Intent(this,RemoveFloor.class);
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
                Intent intent =new Intent(Floor.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}