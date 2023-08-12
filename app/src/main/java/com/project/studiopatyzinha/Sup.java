package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URLEncoder;

public class Sup extends AppCompatActivity {
    Toolbar toolbarsup;
    TextView supManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup);
        toolbarsup = findViewById(R.id.toolbarsup);
        supManual = findViewById(R.id.supManual);
        this.setSupportActionBar(toolbarsup);
        this.getSupportActionBar().setTitle("");
        toolbarsup.setTitleTextColor(Color.WHITE);
        toolbarsup.setTitle("Suporte técnico");
        toolbarsup.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbarsup.setNavigationOnClickListener(v -> {
            finish();
        });
        findViewById(R.id.chamarsup).setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Sup").document("numero").get().addOnCompleteListener(task -> {
                openWhatsApp(task.getResult().get("numero") + "", "Olá, estou com uma dúvida");
            });
        });
    }

    private void openWhatsApp(String numero, String message) {

        try {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            } else {
                try {
                    Intent i2 = new Intent(Intent.ACTION_VIEW);
                    String url2 = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(message, "UTF-8");
                    i2.setPackage("com.gbwhatsapp");
                    i2.setData(Uri.parse(url2));
                    startActivity(i2);
                } catch (NullPointerException e) {
                    Intent i2 = new Intent(Intent.ACTION_VIEW);
                    String url2 = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(message, "UTF-8");
                    i2.setPackage("com.yowhatsapp");
                    i2.setData(Uri.parse(url2));
                    startActivity(i2);

                }
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Whatsapp não encontrado", Toast.LENGTH_SHORT).show();
            supManual.setText("Chame no Whatsapp no número " + numero);
            supManual.setVisibility(View.VISIBLE);
        }
    }
}