package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.HiewHolders.BoysHostelViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.BoysHostel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class BoysChaletActivity extends AppCompatActivity {


    //firebase
    private FirebaseDatabase database;
    private DatabaseReference boysChalets;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseRecyclerAdapter<BoysHostel, BoysHostelViewHolder> adapter;

    RecyclerView recyclerView_chalets;
    RecyclerView.LayoutManager layoutManager;

    private Toolbar chaletToolBar;

    BoysHostel newBoysHostel;
    private Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 90;

    //add new chalet layout
    private MaterialEditText editTextChaletNumber;
    private Button btnUpload, btnSelect;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boys_chalet);



        //firebase
        database = FirebaseDatabase.getInstance();
        boysChalets = database.getReference("boysHostel");
        boysChalets.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        //initialize our toolBar
        chaletToolBar = findViewById(R.id.boys_chalet_toolbar);
        setSupportActionBar(chaletToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Boys Chalets");

        //views
        recyclerView_chalets = findViewById(R.id.recycler_chalets);
        recyclerView_chalets.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_chalets.setLayoutManager(layoutManager);


        //loadChalets
        loadChalets();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });




    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BoysChaletActivity.this);
        alertDialog.setTitle("Add new boys chalet");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_chalet_layout = inflater.inflate(R.layout.add_new_boys_chalet_layout, null);

        editTextChaletNumber = add_chalet_layout.findViewById(R.id.edtChaletNumber);
        btnSelect = add_chalet_layout.findViewById(R.id.btnSelect);
        btnUpload = add_chalet_layout.findViewById(R.id.btnUpload);

        alertDialog.setView(add_chalet_layout);
        alertDialog.setIcon(R.drawable.ic_home_black_24dp);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        //set button
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //we just create a new category
                if (newBoysHostel != null){
                    boysChalets.push().setValue(newBoysHostel);
                    Toast.makeText(BoysChaletActivity.this, "Chalet added successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(BoysChaletActivity.this, "New category is empty", Toast.LENGTH_SHORT).show();
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

    private void uploadImage() {
        if (saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("BoysChaletImages/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(BoysChaletActivity.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new chalet if image upload and we can get download link
                            newBoysHostel = new BoysHostel(editTextChaletNumber.getText().toString(), uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(BoysChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(BoysChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploading... " +progress + "%");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            saveUri = data.getData();
            btnSelect.setText("Image selected");
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    private void loadChalets() {
        adapter = new FirebaseRecyclerAdapter<BoysHostel, BoysHostelViewHolder>(
                BoysHostel.class,
                R.layout.boys_hostel_item_layout,
                BoysHostelViewHolder.class,
                boysChalets
        ) {
            @Override
            protected void populateViewHolder(BoysHostelViewHolder viewHolder, BoysHostel model, int position) {
                viewHolder.txtChaletNumber.setText(model.getRoom());
                Picasso.get().load(model.getImage()).into(viewHolder.imageViewChalet);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //send chalet ID and start new activity
                        Intent roomList = new Intent(BoysChaletActivity.this, RoomList.class);
                        roomList.putExtra("chaletId", adapter.getRef(position).getKey());
                        startActivity(roomList);

                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView_chalets.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }else if (item.getTitle().equals(Common.DELETE)){
            deleteChalet(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteChalet(String key) {
        boysChalets.child(key).removeValue();
        Toast.makeText(this, "Chalet deleted!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final BoysHostel item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BoysChaletActivity.this);
        alertDialog.setTitle("Update boys chalet");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_chalet_layout = inflater.inflate(R.layout.add_new_boys_chalet_layout, null);

        editTextChaletNumber = add_chalet_layout.findViewById(R.id.edtChaletNumber);
        btnSelect = add_chalet_layout.findViewById(R.id.btnSelect);
        btnUpload = add_chalet_layout.findViewById(R.id.btnUpload);

        editTextChaletNumber.setText(item.getRoom());

        alertDialog.setView(add_chalet_layout);
        alertDialog.setIcon(R.drawable.ic_home_black_24dp);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //update information
                item.setRoom(editTextChaletNumber.getText().toString());
                boysChalets.child(key).setValue(item);

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    private void changeImage(final BoysHostel item) {
        if (saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("BoysChaletImages/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(BoysChaletActivity.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new chalet if image upload and we can get download link
                            item.setImage(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(BoysChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(BoysChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded " +progress + "%");
                }
            });
        }
    }
}
