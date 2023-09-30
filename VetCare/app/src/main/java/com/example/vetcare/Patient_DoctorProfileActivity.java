package com.example.vetcare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Patient_DoctorProfileActivity extends AppCompatActivity {

    private TextView mName, mEducation, mExperience, mContactNo;

    private Button mBookAppointmentBtn;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_doctor_profile);

        mName = (TextView) findViewById(R.id.patient_doctorProfile_name);
        mEducation = (TextView) findViewById(R.id.patient_doctorProfile_education);
        mExperience = (TextView) findViewById(R.id.patient_doctorProfile_experiance);
        mContactNo = (TextView) findViewById(R.id.patient_doctorProfile_contact);

        mBookAppointmentBtn = (Button) findViewById(R.id.book_appointment_button);

        mBookAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                calendar = Calendar.getInstance();
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                int month = calendar.get(Calendar.MONTH);
//                int year = calendar.get(Calendar.YEAR);
//
//                datePickerDialog = new DatePickerDialog(Patient_DoctorProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                        String userId = getIntent().getStringExtra("docId");
//
//                        String date = dayOfMonth +"-"+ (month+1) +"-"+ year;
//
//                        Intent intent = new Intent(Patient_DoctorProfileActivity.this, Patient_BookAppointmentActivity.class);
//                        intent.putExtra("Date",date);
//                        intent.putExtra("DoctorUserId",userId);
//                        startActivity(intent);
//                    }
//                },day,month,year);
//                datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
//                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
//                datePickerDialog.show();

                String userId = getIntent().getStringExtra("docId");
                Intent i = new Intent(Patient_DoctorProfileActivity.this,AnimalDetailsActivity.class);
                i.putExtra("docId",userId);
                i.putExtra("name",getIntent().getStringExtra("name"));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        String name = getIntent().getStringExtra("name");
        String education = getIntent().getStringExtra("qual");
        String experience = getIntent().getStringExtra("exp");
        String contact = getIntent().getStringExtra("phone");

        mName.setText(name);
        mEducation.setText(education);
        mExperience.setText(experience);
        mContactNo.setText(contact);
    }
}