package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryActivity extends AppCompatActivity {

    ImageButton dog,cat,birds,cow,aquatics,other;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        productRef = FirebaseDatabase.getInstance().getReference().child("Custom_Categories");

        recyclerView = findViewById(R.id.recyclerViewCategory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = findViewById(R.id.toolbarDescription);
        setSupportActionBar(toolbar);

        dog = findViewById(R.id.dog);
        cat = findViewById(R.id.cat);
        birds = findViewById(R.id.birds);
        cow = findViewById(R.id.cow);
        aquatics = findViewById(R.id.aquatics);
        other = findViewById(R.id.otherAnimals);

        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                i.putExtra("animal","dog");
                startActivity(i);
            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                i.putExtra("animal","cat");
                startActivity(i);
            }
        });

        birds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                i.putExtra("animal","birds");
                startActivity(i);
            }
        });

        cow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                i.putExtra("animal","cow");
                startActivity(i);
            }
        });


        aquatics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                i.putExtra("animal","aquatics");
                startActivity(i);
            }
        });


        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                i.putExtra("animal","other");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Categories> options = new FirebaseRecyclerOptions.Builder<Categories>()
                .setQuery(productRef, Categories.class)
                .build();


        FirebaseRecyclerAdapter<Categories, CategoryViewHolder> adapter = new FirebaseRecyclerAdapter<Categories, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i, @NonNull final Categories categories) {
                categoryViewHolder.txtProductName.setText(categories.getName());
                Picasso.get().load(categories.getImage()).into(categoryViewHolder.imageView);

                categoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                        intent.putExtra("animal", categories.getName());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_add_category, parent, false);
                CategoryViewHolder holder = new CategoryViewHolder(view);
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
            Intent intent = new Intent(CategoryActivity.this, CartActivity.class);
            startActivity(intent);
        }

        return true;
    }
}