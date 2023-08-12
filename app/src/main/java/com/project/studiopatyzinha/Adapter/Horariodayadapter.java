package com.project.studiopatyzinha.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.R;
//import com.project.studiopatyzinha.fragmentos.HorarioFragment;
import com.project.studiopatyzinha.pattern.Dayitempatternhorario;

import java.util.ArrayList;

public class Horariodayadapter extends RecyclerView.Adapter<Horariodayadapter.meuViewholdere> {
    private final ArrayList<Dayitempatternhorario> diasweek;
    private Context context;

    public Horariodayadapter(ArrayList<Dayitempatternhorario> collectEventByDate) {
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
        Dayitempatternhorario dia = diasweek.get(holder.getAdapterPosition());
        holder.parentDay.setOnClickListener(v -> {
            for (int i = 0; i < diasweek.size(); i++) {
                diasweek.get(i).setBackcolor("");
            }
            dia.setBackcolor("s");
            notifyDataSetChanged();
        });

        if (dia.getBackcolor().equals("s")) {
//            HorarioFragment.diaselecionandotv.setText(dia.getDayweek());
            holder.parentDay.setBackgroundResource(R.color.primaria);
        } else {
            holder.parentDay.setBackgroundResource(R.color.terciaria);
        }

        holder.dayweek.setText(dia.getDayweek());
    }


    @Override
    public int getItemCount() {
        return diasweek.size();
    }

    public class meuViewholdere extends RecyclerView.ViewHolder {

        TextView dayweek;
        ConstraintLayout parentDay;

        public meuViewholdere(@NonNull View itemView) {
            super(itemView);
            dayweek = itemView.findViewById(R.id.dayweek);
            parentDay = itemView.findViewById(R.id.parentDay);

        }
    }
}
