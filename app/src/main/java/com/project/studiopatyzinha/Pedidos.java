package com.project.studiopatyzinha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Adapter.AdapterMediceItembuyed;
import com.project.studiopatyzinha.Adapter.Adapteraddress;
import com.project.studiopatyzinha.pattern.Addrespattern;
import com.project.studiopatyzinha.pattern.Pedidospattern;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class Pedidos extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rv_pedidos_feitos;
    public static AdapterMediceItembuyed adapter;
    Produtopattern mip;
    TextView img_empty_itensBuyed;
    public static TextView s;
    static Context context;
    String campo;
    String opcao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        toolbar = findViewById(R.id.toolbar_pedidos);
        s = new TextView(getBaseContext());
        context = Pedidos.this;
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Retiradas agendadas");
        img_empty_itensBuyed = findViewById(R.id.img_empty_itensBuyed);
        rv_pedidos_feitos = findViewById(R.id.rv_pedidos_feitos);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        ArrayList<Pedidospattern> lista1 = new ArrayList<>();
        Intent a = getIntent();
        String id = a.getStringExtra("todos");
        campo = "idPessoa";
        opcao = Login.pessoa.getId();
        String admin = "false";
        if (id != null) {
            campo = "idProduto";
            opcao = null;
            admin = "true";
        }
        adapter = new AdapterMediceItembuyed(lista1, getApplicationContext(),admin);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        s.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new Thread(() -> {
                    db.collection("Pedidos").whereEqualTo(campo, opcao).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                lista1.clear();
                                Map<String, Object> map = (Map<String, Object>) snapshot.getData();
                                Pedidospattern pattern = new Pedidospattern();
                                pattern.setQuantidade(map.get("quantidade") + "");
                                pattern.setDataCompra(map.get("dataCompra") + "");
                                pattern.setValorTotal(map.get("valorTotal") + "");
                                pattern.setIdPedido(map.get("idPedido") + "");
                                pattern.setProdutosString(map.get("produtosString") + "");
                                pattern.setPedidoretirado(map.get("Pedidoretirado") + "");
                                pattern.setFormaPagamento(map.get("formaPagamento") + "");
                                pattern.setDataRetirada(map.get("dataRetirada") + "");
                                pattern.setStatus(map.get("status") + "");

                                ArrayList<Produtopattern> produtos = new ArrayList<>();
                                try {
                                    String[] nome = pattern.getProdutosString().split(";");
                                    for (String ss : nome) {
                                        String[] subnome = ss.split(":");
                                        db.collection("Produtos").whereEqualTo("idexterno", subnome[0]).get().addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task2.getResult()) {
                                                    Map<String, Object> map2 = queryDocumentSnapshot.getData();
                                                    mip = new Produtopattern();
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
                                                    mip.setQuant_produto(String.valueOf(subnome[1]));
                                                    produtos.add(mip);
                                                }
                                            }
                                            pattern.setProdutolist(produtos);
                                            lista1.add(pattern);
                                            Collections.sort(lista1, comparador);
                                            adapter.notifyDataSetChanged();
                                            if (lista1.size() == 0) {
                                                img_empty_itensBuyed.setVisibility(View.VISIBLE);
                                            } else {
                                                img_empty_itensBuyed.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                } catch (NullPointerException e) {
                                    return;
                                }
                            }

                        }
                    });
                    runOnUiThread(() -> {
                        rv_pedidos_feitos.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        rv_pedidos_feitos.setAdapter(adapter);
                    });
                }).start();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        s.setText("");
    }

    public Comparator<Pedidospattern> comparador = new Comparator<Pedidospattern>() {
        @Override
        public int compare(Pedidospattern o1, Pedidospattern o2) {
            long id1 = Long.parseLong(o1.getDataCompra());
            long id2 = Long.parseLong(o2.getDataCompra());
            return Long.compare(id2, id1);

        }

    };

    public static void chamardialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustomfull);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_add_agendamento, null, false);
        AlertDialog dialog;
        builder.setView(view);
        dialog = builder.create();
        TextView title = view.findViewById(R.id.title_add_agendamento);
        RecyclerView rv_add_agendameno = view.findViewById(R.id.rv_add_agendameno);
        ImageView close_dialog = view.findViewById(R.id.close_add_agendamento);
        FloatingActionButton fab = view.findViewById(R.id.nextstep_add_agendamento);
        fab.setVisibility(View.GONE);
        close_dialog.setOnClickListener(v1 -> dialog.dismiss());
        title.setText("Endereços para retirada");
        ArrayList<Addrespattern> lista = new ArrayList<>();

        Adapteraddress adapter = new Adapteraddress(context, lista,"false");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Enderecos").get().addOnSuccessListener(command -> {
            for (QueryDocumentSnapshot snapshot : command) {
                Map<String, Object> map = snapshot.getData();


                lista.add(new Addrespattern(map.get("idinterno")+"",map.get("endereco")+"",map.get("endereco_apelido")+"",map.get("cep")+"","",map.get("numero")+""));
                adapter.notifyDataSetChanged();
            }
        });

        rv_add_agendameno.setAdapter(adapter);
        rv_add_agendameno.setLayoutManager(new LinearLayoutManager(context));
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        } else {
            dialog.setView(null);
            dialog.setView(view);
        }
        dialog.show();
    }

}