package com.example.raymond.signupsigninapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raymond.signupsigninapp.HiewHolders.OccupantsViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.Occupants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class IssueHostelMaterial extends AppCompatActivity {
    private Toolbar viewStudents;

    private FirebaseDatabase database;
    private DatabaseReference applicants;

    private MaterialSearchBar searchBar;

    ArrayList<Occupants> arrayList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<Occupants, OccupantsViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_hostel_material);

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
        getSupportActionBar().setTitle("Hostel Occupants");


        database = FirebaseDatabase.getInstance();
        applicants = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("Occupants");
        applicants.keepSynced(true);


        searchBar = findViewById(R.id.searchBar);

        arrayList = new ArrayList<>();

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    search(s.toString());
                }else {
                    loadOccupants();
                }

            }
        });


        //load occupants
        loadOccupants();

    }

    private void loadOccupants() {
        FirebaseRecyclerOptions<Occupants>options = new FirebaseRecyclerOptions.Builder<Occupants>()
                .setQuery(applicants.orderByChild("status").equalTo("No"), Occupants.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Occupants, OccupantsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OccupantsViewHolder holder, int position, @NonNull Occupants model) {
                //viewHolder.txtDepartment.setText(model.getDepartment());
                holder.txtFullName.setText(model.getFullName());
                holder.txtGender.setText(model.getGender());
                holder.txtMatNo.setText(model.getMatNo());
                holder.txtPhone.setText(model.getPhone());
//                viewHolder.txtParentPhone.setText(model.getParentNo());
                final String code = model.getJAMB();

                Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.ic_account_circle).into(holder.profilePic);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent occupantDetail = new Intent(IssueHostelMaterial.this, IssueMaterialDetails.class);
                        occupantDetail.putExtra("occupantId", adapter.getRef(position).getKey());
                        occupantDetail.putExtra("code", code);
                        startActivity(occupantDetail);

                    }
                });
            }

            @NonNull
            @Override
            public OccupantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.occupants_layout1, parent, false);
                OccupantsViewHolder viewHolder = new OccupantsViewHolder(view);
                return viewHolder;
            }
        };




        //set adapter
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void search(String s) {

        Query query = applicants.orderByChild("fullName")
                .startAt(s)
                .endAt(s+ "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    arrayList.clear();
                    for (DataSnapshot dss: dataSnapshot.getChildren()){
                        final Occupants occupants = dss.getValue(Occupants.class);
                        arrayList.add(occupants);
                    }

                    SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), arrayList);
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
