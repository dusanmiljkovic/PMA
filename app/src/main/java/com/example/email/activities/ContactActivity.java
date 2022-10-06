package com.example.email.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.email.R;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Contact;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ContactActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextInputEditText contactFirstName;
    private TextInputEditText contactLastName;
    private TextInputEditText contactEmail;
    private MaterialButton bCancel;
    private MaterialButton bSave;
    private int contactId;
    private MailDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        db = MailDatabase.getDbInstance(ContactActivity.this);
        final Bundle extras = getIntent().getExtras();

        Log.v("firstName", extras.getString("FirstName"));

        contactFirstName = findViewById(R.id.contact_first_name);
        contactLastName = findViewById(R.id.contact_last_name);
        contactEmail = findViewById(R.id.contact_email);
        bCancel = findViewById(R.id.contact_cancel_button);
        bSave = findViewById(R.id.contact_save_button);

        bCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bCancel.setVisibility(View.INVISIBLE);
                bSave.setVisibility(View.INVISIBLE);
                contactFirstName.setEnabled(false);
                contactLastName.setEnabled(false);
                contactEmail.setEnabled(false);
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateContact();
            }
        });

        contactFirstName.setText(extras.getString("FirstName"), TextInputEditText.BufferType.EDITABLE);
        contactLastName.setText(extras.getString("LastName"), TextInputEditText.BufferType.EDITABLE);
        contactEmail.setText(extras.getString("Email"), TextInputEditText.BufferType.EDITABLE);
        contactId = extras.getInt("id");

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Contact");
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
        menuInflater.inflate(R.menu.contact_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_contact) {
            bCancel.setVisibility(View.VISIBLE);
            bSave.setVisibility(View.VISIBLE);
            contactFirstName.setEnabled(true);
            contactLastName.setEnabled(true);
            contactEmail.setEnabled(true);
//            Toast.makeText(getApplicationContext(), "Edit clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateContact(){
        Contact contact = db.contactDao().findById(contactId);
        contact.firstName = contactFirstName.getText().toString();
        contact.lastName = contactLastName.getText().toString().trim();
        contact.displayName = contact.firstName + " " + contact.lastName;
        if (contact.displayName.trim().length() == 0){
            contact.displayName = contactEmail.getText().toString().trim();
        }
        contact.email = contactEmail.getText().toString().trim();
        if (contact.email.length() == 0){
            Toast.makeText(getApplicationContext(), "Email must not be empty!", Toast.LENGTH_SHORT).show();
        }
        db.contactDao().updateContact(contact);
        finish();
    }

}