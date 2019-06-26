package com.example.raymond.signupsigninapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.RulesModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HostelRules extends AppCompatActivity {
    private FloatingActionButton fab;
    private DatabaseReference rulesRef;

    RecyclerView recyclerView_rules;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<RulesModel, RulesViewHolder>adapter;
    private Toolbar rulesToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel_rules);

        fab = findViewById(R.id.fab);

        rulesRef = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("rules");
        rulesRef.keepSynced(true);

        //initialize our toolBar
        rulesToolBar = findViewById(R.id.rules_toolbar);
        setSupportActionBar(rulesToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Hostel Rules");


        //views
        recyclerView_rules = findViewById(R.id.recycler_rules);
        recyclerView_rules.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_rules.setLayoutManager(layoutManager);


        displayRules();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HostelRules.this, PostRules.class));
            }
        });
    }

    private void displayRules() {
        FirebaseRecyclerOptions<RulesModel> options = new FirebaseRecyclerOptions.Builder<RulesModel>()
                .setQuery(rulesRef,RulesModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<RulesModel, RulesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RulesViewHolder holder, int position, @NonNull RulesModel model) {
                holder.txtRules.setText(model.getRule());
                holder.txtTitle.setText(model.getTitle());
                holder.txtDate.setText(RulesUtils.dateFromLong(model.getRuleDate()));

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public RulesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rules_layout, parent, false);
                RulesViewHolder viewHolder = new RulesViewHolder(view);
                return viewHolder;
            }
        };

        //set adapter
        recyclerView_rules.setAdapter(adapter);
        adapter.startListening();

    }


    //ViewHolder class
    public static class RulesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtTitle,txtRules, txtDate;
        private ItemClickListener itemClickListener;

        public RulesViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.text_view_title);
            txtRules = itemView.findViewById(R.id.text_view_rule);
            txtDate = itemView.findViewById(R.id.text_view_date);

            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);

        }
    }
}
