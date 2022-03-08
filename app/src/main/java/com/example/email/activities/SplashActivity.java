package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.utils.Constants;
import com.example.email.utils.InternetUtil;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private LinearProgressIndicator progressIndicator;
    private int progress = 0;
    private boolean isLoadingFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        progressIndicator = findViewById(R.id.splash_screen_progress_bar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLoadingProgress();
        startLoginActivity();
    }

    private void setLoadingProgress() {
        if (isLoadingFinished) {
            progressIndicator.setProgress(0);
            isLoadingFinished = false;
            progress = 0;
        }
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                progress += 20;
                progressIndicator.setProgressCompat(progress, true);

                if (progress == 100) {
                    timer.cancel();
                    isLoadingFinished = true;
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void startLoginActivity() {
        new Handler().postDelayed(() -> {
            if (InternetUtil.hasInternetConnection(getApplicationContext())) {
                SharedPreferences sp1 = this.getSharedPreferences("Login", MODE_PRIVATE);
                String alreadyLogged = sp1.getString("Username", null);
                Intent intent;
                if (alreadyLogged != null)
                    intent = new Intent(getApplicationContext(), EmailsActivity.class);
                else
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "There is no internet connection!", Toast.LENGTH_LONG).show();
            }
        }, Constants.SPLASH_SCREEN_TIME);
    }
}