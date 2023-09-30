package com.example.vetcare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQty;
    private ItemClickListener itemClickListener;


    public CartViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txtProductName = itemView.findViewById(R.id.productNametextViewCustomCart);
        txtProductPrice = itemView.findViewById(R.id.productTotalPricetextViewCart);
        txtProductQty = itemView.findViewById(R.id.numberOfProductstextViewCart);

    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
