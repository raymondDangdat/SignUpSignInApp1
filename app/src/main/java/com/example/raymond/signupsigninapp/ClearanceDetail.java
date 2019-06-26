package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Modell.ClearanceModel;
import com.example.raymond.signupsigninapp.Modell.Occupants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClearanceDetail extends AppCompatActivity {
    private Toolbar occupantDetailsToolBar;

    private TextView fullName, material;
    private CircleImageView profilePic;
    private ProgressDialog dialog;

    private Button btnClear;
    private DatabaseReference clearance, clearedStudents;

    String occupantId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearance_detail);


        fullName = findViewById(R.id.fullName);
        material = findViewById(R.id.materials);
        profilePic = findViewById(R.id.profile_image);
        btnClear = findViewById(R.id.btnClear);

        dialog = new ProgressDialog(this);

        clearance = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("materialsIssued");

        clearedStudents = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("clearedStudents");

        if (getIntent() != null){
            occupantId= getIntent().getStringExtra("occupantId");
            if (!occupantId.isEmpty()){
                retrieveOccupantDetail(occupantId);
            }else{
                Toast.makeText(this, "No Occupant ID", Toast.LENGTH_SHORT).show();
            }
        }

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Clearing Student");
                dialog.setMessage("Clearing...");
                dialog.show();

                clearance.child(occupantId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ClearanceModel clearanceModel = dataSnapshot.getValue(ClearanceModel.class);
                        final String fullName = clearanceModel.getFullname();
                        String profilePic = clearanceModel.getProfilePic();

                        clearedStudents.child(occupantId).child("fullname").setValue(fullName);
                        clearedStudents.child(occupantId).child("profilePic").setValue(profilePic);
                        clearedStudents.child(occupantId).child("uId").setValue(occupantId)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog.dismiss();

                                            clearance.child(occupantId).child("status").setValue("Cleared");

                                            clearedStudents.child(occupantId).child("status").setValue("Cleared");
                                            Toast.makeText(ClearanceDetail.this, ""+ fullName + " cleared", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ClearanceDetail.this, Clearance.class));
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(ClearanceDetail.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ClearanceDetail.this, Clearance.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //initialize our toolBar
        occupantDetailsToolBar = findViewById(R.id.occupant_details_toolbar);
        setSupportActionBar(occupantDetailsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Clearance");
    }

    private void retrieveOccupantDetail(String occupantId) {
        clearance.child(occupantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClearanceModel clearanceModel = dataSnapshot.getValue(ClearanceModel.class);

                fullName.setText(clearanceModel.getFullname());
                material.setText(clearanceModel.getMaterials());

                Picasso.get().load(clearanceModel.getProfilePic()).placeholder(R.drawable.ic_account_circle)
                        .into(profilePic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
