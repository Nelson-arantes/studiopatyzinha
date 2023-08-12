package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.AgendamentosAdmin;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Dayitempattern;

import java.util.ArrayList;
import java.util.Calendar;

public class Maindayadapter extends RecyclerView.Adapter<Maindayadapter.meuViewholdere> {
    private final ArrayList<Dayitempattern> diasweek;
    private Context context;

    public Maindayadapter(ArrayList<Dayitempattern> collectEventByDate) {
        this.diasweek = collectEventByDate;

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
        String[] selected = AgendamentosAdmin.datainteiera.getText().toString().split("/");

        if (Integer.parseInt(dia.getQuantagend()) > 0) {
            holder.img_quant_agend.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_half_circle_green));
        } else {
            holder.img_quant_agend.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_ellipse_white));
        }


        holder.parentDay.setOnClickListener(v -> {
            AgendamentosAdmin.datainteiera.setText(AgendamentosAdmin.datainteiera.getText()); //v

            if (Integer.parseInt(dia.getDaymonth()) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
                    Integer.parseInt(dia.getMonth()) == Calendar.getInstance().get(Calendar.MONTH) &&
                    Integer.parseInt(dia.getYear()) == Calendar.getInstance().get(Calendar.YEAR)) {
                AgendamentosAdmin.datacarview.setText("Hoje");
            } else {
                String datacompletaaqui = dia.getDaymonth();
                datacompletaaqui += " de " + AgendamentosAdmin.meses.get(Integer.parseInt(dia.getMonth())-1);
                if (Integer.parseInt(dia.getYear()) != Calendar.getInstance().get(Calendar.YEAR)) {
                    datacompletaaqui += " de " + dia.getYear();
                }
                AgendamentosAdmin.datacarview.setText(datacompletaaqui);
            }
            String mes = "";
            if (Integer.parseInt(dia.getMonth() + "") < 10) {
                mes += "0";
            }
            mes += Integer.parseInt(dia.getMonth()) ;
            AgendamentosAdmin.datainteiera.setText(dia.getDaymonth() + "/" + mes + "/" + dia.getYear()); //v

            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(dia.getYear()), Integer.parseInt(dia.getMonth())-1, Integer.parseInt(dia.getDaymonth()), 0, 0);
            if (calendar.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
                AgendamentosAdmin.datacardweek.setText("Esta semana");
            } else {
                AgendamentosAdmin.datacardweek.setText(AgendamentosAdmin.dateweek.getText());
            }
            for (int i = 0; i < diasweek.size(); i++) {
                diasweek.get(i).setBackcolor("");
            }
            dia.setBackcolor("s");
            notifyDataSetChanged();
        });

        if (dia.getBackcolor().equals("s")) {
//            if (Integer.parseInt(dia.getDaymonth())== Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
//                    && Integer.parseInt(dia.getMonth())==Calendar.getInstance().get(Calendar.MONTH)
//                    && Integer.parseInt(dia.getYear())==Calendar.getInstance().get(Calendar.YEAR)) {
            holder.parentDay.setBackgroundResource(R.color.primaria);
//            } else {
//                holder.parentDay.setBackgroundResource(R.color.primaria);
//            }
        } else {
            if (Integer.parseInt(dia.getDaymonth()) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    && Integer.parseInt(dia.getMonth()) == (Calendar.getInstance().get(Calendar.MONTH)+1)
                    && Integer.parseInt(dia.getYear()) == Calendar.getInstance().get(Calendar.YEAR)) {
                holder.parentDay.setBackgroundColor(context.getResources().getColor(R.color.terciariaB, context.getTheme()));
            } else {
                holder.parentDay.setBackgroundResource(R.color.primariaBB);
            }
        }
        if (Integer.parseInt(dia.getDaymonth()) == Integer.parseInt(selected[0])
                && Integer.parseInt(dia.getMonth()) == Integer.parseInt(selected[1])
                && Integer.parseInt(dia.getYear()) == Integer.parseInt(selected[2])) {
            holder.parentDay.setBackgroundResource(R.color.primaria);

        }
        holder.dayweek.setText(dia.getDayweek());
        holder.daymonth.setText(dia.getDaymonth());
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







