package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeAppointmentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference docRef;
    Doctor doc;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_appointment);

        loadingBar = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbarDoctor);
        setSupportActionBar(toolbar);

        docRef = FirebaseDatabase.getInstance().getReference().child("Doctor_Details");

        recyclerView = findViewById(R.id.recyclerViewDoctor);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    protected void onStart() {
        super.onStart();

        loadingBar.setTitle("Doctors");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Query query = docRef.orderByChild("status").equalTo(true);

        FirebaseRecyclerOptions<Doctor> options = new FirebaseRecyclerOptions.Builder<Doctor>()
                .setQuery(query, Doctor.class)
                .build();


        FirebaseRecyclerAdapter<Doctor, DoctorViewHolder> adapter = new FirebaseRecyclerAdapter<Doctor, DoctorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DoctorViewHolder doctorViewHolder, int i, @NonNull final Doctor doctor)
            {
                doctorViewHolder.txtDocName.setText(doctor.getName());
                doctorViewHolder.txtDocQualification.setText(doctor.getQualification());
                loadingBar.dismiss();

                doctorViewHolder.btnbook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeAppointmentActivity.this, Patient_DoctorProfileActivity.class);
                        intent.putExtra("docId",doctor.getDocId());
                        intent.putExtra("name",doctor.getName());
                        intent.putExtra("exp",doctor.getExp());
                        intent.putExtra("qual",doctor.getQualification());
                        intent.putExtra("phone",doctor.getContact());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycle_doctor, parent, false);
                DoctorViewHolder holder = new DoctorViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
