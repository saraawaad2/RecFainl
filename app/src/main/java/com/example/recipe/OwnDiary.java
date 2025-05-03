package com.example.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OwnDiary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_diary);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        findViewById(R.id.back_button).setOnClickListener(v -> startActivity(new Intent(OwnDiary.this, MainActivity.class)));

        findViewById(R.id.card_write).setOnClickListener(v ->
                startActivity(new Intent(OwnDiary.this, Write.class)));

        findViewById(R.id.card_blog).setOnClickListener(v ->
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new BlogFragment())
                        .addToBackStack(null)
                        .commit());
    }
}
