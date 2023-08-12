package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Add_agendamento;
import com.project.studiopatyzinha.Gerenciar;
import com.project.studiopatyzinha.pattern.Addrespattern;
import com.project.studiopatyzinha.R;

import java.util.List;

public class Adapteraddress extends RecyclerView.Adapter<Adapteraddress.meuViewHolder> {
    Context context;
    List<Addrespattern> list;
    String ifselected;

    public Adapteraddress(Context context, List<Addrespattern> list, String ifselected) {
        this.ifselected = ifselected;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public meuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rowaddress, parent, false);
        return new meuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewHolder holder, int position) {
        holder.address.setText(list.get(holder.getAdapterPosition()).getEndereco());
        holder.numero_casa_endereco.setText("Nº " + list.get(holder.getAdapterPosition()).getNumero());
        if (ifselected.equals("true")) {
            holder.parent.setOnClickListener(v -> {
                if (list.get(holder.getAdapterPosition()).getIfseleceted().equals("on")) {
                    list.get(holder.getAdapterPosition()).setIfseleceted("off");
                    Add_agendamento.addresspos = -1;
                    holder.parent.setBackgroundResource(R.color.secundariaB);
                } else {
                    for (int j = 0; j < list.size(); j++) {
                        list.get(j).setIfseleceted("off");
                    }
                    Add_agendamento.addresspos = holder.getAdapterPosition();
                    list.get(holder.getAdapterPosition()).setIfseleceted("on");

                    holder.parent.setBackgroundResource(R.color.primaria);
                }
                notifyDataSetChanged();
            });
            if (list.get(holder.getAdapterPosition()).getIfseleceted().equals("on")) {
                holder.parent.setBackgroundResource(R.color.primaria);
            } else {
                holder.parent.setBackgroundResource(R.color.secundariaB);
            }
        } else {
            holder.parent.setOnLongClickListener(v -> {
                Gerenciar.deleteadrressask(position);
                return true;
            });
        }
        holder.nome_address.setText(list.get(holder.getAdapterPosition()).getEndereco_apelido());
        holder.bloco_address.setText(list.get(holder.getAdapterPosition()).getCep());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class meuViewHolder extends RecyclerView.ViewHolder {
        TextView nome_address, address, bloco_address, numero_casa_endereco;
        ConstraintLayout parent;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);
            nome_address = itemView.findViewById(R.id.apelido_endereco);
            bloco_address = itemView.findViewById(R.id.bloco_endereco);
            parent = itemView.findViewById(R.id.parent_address);
            address = itemView.findViewById(R.id.nome_endereco);
            numero_casa_endereco = itemView.findViewById(R.id.numero_casa_endereco);
        }
    }
}