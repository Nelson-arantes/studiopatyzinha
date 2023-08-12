package com.project.studiopatyzinha;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.studiopatyzinha.Adapter.NotiAdapter;
import com.project.studiopatyzinha.Broadcast.diaPagamentoBroadCast;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.pattern.categoriasLC;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class NotidayFragment extends Fragment {
    ArrayList<Accountpattern> funcs;
    public static Accountpattern pessoa;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<categoriasLC> list;
    NotiAdapter adapter;
    AlarmManager alarmManager;
    Context context;
    double valor = 0;

    public static NotidayFragment newInstance() {
        return new NotidayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notiday, container, false);
        list = new ArrayList<>();
        adapter = new NotiAdapter(list);
        RecyclerView rv_noti = view.findViewById(R.id.rv_noti);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        context = getContext();
        funcs = new ArrayList<>();


        rv_noti.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_noti.setAdapter(adapter);
        checkPaymentDay();
        return view;
    }

    private void checkPaymentDay() {
        if (Login.pessoa.getGrupoAbaixo().isEmpty() || Login.pessoa.getCargo().contains("Dono")) {
            db.collection("Contas").whereNotEqualTo("cargo", "usuario").get().addOnCompleteListener(task -> {
                QuerySnapshot filho = task.getResult();
                filho.forEach(dataSnapshot1 -> {
                    Accountpattern func = new Accountpattern();
                    func.setFormapagamento(dataSnapshot1.get("formapagamento") + "");
                    func.setId(dataSnapshot1.get("id") + "");
                    func.setNome(dataSnapshot1.get("nome") + "");
                    func.setFollowersquant(dataSnapshot1.get("followersquant") + "");
                    func.setFrasefilosofica(dataSnapshot1.get("frasefilosofica") + "");
                    func.setHorario((Map<String, Object>) dataSnapshot1.get("horario"));
                    func.setQuantpost(dataSnapshot1.get("quantpost") + "");
                    func.setFollowersid((Map<String, Object>) dataSnapshot1.get("followersid"));
                    func.setSenha(dataSnapshot1.get("senha") + "");
                    func.setOpinioes((Map<String, Object>) dataSnapshot1.get("opinioes"));
                    func.setBarbersposts((Map<String, Object>) dataSnapshot1.get("barbersposts"));
                    func.setImgUri(dataSnapshot1.get("imgUri") + "");
                    func.setPercentual(dataSnapshot1.get("percentual") + "");
                    func.setDia_salario(dataSnapshot1.get("dia_salario") + "");
                    func.setCargo(dataSnapshot1.get("cargo") + "");
                    func.setGrupoAbaixo(dataSnapshot1.get("grupoAbaixo") + "");
                    func.setEmail(dataSnapshot1.get("email") + "");
                    func.setSelected(dataSnapshot1.get("selected") + "");
                    func.setUltimopagamento(dataSnapshot1.get("ultimopagamento") + "");
                    func.setHorasdiaspagamentos((Map<String, Object>) dataSnapshot1.get("horasdiaspagamentos"));
                    switch (func.getFormapagamento()) {
                        case "editado":
                            initEditado(func);
                            break;
                        case "mensal":
                            if (Integer.parseInt(func.getDia_salario()) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                                initMensal(func);
                            }
                            break;
                        case "semanal":
                            if (func.getDia_salario().contains(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))) {
                                initSemanal(func);
                            }
                            break;
                        case "diario":
                            initDiario(func);
                            break;
                    }
                });
            });
        } else {
            if (Login.pessoa.getGrupoAbaixo().split(";").length > 0) {
                String[] ss = Login.pessoa.getGrupoAbaixo().split(";");
                for (int i = 0; i < Login.pessoa.getGrupoAbaixo().split(";").length; i++) {
                    db.collection("Contas").whereEqualTo("id", ss[i]).get().addOnCompleteListener(task -> {
                        QuerySnapshot filho = task.getResult();
                        filho.forEach(dataSnapshot1 -> {
                            Accountpattern func = new Accountpattern();
                            func.setFormapagamento(dataSnapshot1.get("formapagamento") + "");
                            func.setId(dataSnapshot1.get("id") + "");
                            func.setNome(dataSnapshot1.get("nome") + "");
                            func.setFollowersquant(dataSnapshot1.get("followersquant") + "");
                            func.setFrasefilosofica(dataSnapshot1.get("frasefilosofica") + "");
                            func.setHorario((Map<String, Object>) dataSnapshot1.get("horario"));
                            func.setQuantpost(dataSnapshot1.get("quantpost") + "");
                            func.setFollowersid((Map<String, Object>) dataSnapshot1.get("followersid"));
                            func.setSenha(dataSnapshot1.get("senha") + "");
                            func.setOpinioes((Map<String, Object>) dataSnapshot1.get("opinioes"));
                            func.setBarbersposts((Map<String, Object>) dataSnapshot1.get("barbersposts"));
                            func.setImgUri(dataSnapshot1.get("imgUri") + "");
                            func.setPercentual(dataSnapshot1.get("percentual") + "");
                            func.setDia_salario(dataSnapshot1.get("dia_salario") + "");
                            func.setCargo(dataSnapshot1.get("cargo") + "");
                            func.setGrupoAbaixo(dataSnapshot1.get("grupoAbaixo") + "");
                            func.setEmail(dataSnapshot1.get("email") + "");
                            func.setSelected(dataSnapshot1.get("selected") + "");
                            func.setHorasdiaspagamentos((Map<String, Object>) dataSnapshot1.get("horasdiaspagamentos"));
                            switch (func.getFormapagamento()) {
                                case "editado":
                                    initEditado(func);
                                    break;
                                case "mensal":
                                    if (Integer.parseInt(func.getDia_salario()) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                                        initMensal(func);
                                    }
                                    break;
                                case "semanal":
                                    if (func.getDia_salario().contains(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))) {
                                        initSemanal(func);
                                    }
                                    break;
                                case "diario":
                                    initDiario(func);
                                    break;
                            }
                        });
                    });
                }
            }
        }
    }

    private void initValor(Accountpattern func) {
        valor = 0;
        long valordata = Long.parseLong(func.getUltimopagamento() + "0");//long
        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Agendamentos");
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> a = snapshot.getChildren();
                a.forEach(dataSnapshot -> {
                    Map<String, Object> servicomap = (Map<String, Object>) dataSnapshot.getValue();
                    servicomap.forEach((s, o) -> {
                        Map<String, Object> map = (Map<String, Object>) o;
                        Appoitmentpattern agendamento = new Appoitmentpattern();
                        agendamento.setValorservico(map.get("valorservico") + "");
                        agendamento.setHora(map.get("hora") + "");
                        agendamento.setWeekyear(map.get("weekyear") + "");
                        agendamento.setTimeinmilis(map.get("timeinmilis") + "");
                        agendamento.setNomefunc(map.get("nomefunc") + "");
                        agendamento.setTempo(map.get("tempo") + "");
                        agendamento.setIdpessoa(map.get("idpessoa") + "");
                        agendamento.setIdservico(map.get("idservico") + "");
                        agendamento.setPresenca(map.get("presenca") + "");
                        agendamento.setFunc(map.get("func") + "");
                        agendamento.setNomepessoa(map.get("nomepessoa") + "");
                        agendamento.setConcluido(map.get("concluido") + "");
                        agendamento.setAlarm(map.get("alarm") + "");
                        agendamento.setId(map.get("id") + "");
                        agendamento.setId(map.get("id") + "");
                        agendamento.setNomeservico(map.get("nomeservico") + "");
                        agendamento.setDia(map.get("dia") + "");
                        if (agendamento.getFunc().equals(func.getId())) {
                            if (Long.parseLong(agendamento.getId()) < valordata) {
                                valor += Double.parseDouble(agendamento.getValorservico());
                            }
                        }
                    });
                });
                list.add(new categoriasLC("Pagamento", "O(a) " + func.getNome() + " irá receber " + String.format("%.02f", valor) + " hoje as " + func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase()), "", ColorTemplate.MATERIAL_COLORS[0]));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //editado
    private void initEditado(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        String data = func.getDia_salario().split("!")[1];
        int milis2 = Integer.parseInt(func.getDia_salario().split("!")[0]);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        calendar.set(Integer.parseInt(data.split("/")[2]), Integer.parseInt(data.split("/")[1]) - 1, Integer.parseInt(data.split("/")[0]));
        Intent myIntent = new Intent(context, diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        while (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            if (calendar.getTimeInMillis() + milis2 > System.currentTimeMillis()) {
//                break;
//            } else {
                calendar.add(Calendar.DAY_OF_YEAR, milis2);
//            }
        }
        int hora2 = 0;
        int minuto2 = 0;
        if(calendar.get(Calendar.DAY_OF_MONTH)==Calendar.getInstance().get(Calendar.DAY_OF_MONTH)&&calendar.get(Calendar.MONTH)==Calendar.getInstance().get(Calendar.MONTH)){
            initValor(func);
        }
        String tempo2 = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        hora2 = Integer.parseInt(tempo2.split(":")[0]);
        minuto2 = Integer.parseInt(tempo2.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora2);
        calendar.set(Calendar.MINUTE, minuto2);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    //diario
    private void initDiario(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        int hora = Integer.parseInt(tempo.split(":")[0]);
        int minuto = Integer.parseInt(tempo.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        initValor(func);
    }

    //mensal
    private void initMensal(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        int hora = Integer.parseInt(tempo.split(":")[0]);
        int minuto = Integer.parseInt(tempo.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        initValor(func);
    }

    //semanal
    private void initSemanal(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        int hora = Integer.parseInt(tempo.split(":")[0]);
        int minuto = Integer.parseInt(tempo.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        initValor(func);

    }


//    private void initdaypaymenteditar(Accountpattern func) {
//        valor = 0;
//        int Requestcode = 1001;
//        Intent myIntent = new Intent(context, diaPagamentoBroadCast.class);
//        myIntent.putExtra("nome", func.getNome());
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Requestcode, myIntent, PendingIntent.FLAG_IMMUTABLE);
//        long inicio = System.currentTimeMillis();
//        switch (func.getFormapagamento()) {
//            case "semanal":
//                Calendar calendar2 = Calendar.getInstance();
//                calendar2.set(Calendar.SECOND, 0);
//                calendar2.set(Calendar.MILLISECOND, 0);
//                for (int i = 0; i < func.getDia_salario().length(); i++) {
//                    int a = Integer.parseInt(func.getDia_salario().charAt(i) + "");
//                    if (calendar2.get(Calendar.DAY_OF_WEEK) > a) {
//                        calendar2.add(Calendar.WEEK_OF_YEAR, +1);
//                    }
//                    calendar2.set(Calendar.DAY_OF_WEEK, a);
//                    int hora = 0;
//                    int minuto = 0;
//                    String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(a).toUpperCase());
//                    hora = Integer.parseInt(tempo.split(":")[0]);
//                    minuto = Integer.parseInt(tempo.split(":")[1]);
//                    calendar2.set(Calendar.HOUR_OF_DAY, hora);
//                    calendar2.set(Calendar.MINUTE, minuto);
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
//                }
//
//                break;
//            case "mensal":
//                Calendar calendario = Calendar.getInstance();
//                calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(func.getDia_salario()));
//                calendario.set(Calendar.HOUR_OF_DAY, 0);
//                calendario.set(Calendar.MINUTE, 0);
//                calendario.set(Calendar.SECOND, 0);
//                calendario.set(Calendar.MILLISECOND, 0);
//                if (calendario.get(Calendar.DAY_OF_MONTH) > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
//                    calendario.add(Calendar.MONTH, +1);
//                }
//                int hora = 0;
//                int minuto = 0;
//                String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).toUpperCase());
//                hora = Integer.parseInt(tempo.split(":")[0]);
//                minuto = Integer.parseInt(tempo.split(":")[1]);
//                calendario.set(Calendar.HOUR_OF_DAY, hora);
//                calendario.set(Calendar.MINUTE, minuto);
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendario.getTimeInMillis(), pendingIntent);
//                break;
//            case "diario":
//                milis = (long) 1000;//time in milis in a day (24 hours)
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, inicio, pendingIntent);
//                break;
//            case "editado":
//                milis = (long) Integer.parseInt(func.getDia_salario().split("!")[0]) * 86400000;//5min 86400000
//                Calendar calendar = Calendar.getInstance();
//                String data = func.getDia_salario().split("!")[1];
//                calendar.set(Integer.parseInt(data.split("/")[2]), Integer.parseInt(data.split("/")[1]) - 1, Integer.parseInt(data.split("/")[0]));
//                calendar.set(Calendar.HOUR_OF_DAY, 0);
//                calendar.set(Calendar.MINUTE, 0);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//                int day, month = 0;
//                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//                month = Calendar.getInstance().get(Calendar.MONTH) + 1;
//                while (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//                    if (calendar.get(Calendar.MONTH) + 1 == month && day == calendar.get(Calendar.DAY_OF_MONTH)) {
//                        long valordata = Long.parseLong(func.getUltimopagamento() + "0");//long
//                        FirebaseDatabase.getInstance().getReference().child("Agendamentos").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Iterable<DataSnapshot> a = snapshot.getChildren();
//                                a.forEach(dataSnapshot -> {
//                                    Map<String, Object> servicomap = (Map<String, Object>) dataSnapshot.getValue();
//                                    servicomap.forEach((s, o) -> {
//                                        Map<String, Object> map = (Map<String, Object>) o;
//                                        Appoitmentpattern agendamento = new Appoitmentpattern();
//                                        agendamento.setValorservico(map.get("valorservico") + "");
//                                        agendamento.setHora(map.get("hora") + "");
//                                        agendamento.setWeekyear(map.get("weekyear") + "");
//                                        agendamento.setTimeinmilis(map.get("timeinmilis") + "");
//                                        agendamento.setNomefunc(map.get("nomefunc") + "");
//                                        agendamento.setTempo(map.get("tempo") + "");
//                                        agendamento.setIdpessoa(map.get("idpessoa") + "");
//                                        agendamento.setIdservico(map.get("idservico") + "");
//                                        agendamento.setPresenca(map.get("presenca") + "");
//                                        agendamento.setFunc(map.get("func") + "");
//                                        agendamento.setNomepessoa(map.get("nomepessoa") + "");
//                                        agendamento.setConcluido(map.get("concluido") + "");
//                                        agendamento.setAlarm(map.get("alarm") + "");
//                                        agendamento.setId(map.get("id") + "");
//                                        agendamento.setId(map.get("id") + "");
//                                        agendamento.setNomeservico(map.get("nomeservico") + "");
//                                        agendamento.setDia(map.get("dia") + "");
//                                        if (agendamento.getFunc().equals(Login.pessoa.getId())) {
//                                            if (Long.parseLong(agendamento.getId()) < valordata) {
//                                                valor += Double.parseDouble(agendamento.getValorservico());
//                                            }
//                                        }
//                                    });
//                                });
//                                list.add(new categoriasLC("Pagamento", "O(a) " + func.getNome() + " irá receber " + String.format("%.02f", valor) + " hoje as " + func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)).toUpperCase()), "", ColorTemplate.MATERIAL_COLORS[0]));
//                                adapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//
//
//                    }
//                    calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(func.getDia_salario().split("!")[0]));
//                }
//
//
//                break;
//        }
//
//    }


}