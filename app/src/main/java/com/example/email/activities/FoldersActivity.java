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
import com.example.email.adapters.FolderListAdapter;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoldersActivity extends BaseActivity {
    private MailDatabase db;
    private RecyclerView recyclerView;
    private FolderListAdapter folderListAdapter;
    private List<Folder> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        db = MailDatabase.getDbInstance(FoldersActivity.this);
        initFolders();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_folders);
        setCheckedItem(R.id.menu_folders);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        folderListAdapter = new FolderListAdapter(folders);

        recyclerView.setAdapter(folderListAdapter);

        folderListAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getApplicationContext(), FolderActivity.class);
            startActivity(intent);
        });
    }

    private void initFolders() {
        folders = db.folderDao().getAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.folders_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_create_folder) {
            startActivity(new Intent(getApplicationContext(), CreateFolderActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}