package com.example.raymond.signupsigninapp.HiewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class OccupantsViewHolder  extends RecyclerView.ViewHolder implements
        View.OnClickListener {

    public TextView txtFullName, txtMatNo, txtGender, txtDepartment, txtPhone, txtParentPhone;
    public CircleImageView profilePic;

    private ItemClickListener itemClickListener;


    public OccupantsViewHolder(View itemView) {
        super(itemView);

        //txtDepartment = itemView.findViewById(R.id.text_view_department);
        txtFullName = itemView.findViewById(R.id.text_fullName);
        txtGender = itemView.findViewById(R.id.text_view_gender);
        txtMatNo = itemView.findViewById(R.id.text_view_matriculationNo);
        txtPhone = itemView.findViewById(R.id.phone);
       // txtParentPhone = itemView.findViewById(R.id.text_view_parentNo);
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
