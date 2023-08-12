package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.Servicos;
import com.project.studiopatyzinha.Subordinados;
import com.project.studiopatyzinha.gerenciar.Funcionarios;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Descpattern;

import java.util.ArrayList;

public class descAdapter extends RecyclerView.Adapter<descAdapter.ViewHolder> {
    ArrayList<Descpattern> funcs;
    Context context;

    public descAdapter(ArrayList<Descpattern> funcs) {
        this.funcs = funcs;
    }

    @NonNull
    @Override
    public descAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_func, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull descAdapter.ViewHolder holder, int position) {
        Descpattern desc = funcs.get(position);
        holder.Namefunc.setText(desc.getNome());
        holder.trash.setOnClickListener(v3 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Deseja apagar esse cupom de desconto ?")
                    .setCancelable(true)
                    .setPositiveButton("Sim", (dialog, id) -> holder.deletefunc(position))
                    .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return funcs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ftfunc, trash;
        TextView Namefunc;
        ConstraintLayout parentfuncs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trash = itemView.findViewById(R.id.deletefunc);
            ftfunc = itemView.findViewById(R.id.imageView2);
            parentfuncs = itemView.findViewById(R.id.parentfuncs);
            Namefunc = itemView.findViewById(R.id.Namefunc);
        }

        private void deletefunc(int pos) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Desconto").document(funcs.get(pos).getId()).delete();
            funcs.remove(pos);
            notifyDataSetChanged();
        }
    }
}
