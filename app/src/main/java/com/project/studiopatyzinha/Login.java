package com.project.studiopatyzinha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.studiopatyzinha.Broadcast.diaPagamentoBroadCast;
import com.project.studiopatyzinha.pattern.Accountpattern;
import com.project.studiopatyzinha.pattern.Appoitmentpattern;
import com.project.studiopatyzinha.pattern.Comments_pattern;
import com.project.studiopatyzinha.pattern.MessageModel;
import com.project.studiopatyzinha.pattern.PostPattern;
import com.project.studiopatyzinha.pattern.Produtopattern;
import com.project.studiopatyzinha.pattern.UserPattern;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.studiopatyzinha.pattern.categoriasLC;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Login extends AppCompatActivity {
    public static Accountpattern pessoa;
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String blogstring = "";
    public static int messagenews = 0;
    public static TextView messageQuant;
    Bitmap bitmap = null;
    double valor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int v = 0;
        TextInputLayout emailed = findViewById(R.id.email);
        TextInputLayout senhaed = findViewById(R.id.pass);
//        emailed.getEditText().setText("burismpr@gmail.com");
//        senhaed.getEditText().setText("TonqA98y");

        Button entrar = findViewById(R.id.entrar);
        Button singup = findViewById(R.id.singup);
        messageQuant = new TextView(getBaseContext());
        messageQuant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    MainActivity.changenumberchatnoti(s + "");
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailed.setTranslationX(800);
        senhaed.setTranslationX(800);
        entrar.setTranslationX(800);
        singup.setTranslationX(800);

        emailed.setAlpha(v);
        senhaed.setAlpha(v);
        entrar.setAlpha(v);
        singup.setAlpha(v);

        emailed.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        senhaed.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        entrar.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        singup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        singup.setOnClickListener(view1 -> startActivity(new Intent(this, Register.class)));

        entrar.setOnClickListener(view1 -> {
            boolean mycontinuar = true;
            if (emailed.getEditText().getText().toString().trim().isEmpty()) {
                emailed.setError("Preencha esse campo");
                mycontinuar = false;
            }
            if (senhaed.getEditText().getText().toString().trim().isEmpty()) {
                senhaed.setError("Preencha esse campo");
                mycontinuar = false;
            }
            if (mycontinuar) {
                if (isNetworkAvailable(getApplication(), getBaseContext())) {
                    testelogin(emailed.getEditText().getText().toString().trim(), senhaed.getEditText().getText().toString().trim());
                }
            }
        });

    }


    private void testelogin(String email, String senha) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Contas")
                .whereEqualTo("email", email).whereEqualTo("senha", senha).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot a = task.getResult();
                        if (!a.isEmpty()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : a) {
                                Map<String, Object> map = (Map<String, Object>) queryDocumentSnapshot.getData();
                                new Thread(() -> {
                                    pessoa = new Accountpattern();
                                    pessoa.setId(map.get("id") + "");
                                    pessoa.setIfgerente(map.get("ifgerente") + "");
                                    pessoa.setNome(map.get("nome") + "");
                                    pessoa.setFollowersquant(map.get("followersquant") + "");
                                    pessoa.setFrasefilosofica(map.get("frasefilosofica") + "");
                                    pessoa.setHorario((Map<String, Object>) map.get("horario"));
                                    pessoa.setQuantpost(map.get("quantpost") + "");
                                    pessoa.setFollowersid((Map<String, Object>) map.get("followersid"));
                                    pessoa.setSenha(map.get("senha") + "");
                                    pessoa.setOpinioes((Map<String, Object>) map.get("opinioes"));
                                    pessoa.setBarbersposts((Map<String, Object>) map.get("barbersposts"));
                                    pessoa.setImgUri(map.get("imgUri") + "");
                                    pessoa.setPercentual(map.get("percentual") + "");
                                    pessoa.setDia_salario(map.get("dia_salario") + "");
                                    pessoa.setCargo(map.get("cargo") + "");
                                    pessoa.setFormapagamento(map.get("formapagamento") + "");
                                    pessoa.setGrupoAbaixo(map.get("grupoAbaixo") + "");
                                    pessoa.setEmail(map.get("email") + "");
                                    pessoa.setSelected(map.get("selected") + "");
                                    if (pessoa.getCargo().contains("Dono")) {
                                        checkInventory();
                                        initPayCheck();
                                    }
                                    runOnUiThread(this::callMainActivity);
                                }).start();
                            }

                            initNotificationBlog(getBaseContext());
                            initchat();
                        } else {
                            Toast.makeText(this, "Conta não encontrada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initPayCheck() {
        db.collection("Contas").whereNotEqualTo("cargo", "usuario").get().addOnCompleteListener(task -> {
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
                switch (func.getFormapagamento()) {
                    case "editado":
                        initEditado(func);
                        break;
                    case "mensal":
                        if (Integer.parseInt(func.getDia_salario()) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                            initMensal(func);
                        }
                        break;
                    case "semanal":
                        if (func.getDia_salario().contains(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)))) {
                            initSemanal(func);
                        }
                        break;
                    case "diario":
                        initDiario(func);
                        break;
                }
            });
        });
    }

    private void initEditado(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        String data = func.getDia_salario().split("!")[1];
        int milis2 = Integer.parseInt(func.getDia_salario().split("!")[0]);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar.set(Integer.parseInt(data.split("/")[2]), Integer.parseInt(data.split("/")[1]) - 1, Integer.parseInt(data.split("/")[0]));
        Intent myIntent = new Intent(getBaseContext(), diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        while (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, milis2);
        }
        int hora2 = 0;
        int minuto2 = 0;
        String tempo2 = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        hora2 = Integer.parseInt(tempo2.split(":")[0]);
        minuto2 = Integer.parseInt(tempo2.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora2);
        calendar.set(Calendar.MINUTE, minuto2);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    //diario
    private void initDiario(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getBaseContext(), diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        int hora = Integer.parseInt(tempo.split(":")[0]);
        int minuto = Integer.parseInt(tempo.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    //mensal
    private void initMensal(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getBaseContext(), diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        int hora = Integer.parseInt(tempo.split(":")[0]);
        int minuto = Integer.parseInt(tempo.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    //semanal
    private void initSemanal(Accountpattern func) {
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getBaseContext(), diaPagamentoBroadCast.class);
        myIntent.putExtra("id", func.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
        String tempo = (String) func.getHorasdiaspagamentos().get(MainActivity.semana.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1).toUpperCase());
        int hora = Integer.parseInt(tempo.split(":")[0]);
        int minuto = Integer.parseInt(tempo.split(":")[1]);
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public static Boolean isNetworkAvailable(Application application, Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) {
            Toast.makeText(context, R.string.withoutNet, Toast.LENGTH_SHORT).show();
            return false;
        }
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        boolean resultado = actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        if (!resultado) {
            Toast.makeText(context, R.string.withoutNet, Toast.LENGTH_SHORT).show();
        }

        return resultado;
    }

    public static void initNotificationBlog(Context context) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Blog").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                messagenews = 0;
                Map<String, Object> map = (java.util.Map<String, Object>) snapshot2.getValue();
                if (map != null) {
                    map.forEach((s, o) -> {
                        Map<String, Object> map2 = (Map<String, Object>) o;
                        map2.forEach((s1, o1) -> {
                            Map<String, Object> map3 = (Map<String, Object>) o1;
                            System.out.println("dsads a-=> "+map3);
                            db.collection("Blog").document(map3.get("id") + "").get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot snapshot = task.getResult();
                                    PostPattern mip2 = null;
                                    Map<String, Object> mippessoa = (Map<String, Object>) snapshot.get("pessoa");
                                    System.out.println("mi p-> "+mippessoa);
                                    if(mippessoa != null){

                                    Accountpattern pessoa3 = new Accountpattern(null,
                                            "", "",
                                            "", null,
                                            "", "",
                                            "", null,
                                            mippessoa.get("id") + "",
                                            mippessoa.get("imgUri") + ""
                                            , mippessoa.get("nome") + "",
                                            null, "", "", "", "", null, "", "");
                                    mip2 = new PostPattern(snapshot.get("commentsQuant") + "",
                                            snapshot.get("date") + "", snapshot.get("desc") + "", snapshot.get("id") + "",
                                            (Map<String, Object>) snapshot.get("likesids"), snapshot.get("likesquants") + "",
                                            snapshot.get("nomeid") + "", pessoa3, snapshot.get("photo") + "", Boolean.parseBoolean(snapshot.get("selflike") + ""),
                                            snapshot.get("tag") + "", snapshot.get("views") + "", (Map<String, Object>) snapshot.get("notificados"), (ArrayList<Comments_pattern>) snapshot.get("comentarios"));
                                    if (mip2 != null && pessoa.getBarbersposts().containsKey(mip2.getPessoa().getId())) {
                                        if (!mip2.getNotificados().containsKey(pessoa.getId())) {
                                            NotificationChannel channel = new NotificationChannel("channel_id", "Publicações", NotificationManager.IMPORTANCE_HIGH);
                                            channel.setDescription("description");
                                            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(channel);
                                            Intent TaskIntent = new Intent(context, MainActivity.class);
                                            TaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            PendingIntent TaskPIntent = PendingIntent.getActivity(context, 0, TaskIntent, PendingIntent.FLAG_MUTABLE);
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id");
                                            builder.setSmallIcon(R.drawable.ic_laucher);
                                            builder.setContentTitle("NOVA PUBLICAÇÃO!!!");
                                            builder.setContentText(mip2.getDesc());
                                            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                            builder.setContentIntent(TaskPIntent);
                                            builder.setAutoCancel(true);
                                            builder.setGroup("Publicações");
                                            builder.setStyle(new NotificationCompat.BigTextStyle());
                                            NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
                                            notificationCompat.notify(0, builder.build());
                                            mip2.getNotificados().put(Login.pessoa.getId(), "");
                                            db.collection("Blog").document(map3.get("id") + "").set(mip2);
                                        }
                                    }
                                    }
                                }
                            });
                        });
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initchat() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Contas").get().addOnCompleteListener(task -> {
            QuerySnapshot filho = task.getResult();
            filho.forEach(dataSnapshot1 -> {
                Accountpattern pessoaremota = new Accountpattern();
                pessoaremota.setId(dataSnapshot1.get("id") + "");
                boolean ifadmina = !String.valueOf(dataSnapshot1.get("cargo")).equals("usuario");
                pessoaremota.setImgUri(dataSnapshot1.get("imgUri") + "");
                pessoaremota.setNome(dataSnapshot1.get("nome") + "");
                boolean ifadmin = Login.pessoa.getCargo().equals("usuairo");
                if (ifadmina != ifadmin) {
                    UserPattern Userpattern = new UserPattern(pessoaremota.getImgUri(), pessoaremota.getNome(), "", pessoaremota.getId(), "", "0");
                    if (Userpattern.getIdUser() != null) {
                        if (!Userpattern.getIdUser().equals(Login.pessoa.getId())) {
                            reference.child("chats").child(pessoaremota.getId() + Login.pessoa.getId()).addValueEventListener(new ValueEventListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    messagenews = 0;
                                    for (DataSnapshot snapshot12 : snapshot.getChildren()) {
                                        MessageModel message = snapshot12.getValue(MessageModel.class);
                                        try {
                                            if (!message.getuId().equals(Login.pessoa.getId())) {
                                                if (!message.getStatus().contains("Visualizada")) {
                                                    messagenews++;
                                                    if (Chat_activity.receiveId == null) {
                                                        new notificarChat().execute(Userpattern.getImg_user(), Userpattern.getName_user(), message.getMessage());
                                                    } else {
                                                        if (!Chat_activity.receiveId.equals(Userpattern.getIdUser())) {
                                                            new notificarChat().execute(Userpattern.getImg_user(), Userpattern.getName_user(), message.getMessage());
                                                        }
                                                    }
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("status", "recebida");
                                                    reference.child("chats").child(pessoaremota.getId() + pessoa.getId()).child(message.getTimesStamp() + message.getuId()).updateChildren(map);
                                                }
                                                messageQuant.setText(messagenews + "");
                                            }

                                        } catch (NullPointerException e) {
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            });
        });

    }

    private void callMainActivity() {
        if (MainActivity.changeaccount != null) {
            MainActivity.changeaccount.setText("");
        }
        Intent as = new Intent(getBaseContext(), MainActivity.class);
        startActivity(as);
    }

    private void checkInventory() {

        db.collection("Produtos").get().addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task2.getResult()) {
                    Map<String, Object> map2 = queryDocumentSnapshot.getData();
                    Produtopattern mip = new Produtopattern();
                    mip.setIdexterno(map2.get("idexterno") + "");
                    mip.setNome(map2.get("nome") + "");
                    mip.setModelo(map2.get("modelo") + "");
                    mip.setFabricante(map2.get("fabricante") + "");
                    mip.setDesconto(map2.get("desconto") + "");
                    mip.setPreco_original(map2.get("preco_original") + "");
                    mip.setQuant_per_cx(map2.get("quant_per_cx") + "");
                    mip.setUsosindicados(map2.get("usosindicados") + "");
                    mip.setUsosNindicados(map2.get("usosNindicados") + "");
                    mip.setBitmapImg(map2.get("bitmapImg") + "");
                    mip.setQuant_minima(map2.get("quant_minima") + "");
                    mip.setQuant_produto_interno(map2.get("quant_produto_interno") + "");
                    mip.setQuant_produto("");

                    String mensage = "";
                    if (mip.getQuant_produto_interno().equals("0")) {
                        mensage = "Estamos sem " + mip.getNome();
                    }

                    if (Integer.parseInt(mip.getQuant_produto_interno()) < Integer.parseInt(mip.getQuant_minima())) {
                        if (mensage.isEmpty()) {
                            mensage = "Estamos com pouco " + mip.getNome() + " " + mip.getModelo() + " no estoque";
                        }
                        NotificationChannel channel = new NotificationChannel("Estoque", "Estoque", NotificationManager.IMPORTANCE_HIGH);
                        channel.setDescription("Estoque");
                        NotificationManager notificationManager = getBaseContext().getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                        Intent TaskIntent = new Intent(getBaseContext(), MainActivity.class);
                        TaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent TaskPIntent = PendingIntent.getActivity(getBaseContext(), 0, TaskIntent, PendingIntent.FLAG_MUTABLE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "Estoque");
                        builder.setSmallIcon(R.drawable.ic_laucher);
                        builder.setContentTitle("Estoque");
                        builder.setContentText(mensage);
                        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        builder.setContentIntent(TaskPIntent);
                        builder.setAutoCancel(true);
                        builder.setGroup("Estoque");
                        builder.setStyle(new NotificationCompat.BigTextStyle());
                        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(getBaseContext());
                        notificationCompat.notify(0, builder.build());
                    }


                }
            }
        });

    }

    //    private Bitmap getBitmapFromURL(String src) {
//        try {
//            URL s = new URL(src);
//
//        } catch (MalformedURLException e) {
//            System.out.println("sssssss -> " + e);
//        }
//        ImageView s = new ImageView(getBaseContext());
//        Glide.with(getBaseContext()).load(src).into(s);
//        Drawable d = s.getDrawable();
//        Bitmap myLogo = ((BitmapDrawable) d).getBitmap();
//
//        return myLogo;
//
//    }
//
//    public static Bitmap drawableToBitmap(Drawable drawable) {
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//
//        int width = drawable.getIntrinsicWidth();
//        width = width > 0 ? width : 1;
//        int height = drawable.getIntrinsicHeight();
//        height = height > 0 ? height : 1;
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//
//        return bitmap;
//    }
    class notificarChat extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {

            String urldisplay = url[0];
            InputStream srt = null;
            try {
                srt = new URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(srt);
                NotificationChannel channel = new NotificationChannel("Conversas", "Conversas", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Conversas");
                NotificationManager notificationManager = getBaseContext().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
                Intent TaskIntent = new Intent(getBaseContext(), preChat.class);
                TaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent TaskPIntent = PendingIntent.getActivity(getBaseContext(), 0, TaskIntent, PendingIntent.FLAG_MUTABLE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "Conversas");
                builder.setSmallIcon(R.drawable.ic_laucher);
                builder.setContentTitle(url[1]);
                builder.setContentText(url[2]);
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setContentIntent(TaskPIntent);
                builder.setLargeIcon(bitmap);
                builder.setAutoCancel(true);
                builder.setGroup("Conversas");
                builder.setStyle(new NotificationCompat.BigTextStyle());
                NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(getBaseContext());
                notificationCompat.notify(0, builder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }


}


