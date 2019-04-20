package com.example.raymond.signupsigninapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class GenerateRegPin extends AppCompatActivity {
    private TextView txtCode;
    private Button btnGenrate;

    private Toolbar codeToolBar;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_reg_pin);

        database = FirebaseDatabase.getInstance().getReference().child("plasuHostel2019").child("registrationCodes");

        //initialize our toolBar
        codeToolBar = findViewById(R.id.codes_toolbar);
        setSupportActionBar(codeToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Generate Application Code");


        txtCode = findViewById(R.id.txtCodes);
        btnGenrate = findViewById(R.id.btnGenerate);

        btnGenrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date myDate = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("yyy-MM-dd hh:mm:ss a", Locale.getDefault());
                String date = format1.format(myDate);
                Random r = new Random();
                int n = 10000 + r.nextInt(90000);
                String code = String.valueOf(n);



                database.child(code).child("code").setValue(code);
                database.child(code).child("status").setValue("valid");

                Toast.makeText(GenerateRegPin.this, "Code saved successfully", Toast.LENGTH_SHORT).show();

                txtCode.setText(code);
                btnGenrate.setText("Reset");

            }
        });
    }
}
