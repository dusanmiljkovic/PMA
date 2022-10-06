package com.example.email.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.email.R;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Contact;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class CreateContactActivity extends BaseActivity {

    private MailDatabase db;
    private Toolbar toolbar;

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText email;
    private Button cancelButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        db = MailDatabase.getDbInstance(CreateContactActivity.this);

        try {
            firstName = findViewById(R.id.create_contact_first_name);
            lastName = findViewById(R.id.create_contact_last_name);
            email = findViewById(R.id.create_contact_email);
            cancelButton = findViewById(R.id.create_contact_cancel_button);
            saveButton = findViewById(R.id.create_contact_save_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    saveContact();
                }
            });
        } catch (Exception e){
            String er = e.toString();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create new contact");
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    private void saveContact(){
        Contact contact = new Contact();
        contact.show = true;
        contact.email = email.getText().toString().trim();
        contact.firstName = firstName.getText().toString().trim();
        contact.lastName = lastName.getText().toString().trim();
        contact.displayName = contact.firstName + " " + contact.lastName;
        if (contact.displayName.trim().length() == 0){
            contact.displayName = email.toString();
        }
        if (email.getText().toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(), "Email must not be empty!", Toast.LENGTH_SHORT).show();
        }
        db.contactDao().insertAll(contact);
        finish();
    }
}