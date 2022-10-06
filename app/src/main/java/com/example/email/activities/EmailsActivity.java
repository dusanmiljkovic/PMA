package com.example.email.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.email.R;
import com.example.email.adapters.EmailListAdapter;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Message;
import com.example.email.services.MailWorker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EmailsActivity extends BaseActivity {
    private MailDatabase db;
    private EmailListAdapter emailListAdapter;
    private List<Message> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);

        db = MailDatabase.getDbInstance(EmailsActivity.this);
        initEmails();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_emails);
        setCheckedItem(R.id.menu_emails);
        FloatingActionButton createEmailButton = findViewById(R.id.create_email_button);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        emailListAdapter = new EmailListAdapter(messagesList);

        recyclerView.setAdapter(emailListAdapter);

        Constraints constraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest uploadWorkRequest = new PeriodicWorkRequest.Builder(
                MailWorker.class,
                2,
                TimeUnit.MINUTES
        )
                .setConstraints(constraint)
                .addTag("my_unique_worker")
                .build();

        WorkManager
                .getInstance(EmailsActivity.this)
                .enqueue(uploadWorkRequest);


        emailListAdapter.setOnItemClickListener(position -> {
            Message message = messagesList.get(position);
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            intent.putExtra("From", message.from);
            intent.putExtra("Subject", message.subject);
            intent.putExtra("Content", message.content);
            startActivity(intent);
        });

        createEmailButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreateEmailActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.emails_options_menu, menu);

        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search for email here...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                emailListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void initEmails() {
        messagesList = db.messageDao().getAll();
    }
}