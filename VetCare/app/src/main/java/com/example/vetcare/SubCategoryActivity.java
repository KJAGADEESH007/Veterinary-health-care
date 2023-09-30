package com.example.vetcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SubCategoryActivity extends AppCompatActivity {

    String animalCategory;
    ImageButton food,treats,hygiene,medicine,grooming,accesories;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        animalCategory = getIntent().getExtras().get("animal").toString();

        productRef = FirebaseDatabase.getInstance().getReference().child("Custom_Sub_Categories").child(animalCategory);

        recyclerView = findViewById(R.id.recyclerViewSubCategory);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = findViewById(R.id.toolbarDescription);
        setSupportActionBar(toolbar);

        food = findViewById(R.id.animalFood);
        treats = findViewById(R.id.treats);
        hygiene = findViewById(R.id.hygiene);
        medicine = findViewById(R.id.medicine);
        grooming = findViewById(R.id.grooming);
        accesories = findViewById(R.id.accessories);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubCategoryActivity.this,PharmacyActivity.class);
                i.putExtra("category","food");
                i.putExtra("animalCategory",animalCategory);
                startActivity(i);
            }
        });

        treats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubCategoryActivity.this,PharmacyActivity.class);
                i.putExtra("category","treats");
                i.putExtra("animalCategory",animalCategory);
                startActivity(i);
            }
        });

        hygiene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubCategoryActivity.this,PharmacyActivity.class);
                i.putExtra("category","hygiene");
                i.putExtra("animalCategory",animalCategory);
                startActivity(i);
            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubCategoryActivity.this,PharmacyActivity.class);
                i.putExtra("category","medicine");
                i.putExtra("animalCategory",animalCategory);
                startActivity(i);
            }
        });

        grooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubCategoryActivity.this,PharmacyActivity.class);
                i.putExtra("category","grooming");
                i.putExtra("animalCategory",animalCategory);
                startActivity(i);
            }
        });

        accesories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubCategoryActivity.this,PharmacyActivity.class);
                i.putExtra("category","accesories");
                i.putExtra("animalCategory",animalCategory);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<SubCategories> options = new FirebaseRecyclerOptions.Builder<SubCategories>()
                .setQuery(productRef, SubCategories.class)
                .build();


        FirebaseRecyclerAdapter<SubCategories, SubCategoryViewHolder> adapter = new FirebaseRecyclerAdapter<SubCategories, SubCategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SubCategoryViewHolder subcategoryViewHolder, int i, @NonNull final SubCategories subcategories) {
                subcategoryViewHolder.txtProductName.setText(subcategories.getName());
                Picasso.get().load(subcategories.getImage()).into(subcategoryViewHolder.imageView);

                subcategoryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubCategoryActivity.this, PharmacyActivity.class);
                        intent.putExtra("category", subcategories.getName());
                        intent.putExtra("animalCategory",animalCategory);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_add_category, parent, false);
                SubCategoryViewHolder holder = new SubCategoryViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}