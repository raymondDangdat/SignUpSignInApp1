package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Modell.JambConfirmation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ConfirmStudentActivity extends AppCompatActivity {
    private MaterialEditText editTextMatJambNo;
    private Button buttonConfirm;


    private DatabaseReference eligibleStudents;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_student);


        eligibleStudents = FirebaseDatabase.getInstance().getReference().child("Eligible Students");

        progressDialog = new ProgressDialog(this);


        editTextMatJambNo = findViewById(R.id.edtMatOrJamb);
        buttonConfirm = findViewById(R.id.btnConfirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmStudent();
            }
        });
    }

    private void confirmStudent() {
        final String matOrJambNo = editTextMatJambNo.getText().toString().trim();

        if (TextUtils.isEmpty(matOrJambNo)){
            Toast.makeText(this, "Type the student Mat. No or Jamb No", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Confirming Student");
            progressDialog.setMessage("Confirming please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //search in the database if the number has been confirm already

            eligibleStudents.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(matOrJambNo).exists()){


                        editTextMatJambNo.setText("");
                        progressDialog.dismiss();

                        JambConfirmation jambConfirmation = new JambConfirmation(matOrJambNo, "valid");
                        eligibleStudents.child(matOrJambNo).setValue(jambConfirmation).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(ConfirmStudentActivity.this, "Confirmation Successfull", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }
                        );
                    }else{
                        editTextMatJambNo.setText("");
                        progressDialog.dismiss();
                        Toast.makeText(ConfirmStudentActivity.this, "Number already confirmed", Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
