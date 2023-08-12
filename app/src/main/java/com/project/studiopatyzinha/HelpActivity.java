package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import com.project.studiopatyzinha.Adapter.helpAdapter;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {
    Toolbar toolbar_help;
    RecyclerView rv_subordinados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinados);
        toolbar_help = findViewById(R.id.toolbar_subordinados);
        rv_subordinados = findViewById(R.id.rv_subordinados);
        this.setSupportActionBar(toolbar_help);
        this.getSupportActionBar().setTitle("");
        toolbar_help.setTitle(R.string.help);
        toolbar_help.setTitleTextColor(Color.WHITE);
        toolbar_help.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbar_help.setNavigationContentDescription("Voltar para Tendências");
        toolbar_help.setNavigationOnClickListener(v -> {
            finish();
        });
        ArrayList<String> titles= new ArrayList<>();

        titles.add("");
        helpAdapter adapter = new helpAdapter(titles);
        rv_subordinados.setAdapter(adapter);
        rv_subordinados.setLayoutManager(new LinearLayoutManager(this));


    }
}