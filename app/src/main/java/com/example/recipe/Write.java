package com.example.recipe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Write extends AppCompatActivity {

    private EditText recipeNameInput, descriptionInput;
    private TextView durationDisplay;
    private Slider durationSlider;
    private ImageView imagePreview, closeImageButton, backButton;
    private Button pickImage, saveBtn;
    private RelativeLayout imagePreviewContainer;

    private Uri imageUri;
    private Bitmap imageBitmap;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser user;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        recipeNameInput = findViewById(R.id.edit_recipe_name);
        descriptionInput = findViewById(R.id.edit_description);
        durationDisplay = findViewById(R.id.duration_display);
        durationSlider = findViewById(R.id.duration_slider);
        imagePreview = findViewById(R.id.image_preview);
        closeImageButton = findViewById(R.id.close_image_button);
        imagePreviewContainer = findViewById(R.id.image_preview_container);
        pickImage = findViewById(R.id.btn_pick_image);
        saveBtn = findViewById(R.id.btn_save);
        backButton = findViewById(R.id.back_button);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        durationSlider.addOnChangeListener((slider, value, fromUser) ->
                durationDisplay.setText((int) value + " min"));

        pickImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            } else {
                dispatchTakePictureIntent();
            }
        });

        closeImageButton.setOnClickListener(v -> {
            imageBitmap = null;
            imagePreviewContainer.setVisibility(View.GONE);
        });
        PackageManager pm = getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, "This device has no camera.", Toast.LENGTH_SHORT).show();
            return;
        }


        saveBtn.setOnClickListener(v -> saveRecipe());

        backButton.setOnClickListener(v -> finish());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            Toast.makeText(this, "Opening camera...", Toast.LENGTH_SHORT).show();
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_LONG).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageUri = getImageUri(this, imageBitmap);
            imagePreview.setImageBitmap(imageBitmap);
            imagePreviewContainer.setVisibility(View.VISIBLE);
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                context.getContentResolver(), bitmap, "RecipeImage", null);
        return Uri.parse(path);
    }


    private void saveRecipe() {
        String name = recipeNameInput.getText().toString().trim();
        String desc = descriptionInput.getText().toString().trim();
        int duration = (int) durationSlider.getValue();

        if (name.isEmpty() || desc.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and capture an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = UUID.randomUUID().toString() + ".jpg";
        StorageReference ref = storage.getReference("diary_images/" + fileName);

        ref.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("recipeName", name);
                    entry.put("description", desc);
                    entry.put("duration", duration);
                    entry.put("imageUrl", uri.toString());
                    entry.put("userId", user.getUid());

                    db.collection("diary_entries")
                            .add(entry)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(Write.this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(Write.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
        ).addOnFailureListener(e ->
                Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
}
