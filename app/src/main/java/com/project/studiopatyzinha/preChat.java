package com.project.studiopatyzinha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.project.studiopatyzinha.Adapter.Adaptercontatos;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.MessageModel;
import com.project.studiopatyzinha.pattern.UserPattern;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class preChat extends AppCompatActivity {
    ImageView imgvoltar_preChat_Main;
    RecyclerView rv_contatos_admin_pessoas;
    Adaptercontatos adapter;

    Toolbar toolbar_prechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prechat);
        ArrayList<UserPattern> lista = new ArrayList<>();
        toolbar_prechat = findViewById(R.id.toolbar_prechat);
        this.setSupportActionBar(toolbar_prechat);
        this.getSupportActionBar().setTitle("");
        toolbar_prechat.setTitle("Conversas");
        toolbar_prechat.setNavigationIcon(R.drawable.ic_menu);
        toolbar_prechat.setNavigationContentDescription("Voltar para tendêndicas");
        toolbar_prechat.setNavigationOnClickListener(v -> finish());
        toolbar_prechat.setNavigationIcon(R.drawable.ic_arrow_back);
//            ArrayList<UserPattern> lista2 = new ArrayList<>();
//            searchlayout_preChat.getEditText().addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    try {
//                        lista2.clear();
//                        if (s.length() == 0) {
//                            View view = preChat.this.getCurrentFocus();
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                            searchlayout_preChat.setVisibility(View.GONE);
//                            lista2.clear();
//                            adapter = new Adaptercontatos(getBaseContext(), lista);
//                        } else {
//                            for (int i = 0; i < lista.size(); i++) {
//                                if (lista.get(i).getName_user().startsWith(s + "")) {
//                                    lista2.add(lista.get(i));
//                                }
//                            }
//                            adapter = new Adaptercontatos(getBaseContext(), lista2);
//                        }
//                        rv_contatos_admin_pessoas.setAdapter(adapter);
//                    } catch (NullPointerException e) {
//
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//
//        });
        Accountpattern pessoalocal = Login.pessoa;
        rv_contatos_admin_pessoas = findViewById(R.id.rv_contatos_admin_pessoas);
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Task<QuerySnapshot> a = null;

                if (!Login.pessoa.getCargo().equals("usuario")) {
                    a = db.collection("Contas").whereEqualTo("cargo", "usuario").get();
                } else {
                    a = db.collection("Contas").whereNotEqualTo("cargo", "usuario").get();
                }
                a.addOnCompleteListener(task -> {
                    QuerySnapshot filho = task.getResult();
                    filho.forEach(dataSnapshot1 -> {
                        Accountpattern pessoaremota = new Accountpattern();
                        pessoaremota.setId(dataSnapshot1.get("id") + "");
                        pessoaremota.setImgUri(dataSnapshot1.get("imgUri") + "");
                        pessoaremota.setNome(dataSnapshot1.get("nome") + "");
                        UserPattern Userpattern = new UserPattern(pessoaremota.getImgUri(), pessoaremota.getNome(), "", pessoaremota.getId(), "", "0");
                        if (Userpattern.getIdUser() != null) {
                            if (!Userpattern.getIdUser().equals(pessoalocal.getId())) {
                                reference.child("chats").child(pessoaremota.getId() + pessoalocal.getId()).addValueEventListener(new ValueEventListener() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        final int[] messagenews = {0};
                                        for (DataSnapshot snapshot12 : snapshot.getChildren()) {
                                            MessageModel message = snapshot12.getValue(MessageModel.class);
                                            try {
                                                if (!message.getuId().equals(pessoalocal.getId())) {
                                                    if (!message.getStatus().contains("Visualizada")) {
                                                        messagenews[0] = messagenews[0] + 1;
                                                    }
                                                }
                                                Date date = new Date(message.getTimesStamp());
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm");
                                                String hour = simpleDateFormat.format(date);
                                                Userpattern.setLast_message_user(message.getMessage());
                                                if (message.getMessage().isEmpty() && message.getFt() != null) {
                                                    Userpattern.setLast_message_user(Userpattern.getName_user() + " Enviou uma imagem");
                                                }
                                                Userpattern.setStatuaLasMessage(message.getStatus() + "");
                                                Userpattern.setTime_last_message_user(hour);
                                                if (messagenews[0] > 0) {

                                                    Userpattern.setQuant_new_message(messagenews[0] + "");
                                                } else {
                                                    Userpattern.setQuant_new_message("0");
                                                }
                                            } catch (NullPointerException e) {
                                                Userpattern.setLast_message_user(message.getMessage());
                                                if (message.getMessage().isEmpty() && message.getFt() != null) {
                                                    Userpattern.setLast_message_user(Userpattern.getName_user() + " Enviou uma imagem");
                                                }

                                                Userpattern.setStatuaLasMessage(null);
                                                Date date = new Date(message.getTimesStamp());
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" HH:mm");
                                                String hour = simpleDateFormat.format(date);
                                                Userpattern.setTime_last_message_user(hour);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                Userpattern.setName_user(pessoaremota.getNome());
                                Userpattern.setImg_user(pessoaremota.getImgUri());
                                Userpattern.setIdUser(pessoaremota.getId());
                                lista.add(0, Userpattern);
                                adapter.listall.add(0, Userpattern);

                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                });
                adapter = new Adaptercontatos(getBaseContext(), lista);
                GridLayoutManager llm = new GridLayoutManager(getBaseContext(), 1);
                runOnUiThread(() -> {
                    rv_contatos_admin_pessoas.setAdapter(adapter);
                    rv_contatos_admin_pessoas.setLayoutManager(llm);
                    adapter.notifyDataSetChanged();

                });
            }
        }).start();
    }

    public Comparator<UserPattern> comparador = new Comparator<UserPattern>() {
        @Override
        public int compare(UserPattern o1, UserPattern o2) {
            long id1 = Long.parseLong(o1.getTime_last_message_user());
            long id2 = Long.parseLong(o2.getTime_last_message_user());
            return Long.compare(id1, id2);

        }


    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.blog_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_blog).getActionView();
        String hint = "Pesquise por nomes";
        searchView.setQueryHint(hint);
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
}
