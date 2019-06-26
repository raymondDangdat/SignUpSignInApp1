package com.example.raymond.signupsigninapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Modell.Occupants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class OccupantDetail extends AppCompatActivity {
    private Toolbar occupantDetailsToolBar;

    private TextView fullName, matNo, phone, parentNo, bedNumber, chaletName, gender, department;
    private CircleImageView profilePic;

    private DatabaseReference applications;

    String occupantId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupant_detail);

        fullName = findViewById(R.id.fullName);
        matNo = findViewById(R.id.matNo);
        gender = findViewById(R.id.gender);
        phone = findViewById(R.id.phone);
        parentNo = findViewById(R.id.parentNo);
        bedNumber = findViewById(R.id.bedNumber);
        chaletName = findViewById(R.id.chaletName);
        department = findViewById(R.id.department);
        profilePic = findViewById(R.id.profile_image);

        applications = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("Occupants");


        if (getIntent() != null){
            occupantId= getIntent().getStringExtra("occupantId");
            if (!occupantId.isEmpty()){
                retrieveOccupantDetail(occupantId);
            }else{
                Toast.makeText(this, "No Occupant ID", Toast.LENGTH_SHORT).show();
            }
        }

        //initialize our toolBar
        occupantDetailsToolBar = findViewById(R.id.occupant_details_toolbar);
        setSupportActionBar(occupantDetailsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Occupant Details");

    }

    private void retrieveOccupantDetail(final String occupantId) {
        applications.child(occupantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Occupants occupants = dataSnapshot.getValue(Occupants.class);

                phone.setText(occupants.getPhone());
                parentNo.setText(occupants.getParentNo());
                fullName.setText(occupants.getFullName());
                matNo.setText(occupants.getMatNo());
                department.setText(occupants.getDepartment());
                bedNumber.setText(occupants.getBedNumber());
                chaletName.setText(occupants.getChaletName());
                gender.setText(occupants.getGender());

                Picasso.get().load(occupants.getProfilePic()).placeholder(R.drawable.ic_account_circle)
                .into(profilePic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
