package com.example.email.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ContactActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextInputEditText contactFirstName;
    private TextInputEditText contactLastName;
    private TextInputEditText contactEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        final Bundle extras = getIntent().getExtras();

        Log.v("firstName", extras.getString("FirstName"));

        contactFirstName = findViewById(R.id.contact_first_name);
        contactLastName = findViewById(R.id.contact_last_name);
        contactEmail = findViewById(R.id.contact_email);

        contactFirstName.setText(extras.getString("FirstName"), TextInputEditText.BufferType.EDITABLE);
        contactLastName.setText(extras.getString("LastName"), TextInputEditText.BufferType.EDITABLE);
        contactEmail.setText(extras.getString("Email"), TextInputEditText.BufferType.EDITABLE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Contact");
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
        menuInflater.inflate(R.menu.contact_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_contact) {
            Toast.makeText(getApplicationContext(), "Edit clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}