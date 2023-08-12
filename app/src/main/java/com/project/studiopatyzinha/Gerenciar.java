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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.Adapter.Adapteraddress;
import com.project.studiopatyzinha.gerenciar.Balanco;
import com.project.studiopatyzinha.gerenciar.Funcionarios;
import com.project.studiopatyzinha.gerenciar.Produtosadm;
import com.project.studiopatyzinha.gerenciar.Rotina;
import com.project.studiopatyzinha.pattern.Addrespattern;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Gerenciar extends AppCompatActivity {
    Toolbar toolbar;
    Button func, servico, rotina, balanco, lcaixa, produtos, pedidos, enderecos;
    AlertDialog dialog2;
    static Context context;
    static ArrayList<Addrespattern> lista;
    static Adapteraddress adapteraddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar);
        toolbar = findViewById(R.id.toobar_gerenciar);
        context = Gerenciar.this;
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Gerenciar");
        toolbar.setNavigationIcon(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(v -> finish());
        rotina = findViewById(R.id.button1);
        produtos = findViewById(R.id.button6);
        lcaixa = findViewById(R.id.button12);
        servico = findViewById(R.id.button2);
        func = findViewById(R.id.button3);
        enderecos = findViewById(R.id.button13);
        balanco = findViewById(R.id.button5);
        pedidos = findViewById(R.id.button14);
        func.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), Funcionarios.class));
        });
        lcaixa.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), livroCaixa.class));
        });
        rotina.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), Rotina.class));
        });
        servico.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), Servicos.class));
        });
        balanco.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), Balanco.class));
        });
        produtos.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), Produtosadm.class));
        });
        findViewById(R.id.button15).setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), DescontoActivity.class));

        });
        enderecos.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Gerenciar.this, R.style.AlertDialogCustomfull);
            View view = LayoutInflater.from(Gerenciar.this).inflate(R.layout.activity_add_agendamento, null, false);
            AlertDialog dialog;
            builder.setView(view);
            dialog = builder.create();
            TextView title = view.findViewById(R.id.title_add_agendamento);
            RecyclerView rv_add_agendameno = view.findViewById(R.id.rv_add_agendameno);
            Button add_address = view.findViewById(R.id.add_address);
            ImageView close_dialog = view.findViewById(R.id.close_add_agendamento);
            FloatingActionButton fab = view.findViewById(R.id.nextstep_add_agendamento);
            add_address.setVisibility(View.VISIBLE);
            lista = new ArrayList<>();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            adapteraddress = new Adapteraddress(getBaseContext(), lista, "false");

            add_address.setOnClickListener(v1 -> {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(Gerenciar.this, R.style.AlertDialogCustomfull);
                View view2 = LayoutInflater.from(Gerenciar.this).inflate(R.layout.formulario_address, null, false);


                ImageView close = view2.findViewById(R.id.imgvoltar_formularioendereco);
                TextInputLayout enderecoETL = view2.findViewById(R.id.endereco_formulario);
                TextInputLayout CepETL = view2.findViewById(R.id.cep_formulario);
                SimpleMaskFormatter numb = new SimpleMaskFormatter("NNNNN-NNN");
                MaskTextWatcher mtwrg = new MaskTextWatcher(CepETL.getEditText(), numb);
                CepETL.getEditText().addTextChangedListener(mtwrg);
                TextInputLayout numeroETL = view2.findViewById(R.id.numero_casa_formulario);
                TextInputLayout apeETL = view2.findViewById(R.id.apelido_formulario);
                Button save = view2.findViewById(R.id.save_informations_address);

                close.setOnClickListener(v2 -> {
                    dialog2.dismiss();
                });
                save.setOnClickListener(v2 -> {
                    if (!enderecoETL.getEditText().getText().toString().isEmpty()) {
                        String id = System.currentTimeMillis() + "";
                        Addrespattern addrespattern = new Addrespattern();
                        addrespattern.setIdinterno(id);
                        addrespattern.setNumero(numeroETL.getEditText().getText().toString().trim());
                        addrespattern.setEndereco(enderecoETL.getEditText().getText().toString().trim());
                        addrespattern.setIfseleceted("");
                        addrespattern.setCep(CepETL.getEditText().getText().toString().trim());
                        addrespattern.setEndereco_apelido(apeETL.getEditText().getText().toString().trim());
                        db.collection("Enderecos").document(id + "").set(addrespattern).addOnCompleteListener(command -> {
                            lista.add(addrespattern);
                            adapteraddress.notifyDataSetChanged();
                            dialog2.dismiss();
                            Toast.makeText(this, "Endereço salvo", Toast.LENGTH_SHORT).show();

                        });
                    } else {
                        enderecoETL.setError("Preencha esse campo corretamete");
                    }
                });
                builder2.setView(view2);
                dialog2 = builder2.create();
                if (view2.getParent() != null) {
                    ((ViewGroup) view2.getParent()).removeAllViews();
                } else {
                    dialog2.setView(null);
                    dialog2.setView(view2);
                }
                dialog2.show();

            });
            fab.setVisibility(View.GONE);
            close_dialog.setOnClickListener(v1 -> dialog.dismiss());
            title.setText("Endereços");

            db.collection("Enderecos").get().addOnSuccessListener(command -> {
                for (QueryDocumentSnapshot snapshot : command) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getData();
                    Addrespattern address = new Addrespattern();
                    address.setIdinterno(map.get("idinterno") + "");
                    address.setEndereco(map.get("endereco") + "");
                    address.setNumero(map.get("numero") + "");
                    address.setCep(map.get("cep") + "");
                    address.setEndereco_apelido(map.get("endereco_apelido") + "");
                    address.setIfseleceted("");
                    lista.add(address);
                    adapteraddress.notifyDataSetChanged();
                }
            });

            rv_add_agendameno.setAdapter(adapteraddress);
            rv_add_agendameno.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeAllViews();
            } else {
                dialog.setView(null);
                dialog.setView(view);
            }
            dialog.show();
        });
        pedidos.setOnClickListener(v -> {
            Intent s = new Intent(getBaseContext(), Pedidos.class);
            s.putExtra("todos", "sim");
            startActivity(s);
        });

    }

    public static void  deleteadrressask(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Deseja apagar ?")
                .setCancelable(true)
                .setPositiveButton("Sim", (dialog, which) -> {
                    FirebaseFirestore.getInstance().collection("Enderecos").document(lista.get(pos).getIdinterno()).delete().addOnCompleteListener(command -> {
                        lista.remove(pos);
                        adapteraddress.notifyDataSetChanged();
                    });

                }).setNegativeButton("Não", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}