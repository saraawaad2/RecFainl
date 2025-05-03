package com.example.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        menuIcon = findViewById(R.id.menuIcon);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        menuIcon.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Set user email in nav header
        View headerView = navigationView.getHeaderView(0);
        TextView userEmail = headerView.findViewById(R.id.txtUserEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail.setText(user.getEmail());
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
                return true;
            }
            return false;
        });

        findViewById(R.id.recipeGrid).setOnClickListener(null); // No-op

        // Handle card clicks
        setupCardClickListeners();
    }

    private void setupCardClickListeners() {
        CardView diaryCard = findViewById(R.id.card_diary); // Add ID to card if missing
        diaryCard.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, OwnDiary.class)));

        CardView allRecipe = findViewById(R.id.card_all_recipe); // Add ID to card if missing
        allRecipe.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AllRecipe.class)));

        CardView ingredient = findViewById(R.id.card_ingredient); // Add ID to card if missing
        ingredient.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PickIngredient.class)));

        CardView saved = findViewById(R.id.card_saved); // Add ID to card if missing
        saved.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Saved.class)));
    }
}
