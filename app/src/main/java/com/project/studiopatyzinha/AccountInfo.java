package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.Adapter.MaindayadapterSimplyed;
import com.project.studiopatyzinha.pattern.Dayitempattern;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class AccountInfo extends AppCompatActivity {
    ImageView imgvoltar_Adicionar_Pessoa_Main, imageView_Ui;
    TextView nome_Ui, email_UI,  frasefilosofica_Ui, tipo_de_conta_Ui, horadetrabalhocomeco, horadetrabalhofim, horadealmococomeco, horadecomecofim, cargo_UI;
    LinearLayout adminOptions_UI;
    RecyclerView rv_days_UI;
    public static TextView changeday, changeaccount;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> rotina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        changeday = new TextView(getBaseContext());
        changeaccount = new TextView(getBaseContext());
        imgvoltar_Adicionar_Pessoa_Main = findViewById(R.id.imgvoltar_Adicionar_Pessoa_Main);
        cargo_UI = findViewById(R.id.cargo_UI);
        horadetrabalhocomeco = findViewById(R.id.horadetrabalhocomeco);
        horadetrabalhofim = findViewById(R.id.horadetrabalhofim);
        horadecomecofim = findViewById(R.id.horadecomecofim);
        horadealmococomeco = findViewById(R.id.horadealmococomeco);
        imageView_Ui = findViewById(R.id.imageView_Ui);
        nome_Ui = findViewById(R.id.nome_Ui);
        rv_days_UI = findViewById(R.id.rv_days_UI);
        adminOptions_UI = findViewById(R.id.adminOptions_UI);
        tipo_de_conta_Ui = findViewById(R.id.tipo_de_conta_Ui);
        frasefilosofica_Ui = findViewById(R.id.frasefilosofica_Ui);
        email_UI = findViewById(R.id.email_UI);
        imgvoltar_Adicionar_Pessoa_Main.setOnClickListener(v -> finish());

        db.collection("Rotina").get().addOnSuccessListener(snapshot -> {
            snapshot.forEach(snapshot1 -> {
                rotina = snapshot1.getData();
                init();
            });
        });
        FloatingActionButton buttonfab_Ui = findViewById(R.id.buttonfab_alterar_informacoes_Ui);
        buttonfab_Ui.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), Register.class);
            intent.putExtra("edit_Acount", "s");
            startActivity(intent);
        });

        changeday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeday(s + "");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        changeaccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                init();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void init() {
        Glide.with(getBaseContext()).load(Login.pessoa.getImgUri()).into(imageView_Ui);
        nome_Ui.setText(Login.pessoa.getNome());
        email_UI.setText(Login.pessoa.getEmail());
        if (!Login.pessoa.getCargo().equals("usuario")) {
            cargo_UI.setText(Login.pessoa.getCargo());
            tipo_de_conta_Ui.setText("Administradora");
            adminOptions_UI.setVisibility(View.VISIBLE);
            frasefilosofica_Ui.setText(Login.pessoa.getFrasefilosofica());
            rv_days_UI.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            ArrayList<Dayitempattern> listadays = new ArrayList<>();
            for (int i = 0; i < MainActivity.semana.size(); i++) {
                if (MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).equals(MainActivity.semana.get(i))) {
                    changeday.setText(MainActivity.semana.get(i));
                    listadays.add(new Dayitempattern(MainActivity.semana.get(i), "", "s", "", "", ""));
                } else {
                    listadays.add(new Dayitempattern(MainActivity.semana.get(i), "", "", "", "", ""));
                }
            }
            MaindayadapterSimplyed adapter = new MaindayadapterSimplyed(listadays,"true");
            rv_days_UI.setAdapter(adapter);
        }else{
            tipo_de_conta_Ui.setText("Padrão");
        }

    }

    private void changeday(String day) {
        day = day.toUpperCase();
        Map<String, Object> a = (Map<String, Object>) Login.pessoa.getHorario().get(day);
        Map<String, Object> b = (Map<String, Object>) rotina.get(day);
        if (Boolean.parseBoolean(a.get("iffolga") + "") || Boolean.parseBoolean(b.get("iffechado") + "")) {
            horadetrabalhocomeco.setText("x");
            horadetrabalhofim.setText("x");
            horadealmococomeco.setText("x");
            horadecomecofim.setText("x");
        } else {
            horadetrabalhocomeco.setText(a.get("entranotrabalhoas") + "");
            horadealmococomeco.setText(a.get("almococomeco") + "");
            horadecomecofim.setText(a.get("almocofim") + "");
            horadetrabalhofim.setText(a.get("saidotrabalhoas") + "");
        }

    }

}