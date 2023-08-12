package com.project.studiopatyzinha;

import static com.project.studiopatyzinha.R.color.primaria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.studiopatyzinha.pattern.TransicaoPattern;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class livroCaixa extends AppCompatActivity {
    Toolbar toolbar_livroCaixa;
    AlertDialog alert;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static TextView mudarFragmento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livro_caixa);
        mudarFragmento = new TextView(getBaseContext());
        mudarFragmento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).contains("detalhado")) {
                    changeFragment(DetalhesFragment.class);
                } else {
                    changeFragment(fragmentResumo.class);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        tabs = findViewById(R.id.tabs);
        toolbar_livroCaixa = findViewById(R.id.toolbar_livroCaixa);
        this.setSupportActionBar(toolbar_livroCaixa);
        this.getSupportActionBar().setTitle("");
        toolbar_livroCaixa.setTitle("Livro caixa");
        toolbar_livroCaixa.setTitleTextColor(Color.WHITE);
        toolbar_livroCaixa.setBackground(AppCompatResources.getDrawable(getBaseContext(), primaria));
        toolbar_livroCaixa.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbar_livroCaixa.setNavigationOnClickListener(v -> finish());
        mudarFragmento.setText("resumo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_balanco, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.addreforco) {
            initdialog("Reforço");
        } else if (item_id == R.id.addretirada) {
            initdialog("Retirada");
        }
        return true;
    }

    private void changeFragment(Class s) {
        Class fragmentResumoclass = s;
        Fragment fragmentmain = null;
        try {
            fragmentmain = (Fragment) fragmentResumoclass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (fragmentmain != null) {
            FragmentManager fragmentManeger = getSupportFragmentManager();
            fragmentManeger.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.framelayout, fragmentmain).commit();
        }
    }

    private void initdialog(String origem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustomfull);
        View view = LayoutInflater.from(livroCaixa.this).inflate(R.layout.add_transicao, null, false);


        Button salvartransicao = view.findViewById(R.id.salvar_transicao);
        TextInputLayout valor = view.findViewById(R.id.valor_transicao);
        ImageView closeDilog = view.findViewById(R.id.closedialog_add_trasicao);
        closeDilog.setOnClickListener(v -> alert.dismiss());
        TextView title = view.findViewById(R.id.title_add_transicao);
        title.setText(origem);


        TextInputLayout hora = view.findViewById(R.id.hora_trancisao);
        SimpleMaskFormatter numb = new SimpleMaskFormatter("NN/NN/NNNN NN:NN");
        MaskTextWatcher mtwrg = new MaskTextWatcher(hora.getEditText(), numb);
        hora.getEditText().addTextChangedListener(mtwrg);
        String horaString = "";

        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
            horaString += "0";
        }
        horaString += Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/";
        if (Calendar.getInstance().get(Calendar.MONTH) < 10) {
            horaString += "0";
        }
        horaString += (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR) + " ";
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10) {
            horaString += "0";
        }
        horaString += Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":";
        if (Calendar.getInstance().get(Calendar.MINUTE) < 10) {
            horaString += "0";
        }
        horaString += Calendar.getInstance().get(Calendar.MINUTE);
        hora.getEditText().setText(horaString);
        valor.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    valor.getEditText().removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,.  ]", "");
                    float parsed = Float.parseFloat(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    valor.getEditText().setText(formatted);
                    valor.getEditText().setSelection(formatted.length());
                    valor.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        salvartransicao.setOnClickListener(v -> {
            boolean continuar = true;
            String data = hora.getEditText().getText().toString().split(" ")[0];
            String[] dataseparada = data.split("/");
            String horatring = hora.getEditText().getText().toString().split(" ")[1];
            String[] horaseparada = horatring.split(":");

            if (hora.getEditText().getText().toString().isEmpty()) {
                continuar = false;
                hora.setError("Preencha esse campo corretamente");
            } else {

                Calendar calendar2 = Calendar.getInstance();
                if (Integer.parseInt(dataseparada[1]) > 12) {
                    continuar = false;
                    hora.setError("Preencha esse campo corretamente");
                } else {
                    calendar2.set(Calendar.MONTH, Integer.parseInt(dataseparada[1]));
                }


                if (Integer.parseInt(dataseparada[0]) > calendar2.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    continuar = false;
                    hora.setError("Preencha esse campo corretamente");
                }
                if (Integer.parseInt(horaseparada[0]) > 23) {
                    continuar = false;
                    hora.setError("Preencha esse campo corretamente");
                }
                if (Integer.parseInt(horaseparada[0]) > 59) {
                    continuar = false;
                    hora.setError("Preencha esse campo corretamente");
                }

            }

            if (valor.getEditText().getText().toString().isEmpty()) {
                continuar = false;
                valor.setError("Preencha esse campo corretamente");
            }
            if (continuar) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(dataseparada[2]), Integer.parseInt(dataseparada[1])-1, Integer.parseInt(dataseparada[0]), Integer.parseInt(horaseparada[0]), Integer.parseInt(horaseparada[1]));
                TransicaoPattern transicaoPattern = new TransicaoPattern(calendar.getTimeInMillis() + "", valor.getEditText().getText().toString().replace(",",".").replaceAll("[R$  ]", ""), calendar.getTime() + "", origem);
                db.collection("Livro caixa").document(transicaoPattern.getId()).set(transicaoPattern).addOnSuccessListener(command -> {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference.child("Livro caixa").child("now").setValue(System.currentTimeMillis() + "");
                });

                alert.dismiss();


            }


        });
        builder.setView(view);
        alert = builder.create();
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        } else {
            alert.setView(null);
            alert.setView(view);
        }
        alert.show();
    }

}