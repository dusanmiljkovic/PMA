package com.example.email.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.ContactListAdapter;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactsActivity extends BaseActivity {

    private MailDatabase db;
    private RecyclerView recyclerView;
    private ContactListAdapter contactListAdapter;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        db = MailDatabase.getDbInstance(ContactsActivity.this);
        initContacts();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_contacts);
        setCheckedItem(R.id.menu_contacts);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        populateList();

        contactListAdapter.setOnItemClickListener(position -> {
            Contact contact = contacts.get(position);
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            intent.putExtra("id", contact.id);
            intent.putExtra("DisplayName", contact.displayName);
            intent.putExtra("Email", contact.email);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initContacts();
        populateList();
    }

    private void initContacts() {
        contacts = db.contactDao().getAll();
    }

    private void populateList(){
        contactListAdapter = new ContactListAdapter(contacts);
        recyclerView.setAdapter(contactListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.contacts_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_create_contact) {
            startActivity(new Intent(getApplicationContext(), CreateContactActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}