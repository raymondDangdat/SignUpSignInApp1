package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddStaff extends AppCompatActivity {
    private EditText editTextFullName, editTextPassword, editTextEmail, editTextCPassword;
    private Button btnAddStaff;
    private CircleImageView imageButtonStaff;

    private Toolbar addStaffToolBar;


    private FirebaseDatabase db;
    private DatabaseReference staff;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    private static final int GALLERY_REQUEST_CODE = 1;
    private Uri mImageUri = null;


    ProgressDialog mProgress;
    private ProgressDialog verificationProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextStaffEmail);
        editTextCPassword = findViewById(R.id.editTextCPassword);
        imageButtonStaff = findViewById(R.id.setUpImageButton);
        btnAddStaff = findViewById(R.id.buttonAddStaff);

        mProgress = new ProgressDialog(this);


        //init firebase
        db = FirebaseDatabase.getInstance();
        staff = db.getReference("staff");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("staffProfile");
        mAuth = FirebaseAuth.getInstance();

        //initialize our toolBar
        addStaffToolBar = findViewById(R.id.add_staff_toolbar);
        setSupportActionBar(addStaffToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Staff");




        //onclick listeners
        imageButtonStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });

        btnAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStaff();
            }
        });
    }

    private void addStaff() {
        mProgress.setMessage("Adding staff...");
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextCPassword.getText().toString();
        final String fullName = editTextFullName.getText().toString();


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) && mImageUri != null
                && !TextUtils.isEmpty(fullName)){
            if (password.equals(confirmPassword)){
                mProgress.show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        StorageReference filepath = storageReference.child(mImageUri.getLastPathSegment());
                        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final String user_id = mAuth.getCurrentUser().getUid();
                                String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                                staff.child(user_id).child("email").setValue(email);
                                staff.child(user_id).child("fullName").setValue(fullName);


                                staff.child(user_id).child("image").setValue(downloadUrl);
                                mProgress.dismiss();
                                Toast.makeText(AddStaff.this, "Staff added", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddStaff.this, Home.class));
                                //sendEmailVerification();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgress.dismiss();
                                Toast.makeText(AddStaff.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                finish();


                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgress.dismiss();
                        Toast.makeText(AddStaff.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }else {
                Toast.makeText(this, "Password miss match", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Sorry can not add staff with empty field(s) or without picture", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendEmailVerification() {
        verificationProgress.setMessage("Sending verification mail...");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){
            verificationProgress.show();
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        verificationProgress.dismiss();
                        Toast.makeText(AddStaff.this, "Registered successfully!! check verification mail sent to you", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    verificationProgress.dismiss();
                    Toast.makeText(AddStaff.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //to set it to square
                    .setAspectRatio(1,1)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                imageButtonStaff.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
