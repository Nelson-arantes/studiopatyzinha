package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.banco.bancolocal;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetalhesActivity extends AppCompatActivity {
    Produtopattern mip = null;
    boolean continuar = false;
    Button addtocartp;
    int editando;
    TextView quantp;
    int pos;
    TextView nomep, usosindicadosp, usosNindicadosp,descricao;
    String idspedidos, idpedido, quant, idinterno, id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    double valornovo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        ImageView ftp = findViewById(R.id.img_medicine_details);//
        ImageView detalhes_main = findViewById(R.id.detalhes_main);
        nomep = findViewById(R.id.nome_remedio);
        usosindicadosp = findViewById(R.id.usosIndicados);
        descricao = findViewById(R.id.descricao);
        usosNindicadosp = findViewById(R.id.usosNindicados);
        quantp = findViewById(R.id.nome_quant_item);
        ImageView lessp = findViewById(R.id.lessbutton);
        ImageButton morep = findViewById(R.id.morebutton);
        addtocartp = findViewById(R.id.addtocart);
        Intent a = getIntent();
        id = a.getStringExtra("idexterno");
        idinterno = a.getStringExtra("idinterno");
        quant = a.getStringExtra("quantidade");
        idpedido = a.getStringExtra("idpedido");
        idspedidos = a.getStringExtra("idspedidos");
        pos = a.getIntExtra("pos", 0);
        editando = a.getIntExtra("editando", 0);

        if (editando != 0) {


            db.collection("Produtos").whereEqualTo("idexterno", id).get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Map<String, Object> map2 = snapshot.getData();
                    mip = new Produtopattern();
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
                    mip.setQuant_produto(quant);
                    quantp.setText(mip.getQuant_produto());
                    Glide.with(getBaseContext()).load(mip.getBitmapImg()).into(ftp);
                    usosindicadosp.setText(mip.getUsosindicados());
                    usosNindicadosp.setText(mip.getUsosNindicados());
                    quantp.setText(quant);
                    nomep.setText(mip.getNome());
                }

            });


            lessp.setVisibility(View.VISIBLE);
            if (Integer.parseInt(quant) > 1) {
                lessp.setBackground(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_less));
            }
            addtocartp.setText("Atualizar");

        } else {
            mip = new Produtopattern(a.getStringExtra("id"), a.getStringExtra("idexterno"), a.getStringExtra("nome"), a.getStringExtra("modelo"),
                    a.getStringExtra("fabricante"), a.getStringExtra("desconto"), a.getStringExtra("preco_original"),
                    a.getStringExtra("quant_per_cx"), a.getStringExtra("usosindicados"), a.getStringExtra("usosNindicados"),
                    a.getStringExtra("bitmapImg"), a.getStringExtra("quant_produto"), a.getStringExtra("quant_produto_minima"), a.getStringExtra("quant_produto_interno"));
            mip.setDescricao(a.getStringExtra("descricao"));
            quantp.setText("0");
            usosindicadosp.setText(mip.getUsosindicados());
            usosNindicadosp.setText(mip.getUsosNindicados());
            descricao.setText(mip.getDescricao());
            nomep.setText(mip.getNome());
            Glide.with(getBaseContext()).load(mip.getBitmapImg()).into(ftp);
        }
        ftp.setOnClickListener(v -> {
            ImageView asd = findViewById(R.id.img_medicine_details_fully);
            ImageView asds = findViewById(R.id.imgcls_img_details_fully);
            asd.setVisibility(View.VISIBLE);
            asds.setVisibility(View.VISIBLE);
            asds.setOnClickListener(vf -> {
                asd.setVisibility(View.GONE);
                asds.setVisibility(View.GONE);
            });
            Glide.with(getBaseContext()).load(mip.getBitmapImg()).into(asd);

        });

        addtocartp.setOnClickListener(v -> {


            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Produtos").whereEqualTo("idexterno", mip.getIdexterno()).get().addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task2.getResult()) {
                        Map<String, Object> map2 = queryDocumentSnapshot.getData();
                        Produtopattern mip = new Produtopattern();
                        mip.setQuant_produto_interno(map2.get("quant_produto_interno") + "");
                        continuação();
                    }
                }
            });

        });
        detalhes_main.setOnClickListener(v -> {
            finish();
        });


        lessp.setOnClickListener(v -> {
            int quantN = Integer.parseInt(quantp.getText().toString());
            int quantidade = quantN - 1;
            if (quantN > 0) {
                quantp.setText(quantidade + "");
                mip.setQuant_produto(quantidade + "");

                if (quantidade <= 1) {
                    lessp.setBackground(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_close_30_red));
                    if (quantidade == 0) {
                        addtocartp.setText("Voltar");
                        lessp.setVisibility(View.GONE);
                    }
                }

            }
        });
        morep.setOnClickListener(vVV -> {
            int quantN = Integer.parseInt(quantp.getText().toString());
            int quantidade = quantN + 1;
            if (editando != 0) {
                addtocartp.setText("Atualizar");
            } else {
                addtocartp.setText("Adicionar");
            }

            lessp.setVisibility(View.VISIBLE);
            if (quantidade > 1) {
                lessp.setBackground(AppCompatResources.getDrawable(getBaseContext(), R.drawable.ic_less));
            }
            quantp.setText(quantidade + "");
            mip.setQuant_produto(quantidade + "");
        });

    }

    private Map<String, String> getidProduto(String idComQuantidade) {
        String a = "";
        Map<String, String> map = new HashMap<>();

        int quant = 0;
        for (int i = 0; i != idComQuantidade.length(); i++) {
            String sss = String.valueOf(idComQuantidade.charAt(i));
            if (sss.equals(":")) {
                map.put("idproduto" + quant, a);//testar usando o i
                a = "";
            } else if (sss.equals(";")) {
                map.put("quant" + quant, a);//testar usando o i
                quant++;
                a = "";
            } else {
                a += sss;
            }
        }
        return map;
    }

    private void continuação() {

        if (!addtocartp.getText().equals("Voltar")) {
            if (Integer.parseInt(mip.getQuant_produto_interno()) >= Integer.parseInt(quantp.getText().toString())) {
                if (editando == 0) {//adicionando o produto
                    mip.setQuant_produto(quantp.getText().toString());
                    bancolocal.getINSTANCE(getBaseContext()).addlistcarrinho(mip);
                    Toast.makeText(getBaseContext(), "Produto salvo", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (editando == 2) {//editando o produto vindo do banco
                    bancolocal.getINSTANCE(getBaseContext()).atualizarProduto(mip);
                    Toast.makeText(getBaseContext(), "Produto editado", Toast.LENGTH_SHORT).show();
                    CartActivity.listItemCart.remove(pos);
                    CartActivity.listItemCart.add(mip);
                    try {
                        CartActivity.adapter.notifyDataSetChanged();
                    } catch (NullPointerException e) {
                    }
                    finish();

                }
                if (editando == 25) {//editando o produto vindo da lista
                    bancolocal.getINSTANCE(getBaseContext()).atualizarProduto(mip);
                    CartActivity.listItemCart.remove(pos);
                    CartActivity.listItemCart.add(mip);
                    CartActivity.setpricecart(getBaseContext(), "");
                    try {
                        CartActivity.adapter.notifyDataSetChanged();
                    } catch (NullPointerException e) {
                    }
                    Toast.makeText(getBaseContext(), "Produto editado", Toast.LENGTH_SHORT).show();
                    finish();

                }
                if (editando == 1) {//editando o pedido
                    HashMap<String, String> map = (HashMap<String, String>) getidProduto(idspedidos);
                    for (int i = 0; i < (map.size() / 2); i++) {
                        String idproduto = map.get("idproduto" + i);
                        String quantidade = map.get("quant" + i);
                        if ((idproduto).equals(id) && (quantidade).equals(quant)) {
                            String key = "quant" + i;
                            map.put(key, quantp.getText().toString());
                        }
                    }
                    String idsProdutosnovo = "";
                    valornovo = 0;
                    for (int i = 0; i < (map.size() / 2); i++) {
                        String idproduto = map.get("idproduto" + i);
                        String quantidade = map.get("quant" + i);
                        idsProdutosnovo += idproduto + ":" + quantidade + ";";
                        db.collection("Produtos").document(idproduto).get().addOnSuccessListener(command -> {
                            Map<String, Object> map2 = command.getData();
                            double preco_original = Double.parseDouble(map2.get("preco_original") + "");
                            double desc = Double.parseDouble(map2.get("desconto") + "");
                            int quantidade_atual = Integer.parseInt(map2.get("quant_produto_interno") + "");
                            if (desc != 0) {
                                valornovo += desc * Integer.parseInt(quantidade);
                            } else {
                                valornovo += preco_original * Integer.parseInt(quantidade);
                            }
                            if (Integer.parseInt(quant) < Integer.parseInt(quantp.getText().toString().trim())) {
                                quantidade_atual -= (Integer.parseInt(quantp.getText().toString().trim())-Integer.parseInt(quant));
                            } else {
                                quantidade_atual += (Integer.parseInt(quant)-Integer.parseInt(quantp.getText().toString().trim()));
                            }
                            db.collection("Produtos").document(idproduto).update("quant_produto_interno",quantidade_atual+"");
                        });
                    }

                    db.collection("Pedidos").document(idpedido).update("produtosString", idsProdutosnovo).addOnSuccessListener(command -> {
                        db.collection("Pedidos").document(idpedido).update("valorTotal", valornovo).addOnSuccessListener(command1 -> {
                            Toast.makeText(this, "Editando o pedido", Toast.LENGTH_SHORT).show();
                            Pedidos.s.setText("");
                            finish();
                        });


                    });
                }
            } else {
                continuar = false;
                if (Integer.parseInt(mip.getQuant_produto_interno()) == 0) {
                    Toast.makeText(this, "produto indisponivel", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Temos somente " + mip.getQuant_produto_interno() + " unidades", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ArrayList<Produtopattern> carrinho = bancolocal.getINSTANCE(getBaseContext()).getListCarrinho();
            int poscerta = -1;
            try {
                for (int w = 0; w < CartActivity.listItemCart.size(); w++) {
                    if (CartActivity.listItemCart.get(w).getIdinterno().equals(idinterno)) {
                        continuar = true;
                        poscerta = w;
                    }

                }
                if (poscerta != -1) {
                    for (int w = 0; w < carrinho.size(); w++) {
                        if (carrinho.get(w).getIdinterno().equals(idinterno)) {
                            continuar = true;
                            poscerta = w;
                        }

                    }

                }
            } catch (NullPointerException e) {

            }
            if (continuar) {
                if (Integer.parseInt(quantp.getText().toString()) == 0) {
                    if (editando == 25) {
                        CartActivity.listItemCart.remove(pos);
                        CartActivity.setpricecart(getBaseContext(), "0");

                    } else {
                        bancolocal.getINSTANCE(getBaseContext()).deleteItemlistCarrinho(carrinho.get(poscerta).getIdinterno());
                        CartActivity.setpricecart(getBaseContext(), null);
                        CartActivity.listItemCart.remove(poscerta);

                    }
                    Toast.makeText(getBaseContext(), "Produto removido", Toast.LENGTH_SHORT).show();
                    try {
                        CartActivity.adapter.notifyDataSetChanged();
                        if (CartActivity.adapter.getItemCount() == 0) {
                            CartActivity.img_empty_rv_medicine_cart.setVisibility(View.VISIBLE);
                        }
                    } catch (NullPointerException e) {
                        return;
                    }
                }
                finish();
            }
        }
    }
}