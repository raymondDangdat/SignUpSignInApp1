package com.example.raymond.signupsigninapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.HiewHolders.JambViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.JambConfirmation;
import com.example.raymond.signupsigninapp.Modell.JambConfirmationModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ConfirmStudentActivity extends AppCompatActivity {
    private FloatingActionButton fab;

    private Toolbar confirmToolBar;
    
    private MaterialEditText editTextJambNo;
    //private Button btnConfirm;


    private DatabaseReference eligibleStudents;

    RecyclerView recyclerView_jambNo;
    RecyclerView.LayoutManager layoutManager;

    private ProgressDialog progressDialog;
    private FirebaseRecyclerAdapter <JambConfirmationModel, JambViewHolder> adapter;

    private JambConfirmationModel jambConfirmationModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_student);


        eligibleStudents = FirebaseDatabase.getInstance().getReference().child("confirmJambNumbers");

        progressDialog = new ProgressDialog(this);


        //initialize our toolBar
        confirmToolBar = findViewById(R.id.confirmToolBar);
        setSupportActionBar(confirmToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Confirm JAMB/Mat. No");


        //views
        recyclerView_jambNo = findViewById(R.id.recycler_jamb);
        recyclerView_jambNo.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_jambNo.setLayoutManager(layoutManager);


        loadJambNumbers();



        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfirmStudentActivity.this);
        alertDialog.setTitle("Confirm JAMB/Mat. No.");
        alertDialog.setMessage("Please type the JAMB No. correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_jamb_number = inflater.inflate(R.layout.add_jamb_number_layout, null);
        
        editTextJambNo = add_jamb_number.findViewById(R.id.editTextJambOrMatNo);
        
        alertDialog.setView(add_jamb_number);


        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String jambNo = editTextJambNo.getText().toString();
                if (TextUtils.isEmpty(jambNo)){
                    Toast.makeText(ConfirmStudentActivity.this, "No JAMB number to confirm", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {

                        progressDialog.setTitle("JAMB Confirmation");
                        progressDialog.setMessage("Please wait");
                        progressDialog.show();
                        eligibleStudents.child(jambNo).child("jambNo").setValue(jambNo);
                        eligibleStudents.child(jambNo).child("status").setValue("Unused");
                        progressDialog.dismiss();
                        Toast.makeText(ConfirmStudentActivity.this, "Confirmed successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                }

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();





    }
//
//    private void addJambNo() {
//        jambConfirmationModel = new JambConfirmationModel(editTextJambNo.getText().toString(),"Unused");
//
//    }

    private void loadJambNumbers() {
        adapter = new FirebaseRecyclerAdapter<JambConfirmationModel, JambViewHolder>(
                JambConfirmationModel.class,
                R.layout.confirm_student_layout,
                JambViewHolder.class,
                eligibleStudents
        ) {
            @Override
            protected void populateViewHolder(JambViewHolder viewHolder, JambConfirmationModel model, int position) {
                viewHolder.txtStatus.setText(model.getStatus());
                viewHolder.txtJambNo.setText(model.getJambNo());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView_jambNo.setAdapter(adapter);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }else if (item.getTitle().equals(Common.DELETE)){
            deleteJamb(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(final String key, final JambConfirmationModel item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfirmStudentActivity.this);
        alertDialog.setTitle("Edit JAMB/Mat. No.");
        alertDialog.setMessage("Please type the JAMB No. correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_jamb_number = inflater.inflate(R.layout.add_jamb_number_layout, null);

        editTextJambNo = add_jamb_number.findViewById(R.id.editTextJambOrMatNo);

        editTextJambNo.setText(item.getJambNo());

        alertDialog.setView(add_jamb_number);



        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String jambNo = editTextJambNo.getText().toString();
                if (TextUtils.isEmpty(jambNo)){
                    Toast.makeText(ConfirmStudentActivity.this, "No JAMB number to edit", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {

                    progressDialog.setTitle("JAMB Confirmation");
                    progressDialog.setMessage("Please wait");
                    progressDialog.show();
                    item.setJambNo(jambNo);
                    eligibleStudents.child(key).setValue(item);
                    progressDialog.dismiss();
                    Toast.makeText(ConfirmStudentActivity.this, "Edited successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();

    }

    private void deleteJamb(String key) {
        eligibleStudents.child(key).removeValue();
        Toast.makeText(this, "JAMB No. deleted!", Toast.LENGTH_SHORT).show();
    }

}
