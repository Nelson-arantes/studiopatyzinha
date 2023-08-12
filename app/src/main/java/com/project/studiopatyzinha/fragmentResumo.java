package com.project.studiopatyzinha;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class fragmentResumo extends Fragment {
    cat_LCAdapter adapter;
    TextView entrada_number, saida_number, saldo_number;
    RecyclerView rv_categorias_livrocaixa;
    ArrayList<categoriasLC> lista;
    String saida;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<TransicaoPattern> trasisoes;
    SwipeRefreshLayout refreshlayout_livrocaixa;
    TextView antesDate_txt, atualDate_txt;
    Calendar mesatual, mesantes;
    DatePickerDialog dialog1;
    DatePickerDialog dialog2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_resumo, container, false);
        rv_categorias_livrocaixa = view.findViewById(R.id.rv_categorias_livrocaixa);
        ImageView TPDantes_livrocaixa = view.findViewById(R.id.TPDantes_livrocaixa);
        ImageView TPDdepois_livrocaixa = view.findViewById(R.id.TPDdepois_livrocaixa);
        Button verdetalhes = view.findViewById(R.id.verdetalhes_livroCaixa);
        antesDate_txt = view.findViewById(R.id.antesDate_txt);
        atualDate_txt = view.findViewById(R.id.atualDate_txt);
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
        verdetalhes.setOnClickListener(v -> {
            livroCaixa.mudarFragmento.setText("detalhado");
        });
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
        trasisoes = new ArrayList<>();
        refreshlayout_livrocaixa = view.findViewById(R.id.refreshlayout_livrocaixa);
        refreshlayout_livrocaixa.setOnRefreshListener(() -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Livro caixa").child("now").setValue(System.currentTimeMillis() + "");
        });
        entrada_number = view.findViewById(R.id.entrada_number);
        saida_number = view.findViewById(R.id.saida_number);
        saldo_number = view.findViewById(R.id.saldo_number);
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
        lista.add(new categoriasLC("Total dos Serviços (+)", "", "0.00", ColorTemplate.MATERIAL_COLORS[3]));
        lista.add(new categoriasLC("Total dos Pedidos (+)", "", "0.00", ColorTemplate.MATERIAL_COLORS[3]));
        lista.add(new categoriasLC("Recebimentos (+)", "Pagamento dos clientes (fiado)", "0.00", R.color.purple_500));
        lista.add(new categoriasLC("Reforco (+)", "", "0.00", ColorTemplate.MATERIAL_COLORS[0]));
        lista.add(new categoriasLC("Retiradas (-)", "", moneyToString(saida), ColorTemplate.MATERIAL_COLORS[2]));
        lista.add(new categoriasLC("Vendas fiado (-)", "", "0.00", ColorTemplate.MATERIAL_COLORS[3]));
        lista.add(new categoriasLC("Saldo (=)", "", "0.00", ColorTemplate.MATERIAL_COLORS[1]));
        ArrayList<Appoitmentpattern> agendamentos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Agendamentos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agendamentos.clear();
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
                        agendamentos.add(agendamento);
                        if (agendamento.getConcluido().equals("true")) {
                            Calendar comecocalendar = Calendar.getInstance();
                            Calendar atualcalendar = Calendar.getInstance();
                            atualcalendar.set(Integer.parseInt(agendamento.getDia().split("/")[2]), Integer.parseInt(agendamento.getDia().split("/")[1]), Integer.parseInt(agendamento.getDia().split("/")[0]));
                            atualcalendar.add(Calendar.MONTH, -1);
                            Calendar fimcalendar = Calendar.getInstance();
                            comecocalendar.setTimeInMillis(Long.parseLong(inicio));
                            fimcalendar.setTimeInMillis(Long.parseLong(fim));
                            if (atualcalendar.compareTo(comecocalendar) >= 0 && atualcalendar.compareTo(fimcalendar) <= 0) {
                                double as = Double.parseDouble(lista.get(0).getValor().replace(",", "."));
                                as += Double.parseDouble(agendamento.getValorservico().replace(",", "."));
                                lista.get(0).setValor(moneyToString(as + ""));
                                db.collection("Contas").document(agendamento.getFunc()).get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        double valor = Double.parseDouble(task.getResult().get("percentual") + "");
                                        double valorpercent = (Double.parseDouble(agendamento.getValorservico().replace(",", ".")) * valor) / 100;
                                        double bs = Double.parseDouble(lista.get(4).getValor().replace(",", "."));
                                        bs += valorpercent;
                                        lista.get(4).setValor(moneyToString(bs + ""));
                                        adapter.notifyDataSetChanged();
                                        remakecount();
                                    }
                                });

                            }
                        }
                    });
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db.collection("Pedidos").get().addOnCompleteListener(command -> {
            command.getResult().forEach(snapshot -> {
                Map<String, Object> map = (Map<String, Object>) snapshot.getData();
                Pedidospattern pattern = new Pedidospattern();
                pattern.setQuantidade(map.get("quantidade") + "");
                pattern.setDataCompra(map.get("dataCompra") + "");
                pattern.setValorTotal(map.get("valorTotal") + "");
                pattern.setIdPedido(map.get("idPedido") + "");
                pattern.setProdutosString(map.get("produtosString") + "");
                pattern.setPedidoretirado(map.get("Pedidoretirado") + "");
                pattern.setFormaPagamento(map.get("formaPagamento") + "");
                pattern.setDataRetirada(map.get("dataRetirada") + "");
                pattern.setStatus(map.get("status") + "");
                if(pattern.getPedidoretirado().equals("true")){
                    Calendar comecocalendar = Calendar.getInstance();
                    Calendar atualcalendar = Calendar.getInstance();
                    atualcalendar.setTimeInMillis(Long.parseLong(pattern.getDataCompra()));
//                    atualcalendar.add(Calendar.MONTH, -1);
                    Calendar fimcalendar = Calendar.getInstance();
                    comecocalendar.setTimeInMillis(Long.parseLong(inicio));
                    fimcalendar.setTimeInMillis(Long.parseLong(fim));
                    if (atualcalendar.compareTo(comecocalendar) >= 0 && atualcalendar.compareTo(fimcalendar) <= 0) {
                    double as = Double.parseDouble(lista.get(1).getValor().replace(",", "."));
                    as += Double.parseDouble(pattern.getValorTotal());
                    lista.get(1).setValor(moneyToString(as + ""));
                }}
                adapter.notifyDataSetChanged();
                remakecount();

            });
        });
        db.collection("Livro caixa").get().addOnSuccessListener(snapshot -> {
            snapshot.forEach(snapshot1 -> {
                Map<String, Object> map = snapshot1.getData();
                TransicaoPattern pattern = new TransicaoPattern(map.get("id") + "", map.get("valor") + "", map.get("data") + "", map.get("operacao") + "");
                double valor = 0;
                Calendar comecocalendar = Calendar.getInstance();
                Calendar atualcalendar = Calendar.getInstance();
                atualcalendar.setTimeInMillis(Long.parseLong(pattern.getId()));
                atualcalendar.add(Calendar.MONTH, -1);
                Calendar fimcalendar = Calendar.getInstance();
                comecocalendar.setTimeInMillis(Long.parseLong(inicio));
                fimcalendar.setTimeInMillis(Long.parseLong(fim));
                if (atualcalendar.compareTo(comecocalendar) > 0 && atualcalendar.compareTo(fimcalendar) < 0) {
                    if (pattern.getOperacao().equals("Reforço")) {
                        valor = StringToMoney(lista.get(3).getValor());
                        valor += StringToMoney(pattern.getValor());
                        lista.get(3).setValor(moneyToString(valor + ""));
                        adapter.notifyDataSetChanged();
                    }
                    if (pattern.getOperacao().equals("Retirada")) {
                        valor = StringToMoney(lista.get(4).getValor());
                        valor += StringToMoney(pattern.getValor());
                        lista.get(4).setValor(moneyToString(valor + ""));
                        adapter.notifyDataSetChanged();
                    }
                    trasisoes.add(pattern);
                    remakecount();
                }
            });
            refreshlayout_livrocaixa.setRefreshing(false);
        });
        adapter = new cat_LCAdapter(lista);
        rv_categorias_livrocaixa.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_categorias_livrocaixa.setAdapter(adapter);
        entrada_number.setTextColor(ColorTemplate.MATERIAL_COLORS[0]);
        saida_number.setTextColor(ColorTemplate.MATERIAL_COLORS[2]);
        saldo_number.setTextColor(ColorTemplate.MATERIAL_COLORS[1]);
    }

    private void remakecount() {
        String valor = "0";
        for (int i = 0; i < 4; i++) {
            String s = StringToMoney(lista.get(i).getValor()) + "";
            valor = "" + (StringToMoney(valor) + Double.parseDouble(s));
        }
        double a = StringToMoney(valor) - StringToMoney(lista.get(4).getValor()) - StringToMoney(lista.get(5).getValor());
        saida_number.setText("R$ " + lista.get(4).getValor());
        entrada_number.setText("R$ " + moneyToString(valor));
        lista.get(lista.size() - 1).setValor(moneyToString(a + ""));
        adapter.notifyDataSetChanged();
        saldo_number.setText("R$ " + moneyToString("" + a));
    }

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


    private double StringToMoney(String money) {
        money = money.replaceAll("[R$ ]", "").replace(",", ".");
        return Double.parseDouble(money);

    }


}
