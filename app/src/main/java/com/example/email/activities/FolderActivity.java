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
import com.example.email.database.MailDatabase;
import com.example.email.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FolderActivity extends BaseActivity {
    private MailDatabase db;
    private Bundle extras;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private EmailListAdapter emailListAdapter;
    private List<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        extras = getIntent().getExtras();

        db = MailDatabase.getDbInstance(FolderActivity.this);
        initEmails();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Folder");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        emailListAdapter = new EmailListAdapter(messages);
        emailListAdapter.setOnItemClickListener(position -> {
            Message message = messages.get(position);
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            intent.putExtra("From", message.from);
            intent.putExtra("Subject", message.subject);
            intent.putExtra("Content", message.content);
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
        messages = db.folderDao().getFolderWithMessages(extras.getInt("FolderId")).messages;
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