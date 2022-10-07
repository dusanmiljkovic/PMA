package com.example.email.activities;

import android.annotation.SuppressLint;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        db = MailDatabase.getDbInstance(EmailActivity.this);
        extras = getIntent().getExtras();
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
        Message message = db.messageDao().findById(messageId);

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
                Toast.makeText(getApplicationContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
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

}