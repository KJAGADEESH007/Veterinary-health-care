package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PharmacyActivity extends AppCompatActivity {

    String animalType,prodCategory;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Products p;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        Toolbar toolbar = findViewById(R.id.toolbarPharmacy);
        setSupportActionBar(toolbar);

        loadingBar = new ProgressDialog(this);

        animalType = getIntent().getExtras().get("animalCategory").toString();
        prodCategory = getIntent().getExtras().get("category").toString();

        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(animalType).child(prodCategory);

        recyclerView = findViewById(R.id.recyclerViewProduct);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingBar.setTitle("Pharmacy");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products)
            {
                productViewHolder.txtProductName.setText(products.getName());
                productViewHolder.txtProductPrice.setText(products.getPrice());
                Picasso.get().load(products.getImage()).into(productViewHolder.imageView);
                loadingBar.dismiss();

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PharmacyActivity.this, DescriptionActivity.class);
                        intent.putExtra("pid", products.getPid());
                        intent.putExtra("animal",animalType);
                        intent.putExtra("category",prodCategory);
                        startActivity(intent);
                    }
                });
            }



            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_products, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbarpharmacy, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cart) {
            Intent intent = new Intent(PharmacyActivity.this, CartActivity.class);
            startActivity(intent);
        }

        if (id == R.id.searchItem)
        {
            Intent intent = new Intent(PharmacyActivity.this,SearchActivity.class);
            intent.putExtra("animal",animalType);
            intent.putExtra("category",prodCategory);
            startActivity(intent);
        }

        return true;
    }
}