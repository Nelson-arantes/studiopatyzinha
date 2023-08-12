package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);///configura sempre o modo noturno com desabilitado
        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash.this,Login.class));
            finish();

        },1500);
    }
}