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
import com.example.email.models.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoldersActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private FolderListAdapter folderListAdapter;
    private final List<Folder> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
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
        folders.add(new Folder((long) 1, "Important messages", 6));
        folders.add(new Folder((long) 2, "Starred messages", 1));
        folders.add(new Folder((long) 3, "Sent messages", 15));
        folders.add(new Folder((long) 4, "Junk messages", 3));
        folders.add(new Folder((long) 5, "Deleted messages", 0));
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