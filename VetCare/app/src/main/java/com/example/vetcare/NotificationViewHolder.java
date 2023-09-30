package com.example.vetcare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    TextView msg;
    TextView title;
    private ItemClickListener itemClickListener;

    public NotificationViewHolder(@NonNull View itemView)
    {
        super(itemView);

        msg = itemView.findViewById(R.id.textViewMessageNotify);
        title = itemView.findViewById(R.id.textViewTitleNotify);

    }

    @Override
    public void onClick(View v)
    {
        this.itemClickListener = itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
