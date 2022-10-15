package com.example.email.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.email.R;
import com.example.email.utils.Constants;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class SettingsActivity extends BaseActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch ascendingSwitch;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialise();

        ascendingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(Constants.MESSAGES_SORT_ASCENDING, isChecked);
            editor.apply();
        });

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_settings);
        setCheckedItem(R.id.menu_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    private void initialise(){
        sp = getSharedPreferences(Constants.MESSAGES_SORT, 0);
        ascendingSwitch = findViewById(R.id.settings_message_sort_switch);
        boolean sortAscending = sp.getBoolean(Constants.MESSAGES_SORT_ASCENDING, false);
        ascendingSwitch.setChecked(sortAscending);
    }
}