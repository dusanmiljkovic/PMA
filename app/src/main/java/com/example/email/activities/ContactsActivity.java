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
import com.example.email.models.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ContactListAdapter contactListAdapter;
    private final List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initContacts();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_contacts);
        setCheckedItem(R.id.menu_contacts);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        contactListAdapter = new ContactListAdapter(contacts);
        recyclerView.setAdapter(contactListAdapter);

        contactListAdapter.setOnItemClickListener(position -> {
            Contact contact = contacts.get(position);
            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
            intent.putExtra("id", contact.getId());
            intent.putExtra("FirstName", contact.getFirstName());
            intent.putExtra("LastName", contact.getLastName());
            intent.putExtra("Email", contact.getEmail());
            startActivity(intent);
        });
    }


    private void initContacts() {
        contacts.add(new Contact((long) 1, "Dusan", "Miljkovic", "dusan@gmail.com"));
        contacts.add(new Contact((long) 2, "Marko", "Kljajic", "marko@gmail.com"));
        contacts.add(new Contact((long) 3, "Jovan", "Milic", "jovan@gmail.com"));
        contacts.add(new Contact((long) 4, "Nemanja", "Rodic", "nemanja@gmail.com"));
        contacts.add(new Contact((long) 5, "Uros", "Krkic", "uros@gmail.com"));
        contacts.add(new Contact((long) 6, "Milos", "Milosevic", "milos@gmail.com"));
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