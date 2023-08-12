package com.project.studiopatyzinha;

import static com.project.studiopatyzinha.R.color.primaria;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.Adapter.ServicosAdapterAgendar;
import com.project.studiopatyzinha.Adapter.day_add_service_adapter;
import com.project.studiopatyzinha.Adapter.funcAdapter;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Dayitempatternhorario;
import com.project.studiopatyzinha.pattern.Servicepattern;
import com.project.studiopatyzinha.pattern.SwitchPlus;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class Servicos extends AppCompatActivity {
    static Toolbar toolbar_servicos;
    static FloatingActionButton buttonfab_addservico;
    static ArrayList<Servicepattern> servicos;
    static ServicosAdapterAgendar servicesAdapter;
    static RecyclerView rv_servicos;
    public static Uri filepath;
    public static ActivityResultLauncher<String> mGetContent;
    static ImageView pickup_image_add_service;
    static RecyclerView rv_days_add_service;
    static AlertDialog a;
    static SwitchPlus haspromotion;
    static ConstraintLayout constraintLayoutpromocional;
    public static TextView posdiasvalorespromocionais;
    static funcAdapter adapter;
    static ArrayList<Accountpattern> funcs;
    static ArrayList<Accountpattern> funcsselected;
    static ArrayList<String> funcsnomes;
    static ArrayAdapter<String> adapterstring;
    public static String funcsnomesworks;
    static FirebaseFirestore db;
    String id;
    boolean segundacheckagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
        posdiasvalorespromocionais = new TextView(getBaseContext());
        toolbar_servicos = findViewById(R.id.toolbar_servicos);
        buttonfab_addservico = findViewById(R.id.buttonfab_addservico);
        rv_servicos = findViewById(R.id.rv_servicos);
        this.setSupportActionBar(toolbar_servicos);
        this.getSupportActionBar().setTitle("");
        toolbar_servicos.setTitle("Serviços");
        toolbar_servicos.setTitleTextColor(Color.WHITE);
        toolbar_servicos.setBackground(AppCompatResources.getDrawable(getBaseContext(), primaria));
        toolbar_servicos.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbar_servicos.setNavigationOnClickListener(v -> finish());
        servicos = new ArrayList<>();
        servicesAdapter = new ServicosAdapterAgendar(Servicos.this, servicos, true, "servicos");
        rv_servicos.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_servicos.setAdapter(servicesAdapter);

        db = FirebaseFirestore.getInstance();


        id = getIntent().getStringExtra("id");
        if (id == null) {
            segundacheckagem = true;
            id = Login.pessoa.getId();
        }
        db.collection("Servicos").get().addOnSuccessListener(snapshots -> {
            snapshots.forEach(snapshot -> {
                Map<String, Object> ss = snapshot.getData();
                Servicepattern servico = new Servicepattern();
                servico.setDescricao(ss.get("descricao") + "");
                servico.setDiaspromocao(ss.get("diaspromocao") + "");
                servico.setFt(ss.get("ft") + "");
                servico.setFuncs(ss.get("funcs") + "");
                servico.setId(ss.get("id") + "");
                servico.setIfpromocao(ss.get("ifpromocao") + "");
                servico.setNome(ss.get("nome") + "");
                servico.setSelected("");
                servico.setTempo(ss.get("tempo") + "");
                servico.setValor(ss.get("valor") + "");
                servico.setValorp(ss.get("valorp") + "");
                if (!segundacheckagem && !servico.getFuncs().isEmpty()) {
                    segundacheckagem = false;

                }
                if (servico.getFuncs().isEmpty() || segundacheckagem) {
                    servicos.add(servico);
                    servicesAdapter.notifyDataSetChanged();
                }
            });
        });
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(Servicos.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });

        buttonfab_addservico.setOnClickListener(v -> {
            funcsnomesworks = "";

            AlertDialog.Builder builder = new AlertDialog.Builder(Servicos.this);
            View view4 = LayoutInflater.from(Servicos.this).inflate(R.layout.add_servico, null, false);
            ImageView closedialog_service = view4.findViewById(R.id.closedialog_service);
            haspromotion = view4.findViewById(R.id.haspromotion);
            Button bntsalvarservice = view4.findViewById(R.id.bntsalvarservice);
            ConstraintLayout p_allfuncswontdo = view4.findViewById(R.id.p_allfuncswontdo);
            ImageView add_func_spinner_addservico = view4.findViewById(R.id.add_func_spinner_addservico);
            Spinner spinner_func_nome_addservico = view4.findViewById(R.id.spinner_func_nome_addservico);
            RecyclerView rv_funcs_addservico = view4.findViewById(R.id.rv_funcs_addservico);
            pickup_image_add_service = view4.findViewById(R.id.pickup_image_add_service);
            pickup_image_add_service.setOnClickListener(v1 -> mGetContent.launch("image/*"));
            funcs = new ArrayList<>();
            funcsselected = new ArrayList<>();
            funcsnomes = new ArrayList<>();
            adapter = new funcAdapter(funcsselected, "");
            rv_funcs_addservico.setAdapter(adapter);
            rv_funcs_addservico.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            db.collection("Contas").whereNotEqualTo("cargo", "usuario").get().addOnSuccessListener(snapshot -> {
                snapshot.forEach(snapshot1 -> {
                    Map<String, Object> map = snapshot1.getData();
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
                    funcsnomes.add(pessoa.getNome());
                    adapterstring.notifyDataSetChanged();
                });

            });
            adapterstring = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, funcsnomes);
            spinner_func_nome_addservico.setAdapter(adapterstring);
            add_func_spinner_addservico.setOnClickListener(v1 -> {
                boolean adicionar = true;
                for (int i = 0; i < funcsselected.size(); i++) {
                    if (funcsselected.get(i).getId().equals(funcs.get(spinner_func_nome_addservico.getSelectedItemPosition()).getId())) {
                        adicionar = false;
                    }
                }
                if (adicionar) {
                    funcsselected.add(funcs.get(spinner_func_nome_addservico.getSelectedItemPosition()));
                    funcsnomesworks += funcs.get(spinner_func_nome_addservico.getSelectedItemPosition()).getId() + ";";
                    adapter.notifyDataSetChanged();
                }

            });
            SwitchPlus allfuncswdo = view4.findViewById(R.id.allfuncswdo);
            allfuncswdo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    p_allfuncswontdo.setVisibility(View.VISIBLE);
                } else {
                    posdiasvalorespromocionais.setText("");
                    p_allfuncswontdo.setVisibility(View.GONE);
                }
            });
            TextInputLayout nomeETL = view4.findViewById(R.id.edt1);
            TextInputLayout tempoETL = view4.findViewById(R.id.edt2);
            TextInputLayout idETL = view4.findViewById(R.id.edt10);
            idETL.getEditText().setText("" + System.currentTimeMillis());
            TextInputLayout valorETL = view4.findViewById(R.id.edt3);
            TextInputLayout valorpETL = view4.findViewById(R.id.edt4);
            TextInputLayout descETL = view4.findViewById(R.id.edt5);
            SimpleMaskFormatter rg = new SimpleMaskFormatter("NN:NN");
            MaskTextWatcher mtwrg = new MaskTextWatcher(tempoETL.getEditText(), rg);
            tempoETL.getEditText().addTextChangedListener(mtwrg);
            valorpETL.getEditText().setText("R$ 0,00");
            valorpETL.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                private String current = "";

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(current)) {
                        Locale myLocale = new Locale("pt", "BR");
                        valorpETL.getEditText().removeTextChangedListener(this);
                        String cleanString = s.toString().replaceAll("[R$,. ]", "");
                        float parsed = Float.parseFloat(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                        current = formatted;
                        valorpETL.getEditText().setText(formatted);
                        valorpETL.getEditText().setSelection(formatted.length());
                        valorpETL.getEditText().addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            valorETL.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                private String current = "";

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(current)) {
                        Locale myLocale = new Locale("pt", "BR");
                        valorETL.getEditText().removeTextChangedListener(this);
                        String cleanString = s.toString().replaceAll("[R$,. ]", "");
                        float parsed = Float.parseFloat(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                        current = formatted;
                        valorETL.getEditText().setText(formatted);
                        valorETL.getEditText().setSelection(formatted.length());
                        valorETL.getEditText().addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            bntsalvarservice.setOnClickListener(view2 -> {
                boolean mycontinuar = true;
                if (idETL.getEditText().getText().toString().trim().isEmpty()) {
                    idETL.setError("Preenca esse campo");
                    mycontinuar = false;
                }
                if (nomeETL.getEditText().getText().toString().trim().isEmpty()) {
                    nomeETL.setError("Preenca esse campo");
                    mycontinuar = false;
                }
                if (valorETL.getEditText().getText().toString().trim().isEmpty()) {
                    valorETL.setError("Preenca esse campo");
                    mycontinuar = false;
                }
                if (tempoETL.getEditText().getText().toString().trim().isEmpty()) {
                    tempoETL.setError("Preenca esse campo");
                    mycontinuar = false;
                }
                if (filepath == null) {
                    Toast.makeText(this, "Escolha uma foto para este serviço", Toast.LENGTH_SHORT).show();
                    mycontinuar = false;
                }
                if (haspromotion.isChecked()) {
                    if (valorpETL.getEditText().getText().toString().trim().isEmpty()) {
                        valorpETL.setError("Preenca esse campo");
                        mycontinuar = false;
                    }
                }
                if (mycontinuar) {
                    ProgressDialog progressDialog = new ProgressDialog(Servicos.this);
                    progressDialog.setTitle("Salvando serviço");
                    progressDialog.show();
                    Servicepattern servico = new Servicepattern();
                    servico.setNome(nomeETL.getEditText().getText().toString().trim());
                    servico.setTempo(tempoETL.getEditText().getText().toString().trim());
                    servico.setValor(valorETL.getEditText().getText().toString().trim());
                    servico.setValorp(valorpETL.getEditText().getText().toString().trim());
                    servico.setDiaspromocao(posdiasvalorespromocionais.getText().toString());
                    posdiasvalorespromocionais.setText("");
                    servico.setId(idETL.getEditText().getText().toString().trim());
                    servico.setDescricao(descETL.getEditText().getText().toString().trim());
                    servico.setIfpromocao(haspromotion.isChecked() + "");
                    servico.setFuncs(funcsnomesworks);
                    servico.setSelected("");
                    StorageReference firebase = FirebaseStorage.getInstance("gs://mayla-a286b.appspot.com").getReference().child("imagens servico").child(idETL.getEditText().getText().toString().trim());
                    firebase.putFile(filepath).addOnSuccessListener(command -> {
                        firebase.getDownloadUrl().addOnSuccessListener(uri -> {
                            servico.setFt(uri + "");
                            db.collection("Servicos").document(idETL.getEditText().getText().toString().trim()).set(servico);
                            filepath = null;
                            a.dismiss();
                            servicos.add(servico);
                            servicesAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        });
                    }).addOnProgressListener(command -> {
                        double progressPercent = (100.00 * command.getBytesTransferred() / command.getTotalByteCount());
                        progressDialog.setMessage(progressPercent + "  %");
                    });
                }


            });


            closedialog_service.setOnClickListener(view2 -> {
                a.dismiss();
            });
            rv_days_add_service = view4.findViewById(R.id.rv_days_add_service);
            ArrayList<Dayitempatternhorario> list = new ArrayList<>();
            ArrayList<String> semana = new ArrayList<>();
            semana.add("D");
            semana.add("S");
            semana.add("T");
            semana.add("Q");
            semana.add("Q");
            semana.add("S");
            semana.add("S");
            for (int i = 0; i < 7; i++) {
                list.add(new Dayitempatternhorario(semana.get(i), "", "", "", "", "", ""));
            }
            day_add_service_adapter adapterdays = new day_add_service_adapter(list);
            rv_days_add_service.setAdapter(adapterdays);
            rv_days_add_service.setLayoutManager(new LinearLayoutManager(Servicos.this, LinearLayoutManager.HORIZONTAL, false));
            constraintLayoutpromocional = view4.findViewById(R.id.constraintLayoutpromocional);
            haspromotion.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    constraintLayoutpromocional.setVisibility(View.VISIBLE);
                } else {
                    constraintLayoutpromocional.setVisibility(View.GONE);
                }
            });
            builder.setView(view4);
            a = builder.create();
            a.show();
        });
    }

    public static void editarservico(Servicepattern servico, Context context) {


        funcsnomesworks = servico.getFuncs();
        posdiasvalorespromocionais.setText(servico.getDiaspromocao());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view4 = LayoutInflater.from(context).inflate(R.layout.add_servico, null, false);
        ImageView closedialog_service = view4.findViewById(R.id.closedialog_service);
        haspromotion = view4.findViewById(R.id.haspromotion);
        Button bntsalvarservice = view4.findViewById(R.id.bntsalvarservice);
        ConstraintLayout p_allfuncswontdo = view4.findViewById(R.id.p_allfuncswontdo);
        ImageView add_func_spinner_addservico = view4.findViewById(R.id.add_func_spinner_addservico);
        Spinner spinner_func_nome_addservico = view4.findViewById(R.id.spinner_func_nome_addservico);
        RecyclerView rv_funcs_addservico = view4.findViewById(R.id.rv_funcs_addservico);
        pickup_image_add_service = view4.findViewById(R.id.pickup_image_add_service);
        Glide.with(context).load(servico.getFt()).into(pickup_image_add_service);
        pickup_image_add_service.setOnClickListener(v1 -> mGetContent.launch("image/*"));
        funcs = new ArrayList<>();
        funcsselected = new ArrayList<>();
        funcsnomes = new ArrayList<>();
        adapter = new funcAdapter(funcsselected, "");
        rv_funcs_addservico.setAdapter(adapter);
        rv_funcs_addservico.setLayoutManager(new LinearLayoutManager(context));
        db.collection("Contas").whereNotEqualTo("cargo", "usuario").get().addOnSuccessListener(snapshot -> {
            snapshot.forEach(snapshot1 -> {
                Map<String, Object> map = snapshot1.getData();
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
                funcsnomes.add(pessoa.getNome());
                adapterstring.notifyDataSetChanged();
            });

        });
        adapterstring = new ArrayAdapter<>(context, R.layout.item_spinner, funcsnomes);
        spinner_func_nome_addservico.setAdapter(adapterstring);
        add_func_spinner_addservico.setOnClickListener(v1 -> {
            boolean adicionar = true;
            for (int i = 0; i < funcsselected.size(); i++) {
                if (funcsselected.get(i).getId().equals(funcs.get(spinner_func_nome_addservico.getSelectedItemPosition()).getId())) {
                    adicionar = false;
                }
            }
            if (adicionar) {
                funcsselected.add(funcs.get(spinner_func_nome_addservico.getSelectedItemPosition()));
                funcsnomesworks += funcs.get(spinner_func_nome_addservico.getSelectedItemPosition()).getId() + ";";
                adapter.notifyDataSetChanged();
            }

        });
        SwitchPlus allfuncswdo = view4.findViewById(R.id.allfuncswdo);
        allfuncswdo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                p_allfuncswontdo.setVisibility(View.VISIBLE);
            } else {
                p_allfuncswontdo.setVisibility(View.GONE);
            }
        });
        TextInputLayout nomeETL = view4.findViewById(R.id.edt1);
        TextInputLayout tempoETL = view4.findViewById(R.id.edt2);
        TextInputLayout idETL = view4.findViewById(R.id.edt10);

        TextInputLayout valorETL = view4.findViewById(R.id.edt3);
        TextInputLayout valorpETL = view4.findViewById(R.id.edt4);
        TextInputLayout descETL = view4.findViewById(R.id.edt5);
        SimpleMaskFormatter rg = new SimpleMaskFormatter("NN:NN");
        MaskTextWatcher mtwrg = new MaskTextWatcher(tempoETL.getEditText(), rg);
        tempoETL.getEditText().addTextChangedListener(mtwrg);
        valorpETL.getEditText().setText("R$ 0,00");
        valorpETL.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    valorpETL.getEditText().removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,. ]", "");
                    float parsed = Float.parseFloat(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    valorpETL.getEditText().setText(formatted);
                    valorpETL.getEditText().setSelection(formatted.length());
                    valorpETL.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        valorETL.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    valorETL.getEditText().removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,. ]", "");
                    float parsed = Float.parseFloat(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    valorETL.getEditText().setText(formatted);
                    valorETL.getEditText().setSelection(formatted.length());
                    valorETL.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        bntsalvarservice.setText("Atualizar serviço");
        bntsalvarservice.setOnClickListener(view2 -> {

            boolean mycontinuar = true;
            if (idETL.getEditText().getText().toString().trim().isEmpty()) {
                idETL.setError("Preenca esse campo");
                mycontinuar = false;
            }
            if (nomeETL.getEditText().getText().toString().trim().isEmpty()) {
                nomeETL.setError("Preenca esse campo");
                mycontinuar = false;
            }
            if (valorETL.getEditText().getText().toString().trim().isEmpty()) {
                valorETL.setError("Preenca esse campo");
                mycontinuar = false;
            }
            if (tempoETL.getEditText().getText().toString().trim().isEmpty()) {
                tempoETL.setError("Preenca esse campo");
                mycontinuar = false;
            }
            if (haspromotion.isChecked()) {
                if (valorpETL.getEditText().getText().toString().trim().isEmpty()) {
                    valorpETL.setError("Preenca esse campo");
                    mycontinuar = false;
                }
            }

            if (mycontinuar) {
                ProgressDialog progressDialog = new ProgressDialog(context);
                Servicepattern servicon = new Servicepattern();
                servicon.setNome(nomeETL.getEditText().getText().toString().trim());
                servicon.setTempo(tempoETL.getEditText().getText().toString().trim());
                servicon.setValor(valorETL.getEditText().getText().toString().trim());
                servicon.setValorp(valorpETL.getEditText().getText().toString().trim());
                servicon.setDiaspromocao(posdiasvalorespromocionais.getText().toString());
                posdiasvalorespromocionais.setText("");
                servicon.setId(idETL.getEditText().getText().toString().trim());
                servicon.setDescricao(descETL.getEditText().getText().toString().trim());
                servicon.setIfpromocao(haspromotion.isChecked() + "");
                servicon.setFuncs(funcsnomesworks);
                servicon.setSelected("");
                if (filepath != null) {
                    progressDialog.setTitle("Editando serviço");
                    progressDialog.show();
                    StorageReference firebase = FirebaseStorage.getInstance("gs://mayla-a286b.appspot.com").getReference().child("imagens servico").child(idETL.getEditText().getText().toString().trim());
                    firebase.putFile(filepath).addOnSuccessListener(command -> {
                        firebase.getDownloadUrl().addOnSuccessListener(uri -> {
                            servicon.setFt(uri + "");
                            db.collection("Servicos").document(idETL.getEditText().getText().toString().trim()).set(servicon);
                            filepath = null;
                            a.dismiss();
                            servicos.remove(servico);
                            servicos.add(servicon);
                            servicesAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        });
                    }).addOnProgressListener(command -> {
                        double progressPercent = (100.00 * command.getBytesTransferred() / command.getTotalByteCount());
                        progressDialog.setMessage(progressPercent + "  %");
                    });
                } else {
                    progressDialog.setTitle("Editando serviço");
                    progressDialog.show();
                    servicon.setFt(servico.getFt());
                    db.collection("Servicos").document(idETL.getEditText().getText().toString().trim()).set(servicon).addOnSuccessListener(command -> {
                        progressDialog.dismiss();
                        filepath = null;
                        a.dismiss();
                        servicos.remove(servico);
                        servicos.add(servicon);
                        servicesAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    });

                }

//                    bancolocal.getINSTANCE(getContext()).addService(
//                            new Servicepattern("", nomeETL.getEditText().getText().toString()
//                                    , tempoETL.getEditText().getText().toString(),
//                                    valorETL.getEditText().getText().toString(),
//                                    haspromotion.isChecked() + "",
//                                    valorpETL.getEditText().getText().toString(),
//                                    posdiasvalorespromocionais + "", descETL.getEditText().getText().toString()));
//                    servicos.clear();
//                    servicos.addAll(bancolocal.getINSTANCE(getContext()).getServices());
//                    servicesAdapter.notifyDataSetChanged();
//                    a.dismiss();
            }


        });
        closedialog_service.setOnClickListener(view2 -> {
            a.dismiss();
        });
        rv_days_add_service = view4.findViewById(R.id.rv_days_add_service);
        ArrayList<Dayitempatternhorario> list = new ArrayList<>();
        ArrayList<String> semana = new ArrayList<>();
        semana.add("D");
        semana.add("S");
        semana.add("T");
        semana.add("Q");
        semana.add("Q");
        semana.add("S");
        semana.add("S");
        for (int i = 0; i < 7; i++) {
            list.add(new Dayitempatternhorario(semana.get(i), "", "", "", "", "", ""));
        }
        day_add_service_adapter adapterdays = new day_add_service_adapter(list);
        rv_days_add_service.setAdapter(adapterdays);
        rv_days_add_service.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        constraintLayoutpromocional = view4.findViewById(R.id.constraintLayoutpromocional);
        haspromotion.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                constraintLayoutpromocional.setVisibility(View.VISIBLE);
            } else {
                constraintLayoutpromocional.setVisibility(View.GONE);
            }
        });
        builder.setView(view4);
        a = builder.create();
        a.show();

        idETL.getEditText().setText(servico.getId());
        idETL.setEnabled(false);
        nomeETL.getEditText().setText(servico.getNome());
        tempoETL.getEditText().setText(servico.getTempo());
        valorETL.getEditText().setText(servico.getValor());
        valorpETL.getEditText().setText(servico.getValorp());
        descETL.getEditText().setText(servico.getDesccricao());
        haspromotion.setChecked(Boolean.parseBoolean(servico.getIfpromocao()));
        if (servico.getDiaspromocao().length() > 0) {
            list.clear();
            for (int i = 0; i < 7; i++) {
                if (servico.getDiaspromocao().contains(i + "")) {
                    list.add(new Dayitempatternhorario(semana.get(i), "s", "", "", "", "", ""));
                } else {
                    list.add(new Dayitempatternhorario(semana.get(i), "", "", "", "", "", ""));
                }
            }
            adapterdays.notifyDataSetChanged();
        }
        if (!servico.getFuncs().isEmpty()) {
            allfuncswdo.setChecked(true);
            String[] funcscode = servico.getFuncs().split(";");
            for (int i = 0; i < funcscode.length; i++) {
                db.collection("Contas").whereEqualTo("id", funcscode[i]).get().addOnSuccessListener(snapshot -> {
                    snapshot.forEach(snapshot1 -> {
                        Map<String, Object> map = snapshot1.getData();
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
                        funcsselected.add(pessoa);
                        adapter.notifyDataSetChanged();
                        funcsnomes.add(pessoa.getNome());
                        adapterstring.notifyDataSetChanged();
                    });

                });
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            Uri resultUri = null;
            if (result != null) {
                resultUri = Uri.parse(result);
            }
            filepath = resultUri;
            pickup_image_add_service.setImageURI(resultUri);
        }
    }
}