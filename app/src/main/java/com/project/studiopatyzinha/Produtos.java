package com.project.studiopatyzinha;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Adapter.ProdutoscapaAdapter;
import com.project.studiopatyzinha.gerenciar.Formulario_produto;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Produtos extends AppCompatActivity {
    Toolbar toolbar;
    ProdutoscapaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);
        toolbar = findViewById(R.id.toolbar_produtos);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Produtos");
        RecyclerView rv_produtos = findViewById(R.id.rv_produtos);
        ArrayList<Produtopattern> produtos = new ArrayList<>();
        rv_produtos.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
        adapter = new ProdutoscapaAdapter(getBaseContext(), produtos);
        rv_produtos.setAdapter(adapter);
        FloatingActionButton fab_add_pedido = findViewById(R.id.fab_add_produto);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        fab_add_pedido.setOnClickListener(v -> {
            startActivity(new Intent(this, Formulario_produto.class));
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Produtos").get().addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
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
                    mip.setDescricao(map2.get("descricao") + "");
                    mip.setUsosindicados(map2.get("usosindicados") + "");
                    mip.setUsosNindicados(map2.get("usosNindicados") + "");
                    mip.setBitmapImg(map2.get("bitmapImg") + "");
                    mip.setQuant_minima(map2.get("quant_minima") + "");
                    mip.setQuant_produto_interno(map2.get("quant_produto_interno") + "");
                    mip.setQuant_produto("");
                    produtos.add(mip);
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_produtos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.getItemId();
        startActivity(new Intent(this, CartActivity.class));
        return true;
    }


}