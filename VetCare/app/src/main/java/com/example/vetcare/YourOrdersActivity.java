package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class YourOrdersActivity extends AppCompatActivity {

    RecyclerView ordersList;
    DatabaseReference ordersRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);


        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.recyclerNewOrders);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(ordersRef,Orders.class)
                .build();

        FirebaseRecyclerAdapter<Orders,OrdersVievHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrdersVievHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdersVievHolder ordersVievHolder, final int i, @NonNull final Orders orders)
            {
                ordersVievHolder.userName.setText("Name: " + orders.getName());
                ordersVievHolder.phoneNumber.setText("Phone: " + orders.getPhone());
                ordersVievHolder.totalPrice.setText("Total Price: " + orders.getTotalAmount());
                ordersVievHolder.dateTime.setText("Date: " + orders.getDate() + "Time: " + orders.getTime());
                ordersVievHolder.shippingAddress.setText("Address: " + orders.getHomeAddress() + " " + orders.getStreetAddress());
                ordersVievHolder.pincode.setText("Pincode: " + orders.getPincode());
                ordersVievHolder.state.setText("State: "+orders.getState());
                ordersVievHolder.payementMethod.setText("Payment Method: "+orders.getPaymentMethod());

                ordersVievHolder.showProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String uID = getRef(i).getKey();

                        Intent intent = new Intent(YourOrdersActivity.this, UserProducts.class);
                        intent.putExtra("uid",uID);
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public OrdersVievHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new OrdersVievHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class OrdersVievHolder extends RecyclerView.ViewHolder
    {
        public TextView userName, phoneNumber, totalPrice, dateTime, shippingAddress,pincode,state, payementMethod;
        public Button showProducts;


        public OrdersVievHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.userNameOrders);
            phoneNumber = itemView.findViewById(R.id.phoneNumberOrders);
            totalPrice = itemView.findViewById(R.id.totalPriceOrders);
            dateTime = itemView.findViewById(R.id.dateTimeOrders);
            shippingAddress = itemView.findViewById(R.id.addressOrders);
            showProducts = itemView.findViewById(R.id.showAllProducts);
            pincode = itemView.findViewById(R.id.pincodeOrders);
            state = itemView.findViewById(R.id.stateOrders);
            payementMethod = itemView.findViewById(R.id.paymentMethodOrders);
        }
    }
}