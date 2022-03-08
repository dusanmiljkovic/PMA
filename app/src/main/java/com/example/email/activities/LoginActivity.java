package com.example.email.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.email.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername, textInputPassword;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputUsername = findViewById(R.id.login_username);
        textInputPassword = findViewById(R.id.login_password);
        MaterialButton buttonLogin = findViewById(R.id.login_button);

        buttonLogin.setOnClickListener(view -> {
            username = Objects.requireNonNull(textInputUsername.getEditText()).getText().toString();
            password = Objects.requireNonNull(textInputPassword.getEditText()).getText().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), EmailsActivity.class);
                //TODO: add login functionality
                if (true) {
                    saveLoginCredentials();
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Welcome back " + textInputUsername.getEditText().getText().toString(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Bad credentials!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Username and password are required!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLoginCredentials(){
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("Username", username);
        Ed.putString("Password", password);
        Ed.apply();
    }
}