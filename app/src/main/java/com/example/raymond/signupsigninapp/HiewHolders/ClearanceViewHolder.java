package com.example.raymond.signupsigninapp.HiewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClearanceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtFullName, txtMaterials;
    public CircleImageView profilePic;

    private ItemClickListener itemClickListener;

    public ClearanceViewHolder(View itemView) {
        super(itemView);

        txtFullName = itemView.findViewById(R.id.text_fullName);
        txtMaterials = itemView.findViewById(R.id.materials);
        profilePic = itemView.findViewById(R.id.image_view_profile);


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
