package com.example.email.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.EmailListAdapter;
import com.example.email.models.Email;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class FolderActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EmailListAdapter emailListAdapter;
    private List<Email> emails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        initEmails();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Folder");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        emailListAdapter = new EmailListAdapter(emails);
        emailListAdapter.setOnItemClickListener(position -> {
            Email email = emails.get(position);
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            intent.putExtra("From", email.getFrom());
            intent.putExtra("Subject", email.getSubject());
            intent.putExtra("Content", email.getContent());
            startActivity(intent);
        });
        recyclerView.setAdapter(emailListAdapter);

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    private void initEmails() {
        emails.add(new Email((long) 1, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 2, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 3, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 4, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 5, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 6, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 7, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 8, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 9, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emails.add(new Email((long) 10, "John Doe", "Dusan", new Date(), "Android", "This is a short description"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.folder_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_folder) {
            Toast.makeText(getApplicationContext(), "Edit clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}