package com.project.studiopatyzinha.Adapter;

import android.content.Context;
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
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Price;
import com.project.studiopatyzinha.pattern.Produtopattern;

import java.util.List;

public class ProdutoscapaAdapterFormulario extends RecyclerView.Adapter<ProdutoscapaAdapterFormulario.meuViewholdere> {
    private Context context;
    private final List<Produtopattern> arrayList;

    public ProdutoscapaAdapterFormulario(Context context, List<Produtopattern> collectEventByDate) {
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
        if (mip.getSelected().equals("true")) {
            holder.parent_item_remedio.setBackgroundResource(R.color.primaria);
        } else {
            holder.parent_item_remedio.setBackgroundResource(R.color.secundariaB);
        }
        Glide.with(context).load(mip.getBitmapImg()).into(holder.ivfotoProduto);
        holder.parent_item_remedio.setOnClickListener(v -> {


            if (mip.getSelected().equals("true")) {
                mip.setSelected("false");
            } else {
                mip.setSelected("true");
            }
            notifyDataSetChanged();
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
            tvnomeProduto.setText(produto.getNome() + " " + produto.getModelo() + " " + produto.getFabricante() + " " + produto.getQuant_per_cx());
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





