package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.HiewHolders.BoysHostelViewHolder;
import com.example.raymond.signupsigninapp.HiewHolders.GirlsHostelViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.BoysHostel;
import com.example.raymond.signupsigninapp.Modell.GirlsHostel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class GirlsChaletActivity extends AppCompatActivity {

    //firebase
    private FirebaseDatabase database;
    private DatabaseReference girlsChalets;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseRecyclerAdapter<GirlsHostel, GirlsHostelViewHolder> adapter;

    RecyclerView recyclerView_girls_chalets;
    RecyclerView.LayoutManager layoutManager;

    GirlsHostel newGirlsHostel;
    private Uri saveUri;
    private final int PICK_IMAGE_REQUEST = 90;

    private Toolbar chaletToolBar;

    private int countChalet;
    private TextView txtChaletCount;

    //add new chalet layout
    private MaterialEditText editTextChaletNumber;
    //private Button btnUpload, btnSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girls_chalet);

        txtChaletCount = findViewById(R.id.txtChaletCount);


        //firebase
        database = FirebaseDatabase.getInstance();
        girlsChalets = database.getInstance().getReference().child("plasuHostel2019").child("hostels").child("girlsHostel");
        girlsChalets.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        //initialize our toolBar
        chaletToolBar = findViewById(R.id.girls_chalet_toolbar);
        setSupportActionBar(chaletToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Girls Chalets");

        //views
        recyclerView_girls_chalets = findViewById(R.id.recycler_girls_chalets);
        recyclerView_girls_chalets.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_girls_chalets.setLayoutManager(layoutManager);


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

    @Override
    protected void onStart() {
        loadChalets();
        girlsChalets.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countChalet = (int) dataSnapshot.getChildrenCount();
                    txtChaletCount.setText(Integer.toString(countChalet));

                }else{
                    txtChaletCount.setText("No Chalet");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    private void loadChalets() {
        FirebaseRecyclerOptions<GirlsHostel>options = new FirebaseRecyclerOptions.Builder<GirlsHostel>()
                .setQuery(girlsChalets, GirlsHostel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<GirlsHostel, GirlsHostelViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GirlsHostelViewHolder holder, int position, @NonNull GirlsHostel model) {
                holder.txtChaletNumber.setText(model.getRoom());
                //Picasso.get().load(model.getImage()).into(viewHolder.imageViewChalet);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //send chalet ID and start new activity
                        Intent roomList = new Intent(GirlsChaletActivity.this, GirlsRoomList.class);
                        roomList.putExtra("chaletId", adapter.getRef(position).getKey());
                        startActivity(roomList);

                    }
                });
            }

            @NonNull
            @Override
            public GirlsHostelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.girls_hostel_item_layout, parent, false);
                GirlsHostelViewHolder viewHolder = new GirlsHostelViewHolder(view);
                return viewHolder;
            }
        };

       recyclerView_girls_chalets.setAdapter(adapter);
       adapter.startListening();
    }


    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GirlsChaletActivity.this);
        alertDialog.setTitle("Add new girls chalet");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_chalet_layout = inflater.inflate(R.layout.add_new_girls_chalet_layout, null);

        editTextChaletNumber = add_chalet_layout.findViewById(R.id.edtChaletNumber);
//        btnSelect = add_chalet_layout.findViewById(R.id.btnSelect);
//        btnUpload = add_chalet_layout.findViewById(R.id.btnUpload);

        alertDialog.setView(add_chalet_layout);
        alertDialog.setIcon(R.drawable.ic_home_black_24dp);
//
//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseImage();
//            }
//        });
//
//
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImage();
//            }
//        });

        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                newGirlsHostel = new GirlsHostel(editTextChaletNumber.getText().toString(), "null");

                //we just create a new chalet
                if (newGirlsHostel != null){
                    girlsChalets.push().setValue(newGirlsHostel);
                    Toast.makeText(GirlsChaletActivity.this, "Chalet added successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GirlsChaletActivity.this, "New chalet info is empty", Toast.LENGTH_SHORT).show();
                }

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

    private void uploadImage() {
        if (saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("GirllsChaletImages/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(GirlsChaletActivity.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new chalet if image upload and we can get download link
                            newGirlsHostel = new GirlsHostel(editTextChaletNumber.getText().toString(), uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(GirlsChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(GirlsChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            //btnSelect.setText("Image selected");
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

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
        girlsChalets.child(key).removeValue();
        Toast.makeText(this, "Chalet deleted!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final GirlsHostel item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GirlsChaletActivity.this);
        alertDialog.setTitle("Update girls chalet");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_chalet_layout = inflater.inflate(R.layout.add_new_girls_chalet_layout, null);

        editTextChaletNumber = add_chalet_layout.findViewById(R.id.edtChaletNumber);
//        btnSelect = add_chalet_layout.findViewById(R.id.btnSelect);
//        btnUpload = add_chalet_layout.findViewById(R.id.btnUpload);

        editTextChaletNumber.setText(item.getRoom());

        alertDialog.setView(add_chalet_layout);
        alertDialog.setIcon(R.drawable.ic_home_black_24dp);

//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseImage();
//            }
//        });
//
//
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeImage(item);
//            }
//        });

        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //update information
                item.setRoom(editTextChaletNumber.getText().toString());
                girlsChalets.child(key).setValue(item);

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

    private void changeImage(final GirlsHostel item) {
        if (saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("GirlsChaletImages/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(GirlsChaletActivity.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GirlsChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(GirlsChaletActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
