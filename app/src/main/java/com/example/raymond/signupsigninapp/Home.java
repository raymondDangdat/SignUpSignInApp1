package com.example.raymond.signupsigninapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.HiewHolders.StudentViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.Student;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseDatabase database;
    private DatabaseReference applicants;
    private RecyclerView recyler_students;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser mUser;

    private DatabaseReference staff;

    private String userId;

    private TextView fullName, email;
    private ImageView imgProfile;

    private FirebaseRecyclerAdapter<Student, StudentViewHolder> adapter;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hostel admin dashboard");
        setSupportActionBar(toolbar);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        applicants = database.getReference("Applications");


        mUser = auth.getCurrentUser();
        userId = auth.getUid();


        final FirebaseUser user = auth.getCurrentUser();

        staff = database.getReference().child("staff");








        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Chalet", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        staff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                View headerView = navigationView.getHeaderView(0);
                email = headerView.findViewById(R.id.email);
                email.setText(user.getEmail());
                fullName = headerView.findViewById(R.id.fullname);
                fullName.setText(dataSnapshot.child(userId).child("fullName").getValue(String.class));



                imgProfile = headerView.findViewById(R.id.imageView);
                String profileUri = dataSnapshot.child(userId).child("image").getValue(String.class);
                Picasso.with(getBaseContext()).load(profileUri)
                        .into(imgProfile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyler_students = findViewById(R.id.recycler_view_student);
        recyler_students.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyler_students.setLayoutManager(layoutManager);

        //loadApplicants
        loadApplicants();
    }

    private void loadApplicants() {
        adapter = new FirebaseRecyclerAdapter<Student, StudentViewHolder>(
                Student.class,
                R.layout.student_item,
                StudentViewHolder.class,
                applicants
        ) {
            @Override
            protected void populateViewHolder(StudentViewHolder viewHolder, Student model, int position) {
                viewHolder.txtSurname.setText(model.getSurname());
                Picasso.with(getBaseContext()).load(model.getProfilePic()).into(viewHolder.imgProfile);

            }
        };

        //recyler_students.setAdapter(adapter);




    }

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
        } else if (id == R.id.nav_boysChalet) {
            startActivity(new Intent(Home.this, BoysChaletActivity.class));

        } else if (id == R.id.nav_girls_chalet) {
            Intent girlsIntent = new Intent(Home.this, GirlsChaletActivity.class);
            startActivity(girlsIntent);

        } else if (id == R.id.nav_clearance) {

        } else if (id == R.id.nav_students) {

        } else if (id == R.id.nav_sign_out) {
            //logout
            Intent signIn = new Intent(Home.this, MainActivity.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            auth.signOut();
            startActivity(signIn);

        }else if (id == R.id.nav_students){
            Intent student = new Intent(Home.this,StudentActivity.class);
            startActivity(student);
        }else if (id == R.id.nav_generate_pin){
            Intent genPin = new Intent(Home.this, GenaratePin.class);
            startActivity(genPin);

        }else if (id == R.id.register_eligible_student){
            Intent eligibleIntent = new Intent(Home.this, ConfirmStudentActivity.class);
            startActivity(eligibleIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
