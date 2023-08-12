package com.project.studiopatyzinha.Broadcast;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.studiopatyzinha.Gerenciar;
import com.project.studiopatyzinha.MainActivity;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.TaskManager;
import com.project.studiopatyzinha.banco.bancolocal;
import com.project.studiopatyzinha.gerenciar.Funcionarios;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.services.taskservice;

import java.util.Calendar;
import java.util.Map;

public class diaPagamentoBroadCast extends BroadcastReceiver {
    double valor = 0;

    public diaPagamentoBroadCast() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent i) {
        FirebaseFirestore.getInstance().collection("Contas").whereEqualTo("id", i.getStringExtra("id")).get().addOnCompleteListener(task -> {
            QuerySnapshot filho = task.getResult();
            filho.forEach(dataSnapshot1 -> {
                Accountpattern func = new Accountpattern();
                func.setFormapagamento(dataSnapshot1.get("formapagamento") + "");
                func.setId(dataSnapshot1.get("id") + "");
                func.setNome(dataSnapshot1.get("nome") + "");
                func.setFollowersquant(dataSnapshot1.get("followersquant") + "");
                func.setFrasefilosofica(dataSnapshot1.get("frasefilosofica") + "");
                func.setHorario((Map<String, Object>) dataSnapshot1.get("horario"));
                func.setQuantpost(dataSnapshot1.get("quantpost") + "");
                func.setFollowersid((Map<String, Object>) dataSnapshot1.get("followersid"));
                func.setSenha(dataSnapshot1.get("senha") + "");
                func.setOpinioes((Map<String, Object>) dataSnapshot1.get("opinioes"));
                func.setBarbersposts((Map<String, Object>) dataSnapshot1.get("barbersposts"));
                func.setImgUri(dataSnapshot1.get("imgUri") + "");
                func.setPercentual(dataSnapshot1.get("percentual") + "");
                func.setDia_salario(dataSnapshot1.get("dia_salario") + "");
                func.setCargo(dataSnapshot1.get("cargo") + "");
                func.setGrupoAbaixo(dataSnapshot1.get("grupoAbaixo") + "");
                func.setEmail(dataSnapshot1.get("email") + "");
                func.setSelected(dataSnapshot1.get("selected") + "");
                func.setUltimopagamento(dataSnapshot1.get("ultimopagamento") + "");
                func.setHorasdiaspagamentos((Map<String, Object>) dataSnapshot1.get("horasdiaspagamentos"));
                initValor(func, context);
            });
        });
    }

    private void initValor(Accountpattern func, Context context) {
        long valordata = Long.parseLong(func.getUltimopagamento() + "0");//long
        DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("Agendamentos");
        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> a = snapshot.getChildren();
                a.forEach(dataSnapshot -> {
                    Map<String, Object> servicomap = (Map<String, Object>) dataSnapshot.getValue();
                    servicomap.forEach((s, o) -> {
                        Map<String, Object> map = (Map<String, Object>) o;
                        Appoitmentpattern agendamento = new Appoitmentpattern();
                        agendamento.setValorservico(map.get("valorservico") + "");
                        agendamento.setHora(map.get("hora") + "");
                        agendamento.setWeekyear(map.get("weekyear") + "");
                        agendamento.setTimeinmilis(map.get("timeinmilis") + "");
                        agendamento.setNomefunc(map.get("nomefunc") + "");
                        agendamento.setTempo(map.get("tempo") + "");
                        agendamento.setIdpessoa(map.get("idpessoa") + "");
                        agendamento.setIdservico(map.get("idservico") + "");
                        agendamento.setPresenca(map.get("presenca") + "");
                        agendamento.setFunc(map.get("func") + "");
                        agendamento.setNomepessoa(map.get("nomepessoa") + "");
                        agendamento.setConcluido(map.get("concluido") + "");
                        agendamento.setAlarm(map.get("alarm") + "");
                        agendamento.setId(map.get("id") + "");
                        agendamento.setId(map.get("id") + "");
                        agendamento.setNomeservico(map.get("nomeservico") + "");
                        agendamento.setDia(map.get("dia") + "");
                        if (agendamento.getFunc().equals(func.getId())) {
                            if (Long.parseLong(agendamento.getId()) < valordata) {
                                valor += Double.parseDouble(agendamento.getValorservico());
                            }
                        }
                    });
                });
                NotificationChannel channel = new NotificationChannel("channel_id", "Pagamentos", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("description");
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                Intent TaskIntent = new Intent(context, Funcionarios.class);
                TaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent TaskPIntent = PendingIntent.getActivity(context, 0, TaskIntent, PendingIntent.FLAG_IMMUTABLE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id");
                builder.setSmallIcon(R.mipmap.ic_launcher_foreground);
                builder.setContentTitle("Dia de pagamento");
                String nome = func.getNome();

                builder.setContentText("Hoje é dia de pagar o(a) " + nome + " o Valor de R$ " + valor + ".");//-1000
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setContentIntent(TaskPIntent);
                builder.setAutoCancel(true);
                builder.setGroup("Pagamentos");
                builder.setStyle(new NotificationCompat.BigTextStyle());
                NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
                notificationCompat.notify(0, builder.build());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
