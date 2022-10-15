package com.example.email.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.example.email.services.MailService;
import com.example.email.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FolderActivity extends BaseActivity {
    private MailDatabase db;
    private boolean sortAscending;
    private List<Message> messages = new ArrayList<>();
    private Folder folder;
    private RecyclerView recyclerView;
    private MailService service;
    private boolean folderDeletable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        initData();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        fillData();
    }

    private void initData(){
        recyclerView = findViewById(R.id.recycler_view);
        db = MailDatabase.getDbInstance(FolderActivity.this);
        service = new MailService(FolderActivity.this);

        Bundle extras = getIntent().getExtras();
        int folderId = extras.getInt("FolderId");
        folderDeletable = extras.getBoolean("FolderDeletable");
        folder = db.folderDao().findById(folderId);
        Objects.requireNonNull(getSupportActionBar()).setTitle(folder.name);

        SharedPreferences sp = getSharedPreferences(Constants.MESSAGES_SORT, 0);
        sortAscending = sp.getBoolean(Constants.MESSAGES_SORT_ASCENDING, false);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    private void fillData() {
        messages = db.messageDao().loadAllByFolderId(folder.id, sortAscending);
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
        if(!folderDeletable){
            MenuItem item = menu.findItem(R.id.menu_edit_folder);
            item.setVisible(false);
            item = menu.findItem(R.id.menu_delete_folder);
            item.setVisible(false);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit_folder:
                Intent intent = new Intent(this, UpdateFolderActivity.class);
                intent.putExtra("FolderId", folder.id);
                intent.putExtra("FolderName", folder.name);
                startActivity(intent);
                return true;
            case R.id.menu_delete_folder:
                DeleteFolderAsyncTask deleteFolderAsyncTask = new DeleteFolderAsyncTask();
                deleteFolderAsyncTask.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DeleteFolderAsyncTask extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                service.deleteFolder(folder.id);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Delte folder", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
//            Toast.makeText(getApplicationContext(), "Deleting message", Toast.LENGTH_SHORT).show();
        }
    }
}