package com.project.studiopatyzinha.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.R;
//import com.project.studiopatyzinha.fragmentos.ServicosFragment;
import com.project.studiopatyzinha.Servicos;
import com.project.studiopatyzinha.gerenciar.Funcionarios;
import com.project.studiopatyzinha.pattern.Dayitempatternhorario;

import java.util.ArrayList;

public class day_add_service_adapter extends RecyclerView.Adapter<day_add_service_adapter.meuViewholdere> {
    private final ArrayList<Dayitempatternhorario> diasweek;

    public day_add_service_adapter(ArrayList<Dayitempatternhorario> collectEventByDate) {
        this.diasweek = collectEventByDate;
    }


    @NonNull
    @Override
    public meuViewholdere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_addservice, parent, false);
        return new meuViewholdere(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewholdere holder, int position) {
        Dayitempatternhorario dia = diasweek.get(holder.getAdapterPosition());
        holder.parentDay.setOnClickListener(v -> {
            if (dia.getBackcolor().equals("s")) {
                if (Servicos.posdiasvalorespromocionais != null) {
                    Servicos.posdiasvalorespromocionais.setText(Servicos.posdiasvalorespromocionais.getText().toString().replaceAll("[" + position + "]", ""));
                }
                if (Funcionarios.posdiasvalorespromocionais != null) {
                    Funcionarios.posdiasvalorespromocionais.setText(Funcionarios.posdiasvalorespromocionais.getText().toString().replaceAll("[" + position + "]", ""));
                }
                dia.setBackcolor("");
            } else {
                if (Servicos.posdiasvalorespromocionais != null) {
                    Servicos.posdiasvalorespromocionais.setText(Servicos.posdiasvalorespromocionais.getText().toString() + position);
                }
                if (Funcionarios.posdiasvalorespromocionais != null) {
                    Funcionarios.posdiasvalorespromocionais.setText(Funcionarios.posdiasvalorespromocionais.getText().toString() + position);
                }

                dia.setBackcolor("s");
            }
            notifyDataSetChanged();
        });
        if (dia.getBackcolor().equals("s")) {
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
            dayweek = itemView.findViewById(R.id.bntdayaddservice);
            parentDay = itemView.findViewById(R.id.parentbntdayaddservice);
        }
    }
}
