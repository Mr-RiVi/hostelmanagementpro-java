package com.example.hostelmanagementpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    public static final String EXTRA_USERID="com.example.hostelmanagementpro.EXTRA_USERID";

    ZXingScannerView scannerView;
    DatabaseReference dbRef;

    //formatting date with attributes
    static DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    static long d = System.currentTimeMillis();
    static String date = format.format(new Date(d));

    DateFormat formats = new SimpleDateFormat("hh:mm a");
    long t = System.currentTimeMillis();
    String time = formats.format(new Date(t));

    String id = UUID.randomUUID().toString();

    static String attUUID;

    String studentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        //Catching studentID from StudentHome
        Intent intent=getIntent();
        studentID=intent.getStringExtra(StudentHome.EXTRA_USERID);

        dbRef= FirebaseDatabase.getInstance().getReference("attendance").child(studentID).child(date);
        //implementing qr scanner using dependencies
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    //Inserting Attendance details into database
    @Override
    public void handleResult(Result result) {
        String data = result.getText().toString();
        attUUID = id;
        Toast.makeText(getApplicationContext(), "Attendance Successfully Recorded", Toast.LENGTH_SHORT).show();

        dbRef.child("Type").setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                    }
                });
        dbRef.child("Time").setValue(time)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                    }
                });
        dbRef.child("ATT_ID").setValue(id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onBackPressed();
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}