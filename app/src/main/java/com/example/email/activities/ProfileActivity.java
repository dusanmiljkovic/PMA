package com.example.email.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.email.R;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class ProfileActivity extends BaseActivity {

    private EditText etDisplayName;
    private Button bEdit;
    private Button bSave;
    private Button bCancel;
    private MailDatabase db;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_profile);
        setCheckedItem(R.id.menu_profile);

        initData();

        bEdit.setOnClickListener(v -> {
            bSave.setVisibility(View.VISIBLE);
            bCancel.setVisibility(View.VISIBLE);
            bEdit.setVisibility(View.INVISIBLE);
            etDisplayName.setEnabled(true);
        });

        bSave.setOnClickListener(view -> {
            bSave.setVisibility(View.INVISIBLE);
            bCancel.setVisibility(View.INVISIBLE);
            bEdit.setVisibility(View.VISIBLE);

            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.getHeaderView(0);
            TextView navDisplayName = (TextView) headerView.findViewById(R.id.displayName);
            navDisplayName.setText(etDisplayName.getText().toString().trim());
            account.displayName = etDisplayName.getText().toString().trim();
            db.accountDao().updateAccount(account);
            etDisplayName.setEnabled(false);
        });

        bCancel.setOnClickListener(view -> {
            bSave.setVisibility(View.INVISIBLE);
            bCancel.setVisibility(View.INVISIBLE);
            bEdit.setVisibility(View.VISIBLE);

            etDisplayName.setText(account.displayName);
            etDisplayName.setEnabled(false);
        });


    }

    private void initData(){
        db = MailDatabase.getDbInstance(ProfileActivity.this);
        TextView tProfileEmail = findViewById(R.id.profile_email);
        etDisplayName = findViewById(R.id.profile_display_name);
        bEdit = findViewById(R.id.profile_edit_button);
        bSave = findViewById(R.id.profile_save_button);
        bCancel = findViewById(R.id.profile_cancel_button);

        account = db.accountDao().getLast();
        tProfileEmail.setText(account.username);
        etDisplayName.setText(account.displayName == null ? account.username : account.displayName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }
}