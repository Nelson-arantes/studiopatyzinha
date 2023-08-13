package com.project.studiopatyzinha.gerenciar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.Adapter.MaindayadapterSimplyed;
import com.project.studiopatyzinha.Adapter.day_add_service_adapter;
import com.project.studiopatyzinha.Adapter.funcAdapter;
import com.project.studiopatyzinha.Broadcast.diaPagamentoBroadCast;
import com.project.studiopatyzinha.CropperActivity;
import com.project.studiopatyzinha.Login;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Dayitempattern;
import com.project.studiopatyzinha.pattern.Dayitempatternhorario;
import com.project.studiopatyzinha.pattern.SwitchPlus;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Funcionarios extends AppCompatActivity {
    Toolbar toolbar_func;
    static funcAdapter adapterfuncs;
    static TextInputLayout oftenPayementETL;
    RecyclerView rv_funcs;
    FloatingActionButton fab_add_func;
    static AlertDialog alert, alertchoosinghora;
    public static ActivityResultLauncher<String> mGetContent;
    public static Uri filepath;
    static ImageView ftUser;
    static String formadepagamento;
    static long milis;
    private static Accountpattern account;
    static Map<String, Object> diasfunc;
    public static TextView diaselecionado;
    Map<String, Object> rotina;
    static ArrayAdapter<String> adapterarray;
    static Spinner horafimspinner;
    static Spinner horacomecospinner, horafimalmoco, horacomecoalmoco;
    static SwitchPlus iffolga;
    static Map<String, Object> dia;
    static ArrayList<String> listanome;
    static ArrayAdapter<String> adapternome;
    public static String grupoabaixo;
    static Context context;
    public static TextView posdiasvalorespromocionais, changeday, changeday2;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static ArrayList<Accountpattern> funcs;
    static ImageView chamarDatePicker;
    static TextView datashow_tv, notificaras_addfunc;
    static LinearLayout semanal, mensal;
    static ConstraintLayout editado;
    static Button mensalbnt, semanalbnt, diariobnt, editadobnt;
    static RecyclerView rv_days_UI;
    static Map<String, Object> horarionotificar;
    static ImageView chamartimerpickerdialgo;
    static int hora, minuto;
    static View view22;
    static Button salvar_dia;
    static RecyclerView rv_rotina;
    static AlertDialog.Builder builder2;
    static ImageView savealldays;
    static TextInputLayout daypaymentEDTL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionarios);
        context = Funcionarios.this;
        posdiasvalorespromocionais = new TextView(this);
        changeday = new TextView(this);
        changeday2 = new TextView(this);
        rv_funcs = findViewById(R.id.rv_funcs);
        toolbar_func = findViewById(R.id.toolbar_func);
        this.setSupportActionBar(toolbar_func);
        adapterarray = new ArrayAdapter<>(this, R.layout.item_spinner);
        this.getSupportActionBar().setTitle("");
        toolbar_func.setTitle("Funcionários");
        toolbar_func.setNavigationIcon(AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back));
        toolbar_func.setNavigationOnClickListener(v -> finish());
        funcs = new ArrayList<>();
        diaselecionado = new TextView(this);
        diasfunc = new HashMap<>();
        changeday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeday(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        changeday2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeday2(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(Funcionarios.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });

        db.collection("Rotina").get().addOnSuccessListener(snapshot -> {
            snapshot.forEach(snapshot1 -> {
                rotina = snapshot1.getData();
                diaselecionado.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapterarray.clear();
                        dia = (Map<String, Object>) rotina.get(s.toString().toUpperCase());
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
                        iffolga.setEnabled(sss);
                        iffolga.setChecked(!sss);
                        if (diasfunc.get(s.toString()) != null) {
                            Map<String, Object> diaatual = (Map<String, Object>) diasfunc.get(s.toString());
                            iffolga.setChecked(Boolean.parseBoolean(diaatual.get("iffolga").toString()));
                            horacomecospinner.setSelection(adapterarray.getPosition(diaatual.get("entranotrabalhoas").toString()));
                            horacomecoalmoco.setSelection(adapterarray.getPosition(diaatual.get("almococomeco").toString()));
                            horafimalmoco.setSelection(adapterarray.getPosition(diaatual.get("almocofim").toString()));
                            horafimspinner.setSelection(adapterarray.getPosition(diaatual.get("saidotrabalhoas").toString()));
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
            });

        });

        db.collection("Contas").whereNotEqualTo("cargo", "usuario").get().addOnSuccessListener(snapshot -> {
            snapshot.forEach(snapshot1 -> {
                Map<String, Object> map = (Map<String, Object>) snapshot1.getData();
                Accountpattern pessoa = new Accountpattern();
                pessoa.setFollowersquant(map.get("followersquant").toString());
                pessoa.setFrasefilosofica(map.get("frasefilosofica").toString());
                pessoa.setHorario((Map<String, Object>) map.get("horario"));
                pessoa.setQuantpost(map.get("quantpost").toString());
                pessoa.setNome(map.get("nome").toString());
                pessoa.setFollowersid((Map<String, Object>) map.get("followersid"));
                pessoa.setIfgerente(map.get("ifgerente").toString());
                pessoa.setSenha(map.get("senha").toString());
                pessoa.setOpinioes((Map<String, Object>) map.get("opinioes"));
                pessoa.setBarbersposts((Map<String, Object>) map.get("barbersposts"));
                pessoa.setImgUri(map.get("imgUri").toString());
                pessoa.setPercentual(map.get("percentual").toString());
                pessoa.setDia_salario(map.get("dia_salario").toString());
                pessoa.setFormapagamento(map.get("formapagamento").toString());
                pessoa.setId(map.get("id").toString());
                pessoa.setCargo(map.get("cargo").toString());
                pessoa.setGrupoAbaixo(map.get("grupoAbaixo").toString());
                pessoa.setEmail(map.get("email").toString());
                pessoa.setSelected(map.get("selected").toString());
                pessoa.setHorasdiaspagamentos((Map<String, Object>) map.get("horasdiaspagamentos"));
                pessoa.setUltimopagamento(map.get("ultimopagamento").toString());
                funcs.add(pessoa);
                adapterfuncs.notifyDataSetChanged();
            });
        });
        adapterfuncs = new funcAdapter(funcs, "velho");
        rv_funcs.setAdapter(adapterfuncs);
        rv_funcs.setLayoutManager(new LinearLayoutManager(this));
        fab_add_func = findViewById(R.id.fab_add_func);
        builder2 = new AlertDialog.Builder(context, R.style.AlertDialogCustomfull);
        view22 = LayoutInflater.from(context).inflate(R.layout.activity_rotina, null, false);
        savealldays = view22.findViewById(R.id.savealldays);
        view22.findViewById(R.id.cardView5).setVisibility(View.VISIBLE);
        rv_rotina = view22.findViewById(R.id.rv_rotina);
        salvar_dia = view22.findViewById(R.id.salvar_dia);
        iffolga = view22.findViewById(R.id.iffolga);
        horacomecospinner = view22.findViewById(R.id.horacomecospinner);
        horacomecoalmoco = view22.findViewById(R.id.horacomecoalmoco);
        horafimalmoco = view22.findViewById(R.id.horafimalmoco);
        horafimspinner = view22.findViewById(R.id.horafimspinner);
        fab_add_func.setOnClickListener(v -> {
            Accountpattern func = new Accountpattern();
            func.setFormapagamento("semanal");
            Map<String, Object> map = new HashMap<>();
            func.setHorasdiaspagamentos(map);
            func.setDia_salario("");
            func.setGrupoAbaixo("");
            func.setImgUri("");
            func.setFrasefilosofica("");
            func.setUltimopagamento("");
            func.setSenha("");
            func.setEmail("");
            chamarfunc(func, 0);

        });
    }

    public static void editarfunc(Accountpattern func, int pos) {
        chamarfunc(func, pos);
    }

    private static void chamarfunc(Accountpattern func, int pos) {
        formadepagamento = func.getFormapagamento();
        horarionotificar = func.getHorasdiaspagamentos();
        ArrayList<Dayitempattern> listadays = new ArrayList<>();
        MaindayadapterSimplyed adapter = new MaindayadapterSimplyed(listadays, "false");
        grupoabaixo = func.getGrupoAbaixo();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustomfull);
        View view2 = LayoutInflater.from(context).inflate(R.layout.add_func, null, false);
        RecyclerView choose_dayPayement = view2.findViewById(R.id.choose_dayPayement);
        notificaras_addfunc = view2.findViewById(R.id.notificaras_addfunc);
        rv_days_UI = view2.findViewById(R.id.rv_days_UI);
        chamartimerpickerdialgo = view2.findViewById(R.id.chamartimerpickerdialgo);
        if (func.getHorasdiaspagamentos() == null || func.getHorasdiaspagamentos().isEmpty()) {
            hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            minuto = Calendar.getInstance().get(Calendar.MINUTE);
        } else {
            hora = Integer.parseInt(String.valueOf(func.getHorasdiaspagamentos().get("DOM")).split(":")[0]);
            minuto = Integer.parseInt(String.valueOf(func.getHorasdiaspagamentos().get("DOM")).split(":")[1]);
        }
        changeday.setText(
                MainActivity.semana.get(0));
        listadays.add(new Dayitempattern(MainActivity.semana.get(0), "", "s", "", "", ""));
        for (int i = 1; i < MainActivity.semana.size(); i++) {
            listadays.add(new Dayitempattern(MainActivity.semana.get(i), "", "", "", "", ""));
        }
        chamartimerpickerdialgo.setOnClickListener(v12 -> {
            TimePickerDialog dialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
                hora = hourOfDay;
                minuto = minute;
                horarionotificar.put(diaselecionado.getText().toString().toUpperCase(), c(hora) + ":" + c(minuto));
                notificaras_addfunc.setText("Notificar as: " + c(hora) + ":" + c(minuto));
            }, hora, minuto, true);
            dialog.show();
        });
        rv_days_UI.setAdapter(adapter);


        rv_days_UI.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        choose_dayPayement.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        ArrayList<Dayitempatternhorario> list = new ArrayList<>();
        ArrayList<String> semana = new ArrayList<>();
        semana.add("D");
        semana.add("S");
        semana.add("T");
        semana.add("Q");
        semana.add("Q");
        semana.add("S");
        semana.add("S");
        if (func.getDia_salario() != null) {
            for (int i = 0; i < 7; i++) {
                if (func.getDia_salario().contains(i + "")) {
                    list.add(new Dayitempatternhorario(semana.get(i), "s", "", "", "", "", ""));

                } else {
                    list.add(new Dayitempatternhorario(semana.get(i), "", "", "", "", "", ""));
                }
            }
        } else {
            for (int i = 0; i < 7; i++) {
                list.add(new Dayitempatternhorario(semana.get(i), "", "", "", "", "", ""));
            }
        }
        day_add_service_adapter adapterdays = new day_add_service_adapter(list);
        choose_dayPayement.setAdapter(adapterdays);
        mensal = view2.findViewById(R.id.parentlayoutpagamentomensal);
        editado = view2.findViewById(R.id.parent_often);
        diariobnt = view2.findViewById(R.id.diariobnt);
        oftenPayementETL = view2.findViewById(R.id.oftenPayementETL);
        semanal = view2.findViewById(R.id.parentlayoutpagamentosemanal);
        semanalbnt = view2.findViewById(R.id.semanalbnt);
        editadobnt = view2.findViewById(R.id.editadobnt);
        mensalbnt = view2.findViewById(R.id.mensalbnt);
        diariobnt.setOnClickListener(v1 -> {
            semanal.setVisibility(View.GONE);
            mensal.setVisibility(View.GONE);
            editado.setVisibility(View.GONE);
            formadepagamento = "diario";
        });
        mensalbnt.setOnClickListener(v1 -> {
            semanal.setVisibility(View.GONE);
            mensal.setVisibility(View.VISIBLE);
            editado.setVisibility(View.GONE);
            formadepagamento = "mensal";
        });
        semanalbnt.setOnClickListener(v1 -> {
            semanal.setVisibility(View.VISIBLE);
            formadepagamento = "semanal";
            mensal.setVisibility(View.GONE);
            editado.setVisibility(View.GONE);
        });
        editadobnt.setOnClickListener(v2 -> {
            semanal.setVisibility(View.GONE);
            mensal.setVisibility(View.GONE);
            editado.setVisibility(View.VISIBLE);
            formadepagamento = "editado";
        });

        chamarDatePicker = view2.findViewById(R.id.chamarDatePicker);
        datashow_tv = view2.findViewById(R.id.datashow_tv);
        datashow_tv.setText("Data de início: " + c(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "/" + c(Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR));
        chamarDatePicker.setOnClickListener(v2 -> {
            String data = datashow_tv.getText().toString().split(" ")[3];
            DatePickerDialog s = new DatePickerDialog(context);
            s.setOnDateSetListener((view, year, month, dayOfMonth) -> datashow_tv.setText("Data de início: " + c(dayOfMonth) + "/" + c(month + 1) + "/" + year));
            s.updateDate(Integer.parseInt(data.split("/")[2]), Integer.parseInt(data.split("/")[1]) - 1, Integer.parseInt(data.split("/")[0]));
            s.show();
        });
        Toolbar toolbar_add_func = view2.findViewById(R.id.toolbar_add_func);
        RecyclerView rv_funcs_abaixo = view2.findViewById(R.id.rv_funcs_abaixo);
        ftUser = view2.findViewById(R.id.ftUser);
        ftUser.setOnClickListener(v1 -> mGetContent.launch("image/*"));
        toolbar_add_func.setNavigationOnClickListener(v1 -> alert.dismiss());
        CheckBox if_gerente = view2.findViewById(R.id.if_gerente);


        Spinner spinner_func_nome = view2.findViewById(R.id.spinner_func_nome);
        ImageView add_func_spinner = view2.findViewById(R.id.add_func_spinner_addservico);
        ArrayList<Accountpattern> lista = new ArrayList<>();
        funcAdapter adapter2 = new funcAdapter(lista, "nova");
        rv_funcs_abaixo.setAdapter(adapter2);
        rv_funcs_abaixo.setLayoutManager(new LinearLayoutManager(context));
        listanome = new ArrayList<>();
        for (int i = 0; i < funcs.size(); i++) {
            if (!funcs.get(i).getId().equals(func.getId())) {
                listanome.add(funcs.get(i).getNome());
            }
        }
        add_func_spinner.setOnClickListener(v1 -> {
            boolean adicionar = true;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(funcs.get(spinner_func_nome.getSelectedItemPosition()).getId())) {
                    adicionar = false;
                }
            }
            if (adicionar) {
                lista.add(funcs.get(spinner_func_nome.getSelectedItemPosition()));
                adapter2.notifyDataSetChanged();
                grupoabaixo += funcs.get(spinner_func_nome.getSelectedItemPosition()).getId() + ";";
            }
        });


        adapternome = new ArrayAdapter<>(context, R.layout.item_spinner, listanome);
        spinner_func_nome.setAdapter(adapternome);
        TextInputLayout nomefuncedt = view2.findViewById(R.id.nomeETL);
        ConstraintLayout subordinadoslayout = view2.findViewById(R.id.p_sub_addfunc);
        if (Login.pessoa.getIfgerente().equals("true")) {
            if_gerente.setChecked(true);
            subordinadoslayout.setVisibility(View.VISIBLE);
        }
        TextInputLayout cargoETL = view2.findViewById(R.id.cargoETL);
        TextInputLayout idETL = view2.findViewById(R.id.idfuncETL);
        TextInputLayout percentETL = view2.findViewById(R.id.percentETL);
        SimpleMaskFormatter percent = new SimpleMaskFormatter("NNN.NN%%");
        MaskTextWatcher mtwpercent = new MaskTextWatcher(percentETL.getEditText(), percent);
        percentETL.getEditText().addTextChangedListener(mtwpercent);
        if_gerente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Login.pessoa.setIfgerente("true");
                subordinadoslayout.setVisibility(View.VISIBLE);
            } else {
                Login.pessoa.setIfgerente("false");
                subordinadoslayout.setVisibility(View.GONE);
            }
        });
        daypaymentEDTL = view2.findViewById(R.id.daypaymentETL);
        Button savarfunc = view2.findViewById(R.id.savarfunc);
        savarfunc.setOnClickListener(v3 -> {
            boolean mycontinuar = true;
            account = new Accountpattern();
            switch (formadepagamento) {
                case "diario":
                    account.setDia_salario("x");
                    break;
                case "semanal":
                    if (posdiasvalorespromocionais.getText().toString().isEmpty() && semanal.getVisibility() == View.VISIBLE) {
                        mycontinuar = false;
                        Toast.makeText(context, "Preencha os dias de pagamento", Toast.LENGTH_SHORT).show();
                    } else {
                        account.setDia_salario(posdiasvalorespromocionais.getText().toString());
                    }
                    break;
                case "mensal":
                    if (daypaymentEDTL.getEditText().getText().toString().trim().isEmpty() && mensal.getVisibility() == View.VISIBLE) {
                        mycontinuar = false;
                        Toast.makeText(context, "Preencha o dia de pagamento", Toast.LENGTH_SHORT).show();
                    } else {
                        account.setDia_salario(daypaymentEDTL.getEditText().getText().toString());
                    }
                    break;
                case "editado":
                    if (oftenPayementETL.getEditText().getText().toString().isEmpty() && editado.getVisibility() == View.VISIBLE) {
                        mycontinuar = false;
                        oftenPayementETL.setError("Preencha o dia de pagamento");
                    } else {
                        account.setDia_salario(oftenPayementETL.getEditText().getText().toString() + "!" + datashow_tv.getText().toString().split(":")[1].trim());
                    }
                    break;
            }
            if (nomefuncedt.getEditText().getText().toString().trim().isEmpty()) {
                mycontinuar = false;
                nomefuncedt.setError("Preencha esse campo");
            }
//                todo list: check if monthly and weekly is filled
//                if (daypaymentEDTL.getEditText().getText().toString().isEmpty() && posdiasvalorespromocionais.getText().toString().isEmpty()) {
//                    mycontinuar = false;
//                    if (mensal.getVisibility() == View.VISIBLE) {
//                        daypaymentEDTL.setError("Preencha esse campo corretamente");
//                    } else {
//                        Toast.makeText(this, "Escolha ao menos um dia para o pagamento do funcionário", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    if (Integer.parseInt(daypaymentEDTL.getEditText().getText().toString()) > 31) {
//                        mycontinuar = false;
//                        daypaymentEDTL.setError("Preencha esse campo corretamente");
//                    }
//                }
            if (idETL.getEditText().getText().toString().isEmpty()) {
                mycontinuar = false;
                idETL.setError("Preencha esse campo corretamente");
            }

            if (horarionotificar.size() < 7) {
                mycontinuar = false;
                Toast.makeText(context, "Preencha o horário das notificações", Toast.LENGTH_SHORT).show();
            }
            if (mycontinuar) {
                String id = idETL.getEditText().getText().toString().trim();
                account.setId(id);
                account.setUltimopagamento(func.getUltimopagamento());
                account.setGrupoAbaixo(grupoabaixo);
                account.setFrasefilosofica(func.getFrasefilosofica());
                account.setNome(nomefuncedt.getEditText().getText().toString().trim());
                account.setPercentual(percentETL.getEditText().getText().toString().trim());
                account.setCargo(cargoETL.getEditText().getText().toString().trim());
                account.setIfgerente(if_gerente.isChecked() + "");
                savealldays.setOnClickListener(v1 -> {
                    if (diasfunc.size() < 7) {
                        String text_error = "Preencha os dias";
                        if (diasfunc.get("SAB") == null) {
                            text_error += " SAB";
                        }
                        if (diasfunc.get("DOM") == null) {
                            text_error += " DOM";
                        }
                        if (diasfunc.get("SEG") == null) {
                            text_error += " SEG";
                        }
                        if (diasfunc.get("TER") == null) {
                            text_error += " TER";
                        }
                        if (diasfunc.get("QUA") == null) {
                            text_error += " QUA";
                        }
                        if (diasfunc.get("QUI") == null) {
                            text_error += " QUI";
                        }
                        if (diasfunc.get("SEX") == null) {
                            text_error += " SEX";
                        }
                        Toast.makeText(context, text_error, Toast.LENGTH_SHORT).show();
                    } else {
                        account.setHorasdiaspagamentos(horarionotificar);
                        account.setHorario(diasfunc);
                        account.setQuantpost("0");
                        Map<String, Object> map2 = new HashMap<>();
                        account.setOpinioes(map2);
                        account.setFollowersid(map2);
                        account.setFollowersquant("0");
                        account.setFormapagamento(formadepagamento);
                        account.setSenha(func.getSenha());
                        account.setSelected("");
                        account.setEmail(func.getEmail());
                        account.setQuantpost("0");
                        account.setBarbersposts(map2);
                        ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("Salvando funcionário");
                        progressDialog.show();
                        if (filepath != null) {
                            StorageReference firebase = FirebaseStorage.getInstance("gs://patricia-53cb0.appspot.com").getReference().child("imagens contas").child(idETL.getEditText().getText().toString().trim());
                            firebase.putFile(filepath).addOnSuccessListener(command -> {
                                firebase.getDownloadUrl().addOnSuccessListener(uri -> {
                                    account.setImgUri(uri.toString());
                                    db.collection("Contas").document(id).set(account).addOnSuccessListener(command2 -> {
                                        if (func.getId() != null) {
                                            funcs.remove(pos);
                                        }
                                        funcs.add(account);
                                        adapterfuncs.notifyDataSetChanged();
                                        alertchoosinghora.dismiss();
                                        alert.dismiss();
                                        progressDialog.dismiss();
                                        filepath = null;
                                        account = null;
                                        Toast.makeText(context, "Funcionário salvo", Toast.LENGTH_SHORT).show();
                                        initdaypaymenteditar(account);
                                    });
                                });
                            }).addOnProgressListener(snapshot -> {
                                int progress = Integer.parseInt((100 * snapshot.getBytesTransferred()) + "") / Integer.parseInt(snapshot.getTotalByteCount() + "");
                                progressDialog.setMessage(progress + "%");
                            });
                        } else {
                            account.setImgUri(func.getImgUri());
                            db.collection("Contas").document(id).set(account).addOnSuccessListener(command2 -> {
                                if (func.getId() != null) {
                                    funcs.remove(pos);
                                }
                                initdaypaymenteditar(account);
                                funcs.add(account);
                                adapterfuncs.notifyDataSetChanged();
                                alertchoosinghora.dismiss();
                                alert.dismiss();
                                progressDialog.dismiss();
                                filepath = null;
                                account = null;
                                Toast.makeText(context, "Funcionário salvo", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
                Toolbar toolbar2 = view22.findViewById(R.id.toolbar_rotina);
                toolbar2.setNavigationOnClickListener(v1 -> alertchoosinghora.dismiss());
                rv_rotina.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                ArrayList<Dayitempattern> dias = new ArrayList<>();
                MaindayadapterSimplyed adapter22 = new MaindayadapterSimplyed(dias, "true");
                rv_rotina.setAdapter(adapter22);
                for (int i = 0; i < MainActivity.semana.size(); i++) {
                    if (!MainActivity.semana.get(i).equals("Dom")) {
                        dias.add(new Dayitempattern(MainActivity.semana.get(i).toUpperCase(), "", "", "", "", ""));
                    } else {
                        dias.add(new Dayitempattern(MainActivity.semana.get(i).toUpperCase(), "", "s", "", "", ""));
                    }
                }
                horacomecospinner.setAdapter(adapterarray);
                horafimspinner.setAdapter(adapterarray);
                horacomecoalmoco.setAdapter(adapterarray);
                horafimalmoco.setAdapter(adapterarray);
                horacomecoalmoco.setSelection(adapterarray.getCount() / 2);

                horacomecoalmoco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!Boolean.parseBoolean(dia.get("iffechado").toString())) {
                            horafimalmoco.setSelection(position + 1);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                horafimalmoco.setSelection(horafimalmoco.getSelectedItemPosition() + 1);

                if (func.getHorario() != null) {
                    Map<String, Object> s = (Map<String, Object>) func.getHorario().get("DOM");
                    iffolga.setChecked(Boolean.parseBoolean(String.valueOf(s.get("iffolga"))));
                    if (iffolga.isChecked()) {
                        horacomecospinner.setSelection(adapterarray.getPosition(String.valueOf(s.get("entranotrabalhoas"))));
                        horacomecoalmoco.setSelection(adapterarray.getPosition(String.valueOf(s.get("almococomeco"))));
                        horafimalmoco.setSelection(adapterarray.getPosition(String.valueOf(s.get("almocofim"))));
                        horafimspinner.setSelection(adapterarray.getPosition(String.valueOf(s.get("saidotrabalhoas"))));
                    }
                    diasfunc.putAll(func.getHorario());
                }
                salvar_dia.setOnClickListener(v1 -> {
                    Map<String, Object> dia = new HashMap<>();
                    dia.put("entranotrabalhoas", horacomecospinner.getSelectedItem());
                    dia.put("iffolga", iffolga.isChecked() + "");
                    dia.put("almococomeco", horacomecoalmoco.getSelectedItem());
                    dia.put("almocofim", horafimalmoco.getSelectedItem());
                    dia.put("saidotrabalhoas", horafimspinner.getSelectedItem());
                    diasfunc.put(diaselecionado.getText().toString().split(";")[0], dia);
                    Toast.makeText(context, diaselecionado.getText().toString() + " Salvo", Toast.LENGTH_SHORT).show();

                });
                builder2.setView(view22);
                alertchoosinghora = builder2.create();
                if (view22.getParent() != null) {
                    ((ViewGroup) view22.getParent()).removeAllViews();
                } else {
                    alertchoosinghora.setView(null);
                    alertchoosinghora.setView(view22);
                }
                alertchoosinghora.show();
            }
        });
        builder.setView(view2);
        alert = builder.create();
        if (view2.getParent() != null) {
            ((ViewGroup) view2.getParent()).removeAllViews();
        } else {
            alert.setView(null);
            alert.setView(view2);
        }
        if (func.getId() == null) {
            idETL.getEditText().setText(System.currentTimeMillis() + "");
        } else {
            if (!func.getImgUri().isEmpty()) {
                Glide.with(context).load(func.getImgUri()).into(ftUser);
            }
            switch (func.getFormapagamento()) {
                case "semanal":
                    choose_dayPayement.getAdapter().notifyDataSetChanged();
                    semanalbnt.performClick();
                    break;
                case "mensal":
                    daypaymentEDTL.getEditText().setText(func.getDia_salario());
                    mensalbnt.performClick();
                    break;
                case "x"://diario
                    diariobnt.performClick();
                    break;
                case "editado":
                    oftenPayementETL.getEditText().setText(func.getDia_salario().split("!")[0]);
                    datashow_tv.setText("Data de início: " + func.getDia_salario().split("!")[1]);
                    editadobnt.performClick();
                    break;

            }
            cargoETL.getEditText().setText(func.getCargo());
            percentETL.getEditText().setText(func.getPercentual());
            nomefuncedt.getEditText().setText(func.getNome());
            posdiasvalorespromocionais.setText(func.getDia_salario());
            idETL.getEditText().setText(func.getId());
        }
        alert.show();
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
            ftUser.setImageURI(resultUri);
        }
    }

    private static String c(int o) {
        String ss = "";
        if (o < 10) {
            ss += "0";
        }
        ss += o;
        return ss;
    }

    private void changeday(String day) {
        String hora = "";
        if (horarionotificar.get(day.toUpperCase()) != null) {
            hora = "Notificar as: " + horarionotificar.get(day.toUpperCase());
        } else {
            hora = "Informe o horário desse dia";
        }
        notificaras_addfunc.setText(hora);
    }

    private void changeday2(String day) {
        String hora = "";
        if (horarionotificar.get(day.toUpperCase()) != null) {
            hora = "Notificar as: " + horarionotificar.get(day.toUpperCase());
        } else {
            hora = "Informe o horário desse dia";
        }
        notificaras_addfunc.setText(hora);
    }

    private static void initdaypaymenteditar(Accountpattern func) {
        int Requestcode = 1001;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, diaPagamentoBroadCast.class);
        myIntent.putExtra("id", String.valueOf(Requestcode));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Requestcode, myIntent, PendingIntent.FLAG_IMMUTABLE);
        long inicio = System.currentTimeMillis();
        switch (func.getFormapagamento()) {
            case "semanal":
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.SECOND, 0);
                calendar2.set(Calendar.MILLISECOND, 0);
                for (int i = 0; i < func.getDia_salario().length(); i++) {
                    int a = Integer.parseInt(func.getDia_salario().charAt(i) + "");
                    if (calendar2.get(Calendar.DAY_OF_WEEK) > a) {
                        calendar2.add(Calendar.WEEK_OF_YEAR, +1);
                    }
                    calendar2.set(Calendar.DAY_OF_WEEK, a);
                    int hora = 0;
                    int minuto = 0;
                    String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(a).toUpperCase());
                    hora = Integer.parseInt(tempo.split(":")[0]);
                    minuto = Integer.parseInt(tempo.split(":")[1]);
                    calendar2.set(Calendar.HOUR_OF_DAY, hora);
                    calendar2.set(Calendar.MINUTE, minuto);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                }

                break;
            case "mensal":
                Calendar calendario = Calendar.getInstance();
                calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(func.getDia_salario()));
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                calendario.set(Calendar.MILLISECOND, 0);
                if (calendario.get(Calendar.DAY_OF_MONTH) > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                    calendario.add(Calendar.MONTH, +1);
                }
                int hora = 0;
                int minuto = 0;
                String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).toUpperCase());
                hora = Integer.parseInt(tempo.split(":")[0]);
                minuto = Integer.parseInt(tempo.split(":")[1]);
                calendario.set(Calendar.HOUR_OF_DAY, hora);
                calendario.set(Calendar.MINUTE, minuto);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendario.getTimeInMillis(), pendingIntent);
                break;
            case "diario":
                milis = (long) 86_400_000;//time in milis in a day (24 hours)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, inicio, pendingIntent);
                break;
            case "editado":
                Calendar calendar = Calendar.getInstance();
                String data = func.getDia_salario().split("!")[1];
                calendar.set(Integer.parseInt(data.split("/")[2]), Integer.parseInt(data.split("/")[1]) - 1, Integer.parseInt(data.split("/")[0]));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                milis = (long) Integer.parseInt(func.getDia_salario().split("!")[0]) * 86_400_000;
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + milis);
                }
                int hora2 = 0;
                int minuto2 = 0;
                String tempo2 = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)).toUpperCase());
                hora2 = Integer.parseInt(tempo2.split(":")[0]);
                minuto2 = Integer.parseInt(tempo2.split(":")[1]);
                calendar.set(Calendar.HOUR_OF_DAY, hora2);
                calendar.set(Calendar.MINUTE, minuto2);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), milis, pendingIntent);
                break;

        }


    }
}
