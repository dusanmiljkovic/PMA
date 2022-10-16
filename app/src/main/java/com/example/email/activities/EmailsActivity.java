package com.example.email.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
import com.example.email.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class EmailsActivity extends BaseActivity {
    private MailDatabase db;
    private RecyclerView recyclerView;
    private EmailListAdapter emailListAdapter;
    private boolean sortAscending;
    private List<Message> messagesList = new ArrayList<>();
    boolean isLoading = false;
    private FloatingActionButton createEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);

        initialise();
        addWorker();
        initEmails();
        initAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        initScrollListener();
        initClickListener();
        addListenerForCreatingEmails();
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

    private void initialise(){
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_emails);
        setCheckedItem(R.id.menu_emails);
        createEmailButton = findViewById(R.id.create_email_button);
        db = MailDatabase.getDbInstance(EmailsActivity.this);
        SharedPreferences sp = getSharedPreferences(Constants.MESSAGES_SORT, 0);
        sortAscending = sp.getBoolean(Constants.MESSAGES_SORT_ASCENDING, false);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initEmails() {
        messagesList = db.messageDao().getNext(sortAscending, 0, 10); //TODO: LIMIT
    }

    private void initAdapter(){
        emailListAdapter = new EmailListAdapter(messagesList);
        recyclerView.setAdapter(emailListAdapter);
    }

    private void initClickListener(){
        emailListAdapter.setOnItemClickListener(position -> {
            Message message = messagesList.get(position);
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            intent.putExtra("MessageId", message.id);
            startActivity(intent);
        });
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == messagesList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        messagesList.add(null);
        emailListAdapter.notifyItemInserted(messagesList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(() -> {
            messagesList.remove(messagesList.size() - 1);
            int scrollPosition = messagesList.size();
            emailListAdapter.notifyItemRemoved(scrollPosition);

            List<Message> messagesToAdd = db.messageDao().getNext(sortAscending, scrollPosition, 10);
            messagesList.addAll(messagesToAdd);

            emailListAdapter.notifyDataSetChanged();
            isLoading = false;
        }, 2000);
    }

    private void addListenerForCreatingEmails(){
        createEmailButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreateEmailActivity.class)));
    }

    private void addWorker(){

        Constraints constraint = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest uploadWorkRequest = new PeriodicWorkRequest.Builder(
                MailWorker.class,
                30,
                TimeUnit.MINUTES
        )
                .setConstraints(constraint)
                .addTag("my_unique_worker")
                .build();

        WorkManager
                .getInstance(EmailsActivity.this)
                .enqueue(uploadWorkRequest);
    }
}