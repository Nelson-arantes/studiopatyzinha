package com.project.studiopatyzinha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.studiopatyzinha.Adapter.Mainappointmentadapter;
import com.project.studiopatyzinha.Adapter.Maindayadapter;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.pattern.Dayitempattern;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class AgendamentosAdmin extends AppCompatActivity {
    RecyclerView agendamentosmain, daysmain;
    TextView valortotalsemana, valortotaldia, appointmentetoday, appointmenteweek;
    ImageView less, greater;
    int week;
    Toolbar toolbar_agendamentosadmin;
    public static TextView datainteiera;
    public static ArrayList<Appoitmentpattern> lista, lista2;//preencher a lista com as informaçoes dos agendamentos
    public static ArrayList<String> meses;
    public static ArrayList<String> semana;
    String id = "";
    public static TextView datacarview, dateweek, datacardweek;
    FloatingActionButton fab_add_agendamentoadm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamentos_admin);
        toolbar_agendamentosadmin = findViewById(R.id.toolbar_agendamentosadmin);
        fab_add_agendamentoadm = findViewById(R.id.fab_add_agendamentoadm);
        fab_add_agendamentoadm.setOnClickListener(v -> startActivity(new Intent(this, Add_agendamento.class)));
        this.setSupportActionBar(toolbar_agendamentosadmin);
        this.getSupportActionBar().setTitle("Meus agendamentos");
        toolbar_agendamentosadmin.setTitleTextColor(Color.WHITE);
        toolbar_agendamentosadmin.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar_agendamentosadmin.setNavigationOnClickListener(v -> finish());
        less = findViewById(R.id.lessthan);
        dateweek = findViewById(R.id.dateweek);
        datacardweek = findViewById(R.id.datacardweek);
        week = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        valortotalsemana = findViewById(R.id.valortotalsemana);
        appointmenteweek = findViewById(R.id.appointmenteweek);
        appointmentetoday = findViewById(R.id.appointmentetoday);
        valortotaldia = findViewById(R.id.valortotaldia);
        greater = findViewById(R.id.greaterthen);
        datainteiera = new TextView(getBaseContext());
        datacarview = findViewById(R.id.datacarview);
        daysmain = findViewById(R.id.daysmain);

        id = getIntent().getStringExtra("id");
        if (id == null) {
            id = Login.pessoa.getId();
        }
        String dia = "";
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
            dia += "0";
        }
        dia += "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        String mes = "";
        if ((Calendar.getInstance().get(Calendar.MONTH) + 1) < 10) {
            mes += "0";
        }
        mes += "" + (Calendar.getInstance().get(Calendar.MONTH) + 1);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Agendamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                Iterable<DataSnapshot> a = snapshot.getChildren();
                a.forEach(dataSnapshot -> {
                    Map<String, Object> idusermap = (Map<String, Object>) dataSnapshot.getValue();
                    idusermap.forEach((s, servico) -> {
                        Map<String, Object> servicomap = (Map<String, Object>) servico;
                        if (servicomap.get("func").equals(id)) {
                            Appoitmentpattern agendamento = new Appoitmentpattern();
                            agendamento.setValorservico(servicomap.get("valorservico") + "");
                            agendamento.setHora(servicomap.get("hora") + "");
                            agendamento.setWeekyear(servicomap.get("weekyear") + "");
                            agendamento.setTimeinmilis(servicomap.get("timeinmilis") + "");
                            agendamento.setNomefunc(servicomap.get("nomefunc") + "");
                            agendamento.setTempo(servicomap.get("tempo") + "");
                            agendamento.setIdpessoa(servicomap.get("idpessoa") + "");
                            agendamento.setIdservico(servicomap.get("idservico") + "");
                            agendamento.setPresenca(servicomap.get("presenca") + "");
                            agendamento.setFunc(servicomap.get("func") + "");
                            agendamento.setNomepessoa(servicomap.get("nomepessoa") + "");
                            agendamento.setConcluido(servicomap.get("concluido") + "");
                            agendamento.setAlarm(servicomap.get("alarm") + "");
                            agendamento.setId(servicomap.get("id") + "");
                            agendamento.setNomeservico(servicomap.get("nomeservico") + "");
                            agendamento.setDia(servicomap.get("dia") + "");
                            lista.add(agendamento);
                            atudays(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
                        }
                    });
                });
                datainteiera.setText(datainteiera.getText());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        greater.setOnClickListener(v -> {
            week++;
            atudays(week);
        });
        less.setOnClickListener(v2 -> {
            week--;
            atudays(week);
        });

        agendamentosmain = findViewById(R.id.agendamentosmain);
        lista = new ArrayList<>();
        lista2 = new ArrayList<>();
        Mainappointmentadapter adapter = new Mainappointmentadapter(lista2);
        atudays(week);
        datainteiera.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float total = 0.f;
                int quantsemana = 0;
                float totalsemana = 0.f;
                lista2.clear();
                valortotaldia.setText("0,00");
                valortotalsemana.setText("0,00");
                appointmentetoday.setText("" + lista2.size());
                appointmenteweek.setText("" + lista2.size());
                String[] data = String.valueOf(datainteiera.getText()).split("/");
                for (int j = 0; j < lista.size(); j++) {
                    if (lista.get(j).getWeekyear().equals((week) + "")) {
                        quantsemana++;
                        totalsemana += Double.parseDouble(lista.get(j).getValorservico().replaceAll("[R$  ]", "").replace(",", "."));
                        appointmenteweek.setText("" + quantsemana);
                        valortotalsemana.setText(stringtomoney("" + totalsemana).replace(".", ","));
                    }
                }
                for (int j = 0; j < lista.size(); j++) {
                    String[] dataappointment = lista.get(j).getDia().split("/");
                    if (data[0].equals(dataappointment[0])
                            && data[1].equals(dataappointment[1])
                            && data[2].equals(dataappointment[2])) {

                        total += Double.parseDouble(lista.get(j).getValorservico().replaceAll("[R$  ]", "").replace(",", "."));
                        valortotaldia.setText(stringtomoney("" + total).replace(".", ","));
                        lista2.add(lista.get(j));
                        appointmentetoday.setText("" + lista2.size());
                        Collections.sort(lista2, comparador);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        datainteiera.setText(dia + "/" + mes + "/" + Calendar.getInstance().get(Calendar.YEAR));
        agendamentosmain.setAdapter(adapter);
        agendamentosmain.setLayoutManager(new GridLayoutManager(AgendamentosAdmin.this, 1));
    }

    private void atudays(int week) {
        ArrayList<Dayitempattern> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        semana = new ArrayList<>();
        semana.add("DOM");
        semana.add("SEG");
        semana.add("TER");
        semana.add("QUA");
        semana.add("QUI");
        semana.add("SEX");
        semana.add("SAB");
        meses = new ArrayList<>();
        meses.add("jan");
        meses.add("fev");
        meses.add("mar");
        meses.add("abr");
        meses.add("mai");
        meses.add("jun");
        meses.add("jul");
        meses.add("ago");
        meses.add("set");
        meses.add("out");
        meses.add("nov");
        meses.add("dez");
        ArrayList<Integer> semanacalendar = new ArrayList<>();
        semanacalendar.add(1);
        semanacalendar.add(2);
        semanacalendar.add(3);
        semanacalendar.add(4);
        semanacalendar.add(5);
        semanacalendar.add(6);
        semanacalendar.add(7);
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//        int month = Calendar.getInstance().get(Calendar.MONTH);
//        int year = Calendar.getInstance().get(Calendar.YEAR);
        String dateweektext = "";
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, semanacalendar.get(i));
            if (i == 0) {//primeira data do texto
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                if (dia < 10) {
                    dateweektext += "0";
                }
                dateweektext += dia;
                int mes = calendar.get(Calendar.MONTH) + 1;
                dateweektext += " " + meses.get(mes - 1) + " " + calendar.get(Calendar.YEAR);
            }
            if (i == 6) {//segunda data do text
                dateweektext += " à ";
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                if (dia < 10) {
                    dateweektext += "0";
                }
                dateweektext += dia;
                int mes = (calendar.get(Calendar.MONTH) + 1);
                dateweektext += " " + meses.get(mes - 1) + " " + calendar.get(Calendar.YEAR);
            }
            dateweek.setText(dateweektext);

            String selected = "n";

            String messtring = "";
            if ((calendar.get(Calendar.MONTH) + 1) < 10) {
                messtring += "0";
            }
            messtring += (calendar.get(Calendar.MONTH) + 1);
            String diastring = "";
            if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
                diastring += "0";
            }
            diastring += "" + calendar.get(Calendar.DAY_OF_MONTH);

            int quantagendamento = 0;
            for (int j = 0; j < lista.size(); j++) {
                String[] data = lista.get(j).getDia().split("/");
                if (Integer.parseInt(data[0]) == Integer.parseInt(diastring) &&
                        Integer.parseInt(data[1]) == Integer.parseInt(messtring) &&
                        Integer.parseInt(data[2]) == Calendar.getInstance().get(Calendar.YEAR)) {
                    quantagendamento++;
                }
            }

            list.add(new Dayitempattern(semana.get(i), diastring + "", selected, messtring + "", calendar.get(Calendar.YEAR) + "", "" + quantagendamento));
        }
        Maindayadapter adapterdays = new Maindayadapter(list);
        daysmain.setAdapter(adapterdays);
        daysmain.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private String stringtomoney(String c) {
        String a = c.replace("R$ ", "").replace(",", ".");
        if (!a.equals("R$ 0,00") && !a.isEmpty()) {
            int b = 0;
            StringBuilder result = new StringBuilder("");
            for (int i = 0; i < a.length(); i++) {
                if (b == 0) {
                    result.append(a.charAt(i));
                }
                if (a.charAt(i) == '.') {
                    b++;
                }
                if (b == 1) {
                    result.append(a.charAt(i + b));
                    b++;

                }
                if (b == 2) {
                    result.append(a.charAt(i + 1));
                    break;
                }
            }
            return result.toString();
        } else {
            return "0.00";
        }
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
