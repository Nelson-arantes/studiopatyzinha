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
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.Servicos;
import com.project.studiopatyzinha.Subordinados;
//import com.project.studiopatyzinha.fragmentos.funcsFragment;
import com.project.studiopatyzinha.gerenciar.Funcionarios;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class funcAdapter extends RecyclerView.Adapter<funcAdapter.ViewHolder> {
    ArrayList<Accountpattern> funcs;
    Context context;
    String origem;

    public funcAdapter(ArrayList<Accountpattern> funcs, String origem) {
        this.funcs = funcs;
        this.origem = origem;
    }

    @NonNull
    @Override
    public funcAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_func, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull funcAdapter.ViewHolder holder, int position) {
        Accountpattern func = funcs.get(position);
        holder.Namefunc.setText(func.getNome());
        if (origem.contains("velho")) {
            holder.parentfuncs.setOnClickListener(v -> {
                Funcionarios.editarfunc(func,holder.getAdapterPosition());
            });
        }
        holder.trash.setOnClickListener(v3 -> {
            if (origem.contains("velho")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Deseja demitir esse funcionário ?")
                        .setCancelable(true)
                        .setPositiveButton("Sim", (dialog, id) -> holder.deletefunc(position))
                        .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                if (!origem.contains("endereco")) {
                    if (Servicos.funcsnomesworks != null) {
                        Servicos.funcsnomesworks = Servicos.funcsnomesworks.replace(func.getId() + ";", "");
                    }
                    if (Funcionarios.grupoabaixo != null) {
                        Funcionarios.grupoabaixo = Funcionarios.grupoabaixo.replace(func.getId() + ";", "");
                    }
                    funcs.remove(position);
                    notifyDataSetChanged();
                } else {
                    holder.deletefunc(holder.getAdapterPosition());
                }
            }
        });
        if (origem.contains("sub")) {
            holder.parentfuncs.setOnClickListener(v -> {
                Subordinados.verFunc(func.getId());
            });
            holder.trash.setVisibility(View.GONE);
        }
        if (!origem.contains("muito") && !origem.contains("endereco")) {
            if (func.getImgUri().isEmpty()) {
                Glide.with(context).load(R.drawable.ic_tendencias).into(holder.ftfunc);
            } else {
                Glide.with(context).load(func.getImgUri()).into(holder.ftfunc);
            }
            holder.ftfunc.setVisibility(View.VISIBLE);
        }
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
            db.collection("Contas").document(funcs.get(pos).getId()).delete();
            funcs.remove(pos);
            notifyDataSetChanged();
        }
    }
}
