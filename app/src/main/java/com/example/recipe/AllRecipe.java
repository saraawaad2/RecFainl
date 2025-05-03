package com.example.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AllRecipe extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipe);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        findViewById(R.id.back_button).setOnClickListener(v -> startActivity(new Intent(AllRecipe.this, MainActivity.class)));
        findViewById(R.id.card_breakfast).setOnClickListener(v -> openCategory("Breakfast"));
        findViewById(R.id.card_lunch).setOnClickListener(v -> openCategory("Lunch"));
        findViewById(R.id.card_dinner).setOnClickListener(v -> openCategory("Dinner"));
        findViewById(R.id.card_Desserts).setOnClickListener(v -> openCategory("Desserts"));
    }
//moves the user to the page with the wanted kind of rec(dinner/...)
    private void openCategory(String category) {
        Intent intent = new Intent(this, Collection.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}