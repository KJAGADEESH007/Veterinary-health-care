package com.example.vetcare;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtDocName, txtDocQualification;
    public ItemClickListener listener;
    Button btnbook;

    public DoctorViewHolder(@NonNull View itemView) {
        super(itemView);

        txtDocName = itemView.findViewById(R.id.docNameTextViewCustomDoctor);
        txtDocQualification = itemView.findViewById(R.id.qualificationtextViewCustomDoctor);
        btnbook = itemView.findViewById(R.id.bookbuttonCustomDoctor);

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