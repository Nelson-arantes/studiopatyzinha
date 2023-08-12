package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Add_agendamento;
import com.project.studiopatyzinha.CartActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Horapattern;

import java.util.ArrayList;

public class HoraAdapterChoosing extends RecyclerView.Adapter<HoraAdapterChoosing.MeuviewHolder> {
    ArrayList<Horapattern> horaList;
    Context context;

    public HoraAdapterChoosing(ArrayList<Horapattern> horaList) {
        this.horaList = horaList;
    }

    @NonNull
    @Override
    public MeuviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hora, null, false);
        context = parent.getContext();
        return new MeuviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeuviewHolder holder, int position) {
        Horapattern hora = horaList.get(position);
        holder.tv.setText(hora.getHora());

        holder.bg.setOnClickListener(v -> {
            for (int i = 0; i < horaList.size(); i++) {
                if (i != position)
                    horaList.get(i).setSelected("");
            }
            if (hora.getSelected().equals("s")) {
                hora.setSelected("");
                if (CartActivity.horaescolhida != null) {
                    CartActivity.horaescolhida = "";
                }
                if (Add_agendamento.horaescolhida != null) {
                    Add_agendamento.horaescolhida = "";
                }
            } else {

                if (CartActivity.horaescolhida != null) {
                    CartActivity.horaescolhida = hora.getHora();
                }
                if (Add_agendamento.horaescolhida != null) {
                    Add_agendamento.horaescolhida = hora.getHora();
                }

                hora.setSelected("s");
            }
            notifyDataSetChanged();
        });
        if (hora.getSelected().equals("s")) {
            holder.tv.setTextColor(Color.WHITE);
            holder.bg.setCardBackgroundColor(context.getColor(R.color.primaria));
        } else {
            holder.tv.setTextColor(Color.BLACK);
            holder.bg.setCardBackgroundColor(Color.WHITE);
        }
        if (hora.getOcupado()) {
            holder.bg.setCardBackgroundColor(context.getColor(R.color.cinza));
            holder.bg.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return horaList.size();
    }

    public class MeuviewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        CardView bg;

        public MeuviewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_hora);
            bg = itemView.findViewById(R.id.background_tv_hora);
        }
    }
}
