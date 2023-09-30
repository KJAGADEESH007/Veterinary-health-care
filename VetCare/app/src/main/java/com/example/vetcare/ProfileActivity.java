package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class ProfileActivity extends AppCompatActivity {

    TextView  orders, appointments, cart, logout;
    FirebaseAuth mAuth;
    TextView name, phone, mail;
    String uid;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String uname = snapshot.child("Users").child(uid).child("name").getValue().toString();
                    String uphone = snapshot.child("Users").child(uid).child("contact").getValue().toString();
                    String umail = snapshot.child("Users").child(uid).child("email").getValue().toString();

                    name.setText(uname);
                    phone.setText(uphone);
                    mail.setText(umail);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        name = findViewById(R.id.nameTextViewProfile);
        phone = findViewById(R.id.contactTextViewProfile);
        mail = findViewById(R.id.mailTextViewProfile);

        orders = findViewById(R.id.ordersTextviewProfile);
        appointments = findViewById(R.id.appointmentsTextviewProfile);
        cart = findViewById(R.id.cartTextviewProfile);
        logout = findViewById(R.id.logoutTextviewProfile);

//        try {
//            userRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists())
//                    {
//                        Users usr = snapshot.getValue(Users.class);
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }catch (NullPointerException e)
//        {

//        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationProfile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,CartActivity.class);
                startActivity(i);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,YourOrdersActivity.class);
                startActivity(i);
            }
        });

        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,Patient_ShowBookedAppointmentActivity.class);
                startActivity(i);
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.help:
                        startActivity(new Intent(getApplicationContext(),HelpActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }
}