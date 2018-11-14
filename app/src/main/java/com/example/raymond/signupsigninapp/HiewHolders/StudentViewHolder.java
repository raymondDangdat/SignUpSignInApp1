package com.example.raymond.signupsigninapp.HiewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtSurname, txtFirstName, txtLastName;
    public ImageView imgProfile;

    private ItemClickListener itemClickListener;


    public StudentViewHolder(View itemView) {
        super(itemView);
        txtFirstName = itemView.findViewById(R.id.text_view_fName);
        txtLastName = itemView.findViewById(R.id.textView_LName);
        txtSurname = itemView.findViewById(R.id.textView_surname);
        imgProfile = itemView.findViewById(R.id.image_view_profile_pic);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}
