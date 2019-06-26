package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class IssueMaterialDetails extends AppCompatActivity {

    ArrayList<String> materials = new ArrayList<String>();

    TextView final_materials;

    private ProgressDialog dialog;

    private String fullnameRetrieved ="";
    private String profilePicLink = "";

    private Toolbar occupantDetailsToolBar;

    private TextView fullName;
    private CircleImageView profilePic;


    private DatabaseReference applications, materialsIssued;

    String occupantId = "";
    String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_material_details);


        dialog = new ProgressDialog(this);

        fullName = findViewById(R.id.fullName);
        profilePic = findViewById(R.id.profile_image);
        materialsIssued = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("materialsIssued");
        applications = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("Occupants");



        //initialize our toolBar
        occupantDetailsToolBar = findViewById(R.id.occupant_details_toolbar);
        setSupportActionBar(occupantDetailsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Occupant Details");



        if (getIntent() != null){
            occupantId= getIntent().getStringExtra("occupantId");
            code = getIntent().getStringExtra("code");
            if (!occupantId.isEmpty()){
                retrieveOccupantDetail(occupantId);
            }else{
                Toast.makeText(this, "No Occupant ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void retrieveOccupantDetail(String occupantId) {

        applications.child(occupantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Occupants occupants = dataSnapshot.getValue(Occupants.class);

                fullName.setText(occupants.getFullName());

                fullnameRetrieved = occupants.getFullName();
                profilePicLink = occupants.getProfilePic();

                Picasso.get().load(occupants.getProfilePic()).placeholder(R.drawable.ic_account_circle)
                        .into(profilePic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void selectItem(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case  R.id.mattress:

                if (checked){
                    materials.add("Mattress");
                }else {
                    materials.remove("Mattress");
                }
                break;

            case  R.id.keys:

                if (checked){
                    materials.add("Keys");
                }else {
                    materials.remove("Keys");
                }
                break;

            case  R.id.pillow:

                if (checked){
                    materials.add("Pillow");
                }else {
                    materials.remove("Pillow");
                }
                break;

            case  R.id.bedSheets:

                if (checked){
                    materials.add("Bedsheets");
                }else {
                    materials.remove("Bedsheets");
                }
                break;

            case  R.id.blanket:

                if (checked){
                    materials.add("Blanket");
                }else {
                    materials.remove("Blanket");
                }
                break;



        }
    }

    public void submitMaterialIssued(View view) {
        dialog.setMessage("Issuing Materials");
        dialog.show();


        String final_material_issued = "";

        for (String Materials : materials){
            final_material_issued = final_material_issued + Materials + ", ";
        }
        materialsIssued.child(occupantId).child("fullname").setValue(fullnameRetrieved);
        materialsIssued.child(occupantId).child("uId").setValue(occupantId);
        materialsIssued.child(occupantId).child("status").setValue("Not cleared");
        materialsIssued.child(occupantId).child("profilePic").setValue(profilePicLink);
        materialsIssued.child(occupantId).child("materials").setValue(final_material_issued).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(IssueMaterialDetails.this, "Material issued successfully", Toast.LENGTH_SHORT).show();
                            applications.child(occupantId).child("status").setValue("Yes");
                            startActivity(new Intent(IssueMaterialDetails.this, IssueHostelMaterial.class));
                            finish();
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(IssueMaterialDetails.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//
//        materialsIssued.child(occupantId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//              if (dataSnapshot.exists()){
//                  startActivity(new Intent(IssueMaterialDetails.this, Clearance.class));
//              } else{
//
//
//              }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }
}
