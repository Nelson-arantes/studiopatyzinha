package com.project.studiopatyzinha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.studiopatyzinha.Adapter.descAdapter;
import com.project.studiopatyzinha.Adapter.funcAdapter;
import com.project.studiopatyzinha.pattern.Descpattern;
import com.project.studiopatyzinha.pattern.SwitchPlus;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class DescontoActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rv;
    FloatingActionButton fab;
    AlertDialog dialog;
    TextInputLayout id, valPercent, valReal, cod;
    SwitchPlus valorPromo;
    Button bntsalvardec;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funcionarios);
        toolbar = findViewById(R.id.toolbar_func);
        rv = findViewById(R.id.rv_funcs);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        fab = findViewById(R.id.fab_add_func);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Cupom de desconto");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        fab.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DescontoActivity.this);
            View view = LayoutInflater.from(DescontoActivity.this).inflate(R.layout.form_desc_cup, null, false);
            view.findViewById(R.id.closedialog).setOnClickListener(v2 -> dialog.dismiss());
            id = view.findViewById(R.id.id);
            id.getEditText().setText(System.currentTimeMillis() + "");
            valorPromo = view.findViewById(R.id.valorPromo);
            bntsalvardec = view.findViewById(R.id.bntsalvardec);
            valPercent = view.findViewById(R.id.valPercent);
            SimpleMaskFormatter percent = new SimpleMaskFormatter("NNN.NN");
            MaskTextWatcher mtwpercent = new MaskTextWatcher(valPercent.getEditText(), percent);
            valPercent.getEditText().addTextChangedListener(mtwpercent);


            valReal = view.findViewById(R.id.valReal);

            valReal.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                private String current = "";

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(current)) {
                        Locale myLocale = new Locale("pt", "BR");
                        valReal.getEditText().removeTextChangedListener(this);
                        String cleanString = s.toString().replaceAll("[R$,. ]", "");
                        float parsed = Float.parseFloat(cleanString + "0");
                        String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                        current = formatted;
                        valReal.getEditText().setText(formatted);
                        valReal.getEditText().setSelection(formatted.length());
                        valReal.getEditText().addTextChangedListener(this);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cod = view.findViewById(R.id.cod);
            int a = new Random().nextInt((int) (-1 * (Calendar.getInstance().getTimeInMillis() - SystemClock.uptimeMillis())));
            cod.getEditText().setText("" + a);
            dialog = builder.create();
            valorPromo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    valPercent.setVisibility(View.VISIBLE);
                    valReal.setVisibility(View.GONE);
                } else {
                    valPercent.setVisibility(View.GONE);
                    valReal.setVisibility(View.VISIBLE);
                }
            });
            bntsalvardec.setOnClickListener(v1 -> {
                boolean continuar = true;
                if (id.getEditText().getText().toString().trim().isEmpty()) {
                    id.setError("Preencha esse campo corretamente");
                    continuar = false;
                }
                if (cod.getEditText().getText().toString().trim().isEmpty()) {
                    cod.setError("Preencha esse campo corretamente");
                    continuar = false;
                }

                if (valReal.getEditText().getText().toString().trim().isEmpty() && valReal.getVisibility() == View.VISIBLE) {
                    valReal.setError("Preencha esse campo corretamente");
                    continuar = false;
                }
                if (valPercent.getEditText().getText().toString().trim().isEmpty() && valPercent.getVisibility() == View.VISIBLE) {
                    valPercent.setError("Preencha esse campo corretamente");
                    continuar = false;
                }
                if (continuar) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("valorReal", valReal.getEditText().getText().toString().trim());
                    map.put("valorPerc", valPercent.getEditText().getText().toString().trim());
                    map.put("id", id.getEditText().getText().toString().trim());
                    map.put("ifpercent", valorPromo.isChecked() + "");
                    map.put("cod", cod.getEditText().getText().toString().trim());
                    db.collection("Desconto").document(id.getEditText().getText().toString().trim()).set(map);
                    dialog.dismiss();
                }

            });
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeAllViews();
            } else {
                dialog.setView(null);
                dialog.setView(view);
            }
            dialog.show();

        });
        ArrayList<Descpattern> list = new ArrayList<>();
        descAdapter adapter = new descAdapter(list);
        rv.setAdapter(adapter);
        db.collection("Desconto").get().addOnCompleteListener(command -> {
            command.getResult().getDocuments().forEach(snapshot -> {
                Map<String, Object> map = snapshot.getData();
                Descpattern s = new Descpattern(map.get("id") + "", map.get("cod") + "", map.get("valorReal") + "", map.get("valorPerc") + "");
                list.add(s);
                adapter.notifyDataSetChanged();
            });

        });


    }
}