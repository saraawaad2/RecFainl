package com.example.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonRegister;
    TextView alreadyHaveAccount;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.username);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.ConfirmPassword);
        buttonRegister = findViewById(R.id.button_register);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveaccount);
        progressBar = findViewById(R.id.progressbar);

        alreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, login.class));
            finish();
        });

        buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editTextName.setError("Enter username");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, login.class));
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}