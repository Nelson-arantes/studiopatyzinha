package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.project.studiopatyzinha.Adapter.CommentAdapter;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Comments_pattern;
import com.project.studiopatyzinha.pattern.PostPattern;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class Comentar extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static PostPattern post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentar);
        ArrayList<Comments_pattern> comentarios = new ArrayList<>();
        CommentAdapter adapter = new CommentAdapter(comentarios);

        db.collection("Blog").document(Login.blogstring).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                Map<String, Object> mippessoa = (Map<String, Object>) snapshot.get("pessoa");
                Accountpattern pessoa = new Accountpattern((Map<String, Object>) mippessoa.get("barbersposts"),//reduzir essas informações
                        mippessoa.get("cargo") + "", mippessoa.get("dia_salario") + "",
                        mippessoa.get("email") + "", (Map<String, Object>) mippessoa.get("followersid"),
                        mippessoa.get("followersquant") + "", mippessoa.get("frasefilosofica") + "",
                        mippessoa.get("grupoAbaixo") + "", (Map<String, Object>) mippessoa.get("horario"),
                        mippessoa.get("id") + "", mippessoa.get("imgUri") + "", mippessoa.get("nome") + "",
                        (Map<String, Object>) mippessoa.get("opinioes"),
                        mippessoa.get("percentual") + "", mippessoa.get("quantpost") + "",
                        mippessoa.get("selected") + "", mippessoa.get("senha") + "", (Map<String, Object>) mippessoa.get("horasdiaspagamentos"), "" + mippessoa.get("ultimosalario"), mippessoa.get("ifgerente") + "");
                ArrayList<Map<String, Object>> a = (ArrayList<Map<String, Object>>) snapshot.get("comentarios");
                ArrayList<Comments_pattern> ad = new ArrayList<>();
                for (int i = 0; i < a.size(); i++) {
                    Map<String, Object> contamap = (Map<String, Object>) a.get(i).get("user");
                    Accountpattern person = new Accountpattern(null, "", "", "", null, "", "", null, null, contamap.get("id") + "", contamap.get("imgUri") + "",
                            contamap.get("nome") + "", null, "", "", "", "", null, "","");
                    Comments_pattern coment = new Comments_pattern(a.get(i).get("comment") + "", a.get(i).get("commented_at") + "", a.get(i).get("id") + "", person);
                    ad.add(0, coment);
                }
                post = new PostPattern(snapshot.get("commentsQuant") + "",
                        snapshot.get("date") + "", snapshot.get("desc") + "", snapshot.get("id") + "",
                        (Map<String, Object>) snapshot.get("likesids"), snapshot.get("likesquants") + "",
                        snapshot.get("nomeid") + "", pessoa, snapshot.get("photo") + "", Boolean.parseBoolean(snapshot.get("selflike") + ""),
                        snapshot.get("tag") + "", snapshot.get("views") + "", (Map<String, Object>) snapshot.get("notificados"), ad);
                for (int i = 0; i < post.getComentarios().size(); i++) {
                    comentarios.add(new Comments_pattern(post.getComentarios().get(i).getComment(), post.getComentarios().get(i).getCommented_at(),
                            post.getComentarios().get(i).getId(), post.getComentarios().get(i).getUser()));


                }
                adapter.notifyDataSetChanged();
            }
        });
        RecyclerView rvcomentarios = findViewById(R.id.rv_comentarios);
        ImageView closedialog_comentar = findViewById(R.id.closedialog_comentar);
        closedialog_comentar.setOnClickListener(v -> {
            MainActivity.initBlog();
            finish();
        });
        ImageView send_comment = findViewById(R.id.send_comment);
        TextInputLayout cometarioTIL = findViewById(R.id.comet_comentario);
        rvcomentarios.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvcomentarios.setAdapter(adapter);
        send_comment.setOnClickListener(v1 -> {
            if (!cometarioTIL.getEditText().getText().toString().trim().isEmpty()) {
                comentarios.add(new Comments_pattern(cometarioTIL.getEditText().getText().toString().trim(), System.currentTimeMillis() + "", System.currentTimeMillis() + "", Login.pessoa));
                post.setComentarios(comentarios);
                adapter.notifyDataSetChanged();
                cometarioTIL.getEditText().setText("");
                post.setCommentsQuant(comentarios.size() + "");
                db.collection("Blog").document(post.getId()).set(post);
            } else {
                cometarioTIL.setError("Preencha esse campo");
            }

        });

    }
}