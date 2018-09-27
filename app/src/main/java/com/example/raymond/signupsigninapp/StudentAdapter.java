package com.example.raymond.signupsigninapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context mContext;
    private List<StudentUpload> mUploads;
    private onItemClickListener mListener;


    public StudentAdapter(Context context, List<StudentUpload> uploads){
        mContext = context;
        mUploads = uploads;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.student_item, parent, false);
        return  new StudentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentUpload uploadCurrent = mUploads.get(position);
        holder.textViewSurname.setText(uploadCurrent.getSurname());
        holder.textViewFName.setText(uploadCurrent.getFirstName());
        holder.textViewLName.setText(uploadCurrent.getLastName());
        holder.textViewUsername.setText(uploadCurrent.getUsername());
        holder.textViewEmail.setText(uploadCurrent.getEmail());
        holder.textViewEmergencyNo.setText(uploadCurrent.getEmergencyNo());
        holder.textViewPhone.setText(uploadCurrent.getPhone());
        holder.textViewGender.setText(uploadCurrent.getGender());
        holder.textViewFaculty.setText(uploadCurrent.getFaculty());
        holder.textViewDepartment.setText(uploadCurrent.getDepartment());
        holder.textViewMatNo.setText(uploadCurrent.getMatNo());
        Picasso.with(mContext)
                .load(uploadCurrent.getImage())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_account1)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        public TextView textViewSurname;
        public TextView textViewFName;
        public TextView textViewLName;
        public TextView textViewUsername;
        public TextView textViewEmail;
        public TextView textViewDepartment;
        public TextView textViewFaculty;
        public TextView textViewPhone;
        public TextView textViewGender;
        public TextView textViewMatNo;
        public TextView textViewEmergencyNo;
        public ImageView imageView;

        public StudentViewHolder(View itemView) {
            super(itemView);

            textViewSurname = itemView.findViewById(R.id.text_view_surname);
            textViewFName = itemView.findViewById(R.id.text_view_fName);
            textViewLName = itemView.findViewById(R.id.text_view_LName);
            textViewUsername = itemView.findViewById(R.id.text_view_username);
            textViewEmail = itemView.findViewById(R.id.text_view_email);
            textViewDepartment = itemView.findViewById(R.id.text_view_department);
            textViewFaculty = itemView.findViewById(R.id.text_view_faculty);
            textViewPhone = itemView.findViewById(R.id.text_view_phone);
            textViewGender = itemView.findViewById(R.id.text_view_gender);
            textViewMatNo = itemView.findViewById(R.id.text_view_matriculationNo);
            textViewEmergencyNo = itemView.findViewById(R.id.text_view_emergencyNo);
            imageView = itemView.findViewById(R.id.image_view_profile);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem viewProfile = menu.add(Menu.NONE, 1, 1, "View profile");
            MenuItem delete = menu.add(Menu.NONE,1,1, "Delete student");

            viewProfile.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onItemClickViewProfile(position);
                            return true;

                        case 2:
                            mListener.onItemClickDelete(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface onItemClickListener{
        void onItemClick(int position);

        Void onItemClickDelete(int position);

        void onItemClickViewProfile(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;

    }
}
