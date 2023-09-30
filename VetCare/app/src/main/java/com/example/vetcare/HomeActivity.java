package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    SliderView sliderView;
    int[] images = {
            R.drawable.vetslider,
            R.drawable.prod,
            R.drawable.products,
    };

    CardView appointment, pharmacy;
    ImageButton appoint, pharma;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);

        dialog = new Dialog(this);

        appointment = findViewById(R.id.cardViewAppointmentHome);
        pharmacy = findViewById(R.id.cardViewPharmacyHome);
        appoint = findViewById(R.id.imageButtonAppointmentHome);
        pharma = findViewById(R.id.imageButtonPharmacyHome);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        bottomNavigationView.setSelectedItemId(R.id.home);

        sliderView = findViewById(R.id.image_slider);
        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;

                    case R.id.help:
                        startActivity(new Intent(getApplicationContext(),HelpActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this, HomeAppointmentActivity.class);
                startActivity(i);

//                dialog.setContentView(R.layout.appointment_popup);
//                TextView txtclose = dialog.findViewById(R.id.txtclose);
//                CardView homeAppointment = dialog.findViewById(R.id.homeAppointment);
//                CardView hospitalAccpointment = dialog.findViewById(R.id.hospitalAppointment);
//                txtclose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                homeAppointment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(HomeActivity.this, HomeAppointmentActivity.class);
//                        startActivity(i);
//                    }
//                });
//
//                hospitalAccpointment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(HomeActivity.this, HospitalAppointmentActivity.class);
//                        startActivity(i);
//                    }
//                });
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.MAGENTA));
//                dialog.setCancelable(false);
//                dialog.show();
            }
        });
        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, HomeAppointmentActivity.class);
                startActivity(i);

//                dialog.setContentView(R.layout.appointment_popup);
//                TextView txtclose = dialog.findViewById(R.id.txtclose);
//                CardView homeAppointment = dialog.findViewById(R.id.homeAppointment);
//                CardView hospitalAccpointment = dialog.findViewById(R.id.hospitalAppointment);
//                txtclose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.MAGENTA));
//                dialog.setCancelable(false);
//                dialog.show();
//
//                homeAppointment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(HomeActivity.this, HomeAppointmentActivity.class);
//                        startActivity(i);
//                    }
//                });
//
//                hospitalAccpointment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(HomeActivity.this, HospitalAppointmentActivity.class);
//                        startActivity(i);
//                    }
//                });
            }
        });

        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(i);
            }
        });

        pharma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbarhome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notification) {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
    }
}