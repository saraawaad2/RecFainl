package com.example.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CollectionFragment extends Fragment {

    private ImageView backButton, recipeImage, likeButton;
    private TextView title, prepTime, ingredients, steps, videoLink;
    private Button addToLikedButton;
    private FirebaseFirestore db;
    private String recipeId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        recipeImage = view.findViewById(R.id.recipe_image);
        likeButton = view.findViewById(R.id.like_button);
        title = view.findViewById(R.id.recipe_title);
        prepTime = view.findViewById(R.id.recipe_prep_time);
        ingredients = view.findViewById(R.id.recipe_ingredients);
        steps = view.findViewById(R.id.recipe_steps);
        videoLink = view.findViewById(R.id.video_link);
        addToLikedButton = view.findViewById(R.id.add_to_liked_button);
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            recipeId = getArguments().getString("recipeId");
            String name = getArguments().getString("name");
            String image = getArguments().getString("image");
            String prep = getArguments().getString("prep_time");
            String ing = getArguments().getString("ingredients");
            String vid = getArguments().getString("video_link");
            String step = getArguments().getString("steps");

            title.setText(name);
            prepTime.setText("Duration: " + prep);
            ingredients.setText("Ingredients: " + ing);
            videoLink.setText("Video: " + vid);
            steps.setText(step);
            Glide.with(requireContext()).load(image).into(recipeImage);

            addToLikedButton.setOnClickListener(v -> saveToLiked(name, image, prep, ing, vid, step));
            likeButton.setOnClickListener(v -> saveToLiked(name, image, prep, ing, vid, step));
        }


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        View decorView = requireActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
    private void saveToLiked(String name, String image, String prep, String ing, String vid, String step) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecipeModel model = new RecipeModel(name, image, prep, ing, vid, step);
        model.setSteps(step);

        db.collection("Liked")
                .document(uid)
                .collection("Recipes")
                .document(recipeId)
                .set(model);
        Toast.makeText(getContext(), "this Recipe is saved", Toast.LENGTH_SHORT).show();
    }
}
