package com.project.studiopatyzinha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.zzd;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Configuracoes extends AppCompatActivity {
    Toolbar toobarconfig;
    AlertDialog alert2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        toobarconfig = findViewById(R.id.toobarconfig);
        this.setSupportActionBar(toobarconfig);
        this.getSupportActionBar().setTitle("");
        toobarconfig.setTitle("Configurações");
        toobarconfig.setTitleTextColor(Color.WHITE);
        toobarconfig.setNavigationIcon(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back));
        toobarconfig.setNavigationOnClickListener(v -> finish());
        findViewById(R.id.politicas_privacidade_configuracoes).setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext(), PoliticasPrivacidade.class));
        });

        findViewById(R.id.change_the_password_notification).setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(Configuracoes.this);
            View Obview2 = LayoutInflater.from(getBaseContext()).inflate(R.layout.changepassword, null);
            TextInputLayout senhaatual = Obview2.findViewById(R.id.passWordolder_changePassword);
            TextInputLayout senhanova = Obview2.findViewById(R.id.passWord_changePassword);
            TextInputLayout repetirnovasenha = Obview2.findViewById(R.id.repeatPassword_changePassword);
            ImageView closedialog = Obview2.findViewById(R.id.closedialog_changepass);
            closedialog.setOnClickListener(v1 -> {
                alert2.dismiss();
            });
            Button changePasswordbnt = Obview2.findViewById(R.id.changePassword_changePassword);
            changePasswordbnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (senhaatual.getEditText().getText().toString().equals(Login.pessoa.getSenha())) {
                        senhaatual.setError(null);
                        String senhaString = senhanova.getEditText().getText().toString();
                        String respostarepetida = repetirnovasenha.getEditText().getText().toString();
                        boolean continuar = true;
                        if (senhaString.trim().equals("")) {
                            senhanova.setError(getString(R.string.empty_field));
                            continuar = false;
                        }
                        if (respostarepetida.trim().equals("")) {
                            repetirnovasenha.setError(getString(R.string.empty_field));
                            continuar = false;
                        }
                        if (!senhaString.equals(respostarepetida)) {
                            repetirnovasenha.setError(getString(R.string.empty_field));
                            continuar = false;
                        }
                        if (continuar) {
                            Login.pessoa.setSenha(repetirnovasenha.getEditText().getText().toString());
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Contas").document(Login.pessoa.getId()).set(Login.pessoa);
                            alert2.dismiss();

                        }
                    } else {
                        senhaatual.setError("Senha incorreta");
                    }
                }
            });
            dialogBuilder2.setView(Obview2);
            alert2 = dialogBuilder2.create();
            alert2.show();
        });
        findViewById(R.id.forget_me_configuracoes).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Deseja apagar todos os seus dados ?")
                    .setCancelable(true)
                    .setPositiveButton("Sim", (dialog, id) -> deleteall())
                    .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        findViewById(R.id.exit_acout_configuracoes).setOnClickListener(v -> {
            Login.pessoa = null;
            startActivity(new Intent(this, Login.class));
            finish();
        });
        findViewById(R.id.share_us_app_configuration).setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            String sharebodytext = "O melhor salão de São Bernardo" + " \nhttps://play.google.com/store/apps/details?id=com.project.studiopatyzinha";
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharebodytext);
            startActivity(Intent.createChooser(shareIntent, "Compartilhar usando"));
        });
        findViewById(R.id.hate_us_configuracoes).setOnClickListener(v -> {
            ReviewManager reviewManager = ReviewManagerFactory.create(this);
            reviewManager.requestReviewFlow().addOnCompleteListener(it -> {
                if (it.isSuccessful()) {
                    reviewManager.launchReviewFlow(this, it.getResult());
                }
            });
        });
    }
    private void deleteall(){
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         db.collection("Contas").document(Login.pessoa.getId()).delete();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("imagens contas").child(Login.pessoa.getId()).delete();

    }
}