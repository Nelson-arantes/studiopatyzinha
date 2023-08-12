package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PoliticasPrivacidade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politicas_privacidade);
        findViewById(R.id.imgvoltar_Chat_Main).setOnClickListener(v -> {
            finish();
        });
    }
}