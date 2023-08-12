package com.project.studiopatyzinha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.Adapter.PostAdapter;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Comments_pattern;
import com.project.studiopatyzinha.pattern.PostPattern;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfilbarber extends AppCompatActivity {
    ImageView backarrow_perfil;
    CircleImageView photo_user_perfil;
    TextView nome, frasefilosofica, quantseguidoes, quantpulicacoes;
    PostAdapter postadapter;
    RecyclerView recyclerViewposts;
    ArrayList<PostPattern> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        posts = new ArrayList<>();
        postadapter = new PostAdapter(posts, getBaseContext(), false);
        setContentView(R.layout.activity_perfilbarber);
        backarrow_perfil = findViewById(R.id.backarrow_perfil);
        backarrow_perfil.setOnClickListener(v -> finish());

        photo_user_perfil = findViewById(R.id.photo_user_perfil);
        nome = findViewById(R.id.tv_nome_perfil);
        quantseguidoes = findViewById(R.id.quantseguidores_perfil);
        quantpulicacoes = findViewById(R.id.quantpublicacoes_perfil);
        frasefilosofica = findViewById(R.id.frasefilosofica_perfil);

        recyclerViewposts = findViewById(R.id.rv_pots_perfil);

        recyclerViewposts.setAdapter(postadapter);
        recyclerViewposts.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Blog").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    PostPattern post = new PostPattern();

                    post.setComentarios((ArrayList<Comments_pattern>) snapshot.get("comentarios"));
                    post.setCommentsQuant(snapshot.get("commentsQuant") + "");
                    post.setDate(snapshot.get("date") + "");
                    post.setDesc(snapshot.get("desc") + "");
                    post.setId(snapshot.get("id") + "");
                    post.setLikesids((Map<String, Object>) snapshot.get("likesids"));
                    post.setLikesquants(snapshot.get("likesquants") + "");
                    post.setNomeid(snapshot.get("nomeid") + "");
                    post.setNotificados((Map<String, Object>) snapshot.get("notificados"));
                    post.setPhoto(snapshot.get("photo") + "");
                    post.setSelflike(false);
                    post.setTag(snapshot.get("tag") + "");
                    post.setViews(snapshot.get("views") + "");
                    Map<String, Object> contamap = (Map<String, Object>) snapshot.get("pessoa");
                    Accountpattern person = new Accountpattern(null, "", "", "", null, "", "", null, null, contamap.get("id") + "", contamap.get("imgUri") + "",
                            contamap.get("nome") + "", null, "", "", "", "", null, "", "");
                    db.collection("Contas").document(contamap.get("id") + "").get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            post.setPessoa(person);
                            if (person.getId().equals(Login.blogstring)) {
                                posts.add(post);
                                Collections.sort(posts, comparador);
                                quantpulicacoes.setText(posts.size() + "");
                                postadapter.notifyDataSetChanged();
                            }
                        }
                    });

                }

            }
        });
        db.collection("Contas").document(Login.blogstring).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot a = task.getResult();
                Glide.with(getBaseContext()).load(a.get("imgUri") + "").into(photo_user_perfil);
                frasefilosofica.setText(a.get("frasefilosofica") + "");
                quantseguidoes.setText(a.get("followersquant") + "");
                nome.setText(a.get("nome") + "");
            }
        });

    }

    public Comparator<PostPattern> comparador = (o1, o2) -> {
        long id1 = Long.parseLong(o1.getDate());
        long id2 = Long.parseLong(o2.getDate());
        return Long.compare(id2, id1);
    };
}