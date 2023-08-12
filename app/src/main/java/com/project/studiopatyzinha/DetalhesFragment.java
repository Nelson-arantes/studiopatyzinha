package com.project.studiopatyzinha;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.Adapter.cat_LCAdapter;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.pattern.Pedidospattern;
import com.project.studiopatyzinha.pattern.TransicaoPattern;
import com.project.studiopatyzinha.pattern.categoriasLC;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class DetalhesFragment extends Fragment {

    cat_LCAdapter adapter;
    TextView entrada_number, saida_number, saldo_number;
    RecyclerView rv_categorias_livrocaixa;
    ArrayList<categoriasLC> lista;
    String saida;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SwipeRefreshLayout refreshlayout_livrocaixa;
    TextView antesDate_txt, atualDate_txt;
    Calendar mesatual, mesantes;
    DatePickerDialog dialog1;
    DatePickerDialog dialog2;
    ConstraintLayout parent_remakecount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalhes, container, false);
        view.findViewById(R.id.voltardetalhes_livroCaixa).setOnClickListener(v -> {
            livroCaixa.mudarFragmento.setText("resumo");
        });

        rv_categorias_livrocaixa = view.findViewById(R.id.rv_categorias_livrocaixa_detalhes);
        parent_remakecount = view.findViewById(R.id.parent_remakecount);
        parent_remakecount.setVisibility(View.GONE);
        ImageView TPDantes_livrocaixa = view.findViewById(R.id.TPDantes_livrocaixa_detalhes);
        ImageView TPDdepois_livrocaixa = view.findViewById(R.id.TPDdepois_livrocaixa_detalhes);
        antesDate_txt = view.findViewById(R.id.antesDate_txt_detalhes);
        atualDate_txt = view.findViewById(R.id.atualDate_txt_detalhes);
        mesantes = Calendar.getInstance();
        mesatual = Calendar.getInstance();
        mesantes.add(Calendar.MONTH, -1);
        String dia = "";
        String mes = "";
        if (mesantes.get(Calendar.DAY_OF_MONTH) < 10) {
            dia += "0";
        }
        dia += mesantes.get(Calendar.DAY_OF_MONTH);
        if ((mesantes.get(Calendar.MONTH) + 1) < 10) {
            mes += "0";
        }
        mes += (mesantes.get(Calendar.MONTH) + 1);
        antesDate_txt.setText(dia + "/" + mes + "/" + mesantes.get(Calendar.YEAR));
        TPDantes_livrocaixa.setOnClickListener(v -> {
            dialog1 = new DatePickerDialog(
                    getContext(), (view1, year, month, dayOfMonth) -> {
                Calendar calendario = Calendar.getInstance();
                calendario.set(year, month, dayOfMonth);
                if (mesatual.getTimeInMillis() < calendario.getTimeInMillis()) {
                    dialog1.updateDate(mesantes.get(Calendar.YEAR), mesantes.get(Calendar.MONTH), mesantes.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(getContext(), "Período invalido", Toast.LENGTH_SHORT).show();

                } else {
                    mesantes.set(year, month, dayOfMonth);
                    String dia1 = "";
                    String mes1 = "";
                    if (mesantes.get(Calendar.DAY_OF_MONTH) < 10) {
                        dia1 += "0";
                    }
                    dia1 += mesantes.get(Calendar.DAY_OF_MONTH);
                    if ((mesantes.get(Calendar.MONTH) + 1) < 10) {
                        mes1 += "0";
                    }
                    mes1 += (mesantes.get(Calendar.MONTH) + 1);
                    antesDate_txt.setText(dia1 + "/" + mes1 + "/" + mesantes.get(Calendar.YEAR));
                }
            }, mesantes.get(Calendar.YEAR), mesantes.get(Calendar.MONTH), mesantes.get(Calendar.DAY_OF_MONTH));
            dialog1.show();
        });
        TPDdepois_livrocaixa.setOnClickListener(v -> {

            dialog2 = new DatePickerDialog(
                    getContext(), (view12, year, month, dayOfMonth) -> {
                Calendar calendario = Calendar.getInstance();
                calendario.set(year, month, dayOfMonth);
                if (mesantes.getTimeInMillis() > calendario.getTimeInMillis()) {
                    dialog1.updateDate(mesatual.get(Calendar.YEAR), mesatual.get(Calendar.MONTH), mesatual.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(getContext(), "Período invalido", Toast.LENGTH_SHORT).show();
                } else {
                    mesatual.set(year, month, dayOfMonth);
                    String dia12 = "";
                    String mes12 = "";
                    if (mesatual.get(Calendar.DAY_OF_MONTH) < 10) {
                        dia12 += "0";
                    }
                    dia12 += mesatual.get(Calendar.DAY_OF_MONTH);
                    if ((mesatual.get(Calendar.MONTH) + 1) < 10) {
                        mes12 += "0";
                    }
                    mes12 += (mesatual.get(Calendar.MONTH) + 1);
                    atualDate_txt.setText(dia12 + "/" + mes12 + "/" + mesatual.get(Calendar.YEAR));
                }
            }, mesatual.get(Calendar.YEAR), mesatual.get(Calendar.MONTH), mesatual.get(Calendar.DAY_OF_MONTH));
            dialog2.show();
        });


        dia = "";
        mes = "";
        if (mesatual.get(Calendar.DAY_OF_MONTH) < 10) {
            dia += "0";
        }
        dia += mesatual.get(Calendar.DAY_OF_MONTH);
        if ((mesatual.get(Calendar.MONTH) + 1) < 10) {
            mes += "0";
        }
        mes += (mesatual.get(Calendar.MONTH) + 1);
        atualDate_txt.setText(dia + "/" + mes + "/" + mesatual.get(Calendar.YEAR));
        refreshlayout_livrocaixa = view.findViewById(R.id.refreshlayout_detalhes);
        refreshlayout_livrocaixa.setOnRefreshListener(() -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Livro caixa").child("now").setValue(System.currentTimeMillis() + "");
        });
        entrada_number = view.findViewById(R.id.rentrada_number_detalhes);
        saida_number = view.findViewById(R.id.saida_number_detalhes);
        saldo_number = view.findViewById(R.id.saldo_number_detalhes);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Livro caixa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                initvalues(mesantes.getTimeInMillis() + "", mesatual.getTimeInMillis() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void initvalues(String inicio, String fim) {
        refreshlayout_livrocaixa.setRefreshing(true);
        saida = "0";
        lista = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Agendamentos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> a = snapshot.getChildren();
                a.forEach(dataSnapshot -> {
                    Map<String, Object> idusermap = (Map<String, Object>) dataSnapshot.getValue();
                    idusermap.forEach((s, servico) -> {
                        Map<String, Object> servicomap = (Map<String, Object>) servico;
                        Appoitmentpattern agendamento = new Appoitmentpattern();
                        agendamento.setValorservico(servicomap.get("valorservico") + "");
                        agendamento.setHora(servicomap.get("hora") + "");
                        agendamento.setWeekyear(servicomap.get("weekyear") + "");
                        agendamento.setTimeinmilis(servicomap.get("timeinmilis") + "");
                        agendamento.setNomefunc(servicomap.get("nomefunc") + "");
                        agendamento.setTempo(servicomap.get("tempo") + "");
                        agendamento.setIdpessoa(servicomap.get("idpessoa") + "");
                        agendamento.setIdservico(servicomap.get("idservico") + "");
                        agendamento.setFunc(servicomap.get("func") + "");
                        agendamento.setNomepessoa(servicomap.get("nomepessoa") + "");
                        agendamento.setConcluido(servicomap.get("concluido") + "");
                        agendamento.setAlarm(servicomap.get("alarm") + "");
                        agendamento.setId(servicomap.get("id") + "");
                        agendamento.setNomeservico(servicomap.get("nomeservico") + "");
                        agendamento.setDia(servicomap.get("dia") + "");
                        if (agendamento.getConcluido().equals("true")) {
                            lista.add(new categoriasLC(agendamento.getId(), agendamento.getNomeservico(),
                                    agendamento.getNomepessoa(), agendamento.getValorservico(), ColorTemplate.MATERIAL_COLORS[3]));
                            Collections.sort(lista, comparador);
                            adapter.notifyDataSetChanged();
                        }
//                        if (agendamento.getConcluido().equals("true")) {
//                            Calendar comecocalendar = Calendar.getInstance();
//                            Calendar atualcalendar = Calendar.getInstance();
//                            atualcalendar.set(Integer.parseInt(agendamento.getDia().split("/")[2]), Integer.parseInt(agendamento.getDia().split("/")[1]), Integer.parseInt(agendamento.getDia().split("/")[0]));
//                            atualcalendar.add(Calendar.MONTH, -1);
//                            Calendar fimcalendar = Calendar.getInstance();
//                            comecocalendar.setTimeInMillis(Long.parseLong(inicio));
//                            fimcalendar.setTimeInMillis(Long.parseLong(fim));
//                            if (atualcalendar.compareTo(comecocalendar) >= 0 && atualcalendar.compareTo(fimcalendar) <= 0) {
//                                double as = Double.parseDouble(lista.get(0).getValor().replace(",", "."));
//                                as += Double.parseDouble(agendamento.getValorservico().replace(",", "."));
//                                lista.get(0).setValor(moneyToString(as + ""));
//                                db.collection("Contas").document(agendamento.getFunc()).get().addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        double valor = Double.parseDouble(task.getResult().get("percentual") + "");
//                                        double valorpercent = (Double.parseDouble(agendamento.getValorservico().replace(",", ".")) * valor) / 100;
//                                        double bs = Double.parseDouble(lista.get(3).getValor().replace(",", "."));
//                                        bs += valorpercent;
//                                        lista.get(3).setValor(moneyToString(bs + ""));
//                                        adapter.notifyDataSetChanged();
//                                        remakecount();
//                                    }
//                                });

//                            }
//                        }
                    });
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });//agendamento

        db.collection("Livro caixa").get().addOnSuccessListener(snapshot -> {
            snapshot.forEach(snapshot1 -> {
                Map<String, Object> map = snapshot1.getData();
                TransicaoPattern pattern = new TransicaoPattern(map.get("id") + "", map.get("valor") + "", map.get("data") + "", map.get("operacao") + "");
                Calendar comecocalendar = Calendar.getInstance();
                comecocalendar.setTimeInMillis(Long.parseLong(inicio));
                Calendar atualcalendar = Calendar.getInstance();
                atualcalendar.setTimeInMillis(Long.parseLong(pattern.getId()));
                Calendar fimcalendar = Calendar.getInstance();
                fimcalendar.setTimeInMillis(Long.parseLong(fim));
                int s = 0;
                if (pattern.getOperacao().equals("Retirada")) {
                    s = 2;
                }
                if (atualcalendar.compareTo(comecocalendar) > 0 && atualcalendar.compareTo(fimcalendar) < 0) {
                    lista.add(new categoriasLC(pattern.getId(), pattern.getOperacao(), "", pattern.getValor(), ColorTemplate.MATERIAL_COLORS[s]));
                    Collections.sort(lista, comparador);
                    adapter.notifyDataSetChanged();

                }
            });
        });

        db.collection("Pedidos").get().addOnCompleteListener(snapshotTask -> {
            for (QueryDocumentSnapshot snapshot : snapshotTask.getResult()) {
                Map<String, Object> map = snapshot.getData();
                Pedidospattern pattern = new Pedidospattern();
                pattern.setQuantidade(map.get("quantidade") + "");
                pattern.setDataCompra(map.get("dataCompra") + "");
                pattern.setValorTotal(map.get("valorTotal") + "");
                pattern.setIdPedido(map.get("idPedido") + "");
                pattern.setProdutosString(map.get("produtosString") + "");
                pattern.setPedidoretirado(map.get("Pedidoretirado") + "");
                pattern.setFormaPagamento(map.get("formaPagamento") + "");
                pattern.setDataRetirada(map.get("dataRetirada") + "");
                pattern.setDia(map.get("dia") + "");
                pattern.setStatus(map.get("status") + "");
                pattern.setNomePessoa(map.get("nomePessoa") + "");

                Calendar atualcalendar = Calendar.getInstance();
                atualcalendar.setTimeInMillis(Long.parseLong(pattern.getDataRetirada()));
                atualcalendar.set(Calendar.HOUR_OF_DAY, 0);
                atualcalendar.set(Calendar.MINUTE, 0);
                atualcalendar.set(Calendar.SECOND, 0);

                Calendar comecocalendar = Calendar.getInstance();
                comecocalendar.setTimeInMillis(Long.parseLong(inicio));
                Calendar fimcalendar = Calendar.getInstance();
                fimcalendar.setTimeInMillis(Long.parseLong(fim));
                if (pattern.getPedidoretirado().equals("true") && atualcalendar.compareTo(comecocalendar) > 0 && atualcalendar.compareTo(fimcalendar) < 0) {
                    lista.add(new categoriasLC(pattern.getIdPedido(), "Pedido N°" + pattern.getIdPedido(),
                            pattern.getNomePessoa(), pattern.getValorTotal(), ColorTemplate.MATERIAL_COLORS[3]));
                    Collections.sort(lista, comparador);
                    adapter.notifyDataSetChanged();
                }
            }
            refreshlayout_livrocaixa.setRefreshing(false);
        });


        adapter = new cat_LCAdapter(lista);
        rv_categorias_livrocaixa.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_categorias_livrocaixa.setAdapter(adapter);
        entrada_number.setTextColor(ColorTemplate.MATERIAL_COLORS[0]);
        saldo_number.setTextColor(ColorTemplate.MATERIAL_COLORS[1]);
        saida_number.setTextColor(ColorTemplate.MATERIAL_COLORS[2]);
    }

//    private void remakecount() {
//        String valor = "0";
//        for (int i = 0; i < 4; i++) {
//            String s = lista.get(i).getValor().replaceAll("[R$ ]", "").replace(",", ".");
//            valor = "" + (Double.parseDouble(valor) + Double.parseDouble(s));
//        }
//        double a = Double.parseDouble(valor) - Double.parseDouble(lista.get(4).getValor().replaceAll("[R$ ]", "").replace(",", ".")) - Double.parseDouble(lista.get(5).getValor());
//        saida_number.setText("R$ " + lista.get(4).getValor());
//        entrada_number.setText("R$ " + moneyToString(valor));
//        lista.get(6).setValor(moneyToString(a + ""));
//        adapter.notifyDataSetChanged();
//        saldo_number.setText("R$ " + moneyToString("" + a));
//    }

    private static String moneyToString(String money) {
        String valorcerto = "";
        if (money.equals("0")) {
            valorcerto = "0.00";
        } else {
            if (money.charAt(money.length() - 2) == '.') {
                money += "0";
            }
            for (int i = 0; i < money.length(); i++) {
                char s = money.charAt(i);
                if (s == '.') {
                    valorcerto += "," + money.charAt(i + 1);
                    valorcerto += money.charAt(i + 2);
                    break;

                }
                valorcerto += s;
            }
        }

        return valorcerto.replace(".", ",");
    }

    public Comparator<categoriasLC> comparador = new Comparator<categoriasLC>() {
        @Override
        public int compare(categoriasLC o1, categoriasLC o2) {
            long id1 = Long.parseLong(o1.getId());
            long id2 = Long.parseLong(o2.getId());
            return Long.compare(id1, id2);
        }
    };
}