package com.example.email.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.email.R;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Message;
import com.example.email.services.MailService;

import java.util.Objects;

public class EmailActivity extends BaseActivity {

    private Bundle extras;
    private MailDatabase db;
    private Toolbar toolbar;
    private TextView tEmailFrom;
    private TextView tEmailTo;
    private TextView tEmailSubject;
    private WebView tEmailContent;
    private ImageView ivSenderImage;
    private MailService service;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        db = MailDatabase.getDbInstance(EmailActivity.this);
        extras = getIntent().getExtras();
        service = new MailService(EmailActivity.this);
        tEmailFrom = findViewById(R.id.email_from);
        tEmailTo = findViewById(R.id.email_to);
        tEmailSubject = findViewById(R.id.email_subject);
        tEmailContent = findViewById(R.id.email_content);
        ivSenderImage = findViewById(R.id.email_sender_icon);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Email");

        initContent();
    }

    private void initContent() {
        int messageId = extras.getInt("MessageId");
        message = db.messageDao().findById(messageId);

        tEmailFrom.setText(message.from.split("<")[0]);
        tEmailTo.setText(message.to);
        tEmailSubject.setText(message.subject);
        ivSenderImage.setBackgroundResource(R.mipmap.ic_launcher);

        tEmailContent.getSettings().setBuiltInZoomControls(true);
        tEmailContent.getSettings().setJavaScriptEnabled(true);
        tEmailContent.getSettings().setDomStorageEnabled(true);
        if (message.textIsHtml) {
            tEmailContent.loadDataWithBaseURL("", message.content, "text/html; charset=utf-8", "utf-8", null);
        }
        else {
            tEmailContent.loadData(message.content, "text; charset=utf-8", "utf-8");
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.email_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                Toast.makeText(getApplicationContext(), "Save clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_delete:
                DeleteMail runner = new DeleteMail();
                runner.execute();
//                Toast.makeText(getApplicationContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_reply:
                Toast.makeText(getApplicationContext(), "Reply clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_reply_all:
                Toast.makeText(getApplicationContext(), "Reply all clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_forward:
                Toast.makeText(getApplicationContext(), "Forward clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DeleteMail extends AsyncTask<String, String, String> {

        private String resp;



        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Deleting message", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                service.deleteMail(message.messageNumber);
                return "Success";
            } catch (Exception e) {
                e.printStackTrace();
                return "Fail";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            if (Objects.equals(result, "Success")) {
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
}