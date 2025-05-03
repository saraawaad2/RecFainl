package com.example.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultDetails extends AppCompatActivity {

    private ImageView backButton, recipeImage, likeButton;
    private TextView title, prepTime, ingredients, steps, videoLink;
    private Button addToLikedButton;
    private FirebaseFirestore db;
    private String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);
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
        recipeImage = findViewById(R.id.recipe_image);
        likeButton = findViewById(R.id.like_button);
        title = findViewById(R.id.recipe_title);
        prepTime = findViewById(R.id.recipe_prep_time);
        ingredients = findViewById(R.id.recipe_ingredients);
        steps = findViewById(R.id.recipe_steps);
        videoLink = findViewById(R.id.video_link);
        addToLikedButton = findViewById(R.id.add_to_liked_button);

        db = FirebaseFirestore.getInstance();

        recipeId = getIntent().getStringExtra("recipeId");
        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        String prep = getIntent().getStringExtra("prep_time");
        String ing = getIntent().getStringExtra("ingredients");
        String vid = getIntent().getStringExtra("video_link");
        String step = getIntent().getStringExtra("steps");

        title.setText(name);
        prepTime.setText("Duration: " + prep);
        ingredients.setText("Ingredients: " + ing);
        videoLink.setText("Video: " + vid);
        steps.setText(step);
        Glide.with(this).load(image).into(recipeImage);

        backButton.setOnClickListener(v -> onBackPressed());

        addToLikedButton.setOnClickListener(v -> saveToLiked(name, image, prep, ing, vid, step));
        likeButton.setOnClickListener(v -> saveToLiked(name, image, prep, ing, vid, step));
    }

    private void saveToLiked(String name, String image, String prep, String ing, String vid, String step) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RecipeModel model = new RecipeModel(name, image, prep, ing, vid, step);
        model.setDocumentId(recipeId);

        db.collection("Liked")
                .document(uid)
                .collection("Recipes")
                .document(recipeId)
                .set(model);
    }
}
