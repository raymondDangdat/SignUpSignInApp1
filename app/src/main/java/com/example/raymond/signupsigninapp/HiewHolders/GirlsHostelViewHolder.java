package com.example.raymond.signupsigninapp.HiewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.R;

public class GirlsHostelViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnCreateContextMenuListener  {
    public TextView txtChaletNumber;
    public ImageView imageViewChalet;

    private ItemClickListener itemClickListener;


    public GirlsHostelViewHolder(View itemView) {
        super(itemView);

        txtChaletNumber = itemView.findViewById(R.id.txtChaletNumber);
        imageViewChalet = itemView.findViewById(R.id.imageViewChalet);

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

