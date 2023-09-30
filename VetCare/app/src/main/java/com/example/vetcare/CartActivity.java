package com.example.vetcare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button nextprocessBtn;
    DatabaseReference cartListRef;
    TextView txtTotalAmt, txtmsg1;
    FirebaseAuth mAuth;

    int overAllPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbarDescription);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.cartList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextprocessBtn = findViewById(R.id.proceedbuttonCart);
        txtTotalAmt = findViewById(R.id.total_price_cart);
        txtmsg1 = findViewById(R.id.msg1);

        nextprocessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this,ConfirmOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overAllPrice));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();

        CheckOrderState();

        cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(userID).child("Products"), Cart.class)
                .build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart)
            {
                cartViewHolder.txtProductQty.setText("Quantity = " + cart.getQuantity());
                cartViewHolder.txtProductPrice.setText("Price = " + cart.getPrice() + "rs");
                cartViewHolder.txtProductName.setText(cart.getName());

                int oneProdPrice = ((Integer.valueOf(cart.getPrice()))) * Integer.valueOf(cart.getQuantity());
                overAllPrice = overAllPrice + oneProdPrice;

                txtTotalAmt.setText("Total Price = rs " + String.valueOf(overAllPrice));

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which == 0)
                                {
                                    Intent intent = new Intent(CartActivity.this,DescriptionActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                }
                                if(which == 1)
                                {
                                    cartListRef.child("User View").child(userID).child("Products").child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(CartActivity.this,"Item removed successfully",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(CartActivity.this,CartActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }


            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void CheckOrderState()
    {

        mAuth = FirebaseAuth.getInstance();
        final String userID = mAuth.getCurrentUser().getUid();

        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        txtTotalAmt.setText("Dear " + userName + "\n order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);

                        txtmsg1.setVisibility(View.GONE);
                        txtmsg1.setText("Your final order is shipped successfully. It will be delevered soon.");
                        nextprocessBtn.setVisibility(View.GONE);
                    }
                    else if (shippingState.equals("not shipped"))
                    {
                        txtTotalAmt.setText("Order is not shipped yet.");
                        recyclerView.setVisibility(View.GONE);

                        txtmsg1.setVisibility(View.GONE);
                        nextprocessBtn.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
