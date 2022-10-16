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

public class CreateFolderActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button bSaveFolder;
    private Button bCancel;
    private MailService service;
    private TextView tFolderName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);

        service = new MailService(CreateFolderActivity.this);
        bSaveFolder = findViewById(R.id.new_folder_save_button);
        bCancel = findViewById(R.id.new_folder_cancel_button);
        tFolderName = findViewById(R.id.new_folder_name);

        bCancel.setOnClickListener(view -> finish());

        bSaveFolder.setOnClickListener(view -> {
            CreateFolderAsyncTask createFolderAsyncTask = new CreateFolderAsyncTask();
            createFolderAsyncTask.execute();
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
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Creating folder", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                service.createFolder(tFolderName.getText().toString());
                return "Success";
            } catch (Exception e) {
                e.printStackTrace();
                return "Fail";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Success")) {
                Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}