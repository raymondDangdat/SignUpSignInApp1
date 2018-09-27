package com.example.raymond.signupsigninapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentActivity extends AppCompatActivity implements StudentAdapter.onItemClickListener {
    private RecyclerView mRecyclerView;
    private StudentAdapter mAdapter;
    private EditText mSearchField;
    private ImageButton mSearchBtn;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private List<StudentUpload> mUploads;

    private ProgressBar mProgressCircle;
    private ProgressDialog searchProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("List of students");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mSearchBtn = findViewById(R.id.search_btn);
        mSearchField = findViewById(R.id.search_field);

        searchProgress = new ProgressDialog(this);

        mRecyclerView = findViewById(R.id.recycler_view_student);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mUploads = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Students");

        mProgressCircle = findViewById(R.id.progress_circle);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mSearchField.getText().toString().trim();
                if (!TextUtils.isEmpty(searchText)){
                    firebaseUserSearch(searchText);
                    searchProgress.dismiss();

                }else{
                    Toast.makeText(StudentActivity.this, "Type a surname and click search", Toast.LENGTH_SHORT).show();
                }

            }
        });


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    StudentUpload upload = postSnapshot.getValue(StudentUpload.class);
                    mUploads.add(upload);
                }

                mAdapter = new StudentAdapter(StudentActivity.this, mUploads);

                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(StudentActivity.this);

                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });

    }


    private void firebaseSearch(String searchText){
        Query fireBaseSearchQuery = mDatabaseRef.orderByChild("surname").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerAdapter<StudentUpload, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<StudentUpload, UserViewHolder>(
                StudentUpload.class,
                R.layout.student_item,
                UserViewHolder.class,
                fireBaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, StudentUpload model, int position) {
                viewHolder.setDetails(getApplicationContext(),(model.getUsername()), model.getSurname(),
                        model.getImage(), model.getPhone(), model.getLastName(), model.getFirstName(),
                        model.getEmergencyNo(), model.getMatNo(), model.getDepartment(), model.getEmail(),
                        model.getFaculty(), model.getGender());

            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    private void firebaseUserSearch(String searchText){
        searchProgress.setMessage("Searching...");
        searchProgress.show();
        //Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
        Query firebaseSearchQuery = mDatabaseRef.orderByChild("matNo").startAt(searchText).endAt(searchText + "\uf8ff");
        FirebaseRecyclerAdapter<StudentUpload, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<StudentUpload, UserViewHolder>(
                StudentUpload.class,
                R.layout.student_item,
                UserViewHolder.class,
                firebaseSearchQuery

        )
        {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, StudentUpload model, int position) {
                viewHolder.setDetails(getApplicationContext(),(model.getUsername()), model.getSurname(),
                        model.getImage(), model.getPhone(), model.getLastName(), model.getFirstName(),
                        model.getEmergencyNo(), model.getMatNo(), model.getDepartment(), model.getEmail(),
                        model.getFaculty(), model.getGender());

            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal click", Toast.LENGTH_SHORT).show();

    }

    @Override
    public Void onItemClickDelete(int position) {
        Toast.makeText(this, "Are you sure you want to delete?", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onItemClickViewProfile(int position) {
        Toast.makeText(this, "To be implemented soon...", Toast.LENGTH_SHORT).show();

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public UserViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setDetails(Context context, String username, String userSurname, String userImage, String fName, String phone,
                               String lName, String mNo, String emerNo, String eMail,
                               String gEnder, String fculty,String dpartment){
            TextView user_name = mView.findViewById(R.id.text_view_username);
            TextView sur_name = mView.findViewById(R.id.text_view_surname);
            ImageView profileImage = mView.findViewById(R.id.image_view_profile);
            TextView matNo = mView.findViewById(R.id.text_view_matriculationNo);
            TextView phoneNo = mView.findViewById(R.id.text_view_phone);
            TextView emergencyNo = mView.findViewById(R.id.text_view_emergencyNo);
            TextView first_name = mView.findViewById(R.id.text_view_fName);
            TextView last_name = mView.findViewById(R.id.text_view_LName);
            TextView email = mView.findViewById(R.id.text_view_email);
            TextView gender = mView.findViewById(R.id.text_view_gender);
            TextView faculty = mView.findViewById(R.id.text_view_faculty);
            TextView department = mView.findViewById(R.id.text_view_department);


            user_name.setText(username);
            matNo.setText(mNo);
            phoneNo.setText(phone);
            emergencyNo.setText(emerNo);
            email.setText(eMail);
            first_name.setText(fName);
            last_name.setText(lName);
            gender.setText(gEnder);
            faculty.setText(fculty);
            department.setText(dpartment);
            sur_name.setText(userSurname);
            Glide.with(context).load(userImage).into(profileImage);


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                //filter as you type
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //handle other action bar clicks
        if (id == R.id.action_logout){
            mAuth.signOut();
            finish();
            startActivity(new Intent(StudentActivity.this, MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }
}
