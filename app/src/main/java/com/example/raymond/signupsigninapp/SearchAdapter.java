package com.example.raymond.signupsigninapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.Modell.Occupants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    public  Context c;
    public ArrayList<Occupants> arrayList;
    public SearchAdapter(Context c, ArrayList<Occupants> arrayList){
        this.c = c;
        this.arrayList = arrayList;
    }


    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        public TextView txtFullName, txtMatNo, txtPhone, txtGender;
        public CircleImageView profilePic;
        private ItemClickListener itemClickListener;

        public SearchAdapterViewHolder(View itemView) {
            super(itemView);
            txtFullName = itemView.findViewById(R.id.text_fullName);
            txtMatNo = itemView.findViewById(R.id.text_view_matriculationNo);
            txtGender = itemView.findViewById(R.id.text_view_gender);
            txtPhone = itemView.findViewById(R.id.phone);
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


    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.occupants_layout1, parent, false);
        return new SearchAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewHolder holder, int position) {
        Occupants occupants = arrayList.get(position);

        holder.txtFullName.setText(occupants.getFullName());
        holder.txtPhone.setText(occupants.getPhone());
        holder.txtMatNo.setText(occupants.getMatNo());
        holder.txtGender.setText(occupants.getGender());
        Picasso.get().load(occupants.getProfilePic()).into(holder.profilePic);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
