package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.email.R;

import java.util.Objects;

public class EmailActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Email");
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.email_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                Toast.makeText(getApplicationContext(), "Save clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_delete:
                Toast.makeText(getApplicationContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_reply:
                Toast.makeText(getApplicationContext(), "Reply clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_reply_all:
                Toast.makeText(getApplicationContext(), "Reply all clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_forward:
                Toast.makeText(getApplicationContext(), "Forward clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}