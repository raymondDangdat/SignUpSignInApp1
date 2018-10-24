package com.example.raymond.signupsigninapp.HiewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.R;

public class GirlsRoomViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnCreateContextMenuListener  {
    public TextView txtRoomDescription, txtBedNumber;
    public ImageView imageViewRoom;

    private ItemClickListener itemClickListener;


    public GirlsRoomViewHolder(View itemView) {
        super(itemView);

        txtRoomDescription = itemView.findViewById(R.id.txtRoomDescription);
        imageViewRoom = itemView.findViewById(R.id.imageViewRoom);
        txtBedNumber = itemView.findViewById(R.id.txtBedNumber);

        itemView.setOnCreateContextMenuListener(this);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select an action");
        contextMenu.add(0,0, getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1, getAdapterPosition(), Common.DELETE);
    }
}

