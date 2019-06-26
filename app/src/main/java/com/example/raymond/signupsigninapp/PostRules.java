package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Modell.RulesModel;
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
                                        startActivity(new Intent(PostRules.this, HostelRules.class));
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



    }
}
