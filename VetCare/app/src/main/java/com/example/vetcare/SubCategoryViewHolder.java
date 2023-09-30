package com.example.vetcare;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName;
    public ImageView imageView;
    public ItemClickListener listener;

    public SubCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.newCategoryImage);
        txtProductName = itemView.findViewById(R.id.newCategoryName);

    }

    public  void setItemClickListener(ItemClickListener Listener)
    {
        this.listener = Listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.onClick(v, getAdapterPosition(), false);
    }
}