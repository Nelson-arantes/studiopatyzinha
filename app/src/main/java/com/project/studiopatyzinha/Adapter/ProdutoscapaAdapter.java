package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.DetalhesActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Price;
import com.project.studiopatyzinha.pattern.Produtopattern;

import java.util.List;

public class ProdutoscapaAdapter extends RecyclerView.Adapter<ProdutoscapaAdapter.meuViewholdere> {
    private Context context;
    private final List<Produtopattern> arrayList;

    public ProdutoscapaAdapter(Context context, List<Produtopattern> collectEventByDate) {
        this.context = context;
        this.arrayList = collectEventByDate;

    }


    @NonNull
    @Override
    public meuViewholdere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.capa_produto_cart, parent, false);
        context = view.getContext();
        return new meuViewholdere(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewholdere holder, int position) {
        Produtopattern mip = arrayList.get(position);
        holder.setData(mip);
        Glide.with(context).load(mip.getBitmapImg()).into(holder.ivfotoProduto);
        holder.parent_item_remedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, DetalhesActivity.class);
                a.putExtra("editando", 0);
                a.putExtra("id", mip.getIdinterno());
                a.putExtra("idexterno", mip.getIdexterno());
                a.putExtra("nome", mip.getNome());
                a.putExtra("modelo", mip.getModelo());
                a.putExtra("fabricante", mip.getFabricante());
                a.putExtra("desconto", mip.getDesconto());
                a.putExtra("preco_original", mip.getPreco_original());
                a.putExtra("quant_per_cx", mip.getQuant_per_cx());
                a.putExtra("usosindicados", mip.getUsosindicados());
                a.putExtra("usosNindicados", mip.getUsosNindicados());
                a.putExtra("descricao", mip.getDescricao());
                a.putExtra("bitmapImg", mip.getBitmapImg());
                a.putExtra("quant_produto", mip.getQuant_produto());
                a.putExtra("quant_produto_interno", mip.getQuant_produto_interno());
                a.putExtra("quant_produto_minima", mip.getQuant_minima()
                );
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(a);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class meuViewholdere extends RecyclerView.ViewHolder {

        private final ImageView ivfotoProduto;
        private final TextView tvnomeProduto;
        private final LinearLayout llDiscountIV;
        private final ConstraintLayout parent_item_remedio;
        private final TextView tvNumberPercentOffer;
        private final TextView tvPriceCurrent;
        private final TextView tvPriceWithoutDiscount;


        public meuViewholdere(@NonNull View itemView) {
            super(itemView);
            ivfotoProduto = itemView.findViewById(R.id.iv_model);
            tvnomeProduto = itemView.findViewById(R.id.tv_model);
            parent_item_remedio = itemView.findViewById(R.id.parent_item_remedio);
            llDiscountIV = itemView.findViewById(R.id.ll_discount);
            tvNumberPercentOffer = itemView.findViewById(R.id.tv_discount);
            tvPriceCurrent = itemView.findViewById(R.id.tv_price_current);
            tvPriceWithoutDiscount = itemView.findViewById(R.id.tv_price_without_discount);
        }

        void setData(Produtopattern produto) {
            tvnomeProduto.setText(produto.getNome()+" "+produto.getModelo()+" "+produto.getFabricante()+" "+produto.getQuant_per_cx());
            double preco = Float.parseFloat(produto.getPreco_original());
            String text = produto.getDesconto();
            double desconto = Float.parseFloat(text);
            setPrice(new Price(preco, 1, Float.parseFloat(text) != 0, desconto));
        }

        void setPrice(Price price) {
            if (price.setemdesconto) {
                llDiscountIV.setVisibility(View.VISIBLE);
                tvPriceWithoutDiscount.setVisibility(View.VISIBLE);
                tvPriceCurrent.setText(price.getDiscount(context));
                tvPriceWithoutDiscount.setText(price.getDesconto(context));
                tvNumberPercentOffer.setText(price.getPercentNumber());
            } else {
                llDiscountIV.setVisibility(View.GONE);
                tvPriceWithoutDiscount.setVisibility(View.GONE);
                tvPriceCurrent.setText(price.getDesconto(context));
            }
        }

    }


}





