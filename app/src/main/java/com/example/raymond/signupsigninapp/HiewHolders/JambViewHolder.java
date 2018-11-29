package com.example.raymond.signupsigninapp.HiewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.raymond.signupsigninapp.Common.Common;
import com.example.raymond.signupsigninapp.Interface.ItemClickListener;
import com.example.raymond.signupsigninapp.R;

public class JambViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener , View.OnCreateContextMenuListener {
    public TextView txtJambNo, txtStatus;

    private ItemClickListener itemClickListener;

    public JambViewHolder(View itemView) {
        super(itemView);

        txtJambNo = itemView.findViewById(R.id.textViewJamNo);
        txtStatus = itemView.findViewById(R.id.txtStatus);

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select an action");
        menu.add(0,0, getAdapterPosition(), Common.UPDATE);
       menu.add(0,1, getAdapterPosition(), Common.DELETE);
    }
}
