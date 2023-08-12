package com.project.studiopatyzinha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.project.studiopatyzinha.Adapter.ServicosagendadosAdapter;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class Agendamentos extends AppCompatActivity {
    Toolbar toolbar_agendamentos;
    FloatingActionButton fab_add_agendamento;
    RecyclerView rv_agendamentos;
    public static ArrayList<Appoitmentpattern> lista;
    public static ServicosagendadosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamentos);
        rv_agendamentos = findViewById(R.id.rv_agendamentos);
        toolbar_agendamentos = findViewById(R.id.toolbar_agendamentos);
        fab_add_agendamento = findViewById(R.id.fab_add_agendamento);
        this.setSupportActionBar(toolbar_agendamentos);
        this.getSupportActionBar().setTitle("");
        toolbar_agendamentos.setTitle("Meus Agendamentos");
        toolbar_agendamentos.setTitleTextColor(Color.WHITE);
        toolbar_agendamentos.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar_agendamentos.setNavigationOnClickListener(v -> finish());
        fab_add_agendamento.setOnClickListener(v -> startActivity(new Intent(Agendamentos.this, Add_agendamento.class)));
        lista = new ArrayList<>();
        adapter = new ServicosagendadosAdapter(lista);
        rv_agendamentos.setAdapter(adapter);
        rv_agendamentos.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        initMyListaAppointent();
    }


    private void initMyListaAppointent() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Agendamentos").child(Login.pessoa.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Map<String, Object> servicepattern = (Map<String, Object>) snapshot1.getValue();
                    Appoitmentpattern servico = new Appoitmentpattern();
                    servico.setNomeservico(servicepattern.get("serviconome") + "");
                    servico.setWeekyear(servicepattern.get("week") + "");
                    servico.setValorservico(servicepattern.get("valor") + "");
                    servico.setHora(servicepattern.get("hora") + "");
                    servico.setIdservico(servicepattern.get("idservico") + "");
                    servico.setValorservico(servicepattern.get("valorservico") + "");
                    servico.setTimeinmilis(servicepattern.get("timeinmilis") + "");
                    servico.setNomefunc(servicepattern.get("nomefunc") + "");
                    servico.setIdpessoa(servicepattern.get("idpessoa") + "");
                    servico.setFunc(servicepattern.get("func") + "");
                    servico.setNomepessoa(servicepattern.get("nomepessoa") + "");
                    servico.setConcluido(servicepattern.get("concluido") + "");
                    servico.setAlarm(servicepattern.get("alarm") + "");
                    servico.setId(servicepattern.get("id") + "");
                    servico.setDia(servicepattern.get("dia") + "");
                    servico.setNomeservico(servicepattern.get("nomeservico") + "");
                    servico.setIfevaluated(servicepattern.get("ifevaluated") + "");
                    if (servico.getIfevaluated().equals("false")&&servico.getConcluido().equals("true")) {
                        Intent a = new Intent(Agendamentos.this, rateUs.class);
                        a.putExtra("id", servico.getId());
                        startActivity(a);
                    }
                    lista.add(servico);
                    Collections.sort(lista,comparador);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public Comparator<Appoitmentpattern> comparador = new Comparator<Appoitmentpattern>() {
        @Override
        public int compare(Appoitmentpattern o1, Appoitmentpattern o2) {
            long id1 = Long.parseLong(o1.getTimeinmilis());
            long id2 = Long.parseLong(o2.getTimeinmilis());
            return Long.compare(id1, id2);
        }

    };
}