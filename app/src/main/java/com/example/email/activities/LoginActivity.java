package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.email.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername, textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputUsername = findViewById(R.id.login_username);
        textInputPassword = findViewById(R.id.login_password);
        MaterialButton buttonLogin = findViewById(R.id.login_button);

        buttonLogin.setOnClickListener(view -> {
            if (!Objects.requireNonNull(textInputUsername.getEditText()).getText().toString().isEmpty() && !Objects.requireNonNull(textInputPassword.getEditText()).getText().toString().isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), EmailsActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Welcome back " + textInputUsername.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bad credentials!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}