package com.example.vetcare;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference helpListRef;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        loadingBar = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recyclerViewNotifies);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        helpListRef = FirebaseDatabase.getInstance().getReference().child("Notifications");

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        loadingBar.setTitle("Notifications");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        FirebaseRecyclerOptions<Notifications> options = new FirebaseRecyclerOptions.Builder<Notifications>()
                .setQuery(helpListRef, Notifications.class)
                .build();

        FirebaseRecyclerAdapter<Notifications, NotificationViewHolder> adapter = new FirebaseRecyclerAdapter<Notifications, NotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder helpViewHolder, int i, @NonNull Notifications help)
            {
                helpViewHolder.msg.setText(help.getMsg());
                helpViewHolder.title.setText(help.getTitle());
                loadingBar.dismiss();
            }

            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_notification,parent,false);
                return new NotificationViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
