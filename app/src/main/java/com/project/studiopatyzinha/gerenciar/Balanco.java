package com.project.studiopatyzinha.gerenciar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.Adapter.funcAdapter;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.pattern.Comments_pattern;
import com.project.studiopatyzinha.pattern.Pedidospattern;
import com.project.studiopatyzinha.pattern.PostPattern;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class Balanco extends AppCompatActivity {
    Toolbar toolbar_balanco;
    ImageView search_balanco, showdatepickerdialog;
    BarChart barChart;
    Spinner spinner_tipoinfo_balanco, spinner_ibjinfo_balanco, spinner_monthinfo_balanco, spinner_duracaoinfo_balanco;
    ArrayList<Accountpattern> funcs;
    ArrayList<PostPattern> posts;
    ArrayList<Appoitmentpattern> agends;
    ArrayList<Pedidospattern> pedidos;
    TextView contruirspinner;
    TextView datetxt_balanco;
    LineChart linechart_balanco;
    String date;
    RecyclerView rv_objsname;
    funcAdapter adapter;
    ArrayList<Accountpattern> selectedfuncs;
    ArrayList<String> selectedfuncsnomes;
    ArrayList<Produtopattern> produtos;
    int sizebarchart = 24;
    ArrayList<String> listaobjs;
    Map<String, Object> rotina;
    Date lastdate;
    DatePickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balanco);
        lastdate = Calendar.getInstance().getTime();
        String diad = "";
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
            diad += "0";
        }
        diad += Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String mesd = "";
        if ((Calendar.getInstance().get(Calendar.MONTH) + 1) < 10) {
            mesd += "0";
        }
        mesd += (Calendar.getInstance().get(Calendar.MONTH) + 1);
        date = diad + "/" + mesd + "/" + Calendar.getInstance().get(Calendar.YEAR);
        rv_objsname = findViewById(R.id.rv_objsname);
        selectedfuncs = new ArrayList<>();
        selectedfuncsnomes = new ArrayList<>();
        adapter = new funcAdapter(selectedfuncs, "muito novo");
        rv_objsname.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rv_objsname.setAdapter(adapter);
        showdatepickerdialog = findViewById(R.id.showdatepickerdialog);
        linechart_balanco = findViewById(R.id.linechart_balanco);
        toolbar_balanco = findViewById(R.id.toolbar_balanco);
        datetxt_balanco = findViewById(R.id.datetxt_balanco);
        spinner_duracaoinfo_balanco = findViewById(R.id.spinner_duracaoinfo_balanco);
        spinner_monthinfo_balanco = findViewById(R.id.spinner_monthinfo_balanco);
        contruirspinner = new TextView(getBaseContext());
        contruirspinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).contains("posts") && String.valueOf(s).contains("agends") && String.valueOf(s).contains("funcs")) {
                    initspinner();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        barChart = findViewById(R.id.barchart_balanco);
        this.setSupportActionBar(toolbar_balanco);
        this.getSupportActionBar().setTitle("");
        toolbar_balanco.setTitle("Balanço");
        toolbar_balanco.setTitleTextColor(Color.WHITE);
        toolbar_balanco.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbar_balanco.setNavigationOnClickListener(v -> finish());
        toolbar_balanco.setBackground(AppCompatResources.getDrawable(getBaseContext(), R.color.primaria));
        search_balanco = findViewById(R.id.search_balanco);
        search_balanco.setOnClickListener(v -> pesquisar());
        barChart.setNoDataText("Sem informações suficientes");
        barChart.setNoDataTextColor(Color.BLACK);

        linechart_balanco.setNoDataText("Sem informações suficientes");
        linechart_balanco.setNoDataTextColor(Color.BLACK);
        showdatepickerdialog.setOnClickListener(v -> {
            dialog = new DatePickerDialog(Balanco.this);
            dialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {//arrumar agora
                String dia = "";
                if (dayOfMonth < 10) {
                    dia += "0";
                }
                dia += dayOfMonth;
                String mes = "";
                if ((month + 1) < 10) {
                    mes += "0";
                }
                mes += (month + 1);
                date = dia + "/" + mes + "/" + year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, (month + 1), dayOfMonth, 0, 0, 0);
                int dias = calendar.get(Calendar.DAY_OF_WEEK);
                String dia2 = "";
                switch (dias) {
                    case 3: //sab
                        dia2 += "Sab";
                        break;
                    case 4: //dom
                        dia2 += "Dom";
                        break;
                    case 5: //seg
                        dia2 += "Seg";
                        break;
                    case 6: //ter
                        dia2 += "Ter";
                        break;
                    case 7: //qua
                        dia2 += "Qua";
                        break;
                    case 1: //qui
                        dia2 += "Qui";
                        break;
                    case 2: //sex
                        dia2 += "Sex";

                        break;
                }
                switch (spinner_duracaoinfo_balanco.getSelectedItemPosition()) {
                    case 0://posaaaa
                        datetxt_balanco.setText(dia2 + " " + date);
                        break;

                    case 1:
                        datetxt_balanco.setText(calendar.get(Calendar.WEEK_OF_MONTH) + "°  semana de " + MainActivity.meses.get(calendar.get(Calendar.MONTH) + 1));
                        break;
                    case 2:
                        spinner_monthinfo_balanco.setSelection(calendar.get(Calendar.MONTH) - 1);
                        break;
                    case 3:
                        datetxt_balanco.setText(calendar.get(Calendar.YEAR) + "");
                        break;
                }
            });
            dialog.show();
        });
        spinner_tipoinfo_balanco = findViewById(R.id.spinner_tipoinfo_balanco);
        spinner_ibjinfo_balanco = findViewById(R.id.spinner_ibjinfo_balanco);
        initlist();
    }


    private void initspinner() {
        ArrayList<String> listatype = new ArrayList<>();
        listatype.add("Agendamentos");
        listatype.add("Publicações");
        listatype.add("Pedidos");
        ArrayAdapter<String> adaptertype = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, listatype);
        spinner_tipoinfo_balanco.setAdapter(adaptertype);
        spinner_tipoinfo_balanco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (listatype.get(position)) {
                    case "Publicações":
                        showdatepickerdialog.setVisibility(View.GONE);
                        spinner_duracaoinfo_balanco.setVisibility(View.GONE);
                        datetxt_balanco.setVisibility(View.GONE);
                        spinner_monthinfo_balanco.setVisibility(View.GONE);
                        listaobjs.clear();
                        listaobjs.add("Todos");
                        for (int i = 0; i < posts.size(); i++) {
                            boolean continuar = true;
                            for (int j = 0; j < listaobjs.size(); j++) {
                                if (listaobjs.get(j).equals(posts.get(i).getPessoa().getNome())) {
                                    continuar = false;
                                }
                            }
                            if (continuar) {
                                listaobjs.add(posts.get(i).getPessoa().getNome());
                            }
                        }
                        break;
                    default:
                        datetxt_balanco.setVisibility(View.VISIBLE);
                        spinner_duracaoinfo_balanco.setVisibility(View.VISIBLE);
                        showdatepickerdialog.setVisibility(View.VISIBLE);
                        listaobjs.clear();
                        listaobjs.add("Todos");
                        for (int i = 0; i < funcs.size(); i++) {
                            listaobjs.add(funcs.get(i).getNome());
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listaobjs = new ArrayList<>();

        listaobjs.add("Todos");
        for (int i = 0; i < funcs.size(); i++) {
            listaobjs.add(funcs.get(i).getNome());
        }
        ArrayAdapter<String> adapterobjs = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, listaobjs);
        spinner_ibjinfo_balanco.setAdapter(adapterobjs);
        ArrayList<String> listamonths = new ArrayList<>();
        listamonths.add("Janeiro");
        listamonths.add("Fevereiro");
        listamonths.add("Março");
        listamonths.add("Abril");
        listamonths.add("Maio");
        listamonths.add("Junho");
        listamonths.add("Julho");
        listamonths.add("Agosto");
        listamonths.add("Setembro");
        listamonths.add("Outubro");
        listamonths.add("Novenbro");
        listamonths.add("Dezembro");
        ArrayAdapter<String> adaptermonths = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, listamonths);
        spinner_monthinfo_balanco.setAdapter(adaptermonths);
        spinner_monthinfo_balanco.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        ArrayList<String> listatempos = new ArrayList<>();
        listatempos.add("Dia");
        listatempos.add("Semana");
        listatempos.add("Mês");
        listatempos.add("Ano");

        ArrayAdapter<String> adaptertempos = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, listatempos);
        spinner_duracaoinfo_balanco.setAdapter(adaptertempos);
        spinner_duracaoinfo_balanco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sizebarchart = 24;
                        showdatepickerdialog.setVisibility(View.VISIBLE);
                        String data = MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) + " ";
                        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
                            data += "0";
                        }
                        data += Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/";
                        if ((Calendar.getInstance().get(Calendar.MONTH) + 1) < 10) {
                            data += "0";
                        }
                        data += (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
                        datetxt_balanco.setText(data);
                        spinner_monthinfo_balanco.setVisibility(View.GONE);
                        break;

                    case 1:
                        showdatepickerdialog.setVisibility(View.VISIBLE);
                        sizebarchart = 7;
                        String a = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) + "° semana de " + MainActivity.meses.get(Calendar.getInstance().get(Calendar.MONTH));
                        datetxt_balanco.setText(a);//
                        spinner_monthinfo_balanco.setVisibility(View.VISIBLE);
                        ArrayList<String> listaadapter = new ArrayList<>();
                        for (int i = 1; i < Calendar.getInstance().getActualMaximum(Calendar.WEEK_OF_MONTH) + 1; i++) {
                            listaadapter.add(i + "°");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, listaadapter);
                        spinner_monthinfo_balanco.setAdapter(adapter);
                        spinner_monthinfo_balanco.setSelection(Calendar.getInstance().get(Calendar.WEEK_OF_MONTH) - 1);
                        break;

                    case 2:
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, spinner_monthinfo_balanco.getSelectedItemPosition());
                        sizebarchart = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        showdatepickerdialog.setVisibility(View.VISIBLE);
                        String data2 = "";// MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1) +
                        if ((Calendar.getInstance().get(Calendar.MONTH) + 1) < 10) {
                            data2 += "0";
                        }
                        data2 += (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
                        datetxt_balanco.setText(data2);
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, listamonths);
                        spinner_monthinfo_balanco.setAdapter(adapter2);
                        spinner_monthinfo_balanco.setVisibility(View.VISIBLE);
                        spinner_monthinfo_balanco.setSelection(Calendar.getInstance().get(Calendar.MONTH));
                        break;
                    case 3:
                        sizebarchart = 12;
                        showdatepickerdialog.setVisibility(View.VISIBLE);
                        String data3 = "" + Calendar.getInstance().get(Calendar.YEAR);
                        datetxt_balanco.setText(data3);
                        spinner_monthinfo_balanco.setVisibility(View.GONE);
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initlist() {
        contruirspinner.setText("");
        funcs = new ArrayList<>();
        posts = new ArrayList<>();
        agends = new ArrayList<>();
        pedidos = new ArrayList<>();
        produtos = new ArrayList<>();
        rv_objsname.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Produtos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Map<String, Object> map2 = queryDocumentSnapshot.getData();
                        Produtopattern mip = new Produtopattern();
                        mip.setIdexterno(map2.get("idexterno") + "");
                        mip.setNome(map2.get("nome") + "");
                        mip.setModelo(map2.get("modelo") + "");
                        mip.setFabricante(map2.get("fabricante") + "");
                        mip.setDesconto(map2.get("desconto") + "");
                        mip.setPreco_original(map2.get("preco_original") + "");
                        mip.setQuant_per_cx(map2.get("quant_per_cx") + "");
                        mip.setUsosindicados(map2.get("usosindicados") + "");
                        mip.setUsosNindicados(map2.get("usosNindicados") + "");
                        mip.setBitmapImg(map2.get("bitmapImg") + "");
                        mip.setQuant_minima(map2.get("quant_minima") + "");
                        mip.setQuant_produto_interno(map2.get("quant_produto_interno") + "");
                        mip.setQuant_produto("");
                        produtos.add(mip);
                    }
                }
            }
        });
        db.collection("Pedidos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                task.getResult().forEach(snapshot -> {
                    Map<String, Object> map = snapshot.getData();
                    Pedidospattern pattern = new Pedidospattern();
                    pattern.setQuantidade(map.get("quantidade") + "");
                    pattern.setDataCompra(map.get("dataCompra") + "");
                    pattern.setValorTotal(map.get("valorTotal") + "");
                    pattern.setIdPedido(map.get("idPedido") + "");
                    pattern.setProdutosString(map.get("produtosString") + "");
                    pattern.setPedidoretirado(map.get("Pedidoretirado") + "");
                    pattern.setFormaPagamento(map.get("formaPagamento") + "");
                    pattern.setDataRetirada(map.get("dataRetirada") + "");
                    pattern.setDia(map.get("dia") + "");
                    pattern.setStatus(map.get("status") + "");
                    pedidos.add(pattern);
                });
            }
        });

        db.collection("Contas").whereNotEqualTo("cargo", "usuario").get().addOnCompleteListener(task -> {
            QuerySnapshot a = task.getResult();
            for (QueryDocumentSnapshot queryDocumentSnapshot : a) {
                Map<String, Object> map = (Map<String, Object>) queryDocumentSnapshot.getData();
                Accountpattern pessoa = new Accountpattern();
                pessoa.setFollowersquant(map.get("followersquant") + "");
                pessoa.setFrasefilosofica(map.get("frasefilosofica") + "");
                pessoa.setHorario((Map<String, Object>) map.get("horario"));
                pessoa.setQuantpost(map.get("quantpost") + "");
                pessoa.setNome(map.get("nome") + "");
                pessoa.setFollowersid((Map<String, Object>) map.get("followersid"));
                pessoa.setSenha(map.get("senha") + "");
                pessoa.setOpinioes((Map<String, Object>) map.get("opinioes"));
                pessoa.setBarbersposts((Map<String, Object>) map.get("barbersposts"));
                pessoa.setImgUri(map.get("imgUri") + "");
                pessoa.setPercentual(map.get("percentual") + "");
                pessoa.setDia_salario(map.get("dia_salario") + "");
                pessoa.setId(map.get("id") + "");
                pessoa.setCargo(map.get("cargo") + "");
                pessoa.setGrupoAbaixo(map.get("grupoAbaixo") + "");
                pessoa.setEmail(map.get("email") + "");
                pessoa.setSelected(map.get("selected") + "");
                funcs.add(pessoa);
            }
            contruirspinner.setText(contruirspinner.getText() + "funcs");
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Agendamentos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agends.clear();
                Iterable<DataSnapshot> a = snapshot.getChildren();
                a.forEach(dataSnapshot -> {
                    Map<String, Object> idusermap = (Map<String, Object>) dataSnapshot.getValue();
                    idusermap.forEach((s, servico) -> {
                        Map<String, Object> servicomap = (Map<String, Object>) servico;
                        Appoitmentpattern agendamento = new Appoitmentpattern();
                        agendamento.setValorservico(servicomap.get("valorservico") + "");
                        agendamento.setHora(servicomap.get("hora") + "");
                        agendamento.setWeekyear(servicomap.get("weekyear") + "");
                        agendamento.setTimeinmilis(servicomap.get("timeinmilis") + "");
                        agendamento.setNomefunc(servicomap.get("nomefunc") + "");
                        agendamento.setTempo(servicomap.get("tempo") + "");
                        agendamento.setIdpessoa(servicomap.get("idpessoa") + "");
                        agendamento.setIdservico(servicomap.get("idservico") + "");
                        agendamento.setFunc(servicomap.get("func") + "");
                        agendamento.setNomepessoa(servicomap.get("nomepessoa") + "");
                        agendamento.setConcluido(servicomap.get("concluido") + "");
                        agendamento.setAlarm(servicomap.get("alarm") + "");
                        agendamento.setId(servicomap.get("id") + "");
                        agendamento.setNomeservico(servicomap.get("nomeservico") + "");
                        agendamento.setDia(servicomap.get("dia") + "");
                        agends.add(agendamento);
                    });
                });
                contruirspinner.setText(contruirspinner.getText() + "agends");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db.collection("Rotina").get().addOnSuccessListener(snapshot -> {
            snapshot.forEach(snapshot1 -> {
                rotina = snapshot1.getData();
            });


        });
        db.collection("Blog").orderBy("date").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().forEach(snapshot -> {
                    Map<String, Object> mippessoa = (Map<String, Object>) snapshot.get("pessoa");
                    Accountpattern pessoa = new Accountpattern((Map<String, Object>) mippessoa.get("barbersposts"),
                            mippessoa.get("cargo") + "", mippessoa.get("dia_salario") + "",
                            mippessoa.get("email") + "", (Map<String, Object>) mippessoa.get("followersid"),
                            mippessoa.get("followersquant") + "", mippessoa.get("frasefilosofica") + "",
                            mippessoa.get("grupoAbaixo") + "", (Map<String, Object>) mippessoa.get("horario"),
                            mippessoa.get("id") + "", mippessoa.get("imgUri") + "", mippessoa.get("nome") + "",
                             (Map<String, Object>) mippessoa.get("opinioes"),
                            mippessoa.get("percentual") + "", mippessoa.get("quantpost") + "",
                            mippessoa.get("selected") + "", mippessoa.get("senha") + "",(Map<String, Object>) mippessoa.get("horasdiaspagamentos"),mippessoa.get("ultimosalario") + "",mippessoa.get("ifgerente") + "");
                    PostPattern mip2 = new PostPattern(snapshot.get("commentsQuant") + "",
                            snapshot.get("date") + "", snapshot.get("desc") + "", snapshot.get("id") + "",
                            (Map<String, Object>) snapshot.get("likesids"), snapshot.get("likesquants") + "",
                            snapshot.get("nomeid") + "", pessoa, snapshot.get("photo") + "", Boolean.parseBoolean(snapshot.get("selflike") + ""),
                            snapshot.get("tag") + "", snapshot.get("views") + "", (Map<String, Object>) snapshot.get("notificados"), (ArrayList<Comments_pattern>) snapshot.get("comentarios"));
                    posts.add(mip2);
                });
                contruirspinner.setText(contruirspinner.getText() + "posts");
            }
        });
    }

    private void initbarchart() {
        selectedfuncsnomes.clear();
        selectedfuncs.clear();
        if (spinner_ibjinfo_balanco.getSelectedItemPosition() == 0) {
            selectedfuncsnomes.clear();
            selectedfuncs.clear();
            selectedfuncsnomes.add("Todos");
            Map<String, Object> s = new HashMap<>();
            selectedfuncs.add(new Accountpattern(s, "", "", "", s, "", "", "", s,  "", "Todos", "", s, "", "", "", "",s,"",""));
        } else {
            selectedfuncsnomes.add(spinner_ibjinfo_balanco.getSelectedItem() + "");
            if (spinner_tipoinfo_balanco.getSelectedItemPosition() == 1) {
                Map<String, Object> s = new HashMap<>();
                selectedfuncs.add(new Accountpattern(s, "", "", "", s, "", "", "", s, "", "", spinner_ibjinfo_balanco.getSelectedItem() + "", s, "", "", "", "",s,"",""));
            } else {
                selectedfuncs.add(funcs.get(spinner_ibjinfo_balanco.getSelectedItemPosition() - 1));
            }
        }
        adapter.notifyDataSetChanged();
        rv_objsname.setVisibility(View.VISIBLE);
        switch (String.valueOf(spinner_tipoinfo_balanco.getSelectedItem())) {
            case "Agendamentos":
                linechart_balanco.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);

                datetxt_balanco.setVisibility(View.VISIBLE);
                spinner_duracaoinfo_balanco.setVisibility(View.VISIBLE);
                showdatepickerdialog.setVisibility(View.VISIBLE);
                listaobjs.clear();
                listaobjs.add("Todos");
                for (int i = 0; i < funcs.size(); i++) {
                    listaobjs.add(funcs.get(i).getNome());
                }
                ArrayList<String> titles = new ArrayList<>();
                switch (spinner_duracaoinfo_balanco.getSelectedItemPosition()) {
                    case 0:

                        Map<String, Object> dia = (Map<String, Object>) rotina.get(datetxt_balanco.getText().toString().split(" ")[0].toUpperCase());
                        int abre = Integer.parseInt(dia.get("abreas").toString().split(":")[0]);
                        int fecha = Integer.parseInt(dia.get("fechaas").toString().split(":")[0]);
                        if (fecha != 0) {
                            for (int i = abre; i < fecha; i++) {
                                titles.add(i + "");
                            }
                        }


                        break;

                    case 1:
                        datetxt_balanco.setText(spinner_monthinfo_balanco.getSelectedItem() + " semana de " + MainActivity.meses.get(Calendar.getInstance().get(Calendar.MONTH)));
                        titles.addAll(MainActivity.semana);
                        break;
                    case 2:
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, spinner_monthinfo_balanco.getSelectedItemPosition());
                        for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1; i++) {
                            titles.add(i + "");
                        }
                        break;
                    case 3:
                        for (int i = 1; i < sizebarchart + 1; i++) {
                            titles.add(i + "");
                        }
                        break;
                }

                BarData bardata = new BarData();


                ArrayList<BarEntry> dataVals = new ArrayList<>();
                for (int i = 0; i < titles.size(); i++) {
                    int v = 0;

                    for (int k = 0; k < agends.size(); k++) {
                        boolean iffucn = false;
                        boolean ifdate = false;
                        boolean ifweek = false;
                        boolean ifhora = false;
                        if (selectedfuncs.get(0).getId().equals(agends.get(k).getFunc()) || spinner_ibjinfo_balanco.getSelectedItemPosition() == 0) {
                            iffucn = true;
                        }
                        switch (spinner_duracaoinfo_balanco.getSelectedItemPosition()) {
                            case 0:
                                if (String.valueOf(Integer.parseInt(agends.get(k).getHora().split(":")[0])).equals(titles.get(i))) {
                                    ifhora = true;
                                }
                                if (agends.get(k).getDia().equals(datetxt_balanco.getText().toString().split(" ")[1])) {
                                    ifdate = true;
                                    ifweek = true;
                                }
                                break;

                            case 1:
                                ArrayList<Integer> lista = new ArrayList<>();
                                lista.add(Calendar.SUNDAY);
                                lista.add(Calendar.MONDAY);
                                lista.add(Calendar.TUESDAY);
                                lista.add(Calendar.WEDNESDAY);
                                lista.add(Calendar.THURSDAY);
                                lista.add(Calendar.FRIDAY);
                                lista.add(Calendar.SATURDAY);
                                Calendar calendarselected = Calendar.getInstance();
                                calendarselected.set(Integer.parseInt(date.split("/")[2]), Integer.parseInt(date.split("/")[1]) - 1, Integer.parseInt(date.split("/")[0]), 0, 0, 0);
                                calendarselected.set(Calendar.WEEK_OF_MONTH, spinner_monthinfo_balanco.getSelectedItemPosition() + 1);
                                if (agends.get(k).getWeekyear().equals(calendarselected.get(Calendar.WEEK_OF_YEAR) + "")) {
                                    ifweek = true;
                                    ifhora = true;
                                }
                                calendarselected.set(Calendar.DAY_OF_WEEK, lista.get(i));
                                String dia = "";
                                if (calendarselected.get(Calendar.DAY_OF_MONTH) < 10) {
                                    dia += "0";
                                }
                                dia += calendarselected.get(Calendar.DAY_OF_MONTH);
                                String mes = "";
                                if ((calendarselected.get(Calendar.MONTH) + 1) < 10) {
                                    mes += "0";
                                }
                                mes += calendarselected.get(Calendar.MONTH) + 1;
                                String dateselected = dia + "/" + mes + "/" + calendarselected.get(Calendar.YEAR);
                                if (agends.get(k).getDia().equals(dateselected)) {
                                    ifdate = true;
                                }
                                break;
                            case 2:
                                if (agends.get(k).getDia().split("/")[1].equals(date.split("/")[1])) {
                                    ifhora = true;
                                    ifweek = true;
                                }
                                Calendar calendarselected2 = Calendar.getInstance();
                                calendarselected2.set(Integer.parseInt(date.split("/")[2]), Integer.parseInt(date.split("/")[1]) - 1, 0, 0, 0, 0);
                                calendarselected2.set(Calendar.DAY_OF_MONTH, i + 1);

                                String dia2 = "";
                                if (calendarselected2.get(Calendar.DAY_OF_MONTH) < 10) {
                                    dia2 += "0";
                                }
                                dia2 += calendarselected2.get(Calendar.DAY_OF_MONTH);
                                String mes2 = "";
                                if ((calendarselected2.get(Calendar.MONTH) + 1) < 10) {
                                    mes2 += "0";
                                }
                                mes2 += (calendarselected2.get(Calendar.MONTH) + 1);
                                String dateselected2 = dia2 + "/" + mes2 + "/" + calendarselected2.get(Calendar.YEAR);
                                if (agends.get(k).getDia().equals(dateselected2)) {
                                    ifdate = true;
                                }

                                break;
                            case 3:
                                ArrayList<Integer> lista2 = new ArrayList<>();
                                lista2.add(1);
                                lista2.add(2);
                                lista2.add(3);
                                lista2.add(4);
                                lista2.add(5);
                                lista2.add(6);
                                lista2.add(7);
                                lista2.add(8);
                                lista2.add(9);
                                lista2.add(10);
                                lista2.add(11);
                                lista2.add(12);
                                Calendar calendarselected3 = Calendar.getInstance();
                                calendarselected3.set(Integer.parseInt(date.split("/")[2]), 0, 0, 0, 0, 0);
                                calendarselected3.set(Calendar.MONTH, lista2.get(i));
                                if (agends.get(k).getDia().split("/")[2].equals("" + calendarselected3.get(Calendar.YEAR))) {
                                    ifhora = true;
                                    ifweek = true;
                                }
                                String mescerto = "";
                                if ((calendarselected3.get(Calendar.MONTH) + 1) < 10) {
                                    mescerto += "0";
                                }
                                mescerto += (calendarselected3.get(Calendar.MONTH) + 1);
                                if (agends.get(k).getDia().split("/")[1].equals(mescerto)) {
                                    ifdate = true;
                                }

                                break;
                        }
                        if (ifhora && iffucn && ifweek) {
                            if (ifdate) {
                                v++;
                            }
                        }
                    }
                    dataVals.add(new BarEntry(i, v));
                }

                BarDataSet bardataset = new BarDataSet(dataVals, selectedfuncsnomes.get(0));
                bardataset.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf(value).split("[.]")[0];
                    }
                });
                bardataset.setColors(ColorTemplate.MATERIAL_COLORS[0]);
                bardataset.setValueTextSize(14);
                bardata.addDataSet(bardataset);


                barChart.setDescription(null);
                XAxis s = barChart.getXAxis();

                s.setValueFormatter(new IndexAxisValueFormatter(titles));

                s.setGranularity(1f);
                s.setGranularityEnabled(true);
                s.setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.setData(bardata);
                barChart.getLegend().setTextSize(15);
                barChart.setDrawGridBackground(false);


                barChart.invalidate();
                break;

            case "Publicações":
                barChart.setVisibility(View.GONE);
                linechart_balanco.setVisibility(View.VISIBLE);
                ArrayList<Entry> comentariosQuant = new ArrayList<>();
                ArrayList<Entry> likesQuant = new ArrayList<>();
                ArrayList<Entry> viewsQuant = new ArrayList<>();
                ArrayList<String> titles2 = new ArrayList<>();
                for (int i = 0; i < posts.size(); i++) {
                    if (spinner_ibjinfo_balanco.getSelectedItemPosition() == 0 || posts.get(i).getPessoa().getNome().equals(spinner_ibjinfo_balanco.getSelectedItem() + "")) {
                        comentariosQuant.add(new Entry(i, Integer.parseInt(posts.get(i).getCommentsQuant())));
                        likesQuant.add(new Entry(i, Integer.parseInt(posts.get(i).getLikesquants())));
                        viewsQuant.add(new Entry(i, Integer.parseInt(posts.get(i).getViews())));
                        titles2.add(posts.get(i).getNomeid());
                    }
                }
                LineDataSet linedataset1 = new LineDataSet(comentariosQuant, "Comentários");
                LineDataSet linedataset2 = new LineDataSet(likesQuant, "Curtidas");
                LineDataSet linedataset3 = new LineDataSet(viewsQuant, "Visualizações");

                linedataset1.setLineWidth(4);
                linedataset1.setCircleRadius(4);
                linedataset1.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                linedataset1.setValueTextSize(14);
                linedataset1.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);
                linedataset1.setDrawCircles(true);
                linedataset1.setDrawCircleHole(true);
                linedataset1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

                linedataset2.setLineWidth(4);
                linedataset2.setCircleRadius(4);
                linedataset2.setColor(ColorTemplate.MATERIAL_COLORS[1]);
                linedataset2.setValueTextSize(14);
                linedataset2.setCircleColor(ColorTemplate.MATERIAL_COLORS[1]);
                linedataset2.setDrawCircles(true);
                linedataset2.setDrawCircleHole(true);
                linedataset2.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

                linedataset3.setLineWidth(4);
                linedataset3.setCircleRadius(4);
                linedataset3.setColor(ColorTemplate.MATERIAL_COLORS[2]);
                linedataset3.setValueTextSize(14);
                linedataset3.setCircleColor(ColorTemplate.MATERIAL_COLORS[2]);
                linedataset3.setDrawCircles(true);
                linedataset3.setDrawCircleHole(true);
                linedataset3.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);


                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(linedataset1);
                dataSets.add(linedataset2);
                dataSets.add(linedataset3);

                Legend legend = linechart_balanco.getLegend();
                legend.setForm(Legend.LegendForm.CIRCLE);
                legend.setTextSize(13);
                legend.setFormSize(13);

                LineData data = new LineData(dataSets);

                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        String result = "";
                        if (value == 0f) {
                            result = "0";
                        } else {
                            result = (value + "").replace(".0", "");
                        }
                        return result;
                    }
                });//formatando as linhas
                XAxis xAxis = linechart_balanco.getXAxis();
                YAxis yAxisLeft = linechart_balanco.getAxisLeft();
                YAxis yAxisRight = linechart_balanco.getAxisRight();
                xAxis.setAvoidFirstLastClipping(false);
                xAxis.setGranularityEnabled(false);
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        axis.setLabelCount(titles2.size(), false);
                        if (value < 0) {
                            value = 0;
                        }
                        if (value >= titles2.size()) {
                            value--;
                        }
                        return titles2.get((int) Math.floor(value));
                    }
                });
                yAxisRight.setDrawLabels(false);
                yAxisLeft.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        String[] text = String.valueOf(value).split("[.]");
                        String fim = text[0];
                        if (text[1].length() >= 2) {
                            fim += "." + text[1].charAt(0) + "" + text[1].charAt(1);
                        } else if (text[1].length() == 1) {
                            fim += "." + text[1].charAt(0);
                        }
                        return super.getAxisLabel(Float.parseFloat(fim), axis);
                    }
                });
                linechart_balanco.setData(data);
                linechart_balanco.invalidate();
                linechart_balanco.setDescription(null);
                break;
            case "Pedidos":

                linechart_balanco.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                datetxt_balanco.setVisibility(View.VISIBLE);
                spinner_duracaoinfo_balanco.setVisibility(View.VISIBLE);
                showdatepickerdialog.setVisibility(View.VISIBLE);
                listaobjs.clear();
                listaobjs.add("Todos");
                ArrayList<String> titles3 = new ArrayList<>();
                switch (spinner_duracaoinfo_balanco.getSelectedItemPosition()) {
                    case 0:
                        Map<String, Object> dia = (Map<String, Object>) rotina.get(datetxt_balanco.getText().toString().split(" ")[0].toUpperCase());
                        int abre = Integer.parseInt(dia.get("abreas").toString().split(":")[0]);
                        int fecha = Integer.parseInt(dia.get("fechaas").toString().split(":")[0]);
                        if (fecha != 0) {
                            for (int i = abre; i < fecha; i++) {
                                titles3.add(i + "");
                            }
                        }
                        break;

                    case 1:
                        datetxt_balanco.setText(spinner_monthinfo_balanco.getSelectedItem() + " semana de " + MainActivity.meses.get(Calendar.getInstance().get(Calendar.MONTH)));
                        titles3.addAll(MainActivity.semana);
                        break;
                    case 2:
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, spinner_monthinfo_balanco.getSelectedItemPosition());
                        for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1; i++) {
                            titles3.add(i + "");
                        }
                        break;
                    case 3:
                        for (int i = 1; i < sizebarchart + 1; i++) {
                            titles3.add(i + "");
                        }
                        break;
                }

                BarData bardata3 = new BarData();


                ArrayList<BarEntry> dataVals3 = new ArrayList<>();
                for (int i = 0; i < titles3.size(); i++) {
                    int v = 0;
                    for (int k = 0; k < pedidos.size(); k++) {
                        boolean ifdate = false;
                        boolean ifweek = false;
                        switch (spinner_duracaoinfo_balanco.getSelectedItemPosition()) {
                            case 0:
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(Long.parseLong(pedidos.get(k).getDataCompra()));
                                if (pedidos.get(k).getDia().contains(datetxt_balanco.getText().toString().split(" ")[1]) && titles3.get(i).contains(calendar.get(Calendar.HOUR_OF_DAY) + "")) {
                                    ifdate = true;
                                    ifweek = true;
                                }
                                break;
                            case 1:
                                ArrayList<Integer> lista = new ArrayList<>();
                                lista.add(Calendar.SUNDAY);
                                lista.add(Calendar.MONDAY);
                                lista.add(Calendar.TUESDAY);
                                lista.add(Calendar.WEDNESDAY);
                                lista.add(Calendar.THURSDAY);
                                lista.add(Calendar.FRIDAY);
                                lista.add(Calendar.SATURDAY);
                                Calendar calendarselected = Calendar.getInstance();
                                calendarselected.set(Integer.parseInt(date.split("/")[2]), Integer.parseInt(date.split("/")[1]) - 1, Integer.parseInt(date.split("/")[0]), 0, 0, 0);
                                calendarselected.set(Calendar.WEEK_OF_MONTH, spinner_monthinfo_balanco.getSelectedItemPosition() + 1);
                                Calendar calendario = Calendar.getInstance();
                                calendario.setTimeInMillis(Long.parseLong(pedidos.get(k).getDataCompra()));
                                if (calendario.get(Calendar.WEEK_OF_YEAR) == calendarselected.get(Calendar.WEEK_OF_YEAR)) {
                                    ifweek = true;
                                }
                                calendarselected.set(Calendar.DAY_OF_WEEK, lista.get(i));
                                String dia = "";
                                if (calendarselected.get(Calendar.DAY_OF_MONTH) < 10) {
                                    dia += "0";
                                }
                                dia += calendarselected.get(Calendar.DAY_OF_MONTH);
                                String mes = "";
                                if ((calendarselected.get(Calendar.MONTH) + 1) < 10) {
                                    mes += "0";
                                }
                                mes += calendarselected.get(Calendar.MONTH) + 1;
                                String dateselected = dia + "/" + mes + "/" + calendarselected.get(Calendar.YEAR);
                                String dia2 = "";
                                if (calendario.get(Calendar.DAY_OF_MONTH) < 10) {
                                    dia2 += "0";
                                }
                                dia2 += calendario.get(Calendar.DAY_OF_MONTH);
                                String mes2 = "";
                                if ((calendario.get(Calendar.MONTH) + 1) < 10) {
                                    mes2 += "0";
                                }
                                mes2 += calendario.get(Calendar.MONTH) + 1;
                                String dateselected2 = dia2 + "/" + mes2 + "/" + calendario.get(Calendar.YEAR);
                                if (dateselected2.equals(dateselected)) {
                                    ifdate = true;
                                }
                                break;
                            case 2:
                                if (pedidos.get(k).getDia().split("/")[1].equals(date.split("/")[1])) {
                                    ifweek = true;
                                }
                                Calendar calendarselected2 = Calendar.getInstance();
                                calendarselected2.set(Integer.parseInt(date.split("/")[2]), Integer.parseInt(date.split("/")[1]) - 1, 0, 0, 0, 0);
                                calendarselected2.set(Calendar.DAY_OF_MONTH, i + 1);

                                String dia3 = "";
                                if (calendarselected2.get(Calendar.DAY_OF_MONTH) < 10) {
                                    dia3 += "0";
                                }
                                dia3 += calendarselected2.get(Calendar.DAY_OF_MONTH);
                                String mes3 = "";
                                if ((calendarselected2.get(Calendar.MONTH) + 1) < 10) {
                                    mes3 += "0";
                                }
                                mes3 += (calendarselected2.get(Calendar.MONTH) + 1);
                                String dateselected3 = dia3 + "/" + mes3 + "/" + calendarselected2.get(Calendar.YEAR);
                                if (agends.get(k).getDia().equals(dateselected3)) {
                                    ifdate = true;
                                }

                                break;
                            case 3:
                                ArrayList<Integer> lista2 = new ArrayList<>();
                                lista2.add(1);
                                lista2.add(2);
                                lista2.add(3);
                                lista2.add(4);
                                lista2.add(5);
                                lista2.add(6);
                                lista2.add(7);
                                lista2.add(8);
                                lista2.add(9);
                                lista2.add(10);
                                lista2.add(11);
                                lista2.add(12);
                                Calendar calendarselected3 = Calendar.getInstance();
                                calendarselected3.set(Integer.parseInt(date.split("/")[2]), 0, 0, 0, 0, 0);
                                calendarselected3.set(Calendar.MONTH, lista2.get(i));
                                if (pedidos.get(k).getDia().split("/")[2].equals("" + calendarselected3.get(Calendar.YEAR))) {
                                    ifweek = true;
                                }
                                String mescerto = "";
                                if ((calendarselected3.get(Calendar.MONTH) + 1) < 10) {
                                    mescerto += "0";
                                }
                                mescerto += (calendarselected3.get(Calendar.MONTH) + 1);
                                if (pedidos.get(k).getDia().split("/")[1].equals(mescerto)) {
                                    ifdate = true;
                                }
                                break;
                        }
                        if (ifweek) {
                            if (ifdate) {
                                v++;
                            }
                        }
                    }
                    dataVals3.add(new BarEntry(i, v));
                }

                BarDataSet bardataset2 = new BarDataSet(dataVals3, selectedfuncsnomes.get(0));
                bardataset2.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf(value).split("[.]")[0];
                    }
                });
                bardataset2.setColors(ColorTemplate.MATERIAL_COLORS[0]);
                bardataset2.setValueTextSize(14);
                bardata3.addDataSet(bardataset2);


                barChart.setDescription(null);
                XAxis s2 = barChart.getXAxis();

                s2.setValueFormatter(new IndexAxisValueFormatter(titles3));

                s2.setGranularity(1f);
                s2.setGranularityEnabled(true);
                s2.setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.setData(bardata3);
                barChart.getLegend().setTextSize(15);
                barChart.setDrawGridBackground(false);

                barChart.invalidate();
                break;
        }
    }

    private void pesquisar() {
        Toast.makeText(this, "pesquisando", Toast.LENGTH_SHORT).show();
        initbarchart();
    }

    private boolean testDate(Map<String, Object> diamap) {
        boolean folga = false;
        if (Boolean.parseBoolean(diamap.get("iffechado") + "")) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(lastdate);
            dialog.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
            folga = true;
        }
        return folga;
    }
}