package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DescriptionActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name, price, weight, description;
    ElegantNumberButton qtyElegant;
    Button addToCart;
    String productID ="", state = "Normal", animal, category;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Toolbar toolbar = findViewById(R.id.toolbarDescription);
        setSupportActionBar(toolbar);

        loadingBar = new ProgressDialog(this);

        productID = getIntent().getStringExtra("pid");
        animal = getIntent().getStringExtra("animal");
        category = getIntent().getStringExtra("category");

        imageView = findViewById(R.id.prodimageViewBuy);
        name = findViewById(R.id.prodNametextViewBuy);
        price = findViewById(R.id.prodPricetextViewBuy);
        weight = findViewById(R.id.prodWeighttextViewBuy);
        description = findViewById(R.id.prodDesctextViewBuy);
        qtyElegant = findViewById(R.id.qtyElegantNumberButton);
        addToCart = findViewById(R.id.addToCartbuttonBuy);

        getProductDetails(productID);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(DescriptionActivity.this,"You can purchase more products, once your order is shipped or confirmed",Toast.LENGTH_LONG).show();
                }
                else
                {
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        CheckOrderState();
    }

    private void addingToCartList()
    {
        loadingBar.setTitle("Cart");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();

        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", name.getText().toString());
        cartMap.put("price", price.getText().toString());
        cartMap.put("quantity", qtyElegant.getNumber());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("weight", weight.getText().toString());

        cartListRef.child("User View").child(userID).child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(userID).child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                loadingBar.dismiss();
                                                Toast.makeText(DescriptionActivity.this, "Product added to cart successfully", Toast.LENGTH_LONG).show();

                                                Intent intent = new Intent(DescriptionActivity.this, CartActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(animal).child(category);
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);

                    name.setText(products.getName());
                    price.setText(products.getPrice());
                    weight.setText(products.getQuantity());
                    description.setText(products.getDesc());
                    Picasso.get().load(products.getImage()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbardesc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cartdesc) {
            Intent intent = new Intent(DescriptionActivity.this, CartActivity.class);
            startActivity(intent);
        }

        return true;
    }

    private void CheckOrderState()
    {
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if (shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}