package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddHostel extends AppCompatActivity {
    private EditText editTextRoomNumber;
    private EditText editTextRoomDesc;
    private Button buttonAddRoom;
    private TextView textViewViewRooms, searchStudent, viewStudents, studentView;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hostel);


        //initialize variables
        editTextRoomNumber = findViewById(R.id.editTextHostelNumber);
        editTextRoomDesc = findViewById(R.id.roomDescription);
        buttonAddRoom = findViewById(R.id.buttonAdd);
        textViewViewRooms = findViewById(R.id.textViewViewRoom);
        searchStudent = findViewById(R.id.seachStudent);
        viewStudents = findViewById(R.id.hostelStudents);
        studentView = findViewById(R.id.viewStudents);


        mProgress = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Rooms");

        studentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddHostel.this, PostStudentActivity.class));
            }
        });


        searchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddHostel.this, SearchStudent.class));
            }
        });

        viewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddHostel.this, StudentActivity.class));
            }
        });



        //setOnclickListeners
        buttonAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setMessage("Adding room...");
                String roomNumber = editTextRoomNumber.getText().toString().trim();
                String roomDescription = editTextRoomDesc.getText().toString().trim();

                if (TextUtils.isEmpty(roomNumber) || TextUtils.isEmpty(roomDescription)){
                    Toast.makeText(AddHostel.this, "Hostel name and description cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    mProgress.show();
                    Upload upload = new Upload(editTextRoomNumber.getText().toString().trim(), editTextRoomDesc.getText().toString().trim());
                    String hostelId = mDatabase.push().getKey();
                    mDatabase.child(hostelId).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mProgress.dismiss();
                            Toast.makeText(AddHostel.this, "Room added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddHostel.this, HostelActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgress.dismiss();
                            Toast.makeText(AddHostel.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });



        textViewViewRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddHostel.this, HosActivity.class));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(AddHostel.this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
