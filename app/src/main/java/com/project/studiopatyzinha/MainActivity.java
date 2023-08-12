package com.project.studiopatyzinha;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.Adapter.PostAdapter;
import com.project.studiopatyzinha.Adapter.TagAdapter;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Comments_pattern;
import com.project.studiopatyzinha.pattern.PostPattern;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    public static Toolbar toolbar;
    public static ArrayList<String> meses, semana;
    LinearLayout lembretes, pedidos_lateralS, adminOptionsparent;
    ConstraintLayout eConstultation;
    public static TextView changeaccount;
    FloatingActionButton add_post;
    public static AlertDialog alert;
    public static ActivityResultLauncher<String> mGetContent;
    public static ImageView pickup_image_add_post, img_lateral_String;
    public static String tags = "";
    RecyclerView rev_posts;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<PostPattern> posts;
    public static PostAdapter adapter;
    public static SwipeRefreshLayout refreshlayout_blog;
    public static Uri filepath;
    static TextView nome_da_pessoa_lateralString, number_news_messagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number_news_messagens = findViewById(R.id.number_news_messagens);
        add_post = findViewById(R.id.add_post);
        semana = new ArrayList<>();
        semana.add("Dom");
        semana.add("Seg");
        semana.add("Ter");
        semana.add("Qua");
        semana.add("Qui");
        semana.add("Sex");
        semana.add("Sab");
        findViewById(R.id.parent_notiLS).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this,Notifications.class));
        });
        meses = new ArrayList<>();
        meses.add("Jan");
        meses.add("Fev");
        meses.add("Mar");
        meses.add("abr");
        meses.add("Mai");
        meses.add("Jun");
        meses.add("Jul");
        meses.add("Ago");
        meses.add("Set");
        meses.add("Out");
        meses.add("Nov");
        meses.add("Dez");
        changeaccount = new TextView(getBaseContext());
        nome_da_pessoa_lateralString = findViewById(R.id.nome_da_pessoa_lateralString);
        adminOptionsparent = findViewById(R.id.adminOptionsparent);
        img_lateral_String = findViewById(R.id.img_lateral_String);
        changeaccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nome_da_pessoa_lateralString.setText(Login.pessoa.getNome());
                Glide.with(getBaseContext()).load(Login.pessoa.getImgUri()).into(img_lateral_String);
                if (!Login.pessoa.getCargo().equals("usuario")) {
                    add_post.setVisibility(View.VISIBLE);
                    adminOptionsparent.setVisibility(View.VISIBLE);
                    findViewById(R.id.parent_addnewproduct).setOnClickListener(v -> {
                        opendrawer(false);
                        startActivity(new Intent(MainActivity.this, VerOpinioes.class));
                    });
                    findViewById(R.id.parent_subordinadosLs).setOnClickListener(v -> {
                        opendrawer(false);
                        startActivity(new Intent(MainActivity.this, Subordinados.class));
                    });
                } else {
                    add_post.setVisibility(View.GONE);
                    adminOptionsparent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.aboutUslateralstring).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, AboutUs.class));
        });
        findViewById(R.id.pedidos_produtos_lateralS).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, Pedidos.class));
        });
        findViewById(R.id.suporte_tecnico).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, Sup.class));
        });
        changeaccount.setText("");
        eConstultation = findViewById(R.id.eConstultation);
        pedidos_lateralS = findViewById(R.id.pedidos_lateralS);
        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        lembretes = findViewById(R.id.lembretes);
        lembretes.setOnClickListener(v -> {
            opendrawer(false);
            Intent intent = new Intent(MainActivity.this, TaskManager.class);
            startActivity(intent);
        });
        pedidos_lateralS.setOnClickListener(v -> {
            opendrawer(false);
            Intent intent = null;
            if (Login.pessoa.getCargo().equals("usuario")) {
                intent = new Intent(MainActivity.this, Agendamentos.class);
            } else {
                intent = new Intent(MainActivity.this, AgendamentosAdmin.class);
            }
            opendrawer(false);
            startActivity(intent);
        });
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        toolbar.setTitle("Tendências");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationContentDescription("Abrir o menu lateral");
        toolbar.setNavigationOnClickListener(v -> {
            opendrawer(true);
        });
        findViewById(R.id.configuracoes_lateralstring).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, Configuracoes.class));
        });
        eConstultation.setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, preChat.class));
        });
        findViewById(R.id.my_information_lateralString).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, AccountInfo.class));
        });
        findViewById(R.id.produtos_lateralS).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, Produtos.class));
        });
        findViewById(R.id.parent_gerenciarLs).setOnClickListener(v -> {
            opendrawer(false);
            startActivity(new Intent(this, Gerenciar.class));
        });
        refreshlayout_blog = findViewById(R.id.refreshlayout_blog);
        rev_posts = findViewById(R.id.rev_posts);
        refreshlayout_blog.setOnRefreshListener(() -> {
            initBlog();
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                Intent intent = new Intent(MainActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }

        });
        new Thread(() -> {
            add_post.setOnClickListener(v -> {
                PostPattern post = new PostPattern();
                addOrEdit(MainActivity.this, 0, false, post);
            });
            posts = new ArrayList<>();
            adapter = new PostAdapter(posts, MainActivity.this, true);
            rev_posts.setAdapter(adapter);
            rev_posts.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            initBlog();
        }).start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkExit();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void checkExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja sair ?")
                .setCancelable(true)
                .setPositiveButton("Sim", (dialog, id) -> finish())
                .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static void addOrEdit(Context context, int pos, boolean editando, PostPattern post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogCustomfull);
        View view = LayoutInflater.from(context).inflate(R.layout.add_post, null, false);
        ArrayList<String> string = new ArrayList<>();
        TagAdapter adapte2r = new TagAdapter(string);
        RecyclerView rv_tags = view.findViewById(R.id.rv_tags_add_post);
        pickup_image_add_post = view.findViewById(R.id.pickup_image_add_post);
        TextInputLayout pubname_post = view.findViewById(R.id.pubname_post);
        TextInputLayout txt_add_post = view.findViewById(R.id.txt_add_post);
        if (editando) {
            pubname_post.getEditText().setText(post.getNomeid());
            txt_add_post.getEditText().setText(post.getDesc());
            String[] stringtasg = post.getTag().split(";");
            string.addAll(Arrays.asList(stringtasg));
            adapte2r.notifyDataSetChanged();
            if (!post.getPhoto().isEmpty()) {
                Glide.with(context).load(post.getPhoto()).into(pickup_image_add_post);
            }
            tags = post.getTag();
            if (stringtasg.length > 0 && !stringtasg[0].isEmpty()) {
                rv_tags.setVisibility(View.VISIBLE);
            }
        } else {
            Map<String, Object> s = new HashMap<>();
            post.setCommentsQuant("0");
            post.setDate(System.currentTimeMillis() + "");
            post.setComentarios(new ArrayList<>());
            post.setDesc("");
            post.setLikesids(s);
            post.setLikesquants("0");
            post.setNomeid("");
            post.setPessoa(Login.pessoa);
            post.setPhoto("");
            post.setSelflike(false);
            if (tags.isEmpty()) {
                for (int i = 0; i < string.size(); i++) {
                    tags += string.get(i) + ";";
                }
            }
            post.setTag(tags);
            post.setViews("0");
            post.setNotificados(s);
        }
        TextInputLayout tags_add_post = view.findViewById(R.id.tags_add_post);
        rv_tags.setAdapter(adapte2r);
        rv_tags.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        tags_add_post.setEndIconOnClickListener(v1 -> {
            tags += tags_add_post.getEditText().getText().toString() + ";";
            string.add(0, tags_add_post.getEditText().getText().toString());
            adapte2r.notifyDataSetChanged();
            rv_tags.setVisibility(View.VISIBLE);
            tags_add_post.getEditText().setText("");
        });
        ImageView close = view.findViewById(R.id.closedialog_add_post);
        Button publicar = view.findViewById(R.id.publicar_add_post);
        pickup_image_add_post.setOnClickListener(v1 -> {
            mGetContent.launch("image/*");
        });
        close.setOnClickListener(v1 -> {
            alert.dismiss();
        });

        publicar.setOnClickListener(v1 -> {
            if (!pubname_post.getEditText().getText().toString().trim().isEmpty()) {
                final ProgressDialog pd = new ProgressDialog(context);
                pd.setTitle("Enviando publicação...");
                pd.show();
                String name = pubname_post.getEditText().getText().toString().trim();
                String desc = txt_add_post.getEditText().getText().toString().trim();
                post.setDesc(desc);
                post.setNomeid(name);
                if (editando) {
                    post.setId(post.getId());
                } else {
                    post.setTag(tags);
                    tags = "";
                    post.setId(Login.pessoa.getId() + ":" + System.currentTimeMillis() + ";" + name);
                }
                if (filepath != null) {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    storageRef.child("imagens blog").child(Login.pessoa.getId()).putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().
                                    addOnSuccessListener(uri -> {
                                        post.setPhoto(uri + "");
                                        db.collection("Blog").document(post.getId()).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                filepath = null;
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                Map<String, Object> map = new HashMap<>();
                                                map.put("desc", post.getDesc());
                                                map.put("id", post.getId());
                                                database.getReference("Blog").child(Login.pessoa.getId() + "").child(post.getId()).setValue(map).addOnCompleteListener(command -> {
                                                    pd.dismiss();
                                                });
                                                if (editando) {
                                                    posts.remove(pos);
                                                    adapter.postsAll.remove(pos);
                                                }
                                                posts.add(pos, post);
                                                adapter.postsAll.add(pos, post);

                                                adapter.notifyDataSetChanged();
                                                alert.dismiss();
                                                Toast.makeText(context, "Terminamos", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    });
                        }
                    }).addOnProgressListener(taskSnapshot -> {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage(progressPercent + "  %");
                    });
                } else {
                    db.collection("Blog").document(post.getId()).set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            filepath = null;
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            Map<String, Object> map = new HashMap<>();
                            map.put("desc", post.getDesc());
                            map.put("id", post.getId());
                            database.getReference("Blog").child(Login.pessoa.getId() + "").child(post.getId()).setValue(map).addOnCompleteListener(command -> {
                                pd.dismiss();
                            });
                            if (editando) {
                                posts.remove(pos);
                                adapter.postsAll.remove(pos);

                            }
                            posts.add(pos, post);
                            adapter.postsAll.add(pos, post);

                            adapter.notifyDataSetChanged();
                            alert.dismiss();
                            Toast.makeText(context, "Terminamos", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } else {
                pubname_post.setError("Preencha esse campo");
            }

        });
        builder.setView(view);
        alert = builder.create();
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        } else {
            alert.setView(null);
            alert.setView(view);
        }
        alert.show();
    }

    public static void initBlog() {
        refreshlayout_blog.setRefreshing(true);
        posts.clear();
        adapter.postsAll.clear();
        db.collection("Blog").orderBy("date").get().addOnCompleteListener(task -> {//adicionar limite para quantidade de postagens e ir aumentando a lista comforme for rodando
            if (task.isSuccessful()) {
                task.getResult().forEach(snapshot -> {
                    Map<String, Object> mippessoa = (Map<String, Object>) snapshot.get("pessoa");
                    Accountpattern pessoa = new Accountpattern((Map<String, Object>) mippessoa.get("barbersposts"),
                            mippessoa.get("cargo") + "", mippessoa.get("dia_salario") + "",
                            mippessoa.get("email") + "", (Map<String, Object>) mippessoa.get("followersid"),
                            mippessoa.get("followersquant") + "", mippessoa.get("frasefilosofica") + "",
                            mippessoa.get("grupoAbaixo") + "", (Map<String, Object>) mippessoa.get("horario"),
                            mippessoa.get("id") + "", mippessoa.get("imgUri") + "", mippessoa.get("nome") + "",
                            (Map<String, Object>) mippessoa.get("opinioes"),
                            mippessoa.get("percentual") + "", mippessoa.get("quantpost") + "",
                            mippessoa.get("selected") + "", mippessoa.get("senha") + "",(Map<String, Object>) mippessoa.get("horasdiaspagamentos"),mippessoa.get("ultimosalario") + "",mippessoa.get("ifgerente") + "");
                    PostPattern mip2 = new PostPattern(snapshot.get("commentsQuant") + "",
                            snapshot.get("date") + "", snapshot.get("desc") + "", snapshot.get("id") + "",
                            (Map<String, Object>) snapshot.get("likesids"), snapshot.get("likesquants") + "",
                            snapshot.get("nomeid") + "", pessoa, snapshot.get("photo") + "", Boolean.parseBoolean(snapshot.get("selflike") + ""),
                            snapshot.get("tag") + "", snapshot.get("views") + "", (Map<String, Object>) snapshot.get("notificados"), (ArrayList<Comments_pattern>) snapshot.get("comentarios"));
                    posts.add(0, mip2);
                    adapter.postsAll.add(0, mip2);
                    adapter.notifyDataSetChanged();
                });
                refreshlayout_blog.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blog_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_blog).getActionView();
        searchView.setQueryHint("Pesquise por palavras-chaves");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == -1 && requestCode == 101) {
                String result = data.getStringExtra("RESULT");
                Uri resultUri = null;
                if (result != null) {
                    resultUri = Uri.parse(result);
                }
                filepath = resultUri;
                pickup_image_add_post.setImageURI(resultUri);
            }
        }
    }

    private void opendrawer(boolean abrir) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (abrir) {
            drawerLayout.open();
        } else {
            drawerLayout.close();
        }
    }

    public static void changenumberchatnoti(String text) {
        if (Login.messagenews > 0) {
            number_news_messagens.setText(text);
            number_news_messagens.setVisibility(View.VISIBLE);
        } else {
            number_news_messagens.setVisibility(View.GONE);
        }
    }
}