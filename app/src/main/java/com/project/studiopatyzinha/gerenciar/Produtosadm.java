package com.project.studiopatyzinha.gerenciar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.project.studiopatyzinha.Adapter.Produtoadapter;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Produtosadm extends AppCompatActivity {
    Toolbar toolbar;
    Produtoadapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Produtopattern> produtos;
    public static TextView refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);
        toolbar = findViewById(R.id.toolbar_produtos);
        refresh = new TextView(getBaseContext());
        RecyclerView rv_produtos = findViewById(R.id.rv_produtos);
        produtos = new ArrayList<>();
        rv_produtos.setLayoutManager(new GridLayoutManager(getBaseContext(),1));
        adapter = new Produtoadapter(produtos, "editar", 0, "", "",getBaseContext());
        rv_produtos.setAdapter(adapter);
        FloatingActionButton fab_add_pedido = findViewById(R.id.fab_add_produto);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Produtos");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        fab_add_pedido.setVisibility(View.VISIBLE);
        fab_add_pedido.setOnClickListener(v -> {
            startActivity(new Intent(this, Formulario_produto.class));
        });


        refresh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                db.collection("Produtos").get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        produtos.clear();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task2.getResult()) {
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
                            mip.setQuant_produto(map2.get("quant_produto_interno") + "");
                            produtos.add(mip);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        refresh.setText("");
    }
}