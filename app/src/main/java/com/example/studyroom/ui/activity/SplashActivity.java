package com.example.studyroom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studyroom.R;
import com.example.studyroom.util.ProjectConstants;

import androidx.appcompat.app.AppCompatDelegate;

public class SplashActivity extends AppCompatActivity {
    //private final int SPLASH_DISPLAY_DURATION = 3000;
    ProjectConstants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView title = findViewById(R.id.appTitle);
        Animation fadeInAnim = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fadein);
        title.setAnimation(fadeInAnim);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        },3000);

    }
}
