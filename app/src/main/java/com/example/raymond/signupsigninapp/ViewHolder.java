package com.example.raymond.signupsigninapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolder(final View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }

    public  void setDetails(Context context, String username, String userSurname, String userImage, String fName, String phone,
                            String lName, String mNo, String emerNo, String eMail,
                            String gEnder, String fculty, String dpartment){

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

    private ViewHolder.clickListener mClickListener;

    // interface to send onCallBack
    public interface clickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

    }

    public void setOnClickListener(ViewHolder.clickListener clickListener){
        mClickListener = clickListener;

    }
}
