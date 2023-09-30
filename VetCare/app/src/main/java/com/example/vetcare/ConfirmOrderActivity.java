package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    EditText name,phone,homeAddress,streetAddress,pincode;
    Button placeOrder;
    String pin1 = "581401";
    String totalAmount = "";
    FirebaseAuth mAuth;
    RadioGroup radioGroup;
    RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        name = findViewById(R.id.fullnameEditTextConfirm);
        phone = findViewById(R.id.contactEditTextConfirm);
        homeAddress = findViewById(R.id.homeAddressEditTextConfirm);
        streetAddress = findViewById(R.id.streetAddressEditTextConfirm);
        pincode = findViewById(R.id.pincodeEditTextConfirm);
        placeOrder = findViewById(R.id.confirmOrderConfirm);
        radioGroup=findViewById(R.id.radiogroup);

        totalAmount = getIntent().getStringExtra("Total Price");

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Check();
            }
        });

    }

    private void Check()
    {
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            name.setError("Name Required");
            name.requestFocus();
        }
        else if(TextUtils.isEmpty(phone.getText().toString()))
        {
            phone.setError("Contact Number Required");
            phone.requestFocus();
        }
        else if(TextUtils.isEmpty(homeAddress.getText().toString()))
        {
            homeAddress.setError("Home Address Required");
            homeAddress.requestFocus();
        }
        else if(TextUtils.isEmpty(streetAddress.getText().toString()))
        {
            streetAddress.setError("Street/Colony Address Required");
            streetAddress.requestFocus();
        }
        else if(TextUtils.isEmpty(pincode.getText().toString()))
        {
            pincode.setError("Pincode Required");
            pincode.requestFocus();
        }

        else
       {
           int id=radioGroup.getCheckedRadioButtonId();
           radioButton=findViewById(id);

           if(radioButton.getText().toString().equals("UPI Payment"))
           {
               String pc = pincode.getText().toString();
               if(pc.equals(pin1)){
                   Intent intent=new Intent(ConfirmOrderActivity.this,PaymentActivity.class);
                   intent.putExtra("name",name.getText().toString());
                   intent.putExtra("phone",phone.getText().toString());
                   intent.putExtra("homeAddress",homeAddress.getText().toString());
                   intent.putExtra("streetAddress",streetAddress.getText().toString());
                   intent.putExtra("pinCode",pincode.getText().toString());
                   intent.putExtra("Total Amount", totalAmount);
                   startActivity(intent);
               }
               else
               {
                   Toast.makeText(ConfirmOrderActivity.this,"Delivery not available",Toast.LENGTH_SHORT).show();
               }

           }
           else if(radioButton.getText().toString().equals("Cash On Delivery")){
               ConfirmOrder();
           }
           else{
               Toast.makeText(this, "Please Enter Payment Method", Toast.LENGTH_SHORT).show();
           }
       }
    }



    private void ConfirmOrder()
    {
        mAuth = FirebaseAuth.getInstance();

        final String userID = mAuth.getCurrentUser().getUid();

        String pin = pincode.getText().toString();

        final String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        if(pin.equals(pin1)) {


            final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);

            HashMap<String, Object> ordersMap = new HashMap<>();
            ordersMap.put("totalAmount", totalAmount);
            ordersMap.put("name", name.getText().toString());
            ordersMap.put("phone", phone.getText().toString());
            ordersMap.put("homeAddress", homeAddress.getText().toString());
            ordersMap.put("streetAddress", streetAddress.getText().toString());
            ordersMap.put("pincode", pincode.getText().toString());
            ordersMap.put("date", saveCurrentDate);
            ordersMap.put("time", saveCurrentTime);
            ordersMap.put("state", "not shipped");
            ordersMap.put("paymentMethod","cash");

            orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        FirebaseDatabase.getInstance().getReference().child("CartList").child("User View").child(userID).removeValue();
                        Toast.makeText(ConfirmOrderActivity.this, "Your Order has been placed successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ConfirmOrderActivity.this, YourOrdersActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else {
            Toast.makeText(ConfirmOrderActivity.this,"Sorry..!! Home delivery is not available for your area.",Toast.LENGTH_LONG).show();
        }

    }


}