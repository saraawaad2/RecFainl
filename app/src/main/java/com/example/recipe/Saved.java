package com.example.recipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Saved extends AppCompatActivity {

    private RecyclerView savedRecycler;
    private ImageView backButton;
    private RecipeAdapter adapter;
    private final List<RecipeModel> savedList = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        findViewById(R.id.back_button).setOnClickListener(v -> startActivity(new Intent(Saved.this, MainActivity.class)));

        savedRecycler = findViewById(R.id.saved_recycler);
        savedRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecipeAdapter(this, savedList, (model, recipeId) -> {
            Intent intent = new Intent(Saved.this, ResultDetails.class);
            intent.putExtra("recipeId", recipeId);
            intent.putExtra("name", model.getName());
            intent.putExtra("image", model.getImage());
            intent.putExtra("prep_time", model.getPrep_time());
            intent.putExtra("ingredients", model.getIngredients());
            intent.putExtra("steps", model.getSteps());
            intent.putExtra("video_link", model.getVideo_link());
            startActivity(intent);
        });

        adapter.setOnItemLongClickListener((model, recipeId) -> showDeleteDialog(recipeId));
        savedRecycler.setAdapter(adapter);


        loadSavedRecipes();
    }

    private void loadSavedRecipes() {
        savedList.clear();
        db.collection("Liked")
                .document(uid)
                .collection("Recipes")
                .get()
                .addOnSuccessListener(query -> {
                    for (QueryDocumentSnapshot doc : query) {
                        RecipeModel model = doc.toObject(RecipeModel.class);
                        model.setDocumentId(doc.getId());
                        savedList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load saved recipes", Toast.LENGTH_SHORT).show());
    }

    private void showDeleteDialog(String recipeId) {
        new AlertDialog.Builder(this)
                .setTitle("Remove from Saved")
                .setMessage("Are you sure you want to remove this recipe from saved?")
                .setPositiveButton("Yes", (dialog, which) -> deleteRecipe(recipeId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteRecipe(String recipeId) {
        db.collection("Liked")
                .document(uid)
                .collection("Recipes")
                .document(recipeId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Recipe removed", Toast.LENGTH_SHORT).show();
                    loadSavedRecipes(); // reload
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to remove recipe", Toast.LENGTH_SHORT).show());
    }
}

