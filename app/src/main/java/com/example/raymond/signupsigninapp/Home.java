package com.example.raymond.signupsigninapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.HiewHolders.OccupantsViewHolder;
import com.example.raymond.signupsigninapp.HiewHolders.StudentViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.Occupants;
import com.example.raymond.signupsigninapp.Modell.Student;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    private FirebaseDatabase database;
//    private DatabaseReference applicants;
     private FirebaseUser mUser;
//
//    private MaterialSearchBar searchBar;
//
//    ArrayList<Occupants> arrayList;
//
//    private RecyclerView recyclerView;
//    private RecyclerView.LayoutManager layoutManager;
//
//    private DatabaseReference staff;

    private String userId;

//    private TextView fullName, email;
//    private ImageView imgProfile;
//
//    private FirebaseRecyclerAdapter<Occupants, OccupantsViewHolder> adapter;

    // rework
    private ImageButton img_gdom, img_boysdom, img_confirmStudent, img_hostelRules;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hostel Administrator");
        setSupportActionBar(toolbar);

        //rework
        img_boysdom = findViewById(R.id.img_bdom);
        img_gdom = findViewById(R.id.img_gdom);
        img_confirmStudent = findViewById(R.id.img_confirmStudent);
        img_hostelRules = findViewById(R.id.img_hostelRules);


        //rework
        //setOnclick listener to the buttons
        img_boysdom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, BoysChaletActivity.class));
            }
        });

        img_gdom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, GirlsChaletActivity.class));
            }
        });

        img_confirmStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, GenerateRegPin.class));
            }
        });

        img_hostelRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, HostelRules.class));
            }
        });

        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        applicants = database.getReference("Applications");
//
//        searchBar = findViewById(R.id.searchBar);
//
//        arrayList = new ArrayList<>();
////
//        searchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!s.toString().isEmpty()){
//                    search(s.toString());
//                }else {
//                    loadOccupants();
//                }
//
//            }
//        });

//
//        //init
//        recyclerView = findViewById(R.id.recycler_occupants);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

//        //initialize our lists
//        fullNameList = new ArrayList<>();
//        phoneList = new ArrayList<>();
//        matNoList = new ArrayList<>();
//        profilePicList = new ArrayList<>();
//        genderList = new ArrayList<>();
//

//        searchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!s.toString().isEmpty()){
//                    //set adapter method
//                    setAdapter(s.toString());
//                }else {
//                    //clear arrayList each time
//                    fullNameList.clear();
//                    matNoList.clear();
//                    profilePicList.clear();
//                    phoneList.clear();
//                    genderList.clear();
//                    recyclerView.removeAllViews();
//
//                    loadOccupants();
//
//                }
//
//            }
//        });


        mUser = auth.getCurrentUser();
        userId = auth.getUid();


        final FirebaseUser user = auth.getCurrentUser();

        //staff = database.getReference().child("staff");







//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Add Chalet", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//
//        staff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                View headerView = navigationView.getHeaderView(0);
//                email = headerView.findViewById(R.id.email);
//                email.setText(user.getEmail());
//                fullName = headerView.findViewById(R.id.fullname);
//                fullName.setText(dataSnapshot.child(userId).child("fullName").getValue(String.class));
//
//
//
//                imgProfile = headerView.findViewById(R.id.imageView);
//                String profileUri = dataSnapshot.child(userId).child("image").getValue(String.class);
//                Picasso.get().load(profileUri).into(imgProfile);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//        loadOccupants();



    }
