package com.project.studiopatyzinha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.studiopatyzinha.Adapter.MaindayadapterSimplyed;
import com.project.studiopatyzinha.Adapter.funcAdapter;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Dayitempattern;
import com.project.studiopatyzinha.pattern.SwitchPlus;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Subordinados extends AppCompatActivity {
    Toolbar toolbar_subordinados;
    RecyclerView rv_subordinados;
    funcAdapter adapter;
    ArrayList<Accountpattern> funcs;
    public static AlertDialog dialog, alertchoosinghora;
    static Context context;
    static ArrayAdapter<String> adapterarray;
    static Map<String, Object> diasfunc;
    public static TextView diaselecionado;
    public static Map<String, Object> rotina;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static Map<String, Object> dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinados);
        context = Subordinados.this;
        toolbar_subordinados = findViewById(R.id.toolbar_subordinados);
        rv_subordinados = findViewById(R.id.rv_subordinados);
        this.setSupportActionBar(toolbar_subordinados);
        this.getSupportActionBar().setTitle("");
        toolbar_subordinados.setTitle("Sobordinados");
        toolbar_subordinados.setTitleTextColor(Color.WHITE);
        toolbar_subordinados.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbar_subordinados.setNavigationContentDescription("Voltar para Tendências");
        toolbar_subordinados.setNavigationOnClickListener(v -> {
            finish();
        });
        funcs = new ArrayList<>();

        adapter = new funcAdapter(funcs, "sub");

        chamarfunc(Login.pessoa.getId());
        rv_subordinados.setAdapter(adapter);
        rv_subordinados.setLayoutManager(new LinearLayoutManager(getBaseContext()));


    }

    private void chamarfunc(String id) {
        db.collection("Contas")
                .whereEqualTo("id", id).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot a = task.getResult();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : a) {
                            Map<String, Object> map = (Map<String, Object>) queryDocumentSnapshot.getData();
                            new Thread(() -> {
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

                                String[] grupo = pessoa.getGrupoAbaixo().split(";");
                                for (int i = 0; i < grupo.length; i++) {
                                    db.collection("Contas")
                                            .whereEqualTo("id", grupo[i]).get().addOnCompleteListener(task1 -> {
                                                if (task.isSuccessful()) {
                                                    QuerySnapshot a1 = task1.getResult();
                                                    for (QueryDocumentSnapshot queryDocumentSnapshot1 : a1) {
                                                        Map<String, Object> map1 = (Map<String, Object>) queryDocumentSnapshot1.getData();
                                                        Accountpattern pessoa1 = new Accountpattern();
                                                        pessoa1.setFollowersquant(map1.get("followersquant") + "");
                                                        pessoa1.setFrasefilosofica(map1.get("frasefilosofica") + "");
                                                        pessoa1.setHorario((Map<String, Object>) map1.get("horario"));
                                                        pessoa1.setQuantpost(map1.get("quantpost") + "");
                                                        pessoa1.setNome(map1.get("nome") + "");
                                                        pessoa1.setFollowersid((Map<String, Object>) map1.get("followersid"));
                                                        pessoa1.setSenha(map1.get("senha") + "");
                                                        pessoa1.setOpinioes((Map<String, Object>) map1.get("opinioes"));
                                                        pessoa1.setBarbersposts((Map<String, Object>) map1.get("barbersposts"));
                                                        pessoa1.setImgUri(map1.get("imgUri") + "");
                                                        pessoa1.setPercentual(map1.get("percentual") + "");
                                                        pessoa1.setDia_salario(map1.get("dia_salario") + "");
                                                        pessoa1.setId(map1.get("id") + "");
                                                        pessoa1.setCargo(map1.get("cargo") + "");
                                                        pessoa1.setGrupoAbaixo(map1.get("grupoAbaixo") + "");
                                                        pessoa1.setEmail(map1.get("email") + "");
                                                        pessoa1.setSelected(map1.get("selected") + "");
                                                        funcs.add(pessoa1);
                                                        runOnUiThread(() -> {
                                                            adapter.notifyDataSetChanged();
                                                        });
                                                    }
                                                }
                                            });
                                }
                            }).start();
                        }
                    }
                });

    }

    public static void verFunc(String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustomfull);
        View view = LayoutInflater.from(context).inflate(R.layout.acompanhar_sub, null, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar_acompanhar_sub);
        toolbar.setTitle("Acompanhar os");
        adapterarray = new ArrayAdapter<>(context, R.layout.item_spinner);
        toolbar.setNavigationIcon(R.drawable.ic_close_30);
        toolbar.setNavigationOnClickListener(v -> dialog.dismiss());
        toolbar.setTitleTextColor(Color.WHITE);
        view.findViewById(R.id.agend_acompanhar_sub).setOnClickListener(v -> {
            Intent intent = new Intent(context, AgendamentosAdmin.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        });
        view.findViewById(R.id.horario_acompanhar_sub).setOnClickListener(v -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(context, R.style.AlertDialogCustomfull);
            View view22 = LayoutInflater.from(context).inflate(R.layout.activity_rotina, null, false);
            ImageView savealldays = view22.findViewById(R.id.savealldays);
            savealldays.setVisibility(View.GONE);
            view22.findViewById(R.id.cardView5).setVisibility(View.VISIBLE);
            RecyclerView rv_rotina = view22.findViewById(R.id.rv_rotina);
            Button salvar_dia = view22.findViewById(R.id.salvar_dia);
            salvar_dia.setVisibility(View.GONE);
            SwitchPlus iffolga = view22.findViewById(R.id.iffolga);
            iffolga.setEnabled(false);
            Spinner horacomecospinner = view22.findViewById(R.id.horacomecospinner);
            Spinner horacomecoalmoco = view22.findViewById(R.id.horacomecoalmoco);
            Spinner horafimalmoco = view22.findViewById(R.id.horafimalmoco);
            Spinner horafimspinner = view22.findViewById(R.id.horafimspinner);
            diasfunc = new HashMap<>();
            diaselecionado = new TextView(context);
            Toolbar toolbar2 = view22.findViewById(R.id.toolbar_rotina);
            toolbar2.setNavigationOnClickListener(v2 -> alertchoosinghora.dismiss());
            rv_rotina.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ArrayList<Dayitempattern> dias = new ArrayList<>();
            MaindayadapterSimplyed adapter22 = new MaindayadapterSimplyed(dias, "true");
            rv_rotina.setAdapter(adapter22);
            for (int i = 0; i < MainActivity.semana.size(); i++) {
                if (i != 0) {
                    dias.add(new Dayitempattern(MainActivity.semana.get(i).toUpperCase(), "", "", "", "", ""));
                } else {
                    dias.add(new Dayitempattern(MainActivity.semana.get(i).toUpperCase(), "", "s", "", "", ""));
                }
            }
            db.collection("Rotina").get().addOnSuccessListener(snapshot -> {
                diaselecionado.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapterarray.clear();
                        dia = (Map<String, Object>) rotina.get(s + "");

                        String horaabertura = (String) dia.get("abreas");
                        String ifchecado = (String) dia.get("iffechado");
                        String hora = (String) dia.get("fechaas");
                        hora = hora.replace(":00", "");
                        horaabertura = horaabertura.replace(":00", "");
                        for (int i = Integer.parseInt(horaabertura); i <= Integer.parseInt(hora); i++) {
                            String horacerta = "";
                            if (i < 10) {
                                horacerta += "0";
                            }
                            horacerta += i + ":00";
                            adapterarray.add(horacerta);
                            adapterarray.notifyDataSetChanged();
                        }
                        boolean sss = !Boolean.parseBoolean(ifchecado);
                        horafimspinner.setEnabled(sss);
                        horacomecospinner.setEnabled(sss);
                        horacomecoalmoco.setEnabled(sss);
                        horafimalmoco.setEnabled(sss);
                        iffolga.setChecked(!sss);

                        if (diasfunc.get(s + "") != null) {
                            Map<String, Object> diaatual = (Map<String, Object>) diasfunc.get(s + "");
                            iffolga.setChecked(Boolean.parseBoolean(diaatual.get("iffolga") + ""));
                            horacomecospinner.setSelection(adapterarray.getPosition(diaatual.get("entranotrabalhoas") + ""));
                            horacomecoalmoco.setSelection(adapterarray.getPosition(diaatual.get("almococomeco") + ""));
                            horafimalmoco.setSelection(adapterarray.getPosition(diaatual.get("almocofim") + ""));
                            horafimspinner.setSelection(adapterarray.getPosition(diaatual.get("saidotrabalhoas") + ""));
                        } else {
                            horacomecospinner.setSelection(0);
                            horacomecoalmoco.setSelection(adapterarray.getCount() / 2);
                            horafimspinner.setSelection(horafimspinner.getCount());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                db.collection("Contas").whereEqualTo("id", id).get().addOnSuccessListener(snapshot2 -> {
                    snapshot2.forEach(snapshot1 -> {
                        diasfunc = (Map<String, Object>) snapshot1.getData().get("horario");

                    });
                }).addOnSuccessListener(command -> {
                    Subordinados.diaselecionado.setText(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());

                });
                snapshot.forEach(snapshot1 -> {
                    rotina = (Map<String, Object>) snapshot1.getData();
                });
            });


            horacomecospinner.setAdapter(adapterarray);
            horafimspinner.setAdapter(adapterarray);
            horacomecoalmoco.setAdapter(adapterarray);
            horafimalmoco.setAdapter(adapterarray);
            horacomecoalmoco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!Boolean.parseBoolean(dia.get("iffechado") + "")) {
                        horafimalmoco.setSelection(position + 1);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            alertchoosinghora = builder2.create();
            if (view22.getParent() != null) {
                ((ViewGroup) view22.getParent()).removeAllViews();
            } else {
                alertchoosinghora.setView(null);
                alertchoosinghora.setView(view22);
            }
            alertchoosinghora.show();
        });


        view.findViewById(R.id.servico_acompanhar_sub).setOnClickListener(v -> {
            Intent intent = new Intent(context, Servicos.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        });
        view.findViewById(R.id.feedback_acompanhar_sub).setOnClickListener(v -> {
            Intent intent = new Intent(context, VerOpinioes.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        });
        builder.setView(view);
        dialog = builder.create();
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        } else {
            dialog.setView(null);
            dialog.setView(view);
        }
        dialog.show();
    }
}
