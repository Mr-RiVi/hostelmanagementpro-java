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

import com.example.hostelmanagementpro.model.BedModel;
import com.example.hostelmanagementpro.model.RoomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bed extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;
    ImageView btn;
    Query myQuery;
    ValueEventListener postListener;
    private List<BedModel> beds;
    private String roomNumber = "";
    private String floorNumber = "";
    private String buildingNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed);

        //catch toolbar and set it as default actionbar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar name and enable back navigation
        getSupportActionBar().setTitle("Bed");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get extra
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomNumber = extras.getString("room_number");
            floorNumber = extras.getString("floor_number");
            buildingNumber = extras.getString("building_number");
        } else  {
            Toast.makeText(this,"Error - Floor ID not found", Toast.LENGTH_LONG).show();
            finish();
        }

        // setting recycler view adapter
        RecyclerView recyclerView = findViewById(R.id.recycler_4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BedAdapter adapter = new BedAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BedAdapter.OnItemClickListener() {
            @Override
            public void onHolderClick(int position) {
                BedModel clickedBed = beds.get(position);

            }

            @Override
            public void onUpdateBedClick(int position) {
                BedModel clickedBed = beds.get(position);

                Intent intent =new Intent(Bed.this,UpdateBed.class);
                intent.putExtra("bed_number",clickedBed.getBedNo());
                intent.putExtra("room_number",roomNumber);
                intent.putExtra("floor_number",floorNumber);
                intent.putExtra("building_number",buildingNumber);
                startActivity(intent);
            }
        });

        // getting and listening data from database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myQuery = database.getReference("buildings").child(buildingNumber).child("floors").child(floorNumber).child("rooms").child(roomNumber).child("beds").orderByChild("BedNo");

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot.exists()){
                    beds = new ArrayList<>();
                    for (DataSnapshot bedSnapshot: dataSnapshot.getChildren()) {
                        BedModel model= new BedModel(
                                bedSnapshot.child("BedNo").getValue(String.class),
                                bedSnapshot.child("StuId").getValue(String.class),
                                bedSnapshot.child("RoomNo").getValue(String.class)
                        );
                        beds.add(model);
                    }
                    adapter.setBeds(beds);
                }
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Bed", "loadBeds:onCancelled", databaseError.toException());
            }
        };
        myQuery.addValueEventListener(postListener);
    }


//    public void onclickBbtn(View view){
//        Intent in=new Intent(this,Room.class);
//        startActivity(in);
//    }

    //    Btn for Remove Bed Page
    public void onclickABed(View view){
        Intent in=new Intent(this,AddBed.class);
        in.putExtra("room_number",roomNumber);
        in.putExtra("floor_number",floorNumber);
        in.putExtra("building_number",buildingNumber);
        startActivity(in);
    }
    //    Btn for Remove Bed Page
    public void onclickRBed(View view){
        Intent in=new Intent(this,RemoveBed.class);
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
                Intent intent =new Intent(Bed.this,MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}