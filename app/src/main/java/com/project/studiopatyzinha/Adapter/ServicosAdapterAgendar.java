package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.studiopatyzinha.Add_agendamento;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.Servicos;
import com.project.studiopatyzinha.pattern.Price;
import com.project.studiopatyzinha.pattern.Servicepattern;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class ServicosAdapterAgendar extends RecyclerView.Adapter<ServicosAdapterAgendar.meuViewholdere> {
    private Context context;
    private final ArrayList<Servicepattern> arrayList;
    boolean ifsetprice;
    String origem;

    public ServicosAdapterAgendar(Context context, ArrayList<Servicepattern> collectEventByDate, boolean ifsetprice, String origem) {
        this.origem = origem;
        this.context = context;
        this.arrayList = collectEventByDate;
        this.ifsetprice = ifsetprice;

    }


    @NonNull
    @Override
    public meuViewholdere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.capa_servico, parent, false);
        context = view.getContext();
        return new meuViewholdere(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewholdere holder, int position) {
        Servicepattern mip = arrayList.get(position);
        holder.setData(mip);
        Glide.with(context).load(mip.getFt()).into(holder.ivfotoProduto);
        if (mip.getSelected().equals("s")) {
            holder.parent_item_remedio.setBackgroundColor(context.getColor(R.color.primaria));
        } else {
            holder.parent_item_remedio.setBackgroundColor(context.getColor(R.color.secundariaB));
        }

        holder.parent_item_remedio.setOnClickListener(v -> {
            if (origem.equals("servicos")) {
                holder.parent_item_remedio.setOnLongClickListener(v1 -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Deseja excluir esse serviço ?")
                            .setCancelable(true)
                            .setPositiveButton("Sim", (dialog, id) -> holder.deletservice(holder.getAdapterPosition()))
                            .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                });
                Servicos.editarservico(mip, context);
            } else {
                String selected = mip.getSelected();
                for (int i = 0; i < arrayList.size(); i++) {
                    arrayList.get(i).setSelected("");
                }
                if (selected.equals("s")) {
                    mip.setSelected("");
                    if (ifsetprice) {
                        Add_agendamento.servicopos = -1;
                    } else {
                        Add_agendamento.barberpos = -1;
                    }
                } else {
                    mip.setSelected("s");
                    if (ifsetprice) {
                        Add_agendamento.servicopos = holder.getAdapterPosition();
                    } else {
                        Add_agendamento.barberpos = holder.getAdapterPosition();
                    }
                }
                notifyDataSetChanged();
            }
        });
        if (origem.contains("servicos")) {
            if (mip.getIfpromocao().equals("true")) {
                String dias = "";
                for (int i = 0; i < mip.getDiaspromocao().length(); i++) {
                    if(Integer.parseInt(String.valueOf(mip.getDiaspromocao().charAt(i))) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
                    dias += " " + MainActivity.semana.get(Integer.parseInt(mip.getDiaspromocao().charAt(i) + ""));
                    }
                }
                if(!dias.isEmpty()) {
                    holder.diaspromocao.setText(dias);
                    holder.diaspromocao.setVisibility(View.VISIBLE);
                    holder.llDiscountIV.setVisibility(View.VISIBLE);
                    holder.tvPriceWithoutDiscount.setVisibility(View.VISIBLE);
                }else{
                    holder.tvPriceWithoutDiscount.setVisibility(View.GONE);
                    holder.llDiscountIV.setVisibility(View.GONE);
                    holder.diaspromocao.setVisibility(View.GONE);
                }
            } else {
                holder.llDiscountIV.setVisibility(View.GONE);
                holder.diaspromocao.setVisibility(View.GONE);
            }

        }
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
        private final TextView diaspromocao;


        public meuViewholdere(@NonNull View itemView) {
            super(itemView);
            ivfotoProduto = itemView.findViewById(R.id.iv_model);
            tvnomeProduto = itemView.findViewById(R.id.tv_model);
            parent_item_remedio = itemView.findViewById(R.id.parent_item_remedio);
            llDiscountIV = itemView.findViewById(R.id.ll_discount);
            tvNumberPercentOffer = itemView.findViewById(R.id.tv_discount);
            diaspromocao = itemView.findViewById(R.id.diaspromocao);
            tvPriceCurrent = itemView.findViewById(R.id.tv_price_current);
            tvPriceWithoutDiscount = itemView.findViewById(R.id.tv_price_without_discount);
        }

        void setData(Servicepattern produto) {
            tvnomeProduto.setText(produto.getNome());
            if (ifsetprice) {
                double a = Float.parseFloat(produto.getValor().replaceAll("[R$  ]", "").replace(",", "."));
                String text = produto.getValorp().replaceAll("[R$  ]", "").replace(",", ".");
                double b = Float.parseFloat(text);
                setPrice(new Price(a, 1, Boolean.parseBoolean(produto.getIfpromocao()), b));
            } else {
                llDiscountIV.setVisibility(View.GONE);
                tvPriceWithoutDiscount.setVisibility(View.GONE);
                tvPriceCurrent.setVisibility(View.GONE);
                tvNumberPercentOffer.setVisibility(View.GONE);
            }
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

        private void deletservice(int position) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Servicos").document(arrayList.get(position).getId()).delete();
            arrayList.remove(position);
            notifyDataSetChanged();

        }
    }


}





