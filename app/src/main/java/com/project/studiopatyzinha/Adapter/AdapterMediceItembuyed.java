
package com.project.studiopatyzinha.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.CartActivity;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.Pedidos;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Pedidospattern;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AdapterMediceItembuyed extends RecyclerView.Adapter<AdapterMediceItembuyed.meuViewHolder> {
    Context context;
    ArrayList<Pedidospattern> lista;
    AlertDialog a;
    String admin;

    public AdapterMediceItembuyed(ArrayList<Pedidospattern> list, Context context, String ifadmin) {
        lista = list;
        admin = ifadmin;
        this.context = context;
    }

    @NonNull
    @Override
    public meuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new meuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_item_pattern, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull meuViewHolder holder, int position) {
        Pedidospattern pp = lista.get(holder.getAdapterPosition());

        if (pp.getProdutoslist().size() > 1) {
            holder.quantidadeItens.setText(pp.getProdutoslist().size() + " itens");
        } else {
            holder.quantidadeItens.setText(pp.getProdutoslist().size() + " item");
        }


        switch (pp.getStatus()){
            case "Cancelado":
                holder.dataRetirada.setVisibility(View.GONE);
                holder.cancelar_pedido.setVisibility(View.GONE);
                holder.verenderecos.setVisibility(View.GONE);
                break;
            case "Processando":
                holder.verenderecos.setVisibility(View.VISIBLE);
                holder.cancelar_pedido.setVisibility(View.VISIBLE);
                break;
            case "Retirado":
                holder.cancelar_pedido.setVisibility(View.GONE);
                holder.buyAgain.setVisibility(View.GONE);
                break;
        }
        if (admin.equals("true")) {
            holder.verenderecos.setVisibility(View.GONE);
            holder.pedidoretirado.setVisibility(View.VISIBLE);
            holder.pedidoretirado.setChecked(pp.getPedidoretirado().equals("true"));
            holder.pedidoretirado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Pedidos").document(pp.getIdPedido()).update("Pedidoretirado", isChecked + "");
                String status = "Retirado";
                if (isChecked) {
                    status = "Processando";
                }
                db.collection("Pedidos").document(pp.getIdPedido()).update("status", status);
            });
        } else {
            holder.pedidoretirado.setVisibility(View.GONE);
        }
        holder.totalValorPedido.setText(context.getString(R.string.unidademontaria) + pp.getValorTotal().replace(".", ","));
        String idsProdutos = "";
        for (int j = 0; j < pp.getProdutoslist().size(); j++) {
            idsProdutos += pp.getProdutoslist().get(j).getIdexterno() + ":" + pp.getProdutoslist().get(j).getQuant_produto() + ";";
        }
        Produtoadapter adapterMediceItem = new Produtoadapter(pp.getProdutoslist(), "True", 1, pp.getIdPedido(), idsProdutos, context);
        holder.cancelar_pedido.setOnClickListener(v -> {
            if (pp.getStatus().equals("Processando")) {
                holder.cancelarPedido(pp);
            }
            holder.dataRetirada.setVisibility(View.GONE);
            pp.setStatus("Cancelado");
            holder.acompanhamentoPedido.setText("Cancelado");
            holder.cancelar_pedido.setVisibility(View.GONE);
            holder.verenderecos.setVisibility(View.GONE);
        });
        holder.acompanhamentoPedido.setText(pp.getStatus());
        holder.produtos.setLayoutManager(new LinearLayoutManager(context));
        holder.produtos.setAdapter(adapterMediceItem);
        String finalIdsProdutos = idsProdutos;

        holder.buyAgain.setOnClickListener(v -> {
            Intent a = new Intent(context, CartActivity.class);
            a.putExtra("quant", pp.getQuantidade());
            a.putExtra("formaPagamento", pp.getFormaPagamento());
            a.putExtra("idsProdutos", finalIdsProdutos);
            a.putExtra("valorTotal", pp.getValorTotal());
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(a);
        });

        String dataRetirada = "";
        String dia = "";
        String mes = "";
        String ano = "";
        String hora = "";
        String minuto = "";
        Calendar calendarretirada = Calendar.getInstance();
        calendarretirada.setTimeInMillis(Long.parseLong(pp.getDataRetirada()));
        if (calendarretirada.get(Calendar.DAY_OF_MONTH) < 10) {
            dia += "0";
        }
        dia += calendarretirada.get(Calendar.DAY_OF_MONTH);

        if ((calendarretirada.get(Calendar.MONTH) + 1) < 10) {
            mes += "0";
        }
        mes += calendarretirada.get(Calendar.MONTH) + 1;
        ano = String.valueOf(calendarretirada.get(Calendar.YEAR)).substring(2);
        if (calendarretirada.get(Calendar.HOUR_OF_DAY) < 10) {
            hora += "0";
        }
        hora += calendarretirada.get(Calendar.HOUR_OF_DAY);
        if (calendarretirada.get(Calendar.MINUTE) < 10) {
            minuto += "0";
        }
        minuto += calendarretirada.get(Calendar.MINUTE);
        dataRetirada = "Retirada agendada para:\n" + MainActivity.semana.get(calendarretirada.get(Calendar.DAY_OF_WEEK) - 1) + " " + dia + "/" + mes + "/" + ano + " ás " + hora + ":" + minuto;
        holder.verenderecos.setOnClickListener(v -> {
            Pedidos.chamardialog();
        });
        holder.dataRetirada.setText(dataRetirada);
        holder.formapagamento.setText("Em " + pp.getFormaPagamento());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class meuViewHolder extends RecyclerView.ViewHolder {
        TextView quantidadeItens, totalValorPedido, acompanhamentoPedido, dataRetirada, verenderecos, formapagamento;
        RecyclerView produtos;
        Button buyAgain, cancelar_pedido;
        CheckBox pedidoretirado;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);
            verenderecos = itemView.findViewById(R.id.verenderecos);
            pedidoretirado = itemView.findViewById(R.id.pedidoretirado);
            acompanhamentoPedido = itemView.findViewById(R.id.aconpanhamentoPedido_Pattern);
            dataRetirada = itemView.findViewById(R.id.dataRetirada);
            quantidadeItens = itemView.findViewById(R.id.quant_itens_pedidos_pattern);
            totalValorPedido = itemView.findViewById(R.id.total_valor_pedido_pattern);
            buyAgain = itemView.findViewById(R.id.buyAgain_pedido_pattern);
            cancelar_pedido = itemView.findViewById(R.id.cancelar_pedido_pattern);
            produtos = itemView.findViewById(R.id.rv_produtos_in_pedidos_pattern);
            formapagamento = itemView.findViewById(R.id.formapagamento);
        }

        private void cancelarPedido(Pedidospattern pp) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> map = new HashMap<>();
            map.put("status", "Cancelado");
            for (Produtopattern produtopattern : pp.getProdutoslist()) {
                Map<String, Object> map1 = new HashMap<>();
                db.collection("Produtos").document(produtopattern.getIdexterno()).get().addOnSuccessListener(command -> {
                    String a = (String) command.getData().get("quant_produto_interno");
                    int quant = Integer.parseInt(a);
                    quant += Integer.parseInt(produtopattern.getQuant_produto());
                    map1.put("quant_produto_interno", quant + "");
                    db.collection("Produtos").document(produtopattern.getIdexterno()).update(map1);
                });

            }

            db.collection("Pedidos").document(pp.getIdPedido()).update(map);
        }

        private String stringtomoney(String c) {
            int b = 0;
            StringBuilder result = new StringBuilder("");
            for (int i = 0; i < c.length(); i++) {
                if (b == 0) {
                    result.append(c.charAt(i));
                }
                if (c.charAt(i) == '.') {
                    b++;
                }
                if (b == 1) {
                    result.append(c.charAt(i + b));
                    b++;

                }
                if (b == 2) {
                    result.append(c.charAt(i + 2));
                    break;
                }
            }
            return result.toString();
        }


    }

}
