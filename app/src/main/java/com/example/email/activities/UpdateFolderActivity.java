package com.example.email.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.email.R;
import com.example.email.services.MailService;

import java.util.Objects;

public class UpdateFolderActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button bSaveFolder;
    private Button bCancel;
    private MailService service;
    private TextView tFolderName;
    private Bundle extras;
    private int folderId;
    private String oldFolderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_folder);

        intiData();

        bCancel.setOnClickListener(view -> finish());

        bSaveFolder.setOnClickListener(view -> {
            UpdateFolderAsyncTask createFolderAsyncTask = new UpdateFolderAsyncTask();
            createFolderAsyncTask.execute();
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Update folder");
    }

    private void intiData(){
        service = new MailService(UpdateFolderActivity.this);
        bSaveFolder = findViewById(R.id.update_folder_save_button);
        bCancel = findViewById(R.id.update_folder_cancel_button);
        tFolderName = findViewById(R.id.update_folder_name);
        extras = getIntent().getExtras();
        folderId = extras.getInt("FolderId");
        oldFolderName = extras.getString("FolderName");
        tFolderName.setText(oldFolderName);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }


    private class UpdateFolderAsyncTask extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                service.updateFolder(folderId, tFolderName.getText().toString(), oldFolderName);
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Updating folder", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
//            Toast.makeText(getApplicationContext(), "Deleting message", Toast.LENGTH_SHORT).show();
        }
    }

}