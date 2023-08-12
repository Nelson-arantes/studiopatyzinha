package com.project.studiopatyzinha.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.categoriasLC;

import java.util.ArrayList;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.mewViewHolder> {

    ArrayList<categoriasLC> list;

    public NotiAdapter(ArrayList<categoriasLC> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public mewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noti, parent, false);
        return new mewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mewViewHolder holder, int position) {
        categoriasLC categoriaslc = list.get(position);
        holder.barcolor.setBackgroundColor(categoriaslc.getColor());
        holder.nomecategoria.setText(categoriaslc.getNome());
        if (!categoriaslc.getLegenda().isEmpty()) {
            holder.legendacategoria.setVisibility(View.VISIBLE);
            holder.legendacategoria.setText(categoriaslc.getLegenda());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class mewViewHolder extends RecyclerView.ViewHolder {
        View barcolor;
        TextView nomecategoria, legendacategoria;

        public mewViewHolder(@NonNull View itemView) {
            super(itemView);
            barcolor = itemView.findViewById(R.id.viewcolor);
            nomecategoria = itemView.findViewById(R.id.nomecategoria);
            legendacategoria = itemView.findViewById(R.id.legendacategoria);

        }
    }
}
