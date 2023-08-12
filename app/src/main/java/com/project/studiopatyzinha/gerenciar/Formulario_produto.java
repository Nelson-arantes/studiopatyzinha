package com.project.studiopatyzinha.gerenciar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.CropperActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.project.studiopatyzinha.pattern.SwitchPlus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class Formulario_produto extends AppCompatActivity {
    Toolbar toolbar;
    SwitchPlus ifdesconto;
    TextInputLayout codi, nome, modelo, fabricante, preco, desconto, quant_per_cx, usosindicados, usosnindicados, quant_minima, quant_atual, descricao;
    ImageView pickpic;
    ActivityResultLauncher<String> mGetContent;
    Uri urioft;
    Button bntsalvarproduto;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Produtopattern mip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_produto);
        toolbar = findViewById(R.id.toolbar_formulario_produto);
        ifdesconto = findViewById(R.id.ifdesconto);
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(Formulario_produto.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });
        Intent a = getIntent();
        String id = a.getStringExtra("idexterno");
        pickpic = findViewById(R.id.pickup_image_formProduto);
        pickpic.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });
        codi = findViewById(R.id.idproduto);
        codi.getEditText().setText(System.currentTimeMillis() + "");
        bntsalvarproduto = findViewById(R.id.bntsalvarproduto);
        nome = findViewById(R.id.nome);
        modelo = findViewById(R.id.modelo);
        fabricante = findViewById(R.id.fabricante);
        preco = findViewById(R.id.preco);
        desconto = findViewById(R.id.desconto);
        quant_per_cx = findViewById(R.id.quant_per_cx);
        usosindicados = findViewById(R.id.usosindicados);
        descricao = findViewById(R.id.descricao);
        usosnindicados = findViewById(R.id.usosnindicados);
        quant_minima = findViewById(R.id.quant_minima);
        quant_atual = findViewById(R.id.quant_atual);
        preco.getEditText().setText("R$ 0,00");
        desconto.getEditText().setText("R$ 0,00");
        ifdesconto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                desconto.setVisibility(View.VISIBLE);
            } else {
                desconto.setVisibility(View.GONE);
            }
        });


        desconto.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    desconto.getEditText().removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,. ]", "");
                    float parsed = Float.parseFloat(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    desconto.getEditText().setText(formatted);
                    desconto.getEditText().setSelection(formatted.length());
                    desconto.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        preco.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    preco.getEditText().removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,. ]", "");
                    float parsed = Float.parseFloat(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    preco.getEditText().setText(formatted);
                    preco.getEditText().setSelection(formatted.length());
                    preco.getEditText().addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        bntsalvarproduto.setOnClickListener(v -> {
            boolean continuar = true;

            if (urioft == null && mip == null) {
                Toast.makeText(this, "Escolha a foto do produto", Toast.LENGTH_SHORT).show();
                continuar = false;
            }
            if (codi.getEditText().getText().toString().trim().isEmpty()) {
                codi.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (quant_atual.getEditText().getText().toString().trim().isEmpty()) {
                quant_atual.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (quant_minima.getEditText().getText().toString().trim().isEmpty()) {
                quant_minima.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (usosnindicados.getEditText().getText().toString().trim().isEmpty()) {
                usosnindicados.setError("Preencha esse campo corretamente");
                continuar = false;
            }

            if (usosindicados.getEditText().getText().toString().trim().isEmpty()) {
                usosindicados.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (quant_per_cx.getEditText().getText().toString().trim().isEmpty()) {
                quant_per_cx.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (preco.getEditText().getText().toString().trim().isEmpty()) {
                preco.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (descricao.getEditText().getText().toString().trim().isEmpty()) {
                descricao.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (fabricante.getEditText().getText().toString().trim().isEmpty()) {
                fabricante.setError("Preencha esse campo corretamente");
                continuar = false;
            }


            if (modelo.getEditText().getText().toString().trim().isEmpty()) {
                modelo.setError("Preencha esse campo corretamente");
                continuar = false;
            }
            if (nome.getEditText().getText().toString().trim().isEmpty()) {
                nome.setError("Preencha esse campo corretamente");
                continuar = false;
            }

            if (continuar) {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                final ProgressDialog pd = new ProgressDialog(Formulario_produto.this);
                pd.setTitle("Enviando imagem...");
                pd.show();
                if (urioft != null) {
                    storageRef.child("imagens produtos").child(codi.getEditText().getText().toString().trim()).putFile(urioft).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                        Produtopattern produto = new Produtopattern();
                                        produto.setIdexterno(codi.getEditText().getText().toString().trim());
                                        produto.setNome(nome.getEditText().getText().toString().trim());
                                        produto.setModelo(modelo.getEditText().getText().toString().trim());
                                        produto.setDescricao(descricao.getEditText().getText().toString().trim());
                                        produto.setFabricante(fabricante.getEditText().getText().toString().trim());
                                        String descontostring = "";
                                        if (desconto.getEditText().getText().toString().trim().isEmpty() || desconto.getEditText().getText().toString().trim().equals("R$ 0,00")) {
                                            descontostring = "0.f";
                                        } else {
                                            descontostring = desconto.getEditText().getText().toString().trim().replaceAll("[R$  ]", "").replace(",", ".");
                                        }
                                        produto.setDesconto(descontostring);
                                        produto.setPreco_original(preco.getEditText().getText().toString().trim().replaceAll("[R$  ]", "").replace(",", "."));
                                        produto.setQuant_per_cx(quant_per_cx.getEditText().getText().toString().trim());
                                        produto.setUsosindicados(usosindicados.getEditText().getText().toString().trim());
                                        produto.setUsosNindicados(usosnindicados.getEditText().getText().toString().trim());
                                        produto.setBitmapImg(uri + "");
                                        produto.setQuant_produto("");
                                        produto.setQuant_minima(quant_minima.getEditText().getText().toString().trim());
                                        produto.setQuant_produto_interno(quant_atual.getEditText().getText().toString().trim());

                                        db.collection("Produtos").document(produto.getIdexterno()).set(produto).addOnSuccessListener(command -> {
                                            if (Produtosadm.refresh != null) {
                                                Produtosadm.refresh.setText("");
                                            }
                                            Toast.makeText(Formulario_produto.this, "Terminamos", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }).addOnFailureListener(command -> {
                                            Toast.makeText(Formulario_produto.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                                        }).addOnCompleteListener(command -> {

                                            pd.dismiss();
                                        });
                                    }
                            );
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Formulario_produto.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }).addOnProgressListener(taskSnapshot -> {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage(progressPercent + "  %");
                    });
                } else {
                    Produtopattern produto = new Produtopattern();
                    produto.setIdexterno(codi.getEditText().getText().toString().trim());
                    produto.setDescricao(descricao.getEditText().getText().toString().trim());
                    produto.setNome(nome.getEditText().getText().toString().trim());
                    produto.setModelo(modelo.getEditText().getText().toString().trim());
                    produto.setFabricante(fabricante.getEditText().getText().toString().trim());
                    String descontostring = "";
                    if (desconto.getEditText().getText().toString().trim().isEmpty() || desconto.getEditText().getText().toString().trim().equals("R$ 0,00")) {
                        descontostring = "0.f";
                    } else {
                        descontostring = desconto.getEditText().getText().toString().trim().replaceAll("[R$  ]", "").replace(",", ".");
                    }
                    produto.setDesconto(descontostring);
                    produto.setPreco_original(preco.getEditText().getText().toString().trim().replaceAll("[R$  ]", "").replace(",", "."));
                    produto.setQuant_per_cx(quant_per_cx.getEditText().getText().toString().trim());
                    produto.setUsosindicados(usosindicados.getEditText().getText().toString().trim());
                    produto.setUsosNindicados(usosnindicados.getEditText().getText().toString().trim());
                    produto.setBitmapImg(mip.getBitmapImg());
                    produto.setQuant_produto("");
                    produto.setQuant_minima(quant_minima.getEditText().getText().toString().trim());
                    produto.setQuant_produto_interno(quant_atual.getEditText().getText().toString().trim());

                    db.collection("Produtos").document(produto.getIdexterno()).set(produto).addOnSuccessListener(command -> {
                        if (Produtosadm.refresh != null) {
                            Produtosadm.refresh.setText("");
                        }
                        Toast.makeText(Formulario_produto.this, "Terminamos", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(command -> {
                        Toast.makeText(Formulario_produto.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                    }).addOnCompleteListener(command -> {
                        pd.dismiss();
                    });
                }
            }
        });
        if (id != null) {
            db.collection("Produtos").whereEqualTo("idexterno", id).get().addOnCompleteListener(task2 -> {
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
                        mip.setDescricao(map2.get("descricao") + "");
                        mip.setBitmapImg(map2.get("bitmapImg") + "");
                        mip.setQuant_minima(map2.get("quant_minima") + "");
                        mip.setQuant_produto_interno(map2.get("quant_produto_interno") + "");
                        mip.setQuant_produto("");
                        Glide.with(getBaseContext()).load(mip.getBitmapImg()).into(pickpic);
                        nome.getEditText().setText(mip.getNome());
                        codi.getEditText().setText(mip.getIdexterno());
                        codi.setEnabled(false);
                        modelo.getEditText().setText(mip.getModelo());
                        fabricante.getEditText().setText(mip.getFabricante());
                        preco.getEditText().setText(mip.getPreco_original());
                        ifdesconto.setChecked(!mip.getDesconto().equals("0.f"));
                        if (!mip.getDesconto().equals("0.f")) {
                            desconto.getEditText().setText(mip.getDesconto());
                        }
                        quant_per_cx.getEditText().setText(mip.getQuant_per_cx());
                        usosindicados.getEditText().setText(mip.getUsosindicados());
                        usosnindicados.getEditText().setText(mip.getUsosNindicados());
                        quant_minima.getEditText().setText(mip.getQuant_minima());
                        descricao.getEditText().setText(mip.getDescricao());
                        quant_atual.getEditText().setText(mip.getQuant_produto_interno());
                        bntsalvarproduto.setText("Atualizar");
                    }
                }
            });
        }
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Formulário produto");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setTitleTextColor(Color.WHITE);
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
            urioft = resultUri;
            pickpic.setImageURI(resultUri);

        }
    }
}