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
import com.example.email.entities.FolderWithMessages;
import com.example.email.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoldersActivity extends BaseActivity {
    private MailDatabase db;
    private RecyclerView recyclerView;
    private List<FolderWithMessages> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        initData();

        fillData();
    }

    private void initData() {
        db = MailDatabase.getDbInstance(FoldersActivity.this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_folders);
        setCheckedItem(R.id.menu_folders);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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

    @Override
    public void onResume()
    {
        super.onResume();
        fillData();
    }

    private void fillData(){
        folders = db.folderDao().getFoldersWithMessages();
        FolderListAdapter folderListAdapter = new FolderListAdapter(folders);

        recyclerView.setAdapter(folderListAdapter);

        folderListAdapter.setOnItemClickListener(position -> {
            FolderWithMessages folderWithMessages = folders.get(position);
            Intent intent = new Intent(getApplicationContext(), FolderActivity.class);
            intent.putExtra("FolderId", folderWithMessages.folder.id);
            startActivity(intent);
        });
    }
}