//
//    private void search(String s) {
//        Query query = applicants.orderByChild("fullName")
//                .startAt(s)
//                .endAt(s+ "\uf8ff");
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChildren()){
//                    arrayList.clear();
//                    for (DataSnapshot dss: dataSnapshot.getChildren()){
//                        final Occupants occupants = dss.getValue(Occupants.class);
//                        arrayList.add(occupants);
//                    }
//
//                    SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), arrayList);
//                    recyclerView.setAdapter(searchAdapter);
//                    searchAdapter.notifyDataSetChanged();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

//    private void setAdapter(final String searchedString) {
//        applicants.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //clear arrayList each time
//                fullNameList.clear();
//                matNoList.clear();
//                profilePicList.clear();
//                phoneList.clear();
//                genderList.clear();
//                recyclerView.removeAllViews();
//
//
//                //create a custom counter to limit result
//                int counter = 0;
//
//                //create a for loop to go through all the values
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    String uid = snapshot.getKey();
//                    String full_name = snapshot.child("fullName").getValue(String.class);
//                    String mat_no = snapshot.child("matNo").getValue(String.class);
//                    String phone_no = snapshot.child("phone").getValue(String.class);
//                    String gen_der = snapshot.child("gender").getValue(String.class);
//                    String profile_pic = snapshot.child("profilePic").getValue(String.class);
//
//                    //check if full name contain the searched string
//                    if (full_name.toLowerCase().contains(searchedString.toLowerCase())){
//                        //if is true add values to arrayList
//                        fullNameList.add(full_name);
//                        matNoList.add(mat_no);
//                        genderList.add(gen_der);
//                        phoneList.add(phone_no);
//                        profilePicList.add(profile_pic);
//
//                        counter++;
//
//                    }else if (mat_no.toLowerCase().contains(searchedString.toLowerCase())){
//                        //if is true add values to arrayList
//                        fullNameList.add(full_name);
//                        matNoList.add(mat_no);
//                        genderList.add(gen_der);
//                        phoneList.add(phone_no);
//                        profilePicList.add(profile_pic);
//
//                        counter++;
//                    }
//
//                    //set counter limit to 15
//                    if (counter == 15){
//                        break;
//                    }
//
//                    searchAdapter = new SearchAdapter(Home.this, profilePicList, fullNameList,
//                            matNoList, genderList, phoneList);
//                    recyclerView.setAdapter(searchAdapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void loadOccupants() {
//        adapter = new FirebaseRecyclerAdapter<Occupants, OccupantsViewHolder>(
//                Occupants.class,
//                R.layout.occupants_layout1,
//                OccupantsViewHolder.class,
//                applicants
//        ) {
//            @Override
//            protected void populateViewHolder(OccupantsViewHolder viewHolder, Occupants model, int position) {
//                //viewHolder.txtDepartment.setText(model.getDepartment());
//                viewHolder.txtFullName.setText(model.getFullName());
//                viewHolder.txtGender.setText(model.getGender());
//                viewHolder.txtMatNo.setText(model.getMatNo());
//                viewHolder.txtPhone.setText(model.getPhone());
////                viewHolder.txtParentPhone.setText(model.getParentNo());
//
//                Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.ic_account_circle).into(viewHolder.profilePic);
//
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent occupantDetail = new Intent(Home.this, OccupantDetail.class);
//                        occupantDetail.putExtra("occupantId", adapter.getRef(position).getKey());
//                        startActivity(occupantDetail);
//
//                    }
//                });
//            }
//        };
//
//        adapter.notifyDataSetChanged();
//        recyclerView.setAdapter(adapter);
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_staff) {

            //open add staff activity
            Intent addStaff = new Intent(Home.this, AddStaff.class);
            startActivity(addStaff);
        } else if (id == R.id.nav_clearance) {
            startActivity(new Intent(Home.this, Clearance.class));

        } else if (id == R.id.nav_students) {
            Intent viewStudents = new Intent(Home.this, ViewStudents.class);
            startActivity(viewStudents);

        } else if (id == R.id.nav_sign_out) {
            //logout
            Intent signIn = new Intent(Home.this, MainActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            auth.signOut();
            startActivity(signIn);

        }else if (id == R.id.register_eligible_student){
            Intent eligibleIntent = new Intent(Home.this, IssueHostelMaterial.class);
            startActivity(eligibleIntent);

        }else if (id == R.id.nav_view_staff){
            Intent viewStaff = new Intent(Home.this, ViewStaff.class);
            startActivity(viewStaff);
        }else if (id == R.id.nav_change_password){
            Intent myIntent = new Intent(Home.this, ChangePassword.class);
            startActivity(myIntent);
        }else if (id == R.id.nav_codes_generated){
            startActivity(new Intent(Home.this, CodeDisplayActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
