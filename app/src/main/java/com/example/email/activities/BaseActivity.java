package com.example.email.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.WorkManager;

import com.example.email.R;
import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;
import com.example.email.entities.AccountWithMessages;
import com.example.email.entities.Message;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MailDatabase db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = MailDatabase.getDbInstance(BaseActivity.this);
    }

    @Override
    public void setContentView(int layoutResID) {
        @SuppressLint("InflateParams") DrawerLayout drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = drawerLayout.findViewById(R.id.container_layout);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        super.setContentView(drawerLayout);
        initToolbar();
        initNavigation();
        setNavigationText();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setNavigationText(){
        Account account = db.accountDao().getLast();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        navUsername.setText(account.username);
        TextView navDisplayName = (TextView) headerView.findViewById(R.id.displayName);
        navDisplayName.setText(account.displayName);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_emails:
                startActivity(new Intent(getApplicationContext(), EmailsActivity.class));
                break;
            case R.id.menu_folders:
                startActivity(new Intent(getApplicationContext(), FoldersActivity.class));
                break;
            case R.id.menu_profile:
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.menu_contacts:
                startActivity(new Intent(getApplicationContext(), ContactsActivity.class));
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.menu_logout:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setCheckedItem(int id) {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(id);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void showBackButton(boolean show) {
        if (show) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if (!mToolBarNavigationListenerIsRegistered) {
                actionBarDrawerToggle.setToolbarNavigationClickListener(v -> onBackPressed());
                mToolBarNavigationListenerIsRegistered = true;
            }
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logout() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Logout message")
                .setMessage("Dou you really want to logout?")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNegativeButton("No", (dialogInterface, i) -> {

                })
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    removeLoginCredentials();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                })
                .show();

        WorkManager workManager = WorkManager.getInstance(BaseActivity.this);
        workManager.cancelUniqueWork("my_unique_worker");
    }

    private void removeLoginCredentials(){
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        String username = sp.getString("Username", "");
        Ed.putString("Username", null);
        Ed.putString("Password", null);
        Ed.apply();
        Account ac = db.accountDao().findByEmail(username);
        if (ac != null){
            List<Message> messagesToBeDeleted = db.accountDao().getAccountWithMessages(ac.id).messages;
            List<Integer> folderIds = new ArrayList<>();
            for (Message msg: messagesToBeDeleted) {
                folderIds.add(msg.folderId);
            }
            db.folderDao().deleteAllByIds(folderIds.stream().mapToInt(i->i).toArray());
            db.messageDao().deleteAllByAccountId(ac.id);
            db.accountDao().delete(ac);
        }
    }
}