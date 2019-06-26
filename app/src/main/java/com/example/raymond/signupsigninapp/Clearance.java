package com.example.raymond.signupsigninapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raymond.signupsigninapp.HiewHolders.ClearanceViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.ClearanceModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Clearance extends AppCompatActivity {
    private Toolbar viewStudents;

    private FirebaseDatabase database;
    private DatabaseReference materialsIssued;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<ClearanceModel, ClearanceViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearance);



        materialsIssued = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("materialsIssued");


        //init
        recyclerView = findViewById(R.id.recycler_occupants);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        //initialize toolBar
        viewStudents = findViewById(R.id.issue_material_toolbar);
        setSupportActionBar(viewStudents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Clearance");


        onStart();

        //load occupants
        loadOccupants();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadOccupants();
    }

    private void loadOccupants() {
        FirebaseRecyclerOptions<ClearanceModel>options = new FirebaseRecyclerOptions.Builder<ClearanceModel>()
                .setQuery(materialsIssued.orderByChild("status").equalTo("Not cleared"), ClearanceModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ClearanceModel, ClearanceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ClearanceViewHolder holder, int position, @NonNull ClearanceModel model) {
                holder.txtFullName.setText(model.getFullname());
                holder.txtMaterials.setText(model.getMaterials());
                Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.ic_account_circle).into(holder.profilePic);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent occupantDetail = new Intent(Clearance.this, ClearanceDetail.class);
                        occupantDetail.putExtra("occupantId", adapter.getRef(position).getKey());
                        startActivity(occupantDetail);

                    }
                });
            }

            @NonNull
            @Override
            public ClearanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clearance_layout, parent, false);
                ClearanceViewHolder viewHolder = new ClearanceViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
