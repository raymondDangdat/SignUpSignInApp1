package com.example.raymond.signupsigninapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;

public class PostStudentActivity extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;// for sorting
    SharedPreferences mSharedPref; //for saving sort settings
    private RecyclerView mRecyclerView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_student);

        //ActionBar
        ActionBar actionBar =  getSupportActionBar();
        actionBar.setTitle("List of students");

        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); //where if no settings newest will be the default
        if (mSorting.equals("newest")){
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means newest first
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);

        }else if (mSorting.equals("oldest")){
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means oldest first
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);

        }
        mRecyclerView = findViewById(R.id.recycler_view_student);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Students");



    }

    //search student
    private void firebaseSearch(String searchText){
        final Query firebaseSearchQuery = mRef.orderByChild("surname").startAt(searchText).endAt(searchText + "\uf8fff");
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.student_item,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getApplicationContext(),(model.getUsername()), model.getSurname(),
                                model.getImage(), model.getPhone(), model.getLastName(), model.getFirstName(),
                                model.getEmergencyNo(), model.getMatNo(), model.getDepartment(), model.getEmail(),
                                model.getFaculty(), model.getGender());

                    }


                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.clickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //views
                                TextView mSurnameTv = view.findViewById(R.id.textView_username);
                                TextView mUsernameTv = view.findViewById(R.id.textView_username);
                                ImageView mImageView = view.findViewById(R.id.image_view_profile_pic);

                                //get data from views
                                String mSurname = mSurnameTv.getText().toString();
                                String mUsername = mUsernameTv.getText().toString();
                                Drawable mDrawable = mImageView.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), PostStudentDedailsActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", bytes);
                                intent.putExtra("surname", mSurname);
                                intent.putExtra("username", mUsername);
                                startActivity(intent);





                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //to be implemented

                            }
                        });
                        return viewHolder;
                    }
                };

        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    //load data into recycleview

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder>firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row,
                        ViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getApplicationContext(),(model.getUsername()), model.getSurname(),
                                model.getImage(), model.getPhone(), model.getLastName(), model.getFirstName(),
                                model.getEmergencyNo(), model.getMatNo(), model.getDepartment(), model.getEmail(),
                                model.getFaculty(), model.getGender());
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.clickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //views
                                TextView mSurnameTv = view.findViewById(R.id.text_view_username);
                                TextView mUsernameTv = view.findViewById(R.id.text_view_username);
                                ImageView mImageView = view.findViewById(R.id.image_view_profile);

                                //get data from views
                                String mSurname = mSurnameTv.getText().toString();
                                String mUsername = mUsernameTv.getText().toString();
                                Drawable mDrawable = mImageView.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), PostStudentDedailsActivity.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", bytes);
                                intent.putExtra("surname", mSurname);
                                intent.putExtra("username", mUsername);
                                startActivity(intent);





                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //to be implemented

                            }
                        });
                        return viewHolder;
                    }
                };

        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //optionMenu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this add item to action ba
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //fileter as you type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //handle other actionBar
        if (id == R.id.action_logout){

            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(PostStudentActivity.this, MainActivity.class));


        }
        if (id == R.id.action_sort){
            //display alert dialog to choose sorting style
            showSortDialog();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        //options to display
        String[] sortOptions = {" Newest", "Oldest"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_action_sort)//set icon
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the 'which' argument contains the index position of the selected item
                        // 0 means "newest" and 1 means "oldest"
                        if (which == 0){
                            //sort by oldest

                        }else if (which == 1){
                            //sort by oldest

                        }
                    }
                });
        builder.show();
    }
}
