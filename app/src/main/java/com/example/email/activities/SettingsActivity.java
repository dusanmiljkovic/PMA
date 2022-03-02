package com.example.email.activities;

import android.os.Bundle;

import com.example.email.R;

import java.util.Objects;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_settings);
        setCheckedItem(R.id.menu_settings);
    }
}