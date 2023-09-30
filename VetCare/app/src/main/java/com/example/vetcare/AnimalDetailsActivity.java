package com.example.vetcare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class AnimalDetailsActivity extends AppCompatActivity {

    TextView docName;
    EditText custName,animalType,animalAge;
    Button mcontinue;

    private Calendar calendar;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        docName = findViewById(R.id.nametextViewAppointment);
        String doctorName = getIntent().getStringExtra("name");
        docName.setText(doctorName);

        custName = findViewById(R.id.ownerNameeditTextAppointment);
        animalAge = findViewById(R.id.animalAgeeditTextAppointment);
        animalType = findViewById(R.id.animalTypeeditTextAppointment);

        mcontinue = findViewById(R.id.confirmAppointmentbuttonAppointment);

        mcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String owner = custName.getText().toString();
                final String type = animalType.getText().toString();
                final String age = animalAge.getText().toString();

                if (owner.isEmpty() || type.isEmpty() || age.isEmpty()) {
                    Toast.makeText(AnimalDetailsActivity.this, "None of the fields should be empty", Toast.LENGTH_LONG).show();
                }
                else {
                    calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);

                    datePickerDialog = new DatePickerDialog(AnimalDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            String userId = getIntent().getStringExtra("docId");

                            String date = dayOfMonth +"-"+ (month+1) +"-"+ year;

                            Intent intent = new Intent(AnimalDetailsActivity.this, Patient_BookAppointmentActivity.class);
                            intent.putExtra("Date",date);
                            intent.putExtra("DoctorUserId",userId);
                            intent.putExtra("custName",owner);
                            intent.putExtra("type",type);
                            intent.putExtra("age",age);
                            startActivity(intent);
                        }
                    },day,month,year);
                    datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + (3 * 60 * 60 * 1000));
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000));
                    datePickerDialog.show();
                }

            }
        });

    }
}