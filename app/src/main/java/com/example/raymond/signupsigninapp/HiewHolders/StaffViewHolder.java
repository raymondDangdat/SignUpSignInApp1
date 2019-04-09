package com.example.raymond.signupsigninapp.HiewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffViewHolder extends RecyclerView.ViewHolder {
    public TextView txtFullName, txtEmail;
    public CircleImageView profilePic;

    public StaffViewHolder(View itemView) {
        super(itemView);
        txtFullName = itemView.findViewById(R.id.text_fullName);
        txtEmail = itemView.findViewById(R.id.text_view_email);
        profilePic = itemView.findViewById(R.id.image_view_profile);
    }
}
