package com.project.studiopatyzinha.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;

import com.project.studiopatyzinha.Login;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class estouChegandoListenerService extends JobService {
    private miniAsyncTaske teste;

    @SuppressWarnings({"static-access", "deprecation"})
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String id = intent.getExtras().getString("id");
        String status = intent.getExtras().getString("status");
        teste = new miniAsyncTaske();
        teste.execute(id, status);
        return super.onStartCommand(intent, flags, startId);}

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
            String id = strings[0];
            String status = strings[1];
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Agendamentos").child(Login.pessoa.getId()).child(id).child("alarm").setValue("true2");
            if (status.equals("going")) {
                reference.child("Agendamentos").child(Login.pessoa.getId()).child(id).child("presenca").setValue("true");
            }else{
                reference.child("Agendamentos").child(Login.pessoa.getId()).child(id).removeValue();
            }
            return "Job Finished";

        }
    }
}