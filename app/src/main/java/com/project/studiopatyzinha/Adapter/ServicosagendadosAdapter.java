package com.project.studiopatyzinha.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Login;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ServicosagendadosAdapter extends RecyclerView.Adapter<ServicosagendadosAdapter.ViewHolder> {

    ArrayList<Appoitmentpattern> lista;

    public ServicosagendadosAdapter(ArrayList<Appoitmentpattern> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appoitment_main, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appoitmentpattern service = lista.get(position);
        holder.delete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Deseja desmarcar esse agendamento ?")
                    .setCancelable(true)
                    .setPositiveButton("Sim", (dialog, id) -> holder.deleteappointment(service.getId(), position))
                    .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        holder.nome.setText(service.getNomeservico());
        holder.desc.setText(service.getNomefunc());
        holder.duracao.setText(service.getTempo());
        holder.hora.setText(service.getHora() + " " + service.getDia());
        holder.valor.setText("R$ "+service.getValorservico().replace(".",","));
        holder.ck.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button delete;
        TextView nome, hora, duracao, valor, desc, diasdisconto;
        CheckBox ck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ck = itemView.findViewById(R.id.ifconcluido);
            delete = itemView.findViewById(R.id.cancelaragendamento);
            nome = itemView.findViewById(R.id.NameagendamentoH);
            diasdisconto = itemView.findViewById(R.id.diasdisconto);
            desc = itemView.findViewById(R.id.descH);
            valor = itemView.findViewById(R.id.valorservico);
            duracao = itemView.findViewById(R.id.duracao);
            hora = itemView.findViewById(R.id.agendamentoH);


        }

        private void deleteappointment(String id, int pos) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Agendamentos").child(Login.pessoa.getId()).child(id).removeValue();
            lista.remove(pos);
            notifyDataSetChanged();
        }
    }
}
