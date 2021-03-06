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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.HiewHolders.BoysRoomViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.BoysHostel;
import com.example.raymond.signupsigninapp.Modell.BoysRoom;
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

import static com.example.raymond.signupsigninapp.Common.Common.PICK_IMAGE_REQUEST;

public class RoomList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;
    BoysRoom newBoysRoom;

    private Uri saveUri;

    //firebase
    private FirebaseDatabase db;
    private DatabaseReference boysRoomList;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    String chaletId = "";

    private int countRoom;
    private TextView txtRoomCount, txtCount;

    private FirebaseRecyclerAdapter<BoysRoom, BoysRoomViewHolder> adapter;

    //add new room
    private MaterialEditText editTextRoomDescription, editTextBedNumber;
    //private Button btnSelect, btnUpload;

    private Toolbar roomToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);


        txtRoomCount = findViewById(R.id.number_of_rooms);
        txtCount = findViewById(R.id.totalBeds);



        //int firebase
        db = FirebaseDatabase.getInstance();
        boysRoomList = db.getInstance().getReference().child("plasuHostel2019").child("hostels").child("boysHostel").child("BoysRooms");
        boysRoomList.keepSynced(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        //initialize our toolBar
        roomToolBar = findViewById(R.id.boys_room_toolbar);
        setSupportActionBar(roomToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Boys Bed Spaces");


        //init
        recyclerView = findViewById(R.id.recycler_boys_room);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
        boysRoomList.orderByChild("room").equalTo(chaletId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countRoom = (int) dataSnapshot.getChildrenCount();
                    txtRoomCount.setText(Integer.toString(countRoom));

                }else{
                    txtRoomCount.setText("No Bed");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onStart();
    }


    private void showAddBoysRoomDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomList.this);
        alertDialog.setTitle("Add new boys bed space");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_boys_room_layout = inflater.inflate(R.layout.add_new_boys_room_layout, null);

        editTextRoomDescription = add_new_boys_room_layout.findViewById(R.id.edtRoomDescription);
        editTextBedNumber = add_new_boys_room_layout.findViewById(R.id.edtBedBumber);
//        btnSelect = add_new_boys_room_layout.findViewById(R.id.btnSelect);
//        btnUpload = add_new_boys_room_layout.findViewById(R.id.btnUpload);

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
//                uploadImage();
//            }
//        });

        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                newBoysRoom = new BoysRoom();
                newBoysRoom.setRoomDescription(editTextRoomDescription.getText().toString());
                newBoysRoom.setBedNumber(editTextBedNumber.getText().toString());
                newBoysRoom.setRoom(chaletId);
                newBoysRoom.setStatus("available");
                newBoysRoom.setImage("null");


                //we just create a new category
                if (newBoysRoom != null){
                    boysRoomList.push().setValue(newBoysRoom);
                    Toast.makeText(RoomList.this, "Bed space added successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RoomList.this, "New room is empty", Toast.LENGTH_SHORT).show();
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

    private void loadRoomList(String chaletId) {
        FirebaseRecyclerOptions<BoysRoom>options = new FirebaseRecyclerOptions.Builder<BoysRoom>()
                .setQuery(boysRoomList.orderByChild("room").equalTo(chaletId), BoysRoom.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<BoysRoom, BoysRoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoysRoomViewHolder holder, int position, @NonNull BoysRoom model) {
                holder.txtRoomDescription.setText(model.getRoomDescription());
                holder.txtBedNumber.setText(model.getBedNumber());
                holder.txtStatus.setText(model.getStatus());
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
            public BoysRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boys_room_item, parent, false);
                BoysRoomViewHolder viewHolder = new BoysRoomViewHolder(view);
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


    //sorting
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort) {
            //display alert to choose sort type
            showSortDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        //Options to display
        String[] sortOptions = {"All Beds", "Available Beds", "Occupied Beds"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Boys Bed Summary: ")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the which contains the index position of the selected item
                        if (which==0){
                            //All beds selected
                            loadAllBeds(chaletId);
                        }else if (which==1){
                            //load available beds
                            loadAvailableBeds(chaletId);
                        }else if (which==2){
                            //occupied beds
                            loadOccupiedBeds(chaletId);
                        }
                    }
                });
        builder.show();
    }

    private void loadAllBeds(String chaletId) {

        //count
        boysRoomList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countRoom = (int) dataSnapshot.getChildrenCount();
                    txtRoomCount.setText(Integer.toString(countRoom));

                }else{
                    txtRoomCount.setText("No Bed");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions<BoysRoom>options = new FirebaseRecyclerOptions.Builder<BoysRoom>()
                .setQuery(boysRoomList, BoysRoom.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<BoysRoom, BoysRoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoysRoomViewHolder holder, int position, @NonNull BoysRoom model) {
                holder.txtRoomDescription.setText(model.getRoomDescription());
                holder.txtBedNumber.setText(model.getBedNumber());
                holder.txtStatus.setText(model.getStatus());
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
            public BoysRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boys_room_item, parent, false);
                BoysRoomViewHolder viewHolder = new BoysRoomViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void loadAvailableBeds(String chaletId) {
        txtCount.setText("Available beds");

        //available beds
        boysRoomList.orderByChild("status").equalTo("available").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countRoom = (int) dataSnapshot.getChildrenCount();
                    txtRoomCount.setText(Integer.toString(countRoom));

                }else{
                    txtRoomCount.setText("No Available Bed");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions<BoysRoom>options = new FirebaseRecyclerOptions.Builder<BoysRoom>()
                .setQuery(boysRoomList.orderByChild("status").equalTo("available"), BoysRoom.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<BoysRoom, BoysRoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoysRoomViewHolder holder, int position, @NonNull BoysRoom model) {
                holder.txtRoomDescription.setText(model.getRoomDescription());
                holder.txtBedNumber.setText(model.getBedNumber());
                holder.txtStatus.setText(model.getStatus());
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
            public BoysRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boys_room_item, parent, false);
                BoysRoomViewHolder viewHolder = new BoysRoomViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadOccupiedBeds(String chaletId) {
        txtCount.setText("Occupied beds");
        //occupied
        boysRoomList.orderByChild("status").equalTo("occupied").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    countRoom = (int) dataSnapshot.getChildrenCount();
                    txtRoomCount.setText(Integer.toString(countRoom));

                }else{
                    txtRoomCount.setText("No Occupied Bed");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions<BoysRoom>options = new FirebaseRecyclerOptions.Builder<BoysRoom>()
                .setQuery(boysRoomList.orderByChild("status").equalTo("occupied"), BoysRoom.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<BoysRoom, BoysRoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BoysRoomViewHolder holder, int position, @NonNull BoysRoom model) {
                holder.txtRoomDescription.setText(model.getRoomDescription());
                holder.txtBedNumber.setText(model.getBedNumber());
                holder.txtStatus.setText(model.getStatus());
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
            public BoysRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boys_room_item, parent, false);
                BoysRoomViewHolder viewHolder = new BoysRoomViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
            final StorageReference imageFolder = storageReference.child("BoysRoomImages/" + imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(RoomList.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //set value for new chalet if image upload and we can get download link
                            newBoysRoom = new BoysRoom();
                            newBoysRoom.setRoomDescription(editTextRoomDescription.getText().toString());
                            newBoysRoom.setBedNumber(editTextBedNumber.getText().toString());
                            newBoysRoom.setRoom(chaletId);
                            newBoysRoom.setStatus("available");
                            newBoysRoom.setImage(uri.toString());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(RoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(RoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        boysRoomList.child(key).removeValue();
    }

    private void showUpdateBoysRoomDialog(final String key, final BoysRoom item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RoomList.this);
        alertDialog.setTitle("Edit Bed Info");
        alertDialog.setMessage("Please fill the information correctly");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_new_boys_room_layout = inflater.inflate(R.layout.add_new_boys_room_layout, null);

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
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeImage(item);
//            }
//        });
//
        //set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //we just create a new category

                    item.setRoomDescription(editTextRoomDescription.getText().toString());
                    item.setBedNumber(editTextBedNumber.getText().toString());
                    boysRoomList.child(key).setValue(item);
                    Toast.makeText(RoomList.this, "Bed info edited  successfully", Toast.LENGTH_SHORT).show();


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

    private void changeImage(final BoysRoom item) {
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
                    Toast.makeText(RoomList.this, "Uploaded !!!!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(RoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(RoomList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
