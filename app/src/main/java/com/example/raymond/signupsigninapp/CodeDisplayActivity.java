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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.HiewHolders.CodesViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.Code;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CodeDisplayActivity extends AppCompatActivity {
    private FloatingActionButton fab;

    private Toolbar codeToolBar;


    private DatabaseReference codes;

    RecyclerView recyclerView_codes;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<Code, CodesViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_display);


        codes = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("registrationCodes");




        //initialize our toolBar
        codeToolBar = findViewById(R.id.Code);
        setSupportActionBar(codeToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Codes Generated");

        //views
        recyclerView_codes = findViewById(R.id.recycler_code);
        recyclerView_codes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_codes.setLayoutManager(layoutManager);


        loadRegCodes();



        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CodeDisplayActivity.this, GenerateRegPin.class));
            }
        });




    }

    private void loadRegCodes() {
        FirebaseRecyclerOptions<Code>options = new FirebaseRecyclerOptions.Builder<Code>()
                .setQuery(codes, Code.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Code, CodesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CodesViewHolder holder, int position, @NonNull Code model) {
                holder.txtCode.setText(model.getCode());
                holder.txtStatus.setText(model.getStatus());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public CodesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.code_layout, parent, false);
                CodesViewHolder viewHolder = new CodesViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView_codes.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }else if (item.getTitle().equals(Common.DELETE)){
            deleteCode(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String key, Code item) {
    }

    private void deleteCode(String key) {
        codes.child(key).removeValue();
        Toast.makeText(this, "Code deleted!", Toast.LENGTH_SHORT).show();
    }


}
