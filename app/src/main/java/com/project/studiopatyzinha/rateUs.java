package com.project.studiopatyzinha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.pattern.Opinionpattern;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class rateUs extends AppCompatActivity {
    ImageView imgvoltar_hateUs_Main;
    ImageView star1, star2, star3, star4, star5;//pode melhorar muito / poderia ser melhor/ bom/muito bom/excelente
    TextView notafinal, askHateUs, textStarsInfo;
    TextInputLayout textInputLayout_rateUs;
    Button sendbnt;
    String stars;
    Appoitmentpattern appoitment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        String agendamentoid = getIntent().getStringExtra("id");
        imgvoltar_hateUs_Main = findViewById(R.id.imgvoltar_hateUs_Main);
        star1 = findViewById(R.id.img_star_01);
        star2 = findViewById(R.id.img_star_02);
        star3 = findViewById(R.id.img_star_03);
        star4 = findViewById(R.id.img_star_04);
        star5 = findViewById(R.id.img_star_05);
        textStarsInfo = findViewById(R.id.textStarsInfo);
        notafinal = findViewById(R.id.notaFinal);
        textInputLayout_rateUs = findViewById(R.id.textInputLayout_rateUs);
        askHateUs = findViewById(R.id.askHateUs);
        sendbnt = findViewById(R.id.send_opinion_rateUs);


        if (agendamentoid != null) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Agendamentos").child(Login.pessoa.getId()).child(agendamentoid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Map<String, Object> servicepattern = (Map<String, Object>) snapshot.getValue();
                    appoitment = new Appoitmentpattern();
                    appoitment.setNomeservico(servicepattern.get("serviconome") + "");
                    appoitment.setWeekyear(servicepattern.get("week") + "");
                    appoitment.setValorservico(servicepattern.get("valor") + "");
                    appoitment.setHora(servicepattern.get("hora") + "");
                    appoitment.setIdservico(servicepattern.get("idservico") + "");
                    appoitment.setValorservico(servicepattern.get("valorservico") + "");
                    appoitment.setTimeinmilis(servicepattern.get("timeinmilis") + "");
                    appoitment.setNomefunc(servicepattern.get("nomefunc") + "");
                    appoitment.setIdpessoa(servicepattern.get("idpessoa") + "");
                    appoitment.setFunc(servicepattern.get("func") + "");
                    appoitment.setNomepessoa(servicepattern.get("nomepessoa") + "");
                    appoitment.setConcluido(servicepattern.get("concluido") + "");
                    appoitment.setAlarm(servicepattern.get("alarm") + "");
                    appoitment.setId(servicepattern.get("id") + "");
                    appoitment.setDia(servicepattern.get("dia") + "");
                    appoitment.setNomeservico(servicepattern.get("nomeservico") + "");
                    appoitment.setIfevaluated(servicepattern.get("ifevaluated") + "");
                    askHateUs.setText("O seu agendamento " + appoitment.getNomeservico() + " com o(a) " + appoitment.getNomefunc() + ", foi concluído.\n Como foi a sua experiência ?");
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        star1.setOnClickListener(v -> {
            stars = "1";
            textStarsInfo.setText("Pode melhorar muito");
            notafinal.setText(stars + ".0");
            star1.setImageResource(R.drawable.ic_starsmileon);
            star2.setImageResource(R.drawable.ic_starsmile);
            star3.setImageResource(R.drawable.ic_starsmile);
            star4.setImageResource(R.drawable.ic_starsmile);
            star5.setImageResource(R.drawable.ic_starsmile);
        });
        star2.setOnClickListener(v -> {
            stars = "2";
            textStarsInfo.setText("Poderia ser melhor");
            notafinal.setText(stars + ".0");
            star1.setImageResource(R.drawable.ic_starsmileon);
            star2.setImageResource(R.drawable.ic_starsmileon);
            star3.setImageResource(R.drawable.ic_starsmile);
            star4.setImageResource(R.drawable.ic_starsmile);
            star5.setImageResource(R.drawable.ic_starsmile);
        });
        star3.setOnClickListener(v -> {
            stars = "3";
            textStarsInfo.setText("Boa");
            notafinal.setText(stars + ".0");
            star1.setImageResource(R.drawable.ic_starsmileon);
            star2.setImageResource(R.drawable.ic_starsmileon);
            star3.setImageResource(R.drawable.ic_starsmileon);
            star4.setImageResource(R.drawable.ic_starsmile);
            star5.setImageResource(R.drawable.ic_starsmile);
        });
        star4.setOnClickListener(v -> {
            stars = "4";
            textStarsInfo.setText("Muito boa");
            notafinal.setText(stars + ".0");
            star1.setImageResource(R.drawable.ic_starsmileon);
            star2.setImageResource(R.drawable.ic_starsmileon);
            star3.setImageResource(R.drawable.ic_starsmileon);
            star4.setImageResource(R.drawable.ic_starsmileon);
            star5.setImageResource(R.drawable.ic_starsmile);
        });
        star5.setOnClickListener(v -> {
            stars = "5";
            textStarsInfo.setText("Excelente");
            notafinal.setText(stars + ".0");
            star1.setImageResource(R.drawable.ic_starsmileon);
            star2.setImageResource(R.drawable.ic_starsmileon);
            star3.setImageResource(R.drawable.ic_starsmileon);
            star4.setImageResource(R.drawable.ic_starsmileon);
            star5.setImageResource(R.drawable.ic_starsmileon);
        });

        sendbnt.setOnClickListener(v -> {
            String opinion = textInputLayout_rateUs.getEditText().getText().toString();
            if (!opinion.isEmpty() || agendamentoid != null) {
                textInputLayout_rateUs.setError(null);
                String pessoa = Login.pessoa.getId() + "";
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                String id = String.valueOf(System.currentTimeMillis());
                Opinionpattern opinionpattern = new Opinionpattern();

                opinionpattern.setOpinion(textInputLayout_rateUs.getEditText().getText().toString());
                opinionpattern.setDate(System.currentTimeMillis() + "");
                opinionpattern.setIduser(id);
                Map<String, Object> map = new HashMap<>();
                map.put("opiniao", textInputLayout_rateUs.getEditText().getText().toString());
                map.put("date", appoitment.getId());
                map.put("id", id);
                map.put("grade", notafinal.getText().toString());
                map.put("idpessoa", Login.pessoa.getId());

                if (agendamentoid != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Contas").document(appoitment.getFunc()).get().addOnSuccessListener(snapshot -> {
                        Map<String, Object> mapfunc = new HashMap<>();
                        mapfunc.put("barbersposts", snapshot.get("barbersposts"));
                        mapfunc.put("cargo", snapshot.get("cargo") + "");
                        mapfunc.put("dia_salario", snapshot.get("dia_salario") + "");
                        mapfunc.put("email", snapshot.get("email") + "");
                        mapfunc.put("followersid", snapshot.get("followersid"));
                        mapfunc.put("followersquant", snapshot.get("followersquant") + "");
                        mapfunc.put("frasefilosofica", snapshot.get("frasefilosofica") + "");
                        mapfunc.put("grupoAbaixo", snapshot.get("grupoAbaixo") + "");
                        mapfunc.put("horario", snapshot.get("horario"));
                        mapfunc.put("id", snapshot.get("id") + "");
                        mapfunc.put("imgUri", snapshot.get("imgUri") + "");
                        mapfunc.put("nome", snapshot.get("nome") + "");
                        mapfunc.put("numero", snapshot.get("numero") + "");
                        mapfunc.put("percentual", snapshot.get("percentual") + "");
                        mapfunc.put("quantpost", snapshot.get("quantpost") + "");
                        mapfunc.put("selected", snapshot.get("selected") + "");
                        mapfunc.put("senha", snapshot.get("senha") + "");
                        Map<String, Object> mapopi = (Map<String, Object>) snapshot.get("opinioes");
                        mapopi.put(String.valueOf(System.currentTimeMillis()), map);
                        mapfunc.put("opinioes", mapopi);
                        db.collection("Contas").document(appoitment.getFunc()).set(mapfunc).addOnSuccessListener(command -> {
                            Map<String, Object> map2 = new HashMap<>();
                            map2.put("ifevaluated", "true");
                            reference.child("Agendamentos").child(Login.pessoa.getId()).child(appoitment.getId()).updateChildren(map2);
                            finish();
                        });

                    });

                } else {
                    reference.child("opinioes").child(pessoa).child(id).setValue(opinionpattern).addOnSuccessListener(command -> {
                        Toast.makeText(getBaseContext(), R.string.salvotxt, Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                textInputLayout_rateUs.getEditText().setText("");
            } else {
                textInputLayout_rateUs.setError(getString(R.string.empty_field));
            }
        });
        imgvoltar_hateUs_Main.setOnClickListener(v -> {
            finish();
        });
    }
}