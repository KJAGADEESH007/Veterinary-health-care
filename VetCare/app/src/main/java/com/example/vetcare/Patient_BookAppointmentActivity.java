package com.example.vetcare;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Patient_BookAppointmentActivity extends AppCompatActivity implements View.OnClickListener{

    private String date, time = "";
    private Button mConfirm;
    private int flagChecked = 0;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    private CardView c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20;
    private DatabaseReference mDataBaseRef = FirebaseDatabase.getInstance().getReference().child("Appointment");
    private DatabaseReference mappointRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mPatientDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__book_appointment);


        mConfirm = (Button) findViewById(R.id.confirm_appointment);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(flagChecked!=0)
                {
                    mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId")).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int i = 1;
                            for(i=1;i<=30;i++)
                            {
                                if(dataSnapshot.hasChild(String.valueOf(i)))
                                {
                                    if(dataSnapshot.child(String.valueOf(i)).child("PatientID").getValue().toString().equals(mAuth.getCurrentUser().getUid()))
                                    {
                                        Toast.makeText(Patient_BookAppointmentActivity.this, "You Have Already An Appointment ", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            if(i>30)
                            {
                                setTime(flagChecked);
                                mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId")).child(date).child(String.valueOf(flagChecked)).child("PatientID").setValue(mAuth.getCurrentUser().getUid().toString());

                                mappointRef.child("Doctor_Details").child(getIntent().getStringExtra("DoctorUserId")).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap<String,String> data = new HashMap<>();
                                        data.put("Doctor_ID",getIntent().getStringExtra("DoctorUserId"));
                                        data.put("Date",date);
                                        data.put("Time",time);
                                        data.put("Owner",getIntent().getStringExtra("custName"));
                                        data.put("AnimalType",getIntent().getStringExtra("type"));
                                        data.put("AnimalAge",getIntent().getStringExtra("age"));

                                        mappointRef.child("Appointment_Doctor").child(getIntent().getStringExtra("DoctorUserId")).push().setValue(data);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                mPatientDatabase.child("Doctor_Details").child(getIntent().getStringExtra("DoctorUserId")).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap<String,String> details = new HashMap<>();
                                        details.put("Doctor_ID",getIntent().getStringExtra("DoctorUserId"));
                                        details.put("Date",date);
                                        details.put("Time",time);
                                        details.put("Owner",getIntent().getStringExtra("custName"));
                                        details.put("AnimalType",getIntent().getStringExtra("type"));
                                        details.put("AnimalAge",getIntent().getStringExtra("age"));

                                        mPatientDatabase.child("Booked_Appointments").child(mAuth.getCurrentUser().getUid()).push().setValue(details);
                                        Toast.makeText(getApplicationContext(), "Appointment successful", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                startActivity(new Intent(Patient_BookAppointmentActivity.this,Patient_ShowBookedAppointmentActivity.class));

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else{
                    Toast.makeText(Patient_BookAppointmentActivity.this, "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                }
            }
        });

        c1 = (CardView) findViewById(R.id.time1);
        c2 = (CardView) findViewById(R.id.time2);
        c3 = (CardView) findViewById(R.id.time3);
        c4 = (CardView) findViewById(R.id.time4);
        c5 = (CardView) findViewById(R.id.time5);
        c6 = (CardView) findViewById(R.id.time6);
        c7 = (CardView) findViewById(R.id.time7);
        c8 = (CardView) findViewById(R.id.time8);
        c9 = (CardView) findViewById(R.id.time9);
        c10 = (CardView) findViewById(R.id.time10);
        c11 = (CardView) findViewById(R.id.time11);
        c12 = (CardView) findViewById(R.id.time12);
        c13 = (CardView) findViewById(R.id.time13);
        c14 = (CardView) findViewById(R.id.time14);
        c15 = (CardView) findViewById(R.id.time15);
        c16 = (CardView) findViewById(R.id.time16);
        c17 = (CardView) findViewById(R.id.time17);
        c18 = (CardView) findViewById(R.id.time18);
        c19 = (CardView) findViewById(R.id.time19);
        c20 = (CardView) findViewById(R.id.time20);

//        selectDate = (TextView) findViewById(R.id.bookAppointment_selectDate);

        date = getIntent().getStringExtra("Date").toString();
//        selectDate.setText(date);
//        selectDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calendar = Calendar.getInstance();
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                int month = calendar.get(Calendar.MONTH);
//                int year = calendar.get(Calendar.YEAR);
//
//                datePickerDialog = new DatePickerDialog(Patient_BookAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                        date = dayOfMonth +"-"+ (month+1) +"-"+ year;
//                        selectDate.setText(date);
//                        onStart();
//
//
//                    }
//                },day,month,year);
//                datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
//                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
//                datePickerDialog.show();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.time1:
                checkIsBooked(1);
                break;
            case R.id.time2:
                checkIsBooked(2);
                break;
            case R.id.time3:
                checkIsBooked(3);
                break;
            case R.id.time4:
                checkIsBooked(4);
                break;
            case R.id.time5:
                checkIsBooked(5);
                break;
            case R.id.time6:
                checkIsBooked(6);
                break;
            case R.id.time7:
                checkIsBooked(7);
                break;
            case R.id.time8:
                checkIsBooked(8);
                break;
            case R.id.time9:
                checkIsBooked(9);
                break;
            case R.id.time10:
                checkIsBooked(10);
                break;
            case R.id.time11:
                checkIsBooked(11);
                break;
            case R.id.time12:
                checkIsBooked(12);
                break;
            case R.id.time13:
                checkIsBooked(13);
                break;
            case R.id.time14:
                checkIsBooked(14);
                break;
            case R.id.time15:
                checkIsBooked(15);
                break;
            case R.id.time16:
                checkIsBooked(16);
                break;
            case R.id.time17:
                checkIsBooked(17);
                break;
            case R.id.time18:
                checkIsBooked(18);
                break;
            case R.id.time19:
                checkIsBooked(19);
                break;
            case R.id.time20:
                checkIsBooked(20);
                break;
            default:
                break;
        }
    }

    private void checkIsBooked(int i) {


        if(flagChecked!=0) {
            setDefaultColor(flagChecked);
            flagChecked = i;
            setColorGreen(i);
        }
        else {
            flagChecked=i;
            setColorGreen(i);
        }


    }

    private void setDefaultColor(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c1.setEnabled(true);
                break;
            case 2:
                c2.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c2.setEnabled(true);
                break;
            case 3:
                c3.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c3.setEnabled(true);
                break;
            case 4:
                c4.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c4.setEnabled(true);
                break;
            case 5:
                c5.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c5.setEnabled(true);
                break;
            case 6:
                c6.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c6.setEnabled(true);
                break;
            case 7:
                c7.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c7.setEnabled(true);
                break;
            case 8:
                c8.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c8.setEnabled(true);
                break;
            case 9:
                c9.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c9.setEnabled(true);
                break;
            case 10:
                c10.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c10.setEnabled(true);
                break;
            case 11:
                c11.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c11.setEnabled(true);
                break;
            case 12:
                c12.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c12.setEnabled(true);
                break;
            case 13:
                c13.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c13.setEnabled(true);
                break;
            case 14:
                c14.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c14.setEnabled(true);
                break;
            case 15:
                c15.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c15.setEnabled(true);
                break;
            case 16:
                c16.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c16.setEnabled(true);
                break;
            case 17:
                c17.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c17.setEnabled(true);
                break;
            case 18:
                c18.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c18.setEnabled(true);
                break;
            case 19:
                c19.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c19.setEnabled(true);
                break;
            case 20:
                c20.setCardBackgroundColor(getResources().getColor(R.color.skyBlue));
                c20.setEnabled(true);
                break;
            default:
                break;

        }
        checkTime();
    }

    private void setColorRed(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                break;
            case 2:
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                break;
            case 3:
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                break;
            case 4:
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                break;
            case 5:
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                break;
            case 6:
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                break;
            case 7:
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                break;
            case 8:
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                break;
            case 9:
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                break;
            case 10:
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                break;
            case 11:
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                break;
            case 12:
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                break;
            case 13:
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                break;
            case 14:
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                break;
            case 15:
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                break;
            case 16:
                c16.setCardBackgroundColor(Color.RED);
                c16.setEnabled(false);
                break;
            case 17:
                c17.setCardBackgroundColor(Color.RED);
                c17.setEnabled(false);
                break;
            case 18:
                c18.setCardBackgroundColor(Color.RED);
                c18.setEnabled(false);
                break;
            case 19:
                c19.setCardBackgroundColor(Color.RED);
                c19.setEnabled(false);
                break;
            case 20:
                c20.setCardBackgroundColor(Color.RED);
                c20.setEnabled(false);
                break;
            default:
                break;
        }
    }

    private void setColorGreen(int i) {

        switch (i) {
            case 1: c1.setCardBackgroundColor(Color.GREEN);
                break;
            case 2:
                c2.setCardBackgroundColor(Color.GREEN);
                break;
            case 3:
                c3.setCardBackgroundColor(Color.GREEN);
                break;
            case 4:
                c4.setCardBackgroundColor(Color.GREEN);
                break;
            case 5:
                c5.setCardBackgroundColor(Color.GREEN);
                break;
            case 6:
                c6.setCardBackgroundColor(Color.GREEN);
                break;
            case 7:
                c7.setCardBackgroundColor(Color.GREEN);
                break;
            case 8:
                c8.setCardBackgroundColor(Color.GREEN);
                break;
            case 9:
                c9.setCardBackgroundColor(Color.GREEN);
                break;
            case 10:
                c10.setCardBackgroundColor(Color.GREEN);
                break;
            case 11:
                c11.setCardBackgroundColor(Color.GREEN);
                break;
            case 12:
                c12.setCardBackgroundColor(Color.GREEN);
                break;
            case 13:
                c13.setCardBackgroundColor(Color.GREEN);
                break;
            case 14:
                c14.setCardBackgroundColor(Color.GREEN);
                break;
            case 15:
                c15.setCardBackgroundColor(Color.GREEN);
                break;
            case 16:
                c16.setCardBackgroundColor(Color.GREEN);
                break;
            case 17:
                c17.setCardBackgroundColor(Color.GREEN);
                break;
            case 18:
                c18.setCardBackgroundColor(Color.GREEN);
                break;
            case 19:
                c19.setCardBackgroundColor(Color.GREEN);
                break;
            case 20:
                c20.setCardBackgroundColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

    private void setTime(int i) {

        switch (i) {
            case 1:
                time = "09:00 AM";
                break;
            case 2:
                time = "09:30 AM";
                break;
            case 3:
                time = "10:00 AM";
                break;
            case 4:
                time = "10:30 AM";
                break;
            case 5:
                time = "11:00 AM";
                break;
            case 6:
                time = "11:30 AM";
                break;
            case 7:
                time = "12:00 PM";
                break;
            case 8:
                time = "12:30 PM";
                break;
            case 9:
                time = "03:00 PM";
                break;
            case 10:
                time = "03:30 PM";
                break;
            case 11:
                time = "04:00 PM";
                break;
            case 12:
                time = "04:30 PM";
                break;
            case 13:
                time = "05:00 PM";
                break;
            case 14:
                time = "05:30 PM";
                break;
            case 15:
                time = "06:00 PM";
                break;
            case 16:
                time = "06:30 PM";
                break;
            case 17:
                time = "07:00 PM";
                break;
            case 18:
                time = "07:30 PM";
                break;
            case 19:
                time = "08:00 PM";
                break;
            case 20:
                time = "08:30 PM";
                break;
            default:
                break;
        }
    }

    public void checkTime() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        int min = Calendar.getInstance().get(Calendar.MINUTE);

        String timemin = hour + "." + min;

        if (hour==9)
        {
            if (min<30)
            {
                timemin = "9.00";
            }
            else{
                timemin = "9.30";
            }
        }
        if (hour==10)
        {
            if (min<30)
            {
                timemin = "10.00";
            }
            else{
                timemin = "10.30";
            }
        }
        if (hour==11)
        {
            if (min<30)
            {
                timemin = "11.00";
            }
            else{
                timemin = "11.30";
            }
        }
        if (hour==12)
        {
            if (min<30)
            {
                timemin = "12.00";
            }
            else{
                timemin = "12.30";
            }
        }

        if (hour==15)
        {
            if (min<30)
            {
                timemin = "15.00";
            }
            else{
                timemin = "15.30";
            }
        }

        if (hour==16)
        {
            if (min<30)
            {
                timemin = "16.00";
            }
            else{
                timemin = "16.30";
            }
        }

        if (hour==17)
        {
            if (min<30)
            {
                timemin = "17.00";
            }
            else{
                timemin = "17.30";
            }
        }
        if (hour==18)
        {
            if (min<30)
            {
                timemin = "18.00";
            }
            else{
                timemin = "18.30";
            }
        }
        if (hour==19)
        {
            if (min<30)
            {
                timemin = "19.00";
            }
            else{
                timemin = "19.30";
            }
        }
        if (hour==20)
        {
            if (min<30)
            {
                timemin = "20.00";
            }
            else{
                timemin = "20.30";
            }
        }


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        String date = day +"-"+ (month+1) +"-"+ year;

        if (date.equals(getIntent().getStringExtra("Date"))) {

        switch (timemin) {
            case "9.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                break;
            case "9.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                break;
            case "10.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                break;
            case "10.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                break;
            case "11.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                break;

            case "11.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                break;

            case "12.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                break;

            case "12.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                break;

            case "15.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                break;

            case "15.30":
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                break;

            case "16.00":
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                break;

            case "16.30":
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                break;

            case "17.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                break;

            case "17.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                break;

            case "18.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                break;

            case "18.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                c16.setCardBackgroundColor(Color.RED);
                c16.setEnabled(false);
                break;

            case "19.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                c16.setCardBackgroundColor(Color.RED);
                c16.setEnabled(false);
                c17.setCardBackgroundColor(Color.RED);
                c17.setEnabled(false);
                break;

            case "19.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                c16.setCardBackgroundColor(Color.RED);
                c16.setEnabled(false);
                c17.setCardBackgroundColor(Color.RED);
                c17.setEnabled(false);
                c18.setCardBackgroundColor(Color.RED);
                c18.setEnabled(false);
                break;

            case "20.00":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                c16.setCardBackgroundColor(Color.RED);
                c16.setEnabled(false);
                c17.setCardBackgroundColor(Color.RED);
                c17.setEnabled(false);
                c18.setCardBackgroundColor(Color.RED);
                c18.setEnabled(false);
                c19.setCardBackgroundColor(Color.RED);
                c19.setEnabled(false);
                break;

            case "20.30":
                c1.setCardBackgroundColor(Color.RED);
                c1.setEnabled(false);
                c2.setCardBackgroundColor(Color.RED);
                c2.setEnabled(false);
                c3.setCardBackgroundColor(Color.RED);
                c3.setEnabled(false);
                c4.setCardBackgroundColor(Color.RED);
                c4.setEnabled(false);
                c5.setCardBackgroundColor(Color.RED);
                c5.setEnabled(false);
                c6.setCardBackgroundColor(Color.RED);
                c6.setEnabled(false);
                c7.setCardBackgroundColor(Color.RED);
                c7.setEnabled(false);
                c8.setCardBackgroundColor(Color.RED);
                c8.setEnabled(false);
                c9.setCardBackgroundColor(Color.RED);
                c9.setEnabled(false);
                c10.setCardBackgroundColor(Color.RED);
                c10.setEnabled(false);
                c11.setCardBackgroundColor(Color.RED);
                c11.setEnabled(false);
                c12.setCardBackgroundColor(Color.RED);
                c12.setEnabled(false);
                c13.setCardBackgroundColor(Color.RED);
                c13.setEnabled(false);
                c14.setCardBackgroundColor(Color.RED);
                c14.setEnabled(false);
                c15.setCardBackgroundColor(Color.RED);
                c15.setEnabled(false);
                c16.setCardBackgroundColor(Color.RED);
                c16.setEnabled(false);
                c17.setCardBackgroundColor(Color.RED);
                c17.setEnabled(false);
                c18.setCardBackgroundColor(Color.RED);
                c18.setEnabled(false);
                c19.setCardBackgroundColor(Color.RED);
                c19.setEnabled(false);
                c20.setCardBackgroundColor(Color.RED);
                c20.setEnabled(false);
                break;
        }
    }
    }



    @Override
    protected void onStart() {
        super.onStart();

        checkTime();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Toast.makeText(this, "You are not Logged In.....Login first for further process", Toast.LENGTH_SHORT).show();

            Intent login_Intent = new Intent(Patient_BookAppointmentActivity.this,LoginActivity.class);
            startActivity(login_Intent);
        }else {
            flagChecked=0;
            mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId").toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(date)){

                        mDataBaseRef.child(getIntent().getStringExtra("DoctorUserId").toString()).child(date).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (int i =1;i<=30;i++) {

                                    if(dataSnapshot.hasChild(String.valueOf(i)))
                                    {
                                        setColorRed(i);

                                    }
                                    else
                                    {
                                        setDefaultColor(i);
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else {
                        for (int i =1;i<=30;i++)
                        {
                            setDefaultColor(i);
                        }
                        // Toast.makeText(Patient_BookAppointmentActivity.this, "all time is available on this date", Toast.LENGTH_SHORT).show();
                        // mDataBaseRef.child(doctorUserId).child(date).child(slot).child("PatientID").setValue(userId);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
