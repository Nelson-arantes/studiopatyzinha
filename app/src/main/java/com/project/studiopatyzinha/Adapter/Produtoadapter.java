package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.DetalhesActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.gerenciar.Formulario_produto;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.DecimalFormat;
import java.util.List;


public class Produtoadapter extends RecyclerView.Adapter<Produtoadapter.meuViewHolder> {
    Context context;
    String ifclicavel;
    List<Produtopattern> lista;
    int editando;
    String pedidoid;
    String idspedidos;

    public Produtoadapter(List<Produtopattern> list, String ifclicalvel, int editando, String pedidoid, String idspedidos, Context context) {
        lista = list;
        ifclicavel = ifclicalvel;
        this.editando = editando;
        this.pedidoid = pedidoid;
        this.idspedidos = idspedidos;

    }

    @NonNull
    @Override
    public meuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new meuViewHolder(LayoutInflater.from(context).inflate(R.layout.rowmedicineitem, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull meuViewHolder holder, int position) {


        Produtopattern mip = lista.get(position);
        if (ifclicavel.equals("True")) {
            holder.parent_rowmedicineitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(context, DetalhesActivity.class);
                    a.putExtra("idexterno", mip.getIdexterno());
                    a.putExtra("idinterno", mip.getIdinterno());
                    a.putExtra("editando", editando);
                    a.putExtra("quantidade", mip.getQuant_produto());
                    a.putExtra("pos", holder.getAdapterPosition());
                    if (!pedidoid.equals("0")) {
                        a.putExtra("idpedido", pedidoid);
                        a.putExtra("idspedidos", idspedidos);
                    }
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(a);
                }
            });
        }
        if (ifclicavel.equals("editar")) {
            holder.parent_rowmedicineitem.setOnLongClickListener(v2 -> {
                holder.deletar();
                return true;
            });
            holder.parent_rowmedicineitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(context, Formulario_produto.class);
                    a.putExtra("idexterno", mip.getIdexterno());
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(a);
                }
            });
        }
        Glide.with(context).load(mip.getBitmapImg()).into(holder.img_produto);

        holder.fabricante_remedio.setText(mip.getFabricante());
        holder.nome_remedio.setText(mip.getNome());
        holder.modelo_remedio.setText(mip.getModelo());
        holder.Quant_per_cx.setText(mip.getQuant_per_cx());
        if (editando != 0) {
            holder.precoMaximoPorProduto(mip);
            holder.quant_medicine_cart.setText("X" + mip.getQuant_produto());
        }
        if (mip.getDesconto().equals("0.f")) {
            holder.preco_original_remedio.setText(context.getString(R.string.unidademontaria) + mip.getPreco_original().replace(".", ","));
            holder.preco_original_remedio.setTextColor(Color.parseColor("#FF000000"));
            holder.preco_remedio.setVisibility(View.GONE);

        } else {
            holder.preco_original_remedio.setPaintFlags(holder.preco_original_remedio.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.preco_original_remedio.setText(context.getString(R.string.unidademontaria) + mip.getPreco_original().replace(".", ","));

            holder.preco_remedio.setText(context.getString(R.string.unidademontaria) + mip.getDesconto().replace(".", ","));
        }
        if (!mip.getQuant_produto().isEmpty()) {
            holder.quant_medicine_cart.setText("x" + mip.getQuant_produto());
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class meuViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout parent_rowmedicineitem;
        TextView nome_remedio;
        ImageView img_produto;
        TextView modelo_remedio;
        TextView quant_medicine_cart;
        TextView fabricante_remedio;
        TextView preco_remedio;
        TextView preco_original_remedio;
        TextView total_valor_item_pattern;
        TextView Quant_per_cx;


        public meuViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_remedio = itemView.findViewById(R.id.name_medicine_item_pattern);
            parent_rowmedicineitem = itemView.findViewById(R.id.parent_row_item_pattern);
            total_valor_item_pattern = itemView.findViewById(R.id.total_valor_item_pattern);
            quant_medicine_cart = itemView.findViewById(R.id.nome_quant_item_pattern);
            img_produto = itemView.findViewById(R.id.imageView_item_pattern);
            modelo_remedio = itemView.findViewById(R.id.name_modelo_item_pattern);
            Quant_per_cx = itemView.findViewById(R.id.Quant_per_cx_item_pattern);
            fabricante_remedio = itemView.findViewById(R.id.nome_factory_item_pattern);
            preco_remedio = itemView.findViewById(R.id.nome_value_no_discount_item_pattern);
            preco_original_remedio = itemView.findViewById(R.id.nome_value_item_pattern);

        }

        private void precoMaximoPorProduto(Produtopattern mip) {
            double atotal;
            double btotal = Integer.parseInt(mip.getQuant_produto());
            DecimalFormat df = new DecimalFormat("#.00");
            if (mip.getDesconto().equals("0.f")) {
                atotal = Double.parseDouble(df.format(Double.parseDouble(mip.getPreco_original().replaceAll("[R$  ]", "").replace(",", "."))).replace(",", "."));
            } else {
                atotal = Double.parseDouble(mip.getDesconto());
            }
            total_valor_item_pattern.setText(context.getString(R.string.unidademontaria) + df.format(atotal * btotal).replace(".", ","));
        }

        public void deletar() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Deseja Apagar esse produto ?")
                    .setCancelable(true)
                    .setPositiveButton("Sim", (dialog, id) -> deletear())
                    .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }

        private void deletear() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Produtos").document(lista.get(getAdapterPosition()).getIdexterno()).delete();
            lista.remove(getAdapterPosition());
        }
    }

}
