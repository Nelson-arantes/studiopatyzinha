package com.project.studiopatyzinha.gerenciar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.Adapter.MaindayadapterSimplyed;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Dayitempattern;
import com.project.studiopatyzinha.pattern.SwitchPlus;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Rotina extends AppCompatActivity {
    public static TextView diaselecionado;
    public static Map<String, Object> diasmap;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotina);
        diaselecionado = new TextView(getBaseContext());
        diasmap = new HashMap<>();
        RecyclerView rv_rotina = findViewById(R.id.rv_rotina);
        ImageView savealldays = findViewById(R.id.savealldays);
        Toolbar toolbar_rotina = findViewById(R.id.toolbar_rotina);

        Spinner horacomecospinner = findViewById(R.id.horacomecospinner);
        Spinner horafimspinner = findViewById(R.id.horafimspinner);
        Button salvar_dia = findViewById(R.id.salvar_dia);
        SwitchPlus iffolga = findViewById(R.id.iffolga);
        this.setSupportActionBar(toolbar_rotina);
        this.getSupportActionBar().setTitle("");
        toolbar_rotina.setTitle("Rotina");
        toolbar_rotina.setNavigationOnClickListener(v -> finish());
        ArrayList<String> listahora = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String hora = "";
            if (i < 10) {
                hora += "0";
            }
            hora += i + ":00";
            listahora.add(hora);
        }
        ArrayAdapter<String> adapterhoracomeco = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, listahora);
        horacomecospinner.setAdapter(adapterhoracomeco);
        horafimspinner.setAdapter(adapterhoracomeco);
        db.collection("Rotina").get().addOnSuccessListener(snapshots -> {
            snapshots.forEach(snapshot -> {
                Map<String, Object> ss = snapshot.getData();
                diasmap.putAll(ss);
                diaselecionado.setText(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase() + ";salvando");
            });
        });
        iffolga.setText("Estará fechado");
        ArrayList<Dayitempattern> dias = new ArrayList<>();
        savealldays.setOnClickListener(v -> {
            if (diasmap.size() < 7) {
                String text_error = "Preencha os dias";
                if (diasmap.get("SAB") == null) {
                    text_error += " SAB";
                }
                if (diasmap.get("DOM") == null) {
                    text_error += " DOM";
                }
                if (diasmap.get("SEG") == null) {
                    text_error += " SEG";
                }
                if (diasmap.get("TER") == null) {
                    text_error += " TER";
                }
                if (diasmap.get("QUA") == null) {
                    text_error += " QUA";
                }
                if (diasmap.get("QUI") == null) {
                    text_error += " QUI";
                }
                if (diasmap.get("SEX") == null) {
                    text_error += " SEX";
                }

                Toast.makeText(this, text_error, Toast.LENGTH_LONG).show();
            } else {
                ProgressDialog progressDialog = new ProgressDialog(Rotina.this);
                progressDialog.setTitle("Salvando rotina");
                progressDialog.show();
                db.collection("Rotina").document("rotina da empresa").set(diasmap).addOnSuccessListener(command -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Rotina salva", Toast.LENGTH_SHORT).show();
                });
            }
        });
        for (int i = 0; i < MainActivity.semana.size(); i++) {
            if (!MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).equals(MainActivity.semana.get(i))) {
                dias.add(new Dayitempattern(MainActivity.semana.get(i).toUpperCase(), "", "", "", "", ""));
            } else {
                dias.add(new Dayitempattern(MainActivity.semana.get(i).toUpperCase(), "", "s", "", "", ""));
            }
        }
        MaindayadapterSimplyed adapter = new MaindayadapterSimplyed(dias,"true");
        rv_rotina.setAdapter(adapter);
        rv_rotina.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        salvar_dia.setOnClickListener(v -> {
            Toast.makeText(this, "Salvando " + diaselecionado.getText(), Toast.LENGTH_SHORT).show();
            Map<String, Object> dia = new HashMap<>();

            dia.put("dayweek", diaselecionado.getText().toString());
            if (iffolga.isChecked()) {
                dia.put("abreas", "00:00");
                dia.put("fechaas", "00:00");
            } else {
                dia.put("abreas", listahora.get(horacomecospinner.getSelectedItemPosition()));
                dia.put("fechaas", listahora.get(horafimspinner.getSelectedItemPosition()));
            }
            dia.put("iffechado", iffolga.isChecked() + "");
            diasmap.put(diaselecionado.getText().toString(), dia);
        });
        diaselecionado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (diasmap.get(s + "") == null) {
                    horacomecospinner.setSelection(0);
                    horafimspinner.setSelection(0);
                    iffolga.setChecked(false);
                    salvar_dia.setText("Salvar");
                } else {
                    Map<String, Object> dia = (Map<String, Object>) diasmap.get(s + "");
                    String abreas = String.valueOf(dia.get("abreas")).replace(":00", "");
                    String fechas = String.valueOf(dia.get("fechaas")).replace(":00", "");
                    horacomecospinner.setSelection(Integer.parseInt(abreas));
                    horafimspinner.setSelection(Integer.parseInt(fechas));
                    iffolga.setChecked((Boolean.parseBoolean(dia.get("iffechado") + "")));
                    salvar_dia.setText("Atualizar");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}