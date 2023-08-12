package com.project.studiopatyzinha;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.SwitchPlus;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    ActivityResultLauncher<String> mGetContent;
    ImageView pickup_image_register;
    Uri fotopessoa;
    String idpessoa = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Accountpattern pessoa = new Accountpattern();
    ImageView close_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Map<String, Object> s = new HashMap<>();
        String receiveId = getIntent().getStringExtra("edit_Acount")+"";
        pessoa.setCargo("usuario");
        pessoa.setDia_salario("");
        pessoa.setFollowersid(s);
        pessoa.setFollowersquant("");
        pessoa.setFrasefilosofica("");
        pessoa.setGrupoAbaixo("");
        pessoa.setPercentual("");
        pessoa.setQuantpost("0");
        pessoa.setSelected("");
        pessoa.setHorario(s);
        pessoa.setBarbersposts(s);
        pessoa.setOpinioes(s);
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(Register.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });
        pickup_image_register = findViewById(R.id.pickup_image_register);
        close_register = findViewById(R.id.close_register);
        close_register.setOnClickListener(v -> finish());
        pickup_image_register.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });
        TextInputLayout idfuncEdtL = findViewById(R.id.idfunc);
        idfuncEdtL.setEndIconDrawable(R.drawable.ic_search);
        TextInputLayout nomeEdtL = findViewById(R.id.nome);
        TextInputLayout bioEdtL = findViewById(R.id.buifunc);
        idfuncEdtL.setEndIconOnClickListener(v -> {
            Toast.makeText(this, "Pesquisando código", Toast.LENGTH_SHORT).show();
            db.collection("Contas").document(idfuncEdtL.getEditText().getText().toString().trim()).get().addOnCompleteListener(task -> {
                if (!task.getResult().toString().isEmpty()) {
                    Toast.makeText(this, "Conta encontrada", Toast.LENGTH_SHORT).show();
                    bioEdtL.setVisibility(View.VISIBLE);
                    pessoa.setCargo(task.getResult().get("cargo") + "");
                    pessoa.setDia_salario(task.getResult().get("dia_salario") + "");
                    pessoa.setFollowersid((Map<String, Object>) task.getResult().get("followersid"));
                    pessoa.setFollowersquant(task.getResult().get("followersquyant") + "");
                    pessoa.setFrasefilosofica(task.getResult().get("frasefilosofica") + "");
                    pessoa.setGrupoAbaixo(task.getResult().get("grupoAbaixo") + "");
                    pessoa.setPercentual(task.getResult().get("percentual") + "");
                    pessoa.setQuantpost(task.getResult().get("quantpost") + "");
                    pessoa.setSelected(task.getResult().get("selected") + "");
                    pessoa.setHorario((Map<String, Object>) task.getResult().get("horario"));
                    pessoa.setBarbersposts((Map<String, Object>) task.getResult().get("barbersposts"));
                    pessoa.setOpinioes((Map<String, Object>) task.getResult().get("opinioes"));
                    nomeEdtL.getEditText().setText(task.getResult().get("nome") + "");
                } else {
                    Toast.makeText(this, "Conta não encontrada", Toast.LENGTH_SHORT).show();
                }
            });
        });
        SwitchPlus iffunc = findViewById(R.id.iffunc);
        TextInputLayout emailEdtL = findViewById(R.id.email);
