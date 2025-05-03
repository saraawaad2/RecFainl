package com.example.recipe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PickIngredient extends AppCompatActivity {

    private Set<String> selectedIngredients = new HashSet<>();
    private Button searchButton;
    private ImageView backButton;
    private List<Chip> allChips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_ingredient);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        backButton = findViewById(R.id.back_button);
        searchButton = findViewById(R.id.search_button);
        backButton.setOnClickListener(v -> startActivity(new Intent(PickIngredient.this, MainActivity.class)));

        int[] chipIds = {
                R.id.chip_beef, R.id.chip_lamb, R.id.chip_salmon, R.id.chip_chicken, R.id.chip_turkey,
                R.id.chip_tomato, R.id.chip_pepper, R.id.chip_cabbage, R.id.chip_zucchini, R.id.chip_carrot,
                R.id.chip_banana, R.id.chip_apple, R.id.chip_orange, R.id.chip_grapes, R.id.chip_strawberry,
                R.id.chip_milk, R.id.chip_cheese, R.id.chip_butter, R.id.chip_cream, R.id.chip_yogurt,
                R.id.chip_salt, R.id.chip_sugar, R.id.chip_pepper_seasoning, R.id.chip_oregano,
                R.id.chip_paprika, R.id.chip_beefgarlic_powder
        };

        for (int id : chipIds) {
            Chip chip = findViewById(id);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            allChips.add(chip);

            chip.setOnClickListener(v -> {
                if (chip.isChecked()) {
                    toggleOn(chip);
                } else {
                    toggleOff(chip);
                }
            });
        }

        searchButton.setOnClickListener(v -> {
            selectedIngredients.clear();
            for (Chip chip : allChips) {
                if (chip.isChecked()) {
                    selectedIngredients.add(chip.getText().toString().trim().toLowerCase());
                }
            }
            Intent intent = new Intent(PickIngredient.this, Results.class);
            intent.putStringArrayListExtra("selectedIngredients", new ArrayList<>(selectedIngredients));
            startActivity(intent);
        });
    }

    private void toggleOn(Chip chip) {
        chip.setChecked(true);
        chip.setChipBackgroundColorResource(R.color.purple_500); // Highlight color
        chip.setTextColor(Color.WHITE);
    }

    private void toggleOff(Chip chip) {
        chip.setChecked(false);
        chip.setChipBackgroundColorResource(R.color.white); // Default background
        chip.setTextColor(Color.BLACK);
    }
}
