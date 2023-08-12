package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.AccountInfo;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.Subordinados;
import com.project.studiopatyzinha.gerenciar.Funcionarios;
import com.project.studiopatyzinha.gerenciar.Rotina;
import com.project.studiopatyzinha.pattern.Dayitempattern;

import java.util.ArrayList;
import java.util.Calendar;

public class MaindayadapterSimplyed extends RecyclerView.Adapter<MaindayadapterSimplyed.meuViewholdere> {
    private final ArrayList<Dayitempattern> diasweek;
    private String iftoday;
    private Context context;

    public MaindayadapterSimplyed(ArrayList<Dayitempattern> collectEventByDate, String iftoday) {
        this.diasweek = collectEventByDate;
        this.iftoday = iftoday;
    }


    @NonNull
    @Override
    public meuViewholdere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_main, parent, false);
        this.context = view.getContext();
        return new meuViewholdere(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewholdere holder, int position) {
        Dayitempattern dia = diasweek.get(holder.getAdapterPosition());


        holder.parentDay.setOnClickListener(v -> {
            for (int i = 0; i < diasweek.size(); i++) {
                diasweek.get(i).setBackcolor("");
            }
            dia.setBackcolor("s");
            notifyDataSetChanged();

        });

        if (dia.getBackcolor().equals("s")) {
            if (Subordinados.diaselecionado != null) {
                Subordinados.diaselecionado.setText(dia.getDayweek());
            }
            if (AccountInfo.changeday != null) {
                AccountInfo.changeday.setText(dia.getDayweek());
            }
            if (Rotina.diaselecionado != null) {
                Rotina.diaselecionado.setText(dia.getDayweek());
            }
            if (Funcionarios.changeday != null) {
                Funcionarios.changeday.setText(dia.getDayweek());
            }
            if (Funcionarios.diaselecionado != null) {
                Funcionarios.diaselecionado.setText(dia.getDayweek());
            }
            holder.parentDay.setBackgroundResource(R.color.primaria);
        } else {
            if (iftoday.equals("true")) {
                if (MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).equals(dia.getDayweek())) {
                    holder.parentDay.setBackgroundColor(context.getResources().getColor(R.color.teal_200, context.getTheme()));
                } else {
                    holder.parentDay.setBackgroundResource(R.color.teal_700);
                }
            } else {
                holder.parentDay.setBackgroundResource(R.color.teal_700);
            }
        }
        holder.dayweek.setText(dia.getDayweek());
    }


    @Override
    public int getItemCount() {
        return diasweek.size();
    }

    public class meuViewholdere extends RecyclerView.ViewHolder {

        TextView dayweek, daymonth;
        ConstraintLayout parentDay;
        ImageView img_quant_agend;

        public meuViewholdere(@NonNull View itemView) {
            super(itemView);
            dayweek = itemView.findViewById(R.id.dayweek);
            daymonth = itemView.findViewById(R.id.daymonth);
            parentDay = itemView.findViewById(R.id.parentDay);
            img_quant_agend = itemView.findViewById(R.id.img_quant_agend);

        }
    }


}







