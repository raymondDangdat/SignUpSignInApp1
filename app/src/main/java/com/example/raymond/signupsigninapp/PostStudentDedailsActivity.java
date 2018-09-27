package com.example.raymond.signupsigninapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PostStudentDedailsActivity extends AppCompatActivity {
    private TextView mSurname, mFirstName, mLastName, mDepartment, mFaculty, mMatNo, mPhone,
    mEmergencyNo, mUserName;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_student_dedails);


        mSurname = findViewById(R.id.textView_surname);
        mFirstName = findViewById(R.id.textView_fName);
        mLastName = findViewById(R.id.textView_LName);
        mDepartment = findViewById(R.id.textView_department);
        mFaculty = findViewById(R.id.textView_faculty);
        mMatNo = findViewById(R.id.textView_matriculationNo);
        mPhone = findViewById(R.id.textView_phone);
        mEmergencyNo = findViewById(R.id.textView_emergencyNo);
        mUserName = findViewById(R.id.textView_username);
        mImageView = findViewById(R.id.image_view_profile_pic);



        //get data from intent
        byte[] bytes = getIntent().getByteArrayExtra("image");
        String surname = getIntent().getStringExtra("surname");
        String username = getIntent().getStringExtra("username");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        //set data to views
        mSurname.setText(surname);
        mUserName.setText(username);
        mImageView.setImageBitmap(bmp);

        //ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Student Details");
        //set back button in the actionBar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    //handle onBackedPress

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
