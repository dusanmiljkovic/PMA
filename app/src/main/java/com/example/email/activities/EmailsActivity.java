package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.email.R;
import com.example.email.adapters.EmailListAdapter;
import com.example.email.models.Email;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EmailsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private EmailListAdapter emailListAdapter;
    private final List<Email> emailList = new ArrayList<>();
    private FloatingActionButton createEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);
        initEmails();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_emails);
        setCheckedItem(R.id.menu_emails);
        createEmailButton = findViewById(R.id.create_email_button);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        emailListAdapter = new EmailListAdapter(emailList);

        recyclerView.setAdapter(emailListAdapter);
        emailListAdapter.setOnItemClickListener(position -> {
            Email email = emailList.get(position);
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            intent.putExtra("id", email.getId());
            intent.putExtra("Subject", email.getSubject());
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
        emailList.add(new Email((long) 1, "John1 Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emailList.add(new Email((long) 2, "John2 Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emailList.add(new Email((long) 3, "John3 Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emailList.add(new Email((long) 4, "John4 Doe", "Dusan", new Date(), "Android", "This is a short description"));
        emailList.add(new Email((long) 5, "John4 Doe", "Dusan", new Date(), "Android", "This is a short description"));
    }

}