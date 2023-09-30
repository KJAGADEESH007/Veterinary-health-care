package com.example.vetcare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;

import com.example.vetcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {//implements PaymentStatusListener

    private TextView amountEdt, upiEdt;
    private TextView transactionDetailsTV;
    Button makePaymentBtn;
    String totalAmt = "";
    String custName, custPhone, custHome, custStreet, custPin;
    FirebaseAuth mAuth;
    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // initializing all our variables.
        amountEdt = findViewById(R.id.textViewAmount);
        upiEdt = findViewById(R.id.texteViweUPI);
        makePaymentBtn = findViewById(R.id.idBtnMakePayment);
        transactionDetailsTV = findViewById(R.id.idTVTransactionDetails);

        totalAmt = getIntent().getStringExtra("Total Amount");
        amountEdt.setText(totalAmt);

        custName = getIntent().getStringExtra("name");
        custPhone = getIntent().getStringExtra("phone");
        custHome = getIntent().getStringExtra("homeAddress");
        custStreet = getIntent().getStringExtra("streetAddress");
        custPin = getIntent().getStringExtra("pinCode");

        // on below line we are adding click listener for our payment button.
        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = totalAmt + ".00";
                String upi = upiEdt.getText().toString();
                String name = custName;
                String desc = "Payment for vetcare product";
                makePayment(amount, upi, name, desc);

            }
        });
    }

    private void makePayment(String amount, String upi, String name, String desc) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upi)
                .appendQueryParameter("pn", upi)
                .appendQueryParameter("tn", desc)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaymentActivity.this, "No UPI app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaymentActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: " + approvalRefNo);
                PlaceOrder();
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PaymentActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void PlaceOrder() {
        Toast.makeText(this, "Save to DB here", Toast.LENGTH_SHORT).show();
        //mAuth = FirebaseAuth.getInstance();
//
//        final String userID = mAuth.getCurrentUser().getUid();
//
//        final String saveCurrentTime, saveCurrentDate;
//
//        Calendar calForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//        saveCurrentDate = currentDate.format(calForDate.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime = currentTime.format(calForDate.getTime());
//
//            final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);
//
//            HashMap<String, Object> ordersMap = new HashMap<>();
//            ordersMap.put("totalAmount", totalAmt);
//            ordersMap.put("name", custName);
//            ordersMap.put("phone", custPhone);
//            ordersMap.put("homeAddress", custHome);
//            ordersMap.put("streetAddress", custStreet);
//            ordersMap.put("pincode", custPin);
//            ordersMap.put("date", saveCurrentDate);
//            ordersMap.put("time", saveCurrentTime);
//            ordersMap.put("state", "not shipped");
//            ordersMap.put("paymentMethod","UPI");
//
//            orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful())
//                    {
//                        FirebaseDatabase.getInstance().getReference().child("CartList").child("User View").child(userID).removeValue();
//                        Toast.makeText(PaymentActivity.this, "Your Order has been placed successfully", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(PaymentActivity.this, YourOrdersActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            });
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
//public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
//
//    private TextView amountEdt, upiEdt;
//    Button makePaymentBtn;
//    String totalAmt = "";
//    String custName, custPhone, custHome, custStreet, custPin;
//    FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
//
//        // initializing all our variables.
//        amountEdt = findViewById(R.id.textViewAmount);
//        upiEdt = findViewById(R.id.texteViweUPI);
//        makePaymentBtn = findViewById(R.id.idBtnMakePayment);
//
//        totalAmt = getIntent().getStringExtra("Total Amount");
//        amountEdt.setText(totalAmt);
//
//        custName = getIntent().getStringExtra("name");
//        custPhone = getIntent().getStringExtra("phone");
//        custHome = getIntent().getStringExtra("homeAddress");
//        custStreet = getIntent().getStringExtra("streetAddress");
//        custPin = getIntent().getStringExtra("pinCode");
//
//        // on below line we are adding click listener for our payment button.
//        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // on below line we are getting
//                // amount that is entered by user.
//                String samount = totalAmt;
//
//                // rounding off the amount.
//                int amount = Math.round(Float.parseFloat(samount) * 100);
//
//                // initialize Razorpay account.
//                Checkout checkout = new Checkout();
//
//                // set your id as below
//                checkout.setKeyID("rzp_test_tyOTtEg7V7IyWq");
//
//                // set image
//                checkout.setImage(R.drawable.logo);
//
//                // initialize json object
//                JSONObject object = new JSONObject();
//                try {
//                    // to put name
//                    object.put("name", "VetCare");
//
//                    // put description
//                    object.put("description", "Pay for your product");
//
//                    // to set theme color
//                    object.put("theme.color", "");
//
//                    // put the currency
//                    object.put("currency", "INR");
//
//                    // put amount
//                    object.put("amount", amount);
//
//                    // put mobile number
//                    object.put("prefill.contact", "9284064503");
//
//                    // put email
//                    object.put("prefill.email", "chaitanyamunje@gmail.com");
//
//                    // open razorpay to checkout activity
//                    checkout.open(PaymentActivity.this, object);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onPaymentSuccess(String s) {
//        // this method is called on payment success.
//        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
//        PlaceOrder();
//        Intent intent = new Intent(this,YourOrdersActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        // on payment failed.
//        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this,PharmacyActivity.class);
//        startActivity(intent);
//    }
//
//    private void PlaceOrder() {
//        Toast.makeText(this, "Save to DB here", Toast.LENGTH_SHORT).show();
//        //mAuth = FirebaseAuth.getInstance();
////
////        final String userID = mAuth.getCurrentUser().getUid();
////
////        final String saveCurrentTime, saveCurrentDate;
////
////        Calendar calForDate = Calendar.getInstance();
////        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
////        saveCurrentDate = currentDate.format(calForDate.getTime());
////
////        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
////        saveCurrentTime = currentTime.format(calForDate.getTime());
////
////            final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);
////
////            HashMap<String, Object> ordersMap = new HashMap<>();
////            ordersMap.put("totalAmount", totalAmt);
////            ordersMap.put("name", custName);
////            ordersMap.put("phone", custPhone);
////            ordersMap.put("homeAddress", custHome);
////            ordersMap.put("streetAddress", custStreet);
////            ordersMap.put("pincode", custPin);
////            ordersMap.put("date", saveCurrentDate);
////            ordersMap.put("time", saveCurrentTime);
////            ordersMap.put("state", "not shipped");
////            ordersMap.put("paymentMethod","UPI");
////
////            orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
////                @Override
////                public void onComplete(@NonNull Task<Void> task) {
////                    if (task.isSuccessful())
////                    {
////                        FirebaseDatabase.getInstance().getReference().child("CartList").child("User View").child(userID).removeValue();
////                        Toast.makeText(PaymentActivity.this, "Your Order has been placed successfully", Toast.LENGTH_LONG).show();
////                        Intent intent = new Intent(PaymentActivity.this, YourOrdersActivity.class);
////                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        startActivity(intent);
////                        finish();
////                    }
////                }
////            });
//    }
//}
