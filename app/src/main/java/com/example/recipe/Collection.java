package com.example.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Collection extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView backButton;
    private TextView titleText;
    private RecipeAdapter adapter;
    private List<RecipeModel> recipeList;
    private DatabaseReference categoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        recyclerView = findViewById(R.id.blog_recycler);
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.collection_title);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String category = getIntent().getStringExtra("category");
        titleText.setText(category);

        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(this, recipeList, (model, recipeId) -> {
            Bundle bundle = new Bundle();
            bundle.putString("recipeId", recipeId);
            bundle.putString("name", model.getName());
            bundle.putString("image", model.getImage());
            bundle.putString("prep_time", model.getPrep_time());
            bundle.putString("ingredients", model.getIngredients());
            bundle.putString("steps", model.getSteps());
            bundle.putString("video_link", model.getVideo_link());

            Fragment fragment = new CollectionFragment();
            fragment.setArguments(bundle);



            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(android.R.id.content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        recyclerView.setAdapter(adapter);

        categoryRef = FirebaseDatabase.getInstance().getReference("Category").child(category);
        loadRecipes();

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void loadRecipes() {
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipeList.clear();
                for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                    String id = recipeSnapshot.getKey();
                    String name = recipeSnapshot.child("name").getValue(String.class);
                    String image = recipeSnapshot.child("image").getValue(String.class);
                    String prepTime = recipeSnapshot.child("prep_time").getValue(String.class);
                    String ingredients = recipeSnapshot.child("ingredients").getValue(String.class);
                    String videoLink = recipeSnapshot.child("video_link").getValue(String.class);

                    StringBuilder stepsBuilder = new StringBuilder();
                    for (DataSnapshot stepSnapshot : recipeSnapshot.child("steps").getChildren()) {
                        stepsBuilder.append("• ").append(stepSnapshot.getValue(String.class)).append("\n");
                    }

                    RecipeModel model = new RecipeModel(name, image, prepTime, ingredients, videoLink, stepsBuilder.toString());
                    model.setDocumentId(id);
                    recipeList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }
}
