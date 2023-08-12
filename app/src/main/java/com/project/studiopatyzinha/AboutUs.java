package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

public class AboutUs extends AppCompatActivity {
    Toolbar toolbarAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        toolbarAboutUs = findViewById(R.id.toolbarAboutUs);
        this.setSupportActionBar(toolbarAboutUs);
        this.getSupportActionBar().setTitle("");
        toolbarAboutUs.setTitle("Sobre nós");
        toolbarAboutUs.setTitleTextColor(Color.WHITE);
        toolbarAboutUs.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbarAboutUs.setNavigationOnClickListener(v ->finish());

    }
}