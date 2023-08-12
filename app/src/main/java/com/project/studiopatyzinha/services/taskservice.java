package com.project.studiopatyzinha.services;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.project.studiopatyzinha.Broadcast.alarmreceiverbroadcast;
import com.project.studiopatyzinha.pattern.Events;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.TaskManager;
import com.project.studiopatyzinha.banco.bancolocal;


public class taskservice extends JobService {
    private miniAsyncTaske teste;

    @SuppressWarnings({"deprecation"})
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String id = intent.getExtras().getString("notid");
        String progresso = intent.getExtras().getString("progress");
        teste = new miniAsyncTaske();
        teste.execute(id, progresso);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private class miniAsyncTaske extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String notid = strings[0];
            Events evento = bancolocal.getINSTANCE(getBaseContext()).getLembreteByID(notid);
            if (evento != null) {
                if (Integer.parseInt(evento.getAtualProgresso()) != Integer.parseInt(evento.getDuracao())) {
                    String progress = "";
                    if (evento.getDuracao().equals("1")) {
                        evento.setAtualProgresso(String.valueOf(Integer.parseInt(evento.getAtualProgresso()) + 1));
                        bancolocal.getINSTANCE(getBaseContext()).updateLembreteAll(evento);
                    } else {
                        int atual = Integer.parseInt(evento.getAtualProgresso());
                        atual++;
                        progress = "Lembrete " + atual + " De " + evento.getDuracao();
                        evento.setAtualProgresso(String.valueOf(Integer.parseInt(evento.getAtualProgresso()) + 1));
                        bancolocal.getINSTANCE(getBaseContext()).updateLembreteAll(evento);
                        Intent myIntent = new Intent(getBaseContext(), alarmreceiverbroadcast.class);
                        myIntent.putExtra("atu", "sim");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                    }

                    NotificationChannel channel = new NotificationChannel("channel_Lembretes", "Lembretes", NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("Seus lembretes serão notificados por aqui");
                    NotificationManager notificationManager = getBaseContext().getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                    Intent TaskIntent = new Intent(getBaseContext(), TaskManager.class);
                    TaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent TaskPIntent = PendingIntent.getActivity(getBaseContext(), 0,
                            TaskIntent, PendingIntent.FLAG_IMMUTABLE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "channel_Lembretes");
                    builder.setSmallIcon(R.drawable.ic_laucher);
                    String texto = evento.getEVENT();
                    if (!evento.getDescricao().isEmpty()) {
                        texto += "\n" + evento.getDescricao();
                    }
                    if (!progress.isEmpty()) {
                        texto += "\n" + progress;
                    }
                    builder.setContentText(texto);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setContentIntent(TaskPIntent);
                    builder.setAutoCancel(true);
                    builder.setGroup("Lembretes");
                    if (!evento.getAtualProgresso().equals(evento.getDuracao())) {
                        builder.addAction(R.drawable.ic_close_30, "Desativar o lembrete", TaskPIntent);
                    }
                    builder.setStyle(new NotificationCompat.BigTextStyle());
                    NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(getBaseContext());
                    notificationCompat.notify(0, builder.build());

                }
            }
            return "Job Finished";

        }
    }
}