package com.example.raymond.signupsigninapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raymond.signupsigninapp.HiewHolders.OccupantsViewHolder;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.Occupants;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewStudents extends AppCompatActivity {

    private Toolbar viewStudents;

    private FirebaseDatabase database;
    private DatabaseReference applicants;

    private MaterialSearchBar searchBar;

    ArrayList<Occupants> arrayList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;



    private FirebaseRecyclerAdapter<Occupants, OccupantsViewHolder> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);



        //initialize toolBar
        viewStudents = findViewById(R.id.view_students_toolbar);
        setSupportActionBar(viewStudents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Hostel Occupants");


        database = FirebaseDatabase.getInstance();
        applicants = database.getReference("Applications");

        searchBar = findViewById(R.id.searchBar);

        arrayList = new ArrayList<>();

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    search(s.toString());
                }else {
                    loadOccupants();
                }

            }
        });

        //init
        recyclerView = findViewById(R.id.recycler_occupants);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));



        //load occupants
        loadOccupants();

    }


//method to perform search
    private void search(String s) {
        Query query = applicants.orderByChild("fullName")
                .startAt(s)
                .endAt(s+ "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    arrayList.clear();
                    for (DataSnapshot dss: dataSnapshot.getChildren()){
                        final Occupants occupants = dss.getValue(Occupants.class);
                        arrayList.add(occupants);
                    }

                    SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), arrayList);
                    recyclerView.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //method to load occupants
    private void loadOccupants() {
        adapter = new FirebaseRecyclerAdapter<Occupants, OccupantsViewHolder>(
                Occupants.class,
                R.layout.occupants_layout1,
                OccupantsViewHolder.class,
                applicants
        ) {
            @Override
            protected void populateViewHolder(OccupantsViewHolder viewHolder, Occupants model, int position) {
                //viewHolder.txtDepartment.setText(model.getDepartment());
                viewHolder.txtFullName.setText(model.getFullName());
                viewHolder.txtGender.setText(model.getGender());
                viewHolder.txtMatNo.setText(model.getMatNo());
                viewHolder.txtPhone.setText(model.getPhone());
//                viewHolder.txtParentPhone.setText(model.getParentNo());

                Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.ic_account_circle).into(viewHolder.profilePic);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent occupantDetail = new Intent(ViewStudents.this, OccupantDetail.class);
                        occupantDetail.putExtra("occupantId", adapter.getRef(position).getKey());
                        startActivity(occupantDetail);

                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


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
        String[] sortOptions = {"Male", "Female", "Chalets" };
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by:")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //the which contains the index position of the selected item
                        if (which==0){
                            //Male selected
                            Toast.makeText(ViewStudents.this, "Males sorted", Toast.LENGTH_SHORT).show();
                        }else if (which==1){
                            //female sorted
                            Toast.makeText(ViewStudents.this, "Female", Toast.LENGTH_SHORT).show();
                        }else if (which==2){
                            //to be sorted according to chalet number
                        }
                    }
                });
        builder.show();
    }
}
