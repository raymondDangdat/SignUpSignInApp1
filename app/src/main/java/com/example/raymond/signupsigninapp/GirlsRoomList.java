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
import com.example.raymond.signupsigninapp.HiewHolders.BoysRoomViewHolder;
import com.example.raymond.signupsigninapp.HiewHolders.GirlsRoomViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.BoysRoom;
import com.example.raymond.signupsigninapp.Modell.GirlsRoom;
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

public class GirlsRoomList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;
    GirlsRoom newGirlsRoom;

    private Toolbar roomToolBar;

    private Uri saveUri;

    //firebase
    private FirebaseDatabase db;
    private DatabaseReference girlsRoomList;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    String chaletId = "";

    private FirebaseRecyclerAdapter<GirlsRoom, GirlsRoomViewHolder> adapter;

    //add new room
    private MaterialEditText editTextRoomDescription, editTextBedNumber;
    //private Button btnSelect, btnUpload;

    private int countRoom;
    private TextView txtRoomCount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girls_room_list);


        //int firebase
        db = FirebaseDatabase.getInstance();
        girlsRoomList = db.getInstance().getReference().child("plasuHostel2019").child("hostels").child("girlsHostel").child("GirlsRooms");
        girlsRoomList.keepSynced(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("GirlsRoomImages");

        txtRoomCount = findViewById(R.id.number_of_rooms);


        //init
        recyclerView = findViewById(R.id.recycler_girls_room);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        //initialize our toolBar
        roomToolBar = findViewById(R.id.girls_room_toolbar);
        setSupportActionBar(roomToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Girls Bed Spaces");



        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddBoysRoomDialog();
            }
        });

        if (getIntent() != null){
            chaletId = getIntent().getStringExtra("chaletId");
            if (!chaletId.isEmpty()){
                loadRoomList(chaletId);
            }else{
                Toast.makeText(this, "No chalet ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        girlsRoomList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countRoom = (int) dataSnapshot.getChildrenCount();
                    txtRoomCount.setText(Integer.toString(countRoom));

                }else{
                    txtRoomCount.setText("No Room");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }

    private void showAddBoysRoomDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GirlsRoomList.this);
        alertDialog.setTitle("Add new girls room");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_boys_room_layout = inflater.inflate(R.layout.add_new_girls_room_layout, null);

        editTextRoomDescription = add_new_boys_room_layout.findViewById(R.id.edtRoomDescription);
        editTextBedNumber = add_new_boys_room_layout.findViewById(R.id.edtBedBumber);
//        btnSelect = add_new_boys_room_layout.findViewById(R.id.btnSelect);
//        btnUpload = add_new_boys_room_layout.findViewById(R.id.btnUpload);

        alertDialog.setView(add_new_boys_room_layout);
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

                newGirlsRoom = new GirlsRoom();
                newGirlsRoom.setRoomDescription(editTextRoomDescription.getText().toString());
                newGirlsRoom.setBedNumber(editTextBedNumber.getText().toString());
                newGirlsRoom.setRoom(chaletId);
                newGirlsRoom.setStatus("available");
                newGirlsRoom.setImage("null");


                //we just create a new category
                if (newGirlsRoom != null){
                    girlsRoomList.push().setValue(newGirlsRoom);
                    Toast.makeText(GirlsRoomList.this, "Room added successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(GirlsRoomList.this, "New room is empty", Toast.LENGTH_SHORT).show();
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

    private void loadRoomList(String chaletId){
        FirebaseRecyclerOptions<GirlsRoom>options = new FirebaseRecyclerOptions.Builder<GirlsRoom>()
                .setQuery(girlsRoomList.orderByChild("room").equalTo(chaletId),GirlsRoom.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<GirlsRoom, GirlsRoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GirlsRoomViewHolder holder, int position, @NonNull GirlsRoom model) {
                holder.txtRoomDescription.setText(model.getRoomDescription());
                holder.txtStatus.setText(model.getStatus());
                holder.txtBedNumber.setText(model.getBedNumber());
                //Picasso.get().load(model.getImage()).into(viewHolder.imageViewRoom);


                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //code later
                    }
                });
            }

            @NonNull
            @Override
            public GirlsRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.girls_room_item, parent, false);
                GirlsRoomViewHolder viewHolder = new GirlsRoomViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    //choose image
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            saveUri = data.getData();
            //btnSelect.setText("Image selected");
        }
    }

    private void uploadImage() {
        if (saveUri != null){
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            final String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("GirlsRoomImages/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(GirlsRoomList.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new chalet if image upload and we can get download link
                            newGirlsRoom = new GirlsRoom();
                            newGirlsRoom.setRoomDescription(editTextRoomDescription.getText().toString());
                            newGirlsRoom.setBedNumber(editTextBedNumber.getText().toString());
                            newGirlsRoom.setRoom(chaletId);
                            newGirlsRoom.setStatus("available");
                            newGirlsRoom.setImage(uri.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(GirlsRoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(GirlsRoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateBoysRoomDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }else if (item.getTitle().equals(Common.DELETE)){
            deleteRoom(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteRoom(String key) {
        girlsRoomList.child(key).removeValue();
    }

    private void showUpdateBoysRoomDialog(final String key, final GirlsRoom item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GirlsRoomList.this);
        alertDialog.setTitle("Edit room");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_boys_room_layout = inflater.inflate(R.layout.add_new_girls_room_layout, null);

        editTextRoomDescription = add_new_boys_room_layout.findViewById(R.id.edtRoomDescription);
        editTextBedNumber = add_new_boys_room_layout.findViewById(R.id.edtBedBumber);
//        btnSelect = add_new_boys_room_layout.findViewById(R.id.btnSelect);
//        btnUpload = add_new_boys_room_layout.findViewById(R.id.btnUpload);


        //set default values
        editTextRoomDescription.setText(item.getRoomDescription());
        editTextBedNumber.setText(item.getBedNumber());

        alertDialog.setView(add_new_boys_room_layout);
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

                //we just create a new category

                item.setRoomDescription(editTextRoomDescription.getText().toString());
                item.setBedNumber(editTextBedNumber.getText().toString());
                girlsRoomList.child(key).setValue(item);
                Toast.makeText(GirlsRoomList.this, "Room edited  successfully", Toast.LENGTH_SHORT).show();


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

    private void changeImage(final GirlsRoom item) {
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
                    Toast.makeText(GirlsRoomList.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GirlsRoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(GirlsRoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
