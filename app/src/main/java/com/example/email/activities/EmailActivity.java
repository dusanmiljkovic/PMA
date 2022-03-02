package com.example.email.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.email.R;

import java.util.Objects;

public class EmailActivity extends BaseActivity {

    private Bundle extras;

    private Toolbar toolbar;
    private TextView tEmailFrom;
    private TextView tEmailSubject;
    private TextView tEmailContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        extras = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Email");

        initContent();
    }

    private void initContent() {
        tEmailFrom = findViewById(R.id.email_from);
        tEmailSubject = findViewById(R.id.email_subject);
        tEmailContent = findViewById(R.id.email_content);

        tEmailFrom.setText(extras.getString("From"));
        tEmailSubject.setText(extras.getString("Subject"));
        tEmailContent.setText(extras.getString("Content"));
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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