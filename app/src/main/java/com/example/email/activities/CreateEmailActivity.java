package com.example.email.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.email.R;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;
import com.example.email.entities.Message;
import com.example.email.services.MailService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class CreateEmailActivity extends BaseActivity {

    private MailDatabase db;
    private Account account;
    private Toolbar toolbar;
    private TextInputEditText emailTo;
    private TextInputEditText emailCc;
    private TextInputEditText emailBcc;
    private TextInputEditText emailSubject;
    private TextInputEditText emailContent;
    private final Message message = new Message();
    private MailService mailService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email);

        initData();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initToolbar();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Compose");
    }

    private void initData(){
        db = MailDatabase.getDbInstance(CreateEmailActivity.this);
        account = db.accountDao().getLast();

        emailTo = findViewById(R.id.create_email_to);
        emailCc = findViewById(R.id.create_email_cc);
        emailBcc = findViewById(R.id.create_email_bcc);
        emailSubject = findViewById(R.id.create_email_subject);
        emailContent = findViewById(R.id.create_email_content);
        Button bCancel = findViewById(R.id.create_email_cancel_button);
        bCancel.setOnClickListener(view -> {
            finish();
        });
        mailService = new MailService(CreateEmailActivity.this);
        Bundle extras = getIntent().getExtras();
        int messageId = extras.getInt("MessageId", -1);
        if (messageId != -1){
            Message message = db.messageDao().findById(messageId);
            emailTo.setText(message.to);
//            emailCc.setText(message.to);
//            emailBcc.setText(message.to);
            emailSubject.setText(message.subject);
            emailContent.setText(message.content);
        }
    }


    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            if (!emailTo.getText().toString().isEmpty()
//                    || !emailCc.getText().toString().isEmpty()
//                    || !emailBcc.getText().toString().isEmpty()
                    || !emailSubject.getText().toString().isEmpty()
                    || !emailContent.getText().toString().isEmpty()
            )
                saveToDatabase();
            finish();
        });
    }

    private void saveToDatabase(){
        String to = emailTo.getText().toString().trim();
        String subject = emailTo.getText().toString().trim();
        String content = emailTo.getText().toString().trim();
        message.to = to;
        message.subject = subject;
        message.content = content;
        if (!to.isEmpty() || !subject.isEmpty() || !content.isEmpty())
            new SaveMail().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_email_options_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        saveToDatabase();
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            String to = emailTo.getText().toString().trim();
            String subject = emailTo.getText().toString().trim();
            String content = emailTo.getText().toString().trim();
            if (to.isEmpty()){
                Toast.makeText(getApplicationContext(),"Receiver is required", Toast.LENGTH_SHORT).show();
            }else{
                message.to = to;
                message.subject = subject;
                message.content = content;
                new SendMail().execute();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class SendMail extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CreateEmailActivity.this,
                    "Please wait", "Sending email...", true, false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                mailService.sendMail(message);
                return "Success";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.equals("Success"))
            {
                finish();
                Toast.makeText(getApplicationContext(),"Email successfully sent", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class SaveMail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Saving message to drafts", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                mailService.saveMessage(message);
                return "Success";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("Success"))
            {
                finish();
                Toast.makeText(getApplicationContext(),"Email saved in drafts", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}