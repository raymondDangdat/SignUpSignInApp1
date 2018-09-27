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

import java.util.List;

public class HostelAdapter extends RecyclerView.Adapter <HostelAdapter.HostelViewHolder> {
    //declare member variables
    private Context mContext;
    private List<Upload> mUploads;

    private onItemClickListener mListener;



    //create a constructor
    public HostelAdapter(Context context, List<Upload> uploads){
        mContext = context;
        mUploads = uploads;
    }


    @NonNull
    @Override
    public HostelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.hostel_item, parent, false);
        return new HostelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HostelViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewRoomNumber.setText(uploadCurrent.getName());
        holder.textViewRoomDescription.setText(uploadCurrent.getDescription());

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    //hostel view constructor
    public class HostelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        public TextView textViewRoomNumber;
        public TextView textViewRoomDescription;
        private TextView textViewApply;
        private ImageView imageViewApply;


        public HostelViewHolder(View itemView) {
            super(itemView);

            textViewRoomNumber = itemView.findViewById(R.id.post_room_number);
            textViewRoomDescription = itemView.findViewById(R.id.post_room_description);
            textViewApply = itemView.findViewById(R.id.text_view_apply);
            imageViewApply = itemView.findViewById(R.id.img_apply);

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
            MenuItem doWhatEver = menu.add(Menu.NONE,1,1,"Do Whatever");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");

            doWhatEver.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    //create an interface
    public interface onItemClickListener{
        void onDeleteClick(int position);
        void onItemClick(int position);
        void onWhatEverClick(int position);
    }


    public void setOnItemClickLister(onItemClickListener lister){
        mListener = lister;

    }
}
