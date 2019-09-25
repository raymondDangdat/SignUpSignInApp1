package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.RulesModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class PostRules extends AppCompatActivity {
    private Toolbar postRuleToolBar;

    private DatabaseReference rulesRef;

    private EditText editTextTitle, editTextRule;
    private Button btnRules;
    private ProgressDialog progressDialog;
    private RulesModel rule;


    RecyclerView recyclerView_rules;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<RulesModel, RulesViewHolder>adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_rules);

        editTextRule = findViewById(R.id.edit_text_rule);
        editTextTitle = findViewById(R.id.edit_text_title);
        btnRules = findViewById(R.id.btnSendRule);

        //initialize our toolBar
        postRuleToolBar = findViewById(R.id.postRulesToolBar);
        setSupportActionBar(postRuleToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Rules and Notices");

        //views
        recyclerView_rules = findViewById(R.id.recycler_rules);
        recyclerView_rules.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_rules.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);


        rulesRef = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("rules");


        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String rules = editTextRule.getText().toString().trim();

                long ruleDate = new Date().getTime();

                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(rules)){
                    Toast.makeText(PostRules.this, "Rule or notice must have title and content", Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.setMessage("Posting...");
                    progressDialog.show();
                    rule = new RulesModel(title, rules, ruleDate);
                    rulesRef.push().setValue(rule)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Toast.makeText(PostRules.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PostRules.this, PostRules.class));
                                        return;
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostRules.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



        loadRules();



    }

    private void loadRules() {
        FirebaseRecyclerOptions<RulesModel>options = new FirebaseRecyclerOptions.Builder<RulesModel>()
                .setQuery(rulesRef, RulesModel.class)
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
        recyclerView_rules.setAdapter(adapter);
        adapter.startListening();
    }


    //ViewHolder class
    public static class RulesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
            ,View.OnCreateContextMenuListener{
        public TextView txtTitle,txtRules, txtDate;
        private ItemClickListener itemClickListener;

        public RulesViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.text_view_title);
            txtRules = itemView.findViewById(R.id.text_view_rule);
            txtDate = itemView.findViewById(R.id.text_view_date);

            itemView.setOnClickListener(this);

            itemView.setOnCreateContextMenuListener(this);


        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an action");
            menu.add(0,0, getAdapterPosition(), Common.UPDATE);
            menu.add(0,1, getAdapterPosition(), Common.DELETE);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            //showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }else if (item.getTitle().equals(Common.DELETE)){
            deleteCode(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    private void deleteCode(String key) {
        rulesRef.child(key).removeValue();
        Toast.makeText(this, "Rule/Notice deleted!", Toast.LENGTH_SHORT).show();
    }
}
