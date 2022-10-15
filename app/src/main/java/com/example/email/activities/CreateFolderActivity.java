package com.example.email.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.email.R;
import com.example.email.services.MailService;

import java.util.Objects;

public class CreateFolderActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button bSaveFolder;
    private Button bCancel;
    private Switch sHoldsFolders;
    private MailService service;
    private TextView tFolderName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        service = new MailService(CreateFolderActivity.this);
        bSaveFolder = findViewById(R.id.create_folder_save_button);
        bCancel = findViewById(R.id.create_folder_cancel_button);
        tFolderName = findViewById(R.id.create_folder_name);
        sHoldsFolders = findViewById(R.id.create_folder_holds_folders);

        bCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        bSaveFolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CreateFolderAsyncTask createFolderAsyncTask = new CreateFolderAsyncTask();
                createFolderAsyncTask.execute();
            }
        });

        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create new folder");
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }


    private class CreateFolderAsyncTask extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            try {
                service.createFolder(tFolderName.getText().toString(), sHoldsFolders.isChecked());
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Creating folder", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... text) {
//            Toast.makeText(getApplicationContext(), "Deleting message", Toast.LENGTH_SHORT).show();
        }
    }

}