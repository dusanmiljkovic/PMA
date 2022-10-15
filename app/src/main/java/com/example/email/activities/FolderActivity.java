package com.example.email.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.email.entities.Folder;
import com.example.email.entities.Message;
import com.example.email.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FolderActivity extends BaseActivity {
    private MailDatabase db;
    private Bundle extras;
    private boolean sortAscending;
    private List<Message> messages = new ArrayList<>();
    private Folder folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        extras = getIntent().getExtras();

        db = MailDatabase.getDbInstance(FolderActivity.this);
        SharedPreferences sp = getSharedPreferences(Constants.MESSAGES_SORT, 0);
        sortAscending = sp.getBoolean(Constants.MESSAGES_SORT_ASCENDING, false);

        initEmails();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle(folder.name);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        EmailListAdapter emailListAdapter = new EmailListAdapter(messages);
        emailListAdapter.setOnItemClickListener(position -> {
            Message message = messages.get(position);
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            intent.putExtra("MessageId", message.id);
            startActivity(intent);
        });
        recyclerView.setAdapter(emailListAdapter);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    private void initEmails() {
        int folderId = extras.getInt("FolderId");
        folder = db.folderDao().findById(folderId);
        messages = db.messageDao().loadAllByFolderId(folderId, sortAscending);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        folder = db.folderDao().findById(folder.id);
        Objects.requireNonNull(getSupportActionBar()).setTitle(folder.name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.folder_options_menu, menu);
        return true;
    }

//    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_folder) {
            Intent intent = new Intent(this, UpdateFolderActivity.class);
            intent.putExtra("FolderId", folder.id);
            intent.putExtra("FolderName", folder.name);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}