package com.project.studiopatyzinha.Broadcast;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.TaskManager;
import com.project.studiopatyzinha.banco.bancolocal;
import com.project.studiopatyzinha.services.estouChegandoListenerService;
import com.project.studiopatyzinha.services.taskservice;

import java.util.Calendar;

public class estouchegandobroadcast extends BroadcastReceiver {
    public estouchegandobroadcast() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent i) {


        String channelid = "Assistente de agendamentos";
        CharSequence name = "Assistente de agendamentos";
        String description = "Mensagens lembrando dos futuros agendamentos";
        NotificationCompat.Builder buildernotification = new NotificationCompat.Builder(context, channelid);
        buildernotification.setSmallIcon(R.drawable.ic_laucher);
        buildernotification.setContentTitle("ESTÁ QUASE NA HORA!!!");
        buildernotification.setContentText("O seu agendamento " + i.getStringExtra("nomeserv") + " das " + i.getStringExtra("horaescolhida") + " está te esperando");
        buildernotification.setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationChannel channel = new NotificationChannel(channelid, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        Intent TaskIntent = new Intent(context, estouChegandoListenerService.class);
        TaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskIntent.putExtra("id", i.getStringExtra("id"));
        TaskIntent.putExtra("status", "going");
        PendingIntent TaskPIntent = PendingIntent.getService(context, 0, TaskIntent, PendingIntent.FLAG_IMMUTABLE);
        buildernotification.setContentIntent(TaskPIntent);
        buildernotification.addAction(R.drawable.tic_enviada, "Já estou chegando", TaskPIntent);
        buildernotification.setStyle(new NotificationCompat.BigTextStyle());
        buildernotification.setAutoCancel(true);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(Integer.parseInt(i.getStringExtra("id").substring(7)), buildernotification.build());

    }


}
