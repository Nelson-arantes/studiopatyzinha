package com.project.studiopatyzinha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.Adapter.Adapteraddress;
import com.project.studiopatyzinha.Adapter.HoraAdapterChoosing;
import com.project.studiopatyzinha.Adapter.ServicosAdapterAgendar;
import com.project.studiopatyzinha.Broadcast.diaPagamentoBroadCast;
import com.project.studiopatyzinha.Broadcast.estouchegandobroadcast;
import com.project.studiopatyzinha.pattern.Addrespattern;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.pattern.Horapattern;
import com.project.studiopatyzinha.pattern.Servicepattern;
import com.project.studiopatyzinha.services.estouChegandoListenerService;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Add_agendamento extends AppCompatActivity {
    ImageView close_add_agendamento, close_choosing;
    FloatingActionButton nextstep_add_agendamento;
    RecyclerView rv_add_agendameno;
    TextView title;
    ServicosAdapterAgendar adapterservicos;
    public static int servicopos;
    public static int addresspos;
    public static String horaescolhida;
    public static int barberpos;
    ArrayList<Servicepattern> listafuncs;
    ArrayList<Addrespattern> listaende;
    AlertDialog choosingHora;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Horapattern> horalist, manhalist, tardelist, noitelist;
    Map<String, Object> folhadeponto;
    Date lastdate;
    DatePicker pickerdate;
    HoraAdapterChoosing adapterChoosing;
    ArrayList<Appoitmentpattern> agendamentosProntos;
    boolean adicionar;
    TextInputLayout notificarAs;
    Button agendarbnt;
    ArrayList<Servicepattern> listaservicos;
    String datafinal;
    Adapteraddress adapterendereco;
    boolean ocupado;
    TextView sss;
    Map<String, Object> mapdesc;
    boolean desc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agendamento);
        close_add_agendamento = findViewById(R.id.close_add_agendamento);
        horaescolhida = "";
        servicopos = -1;
        addresspos = -1;
        barberpos = -1;
        lastdate = Calendar.getInstance().getTime();
        close_add_agendamento.setOnClickListener(v -> finish());
        title = findViewById(R.id.title_add_agendamento);
        nextstep_add_agendamento = findViewById(R.id.nextstep_add_agendamento);
        rv_add_agendameno = findViewById(R.id.rv_add_agendameno);
        nextstep_add_agendamento.setOnClickListener(v -> {
            if (addresspos == -1) {
                Toast.makeText(this, "Escolha uma unidade", Toast.LENGTH_SHORT).show();
            } else {
                if (title.getText().toString().contains("Escolha uma unidade")) {
                    rv_add_agendameno.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    rv_add_agendameno.setAdapter(adapterservicos);
                }
                if (servicopos == -1 && title.getText().toString().contains("Qual será o estilo de hoje ?")) {
                    Toast.makeText(this, "Escolha um estilo", Toast.LENGTH_SHORT).show();
                } else {
                    if (title.getText().toString().contains("Escolha um(a) profissional")) {

                        if (barberpos == -1) {
                            Toast.makeText(this, "Escolha um(a) profissional", Toast.LENGTH_SHORT).show();
                        } else {
                            agendamentosProntos = new ArrayList<>();
                            horalist = new ArrayList<>();
                            manhalist = new ArrayList<>();
                            tardelist = new ArrayList<>();
                            noitelist = new ArrayList<>();
                            String idbarber = listafuncs.get(barberpos).getId();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Add_agendamento.this);
                            initListaAppointent();
                            sss = new TextView(getBaseContext());
                            sss.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    View view = LayoutInflater.from(Add_agendamento.this).inflate(R.layout.choosing_time_dialog, null, false);
                                    pickerdate = view.findViewById(R.id.timerpickerdialog);
                                    notificarAs = view.findViewById(R.id.notificarAs);
                                    SimpleMaskFormatter numb = new SimpleMaskFormatter("NN:NN");
                                    MaskTextWatcher mtwrg = new MaskTextWatcher(notificarAs.getEditText(), numb);
                                    notificarAs.getEditText().addTextChangedListener(mtwrg);
                                    agendarbnt = view.findViewById(R.id.agendarbnt);
                                    close_choosing = view.findViewById(R.id.close_dialog_choosingdate);
                                    close_choosing.setOnClickListener(v1 -> {
                                        choosingHora.dismiss();
                                    });
                                    view.findViewById(R.id.manhabnt).setOnClickListener(v1 -> {
                                        for (int i = 0; i < tardelist.size(); i++) {
                                            tardelist.get(i).setSelected("");
                                        }
                                        for (int i = 0; i < manhalist.size(); i++) {
                                            manhalist.get(i).setSelected("");
                                        }
                                        for (int i = 0; i < noitelist.size(); i++) {
                                            noitelist.get(i).setSelected("");
                                        }
                                        horalist.clear();
                                        horalist.addAll(manhalist);
                                        adapterChoosing.notifyDataSetChanged();
                                    });
                                    view.findViewById(R.id.tardebnt).setOnClickListener(v1 -> {
                                        horalist.clear();
                                        for (int i = 0; i < tardelist.size(); i++) {
                                            tardelist.get(i).setSelected("");
                                        }
                                        for (int i = 0; i < manhalist.size(); i++) {
                                            manhalist.get(i).setSelected("");
                                        }
                                        for (int i = 0; i < noitelist.size(); i++) {
                                            noitelist.get(i).setSelected("");
                                        }
                                        horalist.addAll(tardelist);
                                        adapterChoosing.notifyDataSetChanged();
                                    });
                                    view.findViewById(R.id.noitebnt).setOnClickListener(v1 -> {
                                        horalist.clear();
                                        for (int i = 0; i < tardelist.size(); i++) {
                                            tardelist.get(i).setSelected("");
                                        }
                                        for (int i = 0; i < manhalist.size(); i++) {
                                            manhalist.get(i).setSelected("");
                                        }
                                        for (int i = 0; i < noitelist.size(); i++) {
                                            noitelist.get(i).setSelected("");
                                        }
                                        horalist.addAll(noitelist);
                                        adapterChoosing.notifyDataSetChanged();
                                    });
                                    RecyclerView rv_choosing_hora = view.findViewById(R.id.rv_choosing_hora);
                                    TextInputLayout nomePessoa = view.findViewById(R.id.nomePessoa);
                                    TextInputLayout descTag = view.findViewById(R.id.descTag);
                                    if (Login.pessoa.getCargo().equals("usuario")) {
                                        nomePessoa.getEditText().setText(Login.pessoa.getNome());
                                    }
                                    rv_choosing_hora.setLayoutManager(new GridLayoutManager(Add_agendamento.this, 3));
                                    adapterChoosing = new HoraAdapterChoosing(horalist);
                                    rv_choosing_hora.setAdapter(adapterChoosing);
                                    pickerdate.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.set(year, monthOfYear, dayOfMonth, 0, 0);
                                        int dia = calendar.get(Calendar.DAY_OF_WEEK);
                                        switch (dia) {
                                            case Calendar.SATURDAY: //sab
                                                if (testDate((Map<String, Object>) folhadeponto.get("SAB"), Calendar.SATURDAY)) {
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(lastdate);
                                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                                } else {
                                                    Map<String, Object> diamap = (Map<String, Object>) folhadeponto.get("SAB");
                                                    lastdate = calendar.getTime();
                                                    changelisthora(diamap.get("entranotrabalhoas") + "", diamap.get("saidotrabalhoas") + "", diamap.get("almococomeco") + "", diamap.get("almocofim") + "");
                                                }
                                                break;
                                            case Calendar.SUNDAY: //dom
                                                if (testDate((Map<String, Object>) folhadeponto.get("DOM"), Calendar.SUNDAY)) {
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(lastdate);
                                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                                } else {
                                                    Map<String, Object> diamap = (Map<String, Object>) folhadeponto.get("DOM");
                                                    lastdate = calendar.getTime();
                                                    changelisthora(diamap.get("entranotrabalhoas") + "", diamap.get("saidotrabalhoas") + "", diamap.get("almococomeco") + "", diamap.get("almocofim") + "");

                                                }
                                                break;
                                            case Calendar.MONDAY: //seg
                                                if (testDate((Map<String, Object>) folhadeponto.get("SEG"), Calendar.MONDAY)) {
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(lastdate);
                                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                                } else {
                                                    Map<String, Object> diamap = (Map<String, Object>) folhadeponto.get("SEG");
                                                    lastdate = calendar.getTime();
                                                    changelisthora(diamap.get("entranotrabalhoas") + "", diamap.get("saidotrabalhoas") + "", diamap.get("almococomeco") + "", diamap.get("almocofim") + "");
                                                }
                                                break;
                                            case Calendar.TUESDAY: //ter
                                                if (testDate((Map<String, Object>) folhadeponto.get("TER"), Calendar.TUESDAY)) {
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(lastdate);
                                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                                } else {
                                                    Map<String, Object> diamap = (Map<String, Object>) folhadeponto.get("TER");
                                                    lastdate = calendar.getTime();
                                                    changelisthora(diamap.get("entranotrabalhoas") + "", diamap.get("saidotrabalhoas") + "", diamap.get("almococomeco") + "", diamap.get("almocofim") + "");
                                                }
                                                break;
                                            case Calendar.WEDNESDAY: //qua
                                                if (testDate((Map<String, Object>) folhadeponto.get("QUA"), Calendar.WEDNESDAY)) {
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(lastdate);
                                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                                } else {
                                                    Map<String, Object> diamap = (Map<String, Object>) folhadeponto.get("QUA");
                                                    lastdate = calendar.getTime();
                                                    changelisthora(diamap.get("entranotrabalhoas") + "", diamap.get("saidotrabalhoas") + "", diamap.get("almococomeco") + "", diamap.get("almocofim") + "");
                                                }
                                                break;
                                            case Calendar.THURSDAY: //qui
                                                if (testDate((Map<String, Object>) folhadeponto.get("QUI"), Calendar.THURSDAY)) {
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(lastdate);
                                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                                } else {
                                                    Map<String, Object> diamap = (Map<String, Object>) folhadeponto.get("QUI");
                                                    lastdate = calendar.getTime();
                                                    changelisthora(diamap.get("entranotrabalhoas") + "", diamap.get("saidotrabalhoas") + "", diamap.get("almococomeco") + "", diamap.get("almocofim") + "");
                                                }

                                                break;
                                            case Calendar.FRIDAY: //sex
                                                if (testDate((Map<String, Object>) folhadeponto.get("SEX"), Calendar.FRIDAY)) {
                                                    Calendar calendar1 = Calendar.getInstance();
                                                    calendar1.setTime(lastdate);
                                                    pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                                                } else {
                                                    Map<String, Object> diamap = (Map<String, Object>) folhadeponto.get("SEX");
                                                    lastdate = calendar.getTime();
                                                    changelisthora(diamap.get("entranotrabalhoas") + "", diamap.get("saidotrabalhoas") + "", diamap.get("almococomeco") + "", diamap.get("almocofim") + "");
                                                }
                                                break;
                                        }
                                    });
                                    db.collection("Contas").whereEqualTo("id", idbarber).get().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot a = task.getResult();
                                            if (!a.isEmpty()) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : a) {
                                                    Map<String, Object> map = (Map<String, Object>) queryDocumentSnapshot.getData();
                                                    folhadeponto = new HashMap<>((Map<String, Object>) map.get("horario"));
                                                    pickerdate.updateDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                                                    pickerdate.setMinDate(System.currentTimeMillis());

                                                }
                                            }
                                        }
                                    });
                                    agendarbnt.setOnClickListener(v1 -> {
                                        boolean continuar = true;
                                        if (horaescolhida.isEmpty()) {
                                            Toast.makeText(getBaseContext(), "Escolha algum horario disponível", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Calendar calendaragendado = Calendar.getInstance();
                                            calendaragendado.setTime(lastdate);
                                            calendaragendado.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horaescolhida.split(":")[0]));
                                            calendaragendado.set(Calendar.MINUTE, Integer.parseInt(horaescolhida.split(":")[1]));
                                            calendaragendado.set(Calendar.SECOND, 0);
                                            calendaragendado.set(Calendar.MILLISECOND, 0);
                                            String id = System.currentTimeMillis() + "";
                                            if (!notificarAs.getEditText().getText().toString().trim().isEmpty()) {
                                                if (notificarAs.getEditText().getText().toString().trim().split(":").length > 1) {
                                                    if (Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[0]) > 23 || Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[1]) > 59) {
                                                        notificarAs.setError("Preencha esse campo corretamente");
                                                        continuar = false;
                                                    }
                                                } else {
                                                    continuar = false;
                                                    notificarAs.setError("Preencha esse campo corretamente");
                                                }
                                            }
                                            if (nomePessoa.getEditText().getText().toString().trim().isEmpty()) {
                                                continuar = false;
                                                nomePessoa.setError("Preencha esse campo corretamente");
                                            }

                                            if (continuar) {
                                                if (Login.pessoa.getCargo().equals("usuario")) {
                                                    Calendar calendar = Calendar.getInstance();
                                                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                                    Intent myIntent = new Intent(getBaseContext(), estouchegandobroadcast.class);
                                                    myIntent.putExtra("hora", notificarAs.getEditText().getText().toString().split(":")[0]);
                                                    myIntent.putExtra("minuto", notificarAs.getEditText().getText().toString().split(":")[1]);
                                                    myIntent.putExtra("lastdate", lastdate.getTime());
                                                    myIntent.putExtra("nomeserv", listaservicos.get(servicopos).getNome());
                                                    myIntent.putExtra("horaescolhida", horaescolhida);
                                                    myIntent.putExtra("id", id);
                                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
                                                    calendar.setTime(lastdate);
                                                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[0]));
                                                    calendar.set(Calendar.MINUTE, Integer.parseInt(notificarAs.getEditText().getText().toString().split(":")[1]));
                                                    calendar.set(Calendar.SECOND, 0);
                                                    calendar.set(Calendar.MILLISECOND, 0);
                                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                                                }

                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                                Appoitmentpattern agendamento = new Appoitmentpattern();
                                                agendamento.setEnderecoid(listaende.get(addresspos).getIdinterno());
                                                agendamento.setId(id);
                                                agendamento.setHora(horaescolhida);

                                                agendamento.setTempo(listaservicos.get(servicopos).getTempo());
                                                if (listaservicos.get(servicopos).getValorp().isEmpty() && !listaservicos.get(servicopos).getValorp().equals("0")) {
                                                    if (Double.parseDouble(listaservicos.get(servicopos).getValorp().replace(",", ".").replaceAll("[R$  ]", "")) < Double.parseDouble(listaservicos.get(servicopos).getValor().replaceAll("[R$]", "").replace(",", "."))) {
                                                        agendamento.setValorservico(listaservicos.get(servicopos).getValorp().replace(",", ".").replaceAll("[R$  ]", ""));
                                                    } else {
                                                        agendamento.setValorservico(listaservicos.get(servicopos).getValor().replace(",", ".").replaceAll("[R$  ]", ""));
                                                    }
                                                } else {
                                                    agendamento.setValorservico(listaservicos.get(servicopos).getValor().replace(",", ".").replaceAll("[R$  ]", ""));
                                                }
                                                agendamento.setWeekyear(calendaragendado.get(Calendar.WEEK_OF_YEAR) + "");
                                                agendamento.setNomeservico(listaservicos.get(servicopos).getNome());
                                                agendamento.setIdservico(listaservicos.get(servicopos).getId());
                                                agendamento.setTimeinmilis(calendaragendado.getTimeInMillis() + "");
                                                agendamento.setNomefunc(listafuncs.get(barberpos).getNome());
                                                agendamento.setNomepessoa(nomePessoa.getEditText().getText().toString().trim());
                                                agendamento.setIdpessoa(Login.pessoa.getId());
                                                agendamento.setFunc(listafuncs.get(barberpos).getId());
                                                agendamento.setConcluido("false");
                                                agendamento.setAlarm("false");
                                                agendamento.setPresenca("false");
                                                agendamento.setDia(datafinal);
                                                agendamento.setHora(horaescolhida);
                                                agendamento.setIfevaluated("false");
                                                db.collection("Desconto").get().addOnCompleteListener(command -> {
                                                    command.getResult().getDocuments().forEach(snapshot -> {
                                                        mapdesc = snapshot.getData();
                                                        if (mapdesc != null) {
                                                            if (mapdesc.get("cod").equals(descTag.getEditText().getText().toString().trim()) && !String.valueOf(mapdesc.get("whoused")).contains(Login.pessoa.getId() + ";")) {
                                                                if (Integer.parseInt(mapdesc.get("quantusos") + "") < Integer.parseInt(mapdesc.get("quantusosmax") + "")) {
                                                                    if (mapdesc.get("ifpercent").equals("true")) {//-15%
                                                                        agendamento.setValorservico(String.format("%.02f", (Double.parseDouble(agendamento.getValorservico()) - (Double.parseDouble(agendamento.getValorservico()) * Double.parseDouble(mapdesc.get("valorPerc") + "")) / 100)));
                                                                    } else {
                                                                        agendamento.setValorservico(String.format("%.02f", Double.parseDouble(agendamento.getValorservico()) - Double.parseDouble(String.valueOf(mapdesc.get("valorReal")).replaceAll("[R$  ]", "").replace(",", "."))));
                                                                        ;
                                                                    }
                                                                }
                                                                desc = true;
                                                            }

                                                        }
                                                    });
                                                    reference.child("Agendamentos").child(Login.pessoa.getId()).child(id).setValue(agendamento).addOnSuccessListener(command2 -> {
                                                        if (Agendamentos.lista != null) {
                                                            Agendamentos.lista.add(agendamento);
                                                            String valordesc = "";
                                                            if (mapdesc.get("ifpercent").equals("false")) {
                                                                valordesc += mapdesc.get("valorReal") + " Reais";
                                                            } else {
                                                                valordesc += mapdesc.get("valorPerc");
                                                            }
                                                            Toast.makeText(Add_agendamento.this, "Você recebeu " + valordesc + " de desconto", Toast.LENGTH_LONG).show();
                                                            db.collection("Desconto").document(mapdesc.get("id") + "").update("quantusos", String.valueOf(Integer.parseInt(mapdesc.get("quantusos") + "") + 1));
                                                            Agendamentos.adapter.notifyDataSetChanged();
                                                        }
                                                        choosingHora.dismiss();
                                                        finish();
                                                    });
                                                });

                                            }
                                        }
                                    });
                                    builder.setView(view);
                                    choosingHora = builder.create();
                                    if (view.getParent() != null) {
                                        ((ViewGroup) view.getParent()).removeAllViews();
                                    } else {
                                        choosingHora.setView(null);
                                        choosingHora.setView(view);
                                    }
                                    choosingHora.show();
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });
                            sss.setText("sss");
                        }
                    } else {
                        if (title.getText().toString().contains("Qual será o estilo de hoje ?")) {
                            title.setText("Escolha um(a) profissional");
                            ServicosAdapterAgendar adapteragendar = new ServicosAdapterAgendar(getBaseContext(), listafuncs, false, "");
                            rv_add_agendameno.setAdapter(adapteragendar);
                            if (listaservicos.get(servicopos).getFuncs().isEmpty()) {
                                db.collection("Contas").whereNotEqualTo("cargo", "usuario").get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        listafuncs.clear();
                                        QuerySnapshot a = task.getResult();
                                        if (!a.isEmpty()) {
                                            for (QueryDocumentSnapshot queryDocumentSnapshot : a) {
                                                Map<String, Object> map = (Map<String, Object>) queryDocumentSnapshot.getData();
                                                Servicepattern pessoa = new Servicepattern();
                                                pessoa.setNome(map.get("nome") + "");
                                                pessoa.setFt(map.get("imgUri") + "");
                                                pessoa.setSelected("");
                                                pessoa.setId(map.get("id") + "");
                                                listafuncs.add(pessoa);
                                                adapteragendar.notifyDataSetChanged();
                                                System.out.println("ssss -> "+listafuncs.size());

                                            }
                                        }
                                    }
                                });
                            } else {
                                String[] ids = listaservicos.get(servicopos).getFuncs().split(";");
                                for (String id : ids) {
                                    db.collection("Contas").whereEqualTo("id", id).get().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            QuerySnapshot a = task.getResult();
                                            if (!a.isEmpty()) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : a) {
                                                    Map<String, Object> map = (Map<String, Object>) queryDocumentSnapshot.getData();
                                                    Servicepattern pessoa = new Servicepattern();
                                                    pessoa.setNome(map.get("nome") + "");
                                                    pessoa.setFt(map.get("imgUri") + "");
                                                    pessoa.setSelected("");
                                                    pessoa.setId(map.get("id") + "");
                                                    listafuncs.add(pessoa);
                                                    adapteragendar.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
                if (title.getText().toString().contains("Escolha uma unidade"))
                    title.setText("Qual será o estilo de hoje ?");
            }
        });
        rv_add_agendameno.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listaende = new ArrayList<>();
        listaservicos = new ArrayList<>();
        listafuncs = new ArrayList<>();


        db.collection("Enderecos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot a = task.getResult();
                if (!a.isEmpty()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : a) {
                        Map<String, Object> map = (Map<String, Object>) queryDocumentSnapshot.getData();
                        Addrespattern address = new Addrespattern();
                        address.setIdinterno(map.get("idinterno") + "");
                        address.setEndereco(map.get("endereco") + "");
                        address.setNumero(map.get("numero") + "");
                        address.setCep(map.get("cep") + "");
                        address.setEndereco_apelido(map.get("endereco_apelido") + "");
                        address.setIfseleceted("");
                        listaende.add(address);
                        adapterendereco.notifyDataSetChanged();
                    }
                }
            }
        });
        db.collection("Servicos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Servicepattern servico = new Servicepattern();
                    servico.setDescricao(snapshot.get("descricao") + "");
                    servico.setDiaspromocao(snapshot.get("diaspromocao") + "");
                    servico.setFt(snapshot.get("ft") + "");
                    servico.setFuncs(snapshot.get("funcs") + "");
                    servico.setId(snapshot.get("id") + "");
                    servico.setIfpromocao(snapshot.get("ifpromocao") + "");
                    servico.setNome(snapshot.get("nome") + "");
                    servico.setSelected("");
                    servico.setTempo(snapshot.get("tempo") + "");
                    servico.setValor(snapshot.get("valor") + "");
                    servico.setValorp(snapshot.get("valorp") + "");
                    listaservicos.add(servico);
                    adapterservicos.notifyDataSetChanged();
                }
            }
        });
        adapterservicos = new ServicosAdapterAgendar(Add_agendamento.this, listaservicos, true, "servicoss");
        adapterendereco = new Adapteraddress(getBaseContext(), listaende, "true");
        rv_add_agendameno.setAdapter(adapterendereco);
    }

    private boolean testDate(Map<String, Object> diamap, int day) {
        boolean folga = false;
        if (Boolean.parseBoolean(diamap.get("iffolga") + "")) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(lastdate);
            if (calendar1.get(Calendar.DAY_OF_WEEK) == day) {
                calendar1.add(Calendar.DAY_OF_MONTH, 1);//
                lastdate = calendar1.getTime();
            }
            pickerdate.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
            folga = true;
        }
        return folga;
    }

    private void changelisthora(String comeco, String fim, String almococomeco, String almocofim) {
        horalist.clear();
        manhalist.clear();
        noitelist.clear();
        tardelist.clear();
        Calendar comecocalendar = Calendar.getInstance();
        Calendar fimcalendar = Calendar.getInstance();
        fimcalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fim.split(":")[0]));
        fimcalendar.set(Calendar.MINUTE, Integer.parseInt(fim.split(":")[1]));
        comecocalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(comeco.split(":")[0]));
        comecocalendar.set(Calendar.MINUTE, Integer.parseInt(comeco.split(":")[1]));
        int partedodia = 0;//0 manha //1 tarde // 2 noite
        adicionar = true;
        while (fimcalendar.compareTo(comecocalendar) > 0) {
            String minutostring = "";
            String horastring = "";
            if (comecocalendar.get(Calendar.MINUTE) == 0) {
                minutostring += "0";
            }
            minutostring += comecocalendar.get(Calendar.MINUTE);
            if (comecocalendar.get(Calendar.HOUR_OF_DAY) < 10) {
                horastring += "0";
            }
            horastring += comecocalendar.get(Calendar.HOUR_OF_DAY);
            String horafinal = horastring + ":" + minutostring;
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(lastdate);
            String dia = "";
            if (calendar2.get(Calendar.DAY_OF_MONTH) < 10) {
                dia += "0";
            }
            dia += calendar2.get(Calendar.DAY_OF_MONTH);
            String mes = "";
            if ((calendar2.get(Calendar.MONTH) + 1) < 10) {
                mes += "0";
            }
            mes += calendar2.get(Calendar.MONTH) + 1;
            datafinal = dia + "/" + mes + "/" + calendar2.get(Calendar.YEAR);
            if (horafinal.equals(almococomeco)) {
                adicionar = false;
            }
            if ((horafinal).equals("12:00")) {
                partedodia++;
            }
            if ((horafinal).equals("18:00")) {
                partedodia++;
            }
            if (adicionar) {
                switch (partedodia) {
                    case 0:
                        manhalist.add(new Horapattern(horafinal, chektime(datafinal, horafinal), ""));
                        break;
                    case 1:
                        tardelist.add(new Horapattern(horafinal, chektime(datafinal, horafinal), ""));
                        break;
                    case 2:
                        noitelist.add(new Horapattern(horafinal, chektime(datafinal, horafinal), ""));
                        break;
                }
            }
            if (horafinal.equals(almocofim)) {
                adicionar = true;
            }
            comecocalendar.add(Calendar.MINUTE, 15);
        }
        horalist.addAll(manhalist);
        adapterChoosing.notifyDataSetChanged();

    }

    private void initListaAppointent() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Agendamentos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Map<String, Object> servicepattern = (Map<String, Object>) snapshot1.getValue();

                    servicepattern.forEach((s, o) -> {
                        Map<String, Object> se = (Map<String, Object>) o;
                        Appoitmentpattern servico = new Appoitmentpattern();
                        if ((se.get("func") + "").equals(listafuncs.get(barberpos).getId())) {
                            servico.setNomeservico(se.get("serviconome") + "");
                            servico.setWeekyear(se.get("week") + "");
                            servico.setValorservico(se.get("valor") + "");
                            servico.setHora(se.get("hora") + "");
                            servico.setIdservico(se.get("idservico") + "");
                            servico.setValorservico(se.get("servicovalor") + "");
                            servico.setTimeinmilis(se.get("timeinmilis") + "");
                            servico.setNomefunc(se.get("nomefunc") + "");
                            servico.setIdpessoa(se.get("idpessoa") + "");
                            servico.setFunc(se.get("func") + "");
                            servico.setNomepessoa(se.get("nomepessoa") + "");
                            servico.setConcluido(se.get("concluido") + "");
                            servico.setAlarm(se.get("alarm") + "");
                            servico.setId(se.get("id") + "");
                            servico.setDia(se.get("dia") + "");
                            servico.setNomeservico(se.get("serviconome") + "");
                            agendamentosProntos.add(servico);

                        }
                    });
//                    sss.setText("");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private boolean chektime(String dia1, String hora) {
        ocupado = false;
        for (int i = 0; i < agendamentosProntos.size(); i++) {
            ocupado = agendamentosProntos.get(i).getDia().equals(dia1) && agendamentosProntos.get(i).getHora().equals(hora);
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MINUTE, 30);
        calendar1.set(Calendar.SECOND, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Integer.parseInt(dia1.split("/")[2]), Integer.parseInt(dia1.split("/")[1]) - 1, Integer.parseInt(dia1.split("/")[0]), Integer.parseInt(hora.split(":")[0]), Integer.parseInt(hora.split(":")[1]), 0);
        if (calendar2.compareTo(calendar1) < 0) {
            ocupado = true;
        }
        return ocupado;
    }
}