package com.example.vetcare;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Patient_ShowBookedAppointmentActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    private String BookedAPKey = "", Appointment_date, slot , Appointment_time , doctorID, currentUID;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__show_booked_appointment);

        currentUID = mAuth.getCurrentUser().getUid().toString();

        recyclerView = (RecyclerView) findViewById(R.id.show_Appointment_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query = mDatabase.child("Booked_Appointments").child(currentUID);

        FirebaseRecyclerOptions<BookedAppointmentList> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<BookedAppointmentList>()
                .setQuery(query, BookedAppointmentList.class)
                .build();

        FirebaseRecyclerAdapter<BookedAppointmentList, BookedAppointmentsVH> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<BookedAppointmentList, BookedAppointmentsVH>(firebaseRecyclerOptions){

                    @Override
                    public BookedAppointmentsVH onCreateViewHolder(ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.single_booked_appointment,parent,false);
                        return new BookedAppointmentsVH(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final BookedAppointmentsVH holder, final int position, @NonNull final BookedAppointmentList model) {

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doctorID = model.getDoctor_ID().toString();

                                BookedAPKey = getRef(position).getKey().toString();
//                                Toast.makeText(Patient_ShowBookedAppointmentActivity.this, "Key = "+BookedAPKey, Toast.LENGTH_SHORT).show();

                                Appointment_date = model.getDate();
                                Appointment_time = model.getTime();
                                changeSlotToTime(Appointment_time);
//                                Toast.makeText(Patient_ShowBookedAppointmentActivity.this,"Doctor - "+doctorID, Toast.LENGTH_SHORT).show();

                                alertDialog();

                            }
                        });
                        mDatabase.child("Doctor_Details").child( model.getDoctor_ID().toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String doctorName = dataSnapshot.child("name").getValue(String.class);

                                holder.setDoctorName(doctorName);
                                holder.setDate(model.getDate());
                                holder.setTime(model.getTime());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void alertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Patient_ShowBookedAppointmentActivity.this);
        builder.setIcon(R.drawable.question).setTitle("Cancel Appointment");
        builder.setMessage("Are You Sure! Want to Cancel Appointment");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(Patient_ShowBookedAppointmentActivity.this, Appointment_date, Toast.LENGTH_SHORT).show();
//                Toast.makeText(Patient_ShowBookedAppointmentActivity.this, "KEY = "+BookedAPKey, Toast.LENGTH_SHORT).show();
//                Toast.makeText(Patient_ShowBookedAppointmentActivity.this, "Slot = "+slot, Toast.LENGTH_SHORT).show();

                mDatabase.child("Appointment").child(doctorID).child(Appointment_date).child(slot).removeValue();
                mDatabase.child("Booked_Appointments").child(currentUID).child(BookedAPKey).removeValue();
                onStart();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public class BookedAppointmentsVH extends RecyclerView.ViewHolder{

        View mView;

        public BookedAppointmentsVH(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setDoctorName(String doctorName) {
            TextView name = (TextView) mView.findViewById(R.id.single_doctorName);
            name.setText(doctorName);
        }


        public void setTime(String time) {

            TextView appointmentTime = (TextView) mView.findViewById(R.id.single_time);
            appointmentTime.setText(time);
        }

        public void setDate(String date) {

            TextView appointmentDate = (TextView) mView.findViewById(R.id.single_date);
            appointmentDate.setText(date);

        }

    }

    private void changeSlotToTime(String appointment_time) {

        switch (appointment_time){

            case "09:00 AM":
                slot = "1";
                break;
            case "09:30 AM":
                slot = "2";
                break;
            case "10:00 AM":
                slot = "3";
                break;
            case "10:30 AM":
                slot = "4";
                break;
            case "11:00 AM":
                slot = "5";
                break;
            case "11:30 AM":
                slot = "6";
                break;
            case "12:00 PM":
                slot = "7";
                break;
            case "12:30 PM":
                slot = "8";
                break;
            case "03:00 PM":
                slot = "9";
                break;
            case "03:30 PM":
                slot = "10";
                break;
            case "04:00 PM":
                slot = "11";
                break;
            case "04:30 PM":
                slot = "12";
                break;
            case "05:00 PM":
                slot = "13";
                break;
            case "05:30 PM":
                slot = "14";
                break;
            case "06:00 PM":
                slot = "15";
                break;
            case "06:30 PM":
                slot = "16";
                break;
            case "07:00 PM":
                slot = "17";
                break;
            case "07:30 PM":
                slot = "18";
                break;
            case "08:00 PM":
                slot = "19";
                break;
            case "08:30 PM":
                slot = "20";
                break;
            default:
                break;
        }
    }
}
