package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import com.project.studiopatyzinha.Adapter.OpinionAdapter;
import com.project.studiopatyzinha.pattern.Opinionpattern;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class VerOpinioes extends AppCompatActivity {
    Toolbar toolbar_veropinioes;
    ArrayList<Opinionpattern> opinions;
    OpinionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.veropinioes);
        toolbar_veropinioes = findViewById(R.id.toolbar_veropinioes);
        this.setSupportActionBar(toolbar_veropinioes);
        this.getSupportActionBar().setTitle("");
        toolbar_veropinioes.setTitle("Opiniões");
        toolbar_veropinioes.setTitleTextColor(Color.WHITE);
        toolbar_veropinioes.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbar_veropinioes.setNavigationOnClickListener(v -> finish());
        RecyclerView rv_opinioes = findViewById(R.id.rv_opinioes);
        rv_opinioes.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        opinions = new ArrayList<>();
        adapter = new OpinionAdapter(opinions);
        rv_opinioes.setAdapter(adapter);
        String id = getIntent().getStringExtra("id");
        if (id == null) {
            id = Login.pessoa.getId();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Contas").whereEqualTo("id", id).get().addOnCompleteListener(task -> {
            QuerySnapshot a = task.getResult();
            for (QueryDocumentSnapshot snapshot : a) {
                Map<String, Object> mapopi = (Map<String, Object>) snapshot.get("opinioes");
                mapopi.forEach((s, o) -> {
                    Map<String, Object> mapopini = (Map<String, Object>) o;
                    db.collection("Contas").whereEqualTo("id", mapopini.get("idpessoa") + "").get().addOnCompleteListener(task2 -> {
                        QuerySnapshot sd = task2.getResult();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : sd) {
                            opinions.add(new Opinionpattern(mapopini.get("date") + "", mapopini.get("opiniao") + "", mapopini.get("iduser") + "", mapopini.get("id") + "", queryDocumentSnapshot.get("nome") + "", mapopini.get("grade") + "", queryDocumentSnapshot.get("imgUri") + ""));
                            adapter.notifyDataSetChanged();
                        }
                    });
                });
            }
        });
    }
}