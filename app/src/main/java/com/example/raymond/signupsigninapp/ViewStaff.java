package com.example.raymond.signupsigninapp;

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

import com.example.raymond.signupsigninapp.HiewHolders.StaffViewHolder;
import com.example.raymond.signupsigninapp.Modell.Occupants;
import com.example.raymond.signupsigninapp.Modell.Staff;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewStaff extends AppCompatActivity {

    private Toolbar viewStaff;

    private FirebaseDatabase database;
    private DatabaseReference staff;

    private MaterialSearchBar searchBar;

    ArrayList<Occupants> arrayList;



    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    private FirebaseRecyclerAdapter<Staff, StaffViewHolder>adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_staff);


        //initialize toolBar
        viewStaff = findViewById(R.id.view_staff_toolbar);
        setSupportActionBar(viewStaff);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Hostel Staff");


        database = FirebaseDatabase.getInstance();
        staff = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("users").child("admin").child("bursary");

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


            }
        });


        //init recycler view
        recyclerView = findViewById(R.id.recycler_staff);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));



        //load occupants
        loadStaff();
    }

    private void loadStaff() {
        FirebaseRecyclerOptions<Staff>options = new FirebaseRecyclerOptions.Builder<Staff>()
                .setQuery(staff, Staff.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Staff, StaffViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StaffViewHolder holder, int position, @NonNull Staff model) {
                holder.txtFullName.setText(model.getFullName());
                holder.txtEmail.setText(model.getEmail());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.ic_account_circle).into(holder.profilePic);

            }

            @NonNull
            @Override
            public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_staff_layout, parent, false);
                StaffViewHolder viewHolder = new StaffViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
