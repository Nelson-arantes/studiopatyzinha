package com.project.studiopatyzinha.services;

import android.app.Application;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.PersistableBundle;

import androidx.annotation.NonNull;

//import com.project.studiopatyzinha.Chat_activity;
//import com.project.studiopatyzinha.Pattern.MessageModel;
//import com.project.studiopatyzinha.Pattern.Person;
//import com.project.studiopatyzinha.preChat;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class notificationListener extends JobService {//não uso
    private miniAsyncTask miniAsyncTask;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        PersistableBundle bundle = jobParameters.getExtras();
        String id = bundle.getString("id");
        String admin = bundle.getString("admin");
        miniAsyncTask = new miniAsyncTask();
        miniAsyncTask.execute(id, admin);
        this.jobParameters = jobParameters;
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private class miniAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            new Thread(() -> {
                initChat(strings[0], strings[1]);

            }).start();
            return "Job Finished";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            jobFinished(jobParameters, true);
        }
    }


    private void initChat(String idpessoa, String admin) {
        if (isNetworkAvailable(getApplication())) {
            FirebaseApp.initializeApp(getApplication().getApplicationContext());
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://diskremedio-556ee-default-rtdb.firebaseio.com/");
            DatabaseReference myRef = database.getReference("Users").getRef();

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> filho = snapshot.getChildren();
                    filho.forEach(dataSnapshot1 -> {
//                        Person pessoaremota = dataSnapshot1.getValue(Person.class);
//                        if (!pessoaremota.getIdPessoa().equals(idpessoa)) {
//                            if (!pessoaremota.getAdmin().equals(admin)) {
//                                initListener(pessoaremota.getIdPessoa(), pessoaremota.getNome(), pessoaremota.getImg_bitmap(), idpessoa);
//                            }
//                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }

    }


    private void initListener(String idUser, String nome, String bitmap, String idpessoa) {
        DatabaseReference reference = FirebaseDatabase.getInstance("https://diskremedio-556ee-default-rtdb.firebaseio.com/").getReference();
        reference.child("chats").child(idUser + idpessoa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (MainActivity.pessoa == null) {
//                    for (DataSnapshot snapshot12 : snapshot.getChildren()) {
//                        MessageModel message = snapshot12.getValue(MessageModel.class);
//                        try {
//                            if (!message.getStatus().contains("Visualizada")) {
//                                if (message.getStatus().contains("NN")) {
//                                    String channelid = "MensagemID";
//                                    CharSequence name = "Mensagem";
//                                    String description = "Mensagens recebidas das conversas";
//                                    byte[] encodeByte = Base64.decode(bitmap, Base64.DEFAULT);
//                                    Bitmap bitmapB = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                                    Intent mainIntent = new Intent(getBaseContext(), preChat.class);
//                                    PendingIntent pendingIntentMain = PendingIntent.getActivity(getBaseContext(), 0, mainIntent, PendingIntent.FLAG_ONE_SHOT);
//                                    Intent chatIntent = new Intent(getBaseContext(), Chat_activity.class);
//                                    chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    chatIntent.putExtra("userId", idUser);
//                                    chatIntent.putExtra("userName", nome+"notificationlistener");
//                                    chatIntent.putExtra("profilePic", bitmap);
//                                    PendingIntent chatPIntent = PendingIntent.getActivity(getBaseContext(), 0, chatIntent, PendingIntent.FLAG_ONE_SHOT);
//                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), channelid);
//                                    builder.setSmallIcon(R.drawable.logo_pra_fundo_escuro);
//                                    builder.setContentTitle(nome);
//                                    builder.setContentText(message.getMessage());
//                                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//                                    builder.setAutoCancel(true);
//                                    builder.setLargeIcon(bitmapB);
//                                    builder.setContentIntent(pendingIntentMain);
//                                    builder.addAction(R.drawable.arrow_left, "Responder", chatPIntent);
//                                    NotificationChannel channel = new NotificationChannel(channelid, name, NotificationManager.IMPORTANCE_HIGH);
//                                    channel.setDescription(description);
//                                    NotificationManager notificationManager = getBaseContext().getSystemService(NotificationManager.class);
//                                    notificationManager.createNotificationChannel(channel);
//                                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getBaseContext());
//                                    notificationManagerCompat.notify(Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(7)), builder.build());
//                                    reference.child("chats").child(idUser + idpessoa).child(message.getTimesStamp() + message.getuId()).child("status").setValue("Recebida n");
//                                }
//                            }
//                        } catch (NullPointerException e) {
//                        }
//                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static Boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) {
            return false;
        }
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }


}