//        SimpleMaskFormatter numb = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
//        MaskTextWatcher mtwrg = new MaskTextWatcher(numbEdtL.getEditText(), numb);
//        numbEdtL.getEditText().addTextChangedListener(mtwrg);
        TextInputLayout passwordEdtL = findViewById(R.id.password);
        TextInputLayout password_repeatEdtL = findViewById(R.id.password_repeat);
        Button registrar = findViewById(R.id.registrar);
        iffunc.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                idfuncEdtL.setVisibility(View.VISIBLE);
            } else {
                idfuncEdtL.setVisibility(View.GONE);
            }
        });

        registrar.setOnClickListener(view1 -> {
            boolean mycontinuar = true;
            if (nomeEdtL.getEditText().getText().toString().trim().isEmpty()) {
                nomeEdtL.setError("Preencha esse campo");
                mycontinuar = false;
            }
            if (emailEdtL.getEditText().getText().toString().trim().isEmpty()) {
                emailEdtL.setError("Preencha esse campo");
                mycontinuar = false;
            }
            if (password_repeatEdtL.getEditText().getText().toString().trim().isEmpty()) {
                password_repeatEdtL.setError("Preencha esse campo");
                mycontinuar = false;
            }
            if (passwordEdtL.getEditText().getText().toString().trim().isEmpty()) {
                passwordEdtL.setError("Preencha esse campo");
                mycontinuar = false;
            }
            if (fotopessoa == null && !receiveId.equals("s")) {
                mycontinuar = false;
                Toast.makeText(this, "Escolha uma foto de perfil", Toast.LENGTH_SHORT).show();
            }
            if (!passwordEdtL.getEditText().getText().toString().trim().equals(password_repeatEdtL.getEditText().getText().toString().trim())) {
                mycontinuar = false;
                passwordEdtL.setError("Senhas diferentes");
                password_repeatEdtL.setError("Senhas diferentes");
            }
            if (mycontinuar) {
                final ProgressDialog pd = new ProgressDialog(Register.this);
                pd.setTitle("Enviando imagem...");
                pd.show();
                if (Login.pessoa == null) {
                    if (idfuncEdtL.getEditText().getText().toString().trim().isEmpty()) {
                        idpessoa = +System.currentTimeMillis() + "";
                    } else {
                        idpessoa = idfuncEdtL.getEditText().getText().toString().trim();
                    }
                } else {
                    idpessoa = Login.pessoa.getId();
                }

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                if (fotopessoa == null) {
                    pessoa.setEmail(emailEdtL.getEditText().getText().toString().trim());
                    pessoa.setNome(nomeEdtL.getEditText().getText().toString().trim());
                    pessoa.setSenha(password_repeatEdtL.getEditText().getText().toString().trim());
                    pessoa.setFrasefilosofica(bioEdtL.getEditText().getText().toString().trim());
                    db.collection("Contas").document(idpessoa).set(pessoa).addOnSuccessListener(command -> {
                        Toast.makeText(Register.this, "Conta cadastrada", Toast.LENGTH_SHORT).show();
                        Login.pessoa = pessoa;
                        if (receiveId.equals("s")) {
                            MainActivity.changeaccount.setText("");
                            ArrayList<String> idstochange = new ArrayList<>();
                            db.collection("Blog").get().addOnCompleteListener(task -> {
                                task.getResult().forEach(snapshot -> {
                                    Map<String, Object> pessoamap = (Map<String, Object>) snapshot.getData().get("pessoa");
                                    if (pessoamap.get("id").equals(pessoa.getId())) {
                                        idstochange.add(snapshot.getData().get("id") + "");
                                    }
                                });
                                Accountpattern pessoatoblog = new Accountpattern();
                                pessoatoblog.setId(idfuncEdtL.getEditText().getText() + "");
                                pessoatoblog.setNome(nomeEdtL.getEditText().getText().toString());
                                pessoatoblog.setImgUri(Login.pessoa.getImgUri());
                                for (int i = 0; i < idstochange.size(); i++) {
                                    db.collection("Blog").document(idstochange.get(i)).update("pessoa", pessoatoblog);
                                }
                            });
                            AccountInfo.changeaccount.setText("");
                        }
                        finish();
                    }).addOnFailureListener(command -> {
                        Toast.makeText(Register.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                    }).addOnCompleteListener(command -> {
                        pd.dismiss();
                    });
                } else {
                    storageRef.child("imagens contas").child(idpessoa).putFile(fotopessoa).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().
                                    addOnSuccessListener(uri -> {
                                                pessoa.setId(idpessoa);
                                                pessoa.setEmail(emailEdtL.getEditText().getText().toString().trim());
                                                pessoa.setNome(nomeEdtL.getEditText().getText().toString().trim());
                                                pessoa.setSenha(password_repeatEdtL.getEditText().getText().toString().trim());
                                                pessoa.setFrasefilosofica(bioEdtL.getEditText().getText().toString().trim());
                                                pessoa.setImgUri(uri + "");
                                                db.collection("Contas").document(idpessoa).set(pessoa).addOnSuccessListener(command -> {
                                                    Toast.makeText(Register.this, "Conta cadastrada", Toast.LENGTH_SHORT).show();
                                                    Login.pessoa = pessoa;
                                                    if (receiveId.equals("s")) {
                                                        MainActivity.changeaccount.setText("");
                                                        ArrayList<String> idstochange = new ArrayList<>();
                                                        db.collection("Blog").get().addOnCompleteListener(task -> {
                                                            task.getResult().forEach(snapshot -> {
                                                                Map<String, Object> pessoamap = (Map<String, Object>) snapshot.getData().get("pessoa");
                                                                if (pessoamap.get("id").equals(pessoa.getId())) {
                                                                    idstochange.add(snapshot.getData().get("id") + "");
                                                                }

                                                            });
                                                            Accountpattern pessoatoblog = new Accountpattern();
                                                            pessoatoblog.setId(idfuncEdtL.getEditText().getText() + "");
                                                            pessoatoblog.setNome(nomeEdtL.getEditText().getText().toString());
                                                            pessoatoblog.setImgUri(Login.pessoa.getImgUri());
                                                            for (int i = 0; i < idstochange.size(); i++) {
                                                                db.collection("Blog").document(idstochange.get(i)).update("pessoa", pessoatoblog);
                                                            }
                                                        });


                                                        AccountInfo.changeaccount.setText("");
                                                    }
                                                    finish();
                                                }).addOnFailureListener(command -> {
                                                    Toast.makeText(Register.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                                                }).addOnCompleteListener(command -> {
                                                    pd.dismiss();

                                                });
                                            }
                                    );
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Register.this, "Algo deu errado", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }).addOnProgressListener(taskSnapshot -> {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage(progressPercent + "  %");
                    });
                }
            }
        });
        if (receiveId != null && receiveId.equals("s")) {
            boolean ifadmin = !Login.pessoa.getCargo().equals("usuario");
            iffunc.setChecked(ifadmin);
            Glide.with(getBaseContext()).load(Login.pessoa.getImgUri()).into(pickup_image_register);
            nomeEdtL.getEditText().setText(Login.pessoa.getNome());
            emailEdtL.getEditText().setText(Login.pessoa.getEmail());
            passwordEdtL.getEditText().setText(Login.pessoa.getSenha());
            password_repeatEdtL.getEditText().setText(Login.pessoa.getSenha());
            if (ifadmin) {
                idfuncEdtL.getEditText().setText(Login.pessoa.getId());
                idfuncEdtL.getEditText().setEnabled(false);
                bioEdtL.setVisibility(View.VISIBLE);
                bioEdtL.getEditText().setText(Login.pessoa.getFrasefilosofica());
            } else {
                bioEdtL.setVisibility(View.GONE);

            }
            registrar.setText("Atualizar");
            pessoa = Login.pessoa;

        }

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
            fotopessoa = resultUri;
            pickup_image_register.setImageURI(resultUri);

        }
    }
}