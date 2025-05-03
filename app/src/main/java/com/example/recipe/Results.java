package com.example.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Results extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private final List<RecipeModel> filteredList = new ArrayList<>();
    private ImageView backButton;
    private List<String> selectedIngredients;
    private final List<String> categories = Arrays.asList("Breakfast", "Lunch", "Dinner", "Desserts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        recyclerView = findViewById(R.id.results_recycler);
        backButton = findViewById(R.id.back_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectedIngredients = getIntent().getStringArrayListExtra("selectedIngredients");
        Log.d("RESULTS", "Selected ingredients: " + selectedIngredients);

        adapter = new RecipeAdapter(this, filteredList, (model, recipeId) -> {
            Intent intent = new Intent(Results.this, ResultDetails.class);
            intent.putExtra("recipeId", recipeId);
            intent.putExtra("name", model.getName());
            intent.putExtra("image", model.getImage());
            intent.putExtra("prep_time", model.getPrep_time());
            intent.putExtra("ingredients", model.getIngredients());
            intent.putExtra("steps", model.getSteps());
            intent.putExtra("video_link", model.getVideo_link());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> startActivity(new Intent(Results.this, PickIngredient.class)));

        loadAndFilterRecipes();
    }

    private void loadAndFilterRecipes() {
        filteredList.clear();
        final int[] completedCategories = {0}; // use array to allow modification inside inner class

        for (String category : categories) {
            DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("Category").child(category);

            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                        String id = recipeSnapshot.getKey();
                        String name = recipeSnapshot.child("name").getValue(String.class);
                        String image = recipeSnapshot.child("image").getValue(String.class);
                        String prepTime = recipeSnapshot.child("prep_time").getValue(String.class);
                        String ingredientsRaw = recipeSnapshot.child("ingredients").getValue(String.class);
                        String videoLink = recipeSnapshot.child("video_link").getValue(String.class);

                        List<String> steps = new ArrayList<>();
                        for (DataSnapshot stepSnapshot : recipeSnapshot.child("steps").getChildren()) {
                            steps.add(stepSnapshot.getValue(String.class));
                        }

                        List<String> recipeIngredients = new ArrayList<>();
                        if (ingredientsRaw != null) {
                            for (String ing : ingredientsRaw.split(",")) {
                                recipeIngredients.add(ing.trim().toLowerCase());
                            }
                        }

                        boolean containsAll = true;
                        for (String selected : selectedIngredients) {
                            if (!recipeIngredients.contains(selected.trim().toLowerCase())) {
                                containsAll = false;
                                break;
                            }
                        }

                        if (containsAll) {
                            RecipeModel model = new RecipeModel(name, image, prepTime, ingredientsRaw, videoLink, String.join("\n", steps));
                            model.setDocumentId(id);
                            filteredList.add(model);
                        }
                    }

                    // When a category finishes
                    completedCategories[0]++;
                    if (completedCategories[0] == categories.size()) {
                        if (filteredList.isEmpty()) {
                            Toast.makeText(Results.this, "No Recipe with these ingredients found", Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Results.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
