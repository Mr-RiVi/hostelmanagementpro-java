package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizationDetails extends AppCompatActivity {
    public static final String EXTRA_ORGID="com.example.hostelmanagementpro.EXTRA_ORGID";
    public static final String EXTRA_ORGUSERNAME="com.example.hostelmanagementpro.EXTRA_ORGUSERNAME";

    Toolbar toolbar;
    DatabaseReference db;
    TextView orgName,orgUsername,orgPass;
    Button crtAdmin;
    String orgUsrname;
    String orgname,orgId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        db= FirebaseDatabase.getInstance().getReference("organizations");

        orgName=findViewById(R.id.orgName);
        orgUsername=findViewById(R.id.orgUsername);
        orgPass=findViewById(R.id.orgPassword);
        crtAdmin=findViewById(R.id.btnCrtAcc);

        toolbar=findViewById(R.id.toolbar);
        //catch toolbar and set it as default actionbar
        setSupportActionBar(toolbar);

        //set actionbar name
        getSupportActionBar().setTitle(R.string.Organization_details);

        Intent intent=getIntent();
        orgId=intent.getStringExtra(OrganizationRegistration.EXTRA_ORGID);
        System.out.println("Organization details page id is:"+orgId);
        getOrganizationDetails(orgId);

        crtAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity();
            }
        });
    }

    //go to next activity and passing essential data
    public void openNextActivity(){
        Intent intent=new Intent(this,AdministratorRegistration.class);
        intent.putExtra(EXTRA_ORGID,orgId);
        intent.putExtra(EXTRA_ORGUSERNAME,orgUsrname);
        startActivity(intent);
    }

    //actionbar menu implementation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbarmenu,menu);
        MenuItem i1=menu.findItem(R.id.mnuMyProfile);
        MenuItem i2=menu.findItem(R.id.mnuLogout);
        i2.setVisible(false);
        i1.setTitle("Profile");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuMyProfile:
                //go to organization profile
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getOrganizationDetails(String id){
        db.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    orgname=snapshot.child("orgName").getValue().toString();
                    orgUsrname=snapshot.child("orgUsername").getValue().toString();

                    orgName.setText(orgname);
                    orgUsername.setText(orgUsrname);
                    orgPass.setText(snapshot.child("orgPassword").getValue().toString());
                }
                else
                    Toast.makeText(OrganizationDetails.this,"No data available on that ID",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                crtAdmin.setEnabled(false);
                Toast.makeText(OrganizationDetails.this,"Database error",Toast.LENGTH_LONG).show();
            }
        });
    }
}