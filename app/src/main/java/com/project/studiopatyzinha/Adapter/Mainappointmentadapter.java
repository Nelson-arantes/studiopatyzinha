package com.project.studiopatyzinha.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Login;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mainappointmentadapter extends RecyclerView.Adapter<Mainappointmentadapter.meuViewholdere> {
    private final ArrayList<Appoitmentpattern> appoitmentpatterns;
    private Context context;

    public Mainappointmentadapter(ArrayList<Appoitmentpattern> collectEventByDate) {
        this.appoitmentpatterns = collectEventByDate;

    }


    @NonNull
    @Override
    public meuViewholdere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appoitment_main, parent, false);
        this.context = view.getContext();
        return new meuViewholdere(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewholdere holder, int position) {
        Appoitmentpattern appoitmentpattern = appoitmentpatterns.get(holder.getAdapterPosition());
        holder.ck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> atualizar = new HashMap<>();
            atualizar.put("concluido", isChecked + "");
            reference.child("Agendamentos").child(appoitmentpattern.getIdpessoa()).child(appoitmentpattern.getId()).updateChildren(atualizar);
        });
        holder.ck.setChecked(Boolean.parseBoolean(appoitmentpattern.getConcluido()));
        String valor = "R$ " + appoitmentpattern.getValorservico().replace(".", ",");
        holder.valorservico.setText(valor);
        holder.Hora.setText(appoitmentpattern.getHora());
        holder.duracao.setText(appoitmentpattern.getTempo());
        holder.descH.setText(appoitmentpattern.getNomeservico());
        if (appoitmentpattern.getPresenca().equals("true")) {
            holder.agendamentoconfirmado.setVisibility(View.VISIBLE);
        }
        holder.nomepessoa.setText(appoitmentpattern.getNomepessoa());
        holder.deletbtton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Deseja desmarcar esse agendamento ?")
                    .setCancelable(true)
                    .setPositiveButton("Sim", (dialog, id) -> holder.deleteappointment(appoitmentpattern.getId(), position))
                    .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
    }


    @Override
    public int getItemCount() {
        return appoitmentpatterns.size();
    }

    public class meuViewholdere extends RecyclerView.ViewHolder {

        TextView nomepessoa, Hora, descH, duracao, valorservico, agendamentoconfirmado;
        Button deletbtton;
        ConstraintLayout parentappointment;
        CheckBox ck;

        public meuViewholdere(@NonNull View itemView) {
            super(itemView);
            ck = itemView.findViewById(R.id.ifconcluido);
            nomepessoa = itemView.findViewById(R.id.NameagendamentoH);
            parentappointment = itemView.findViewById(R.id.parentappointment);
            valorservico = itemView.findViewById(R.id.valorservico);
            agendamentoconfirmado = itemView.findViewById(R.id.agendamentoconfirmado);
            duracao = itemView.findViewById(R.id.duracao);
            Hora = itemView.findViewById(R.id.agendamentoH);
            descH = itemView.findViewById(R.id.descH);
            deletbtton = itemView.findViewById(R.id.cancelaragendamento);
        }

        private void deleteappointment(String id, int pos) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Agendamentos").child(Login.pessoa.getId()).child(id).removeValue();
            appoitmentpatterns.remove(pos);
            notifyDataSetChanged();
        }
    }
}







