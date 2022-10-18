package com.example.email.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.email.R;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;
import com.example.email.services.MailWorker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private MailDatabase db;
    private TextInputLayout textInputUsername, textInputPassword;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = MailDatabase.getDbInstance(LoginActivity.this);

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
                    addWorker();
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
        Account account = new Account();
        account.username = username;
        account.password = password;
        db.accountDao().insertAll(account);
    }

    private void addWorker(){
        Constraints constraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest uploadWorkRequest = new PeriodicWorkRequest.Builder(
                MailWorker.class,
                30,
                TimeUnit.MINUTES
        )
                .setConstraints(constraint)
                .addTag("my_unique_worker")
                .build();

        WorkManager
                .getInstance(LoginActivity.this)
                .enqueue(uploadWorkRequest);
    }
}