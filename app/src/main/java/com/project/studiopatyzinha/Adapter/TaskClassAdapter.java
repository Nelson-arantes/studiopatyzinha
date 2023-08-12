package com.project.studiopatyzinha.Adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.project.studiopatyzinha.Broadcast.alarmreceiverbroadcast;
import com.project.studiopatyzinha.R;
import com.project.studiopatyzinha.TaskManager;
import com.project.studiopatyzinha.banco.bancolocal;
import com.project.studiopatyzinha.pattern.Events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskClassAdapter extends RecyclerView.Adapter<TaskClassAdapter.meuViewholdere> {
    private final ArrayList<Events> eventList;
    private Context context;

    public TaskClassAdapter(ArrayList<Events> collectEventByDate) {
        this.eventList = collectEventByDate;

    }


    @NonNull
    @Override
    public meuViewholdere onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayouthorizontal, parent, false);
        this.context = view.getContext();
        return new meuViewholdere(view);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewholdere holder, int position) {
        Events event = eventList.get(holder.getAdapterPosition());
        TaskManager.iv_today.setVisibility(View.GONE);
        if (!event.getDuracao().equals("001")) {
            holder.eventeprogress.setText(event.getAtualProgresso() + " de " + event.getDuracao());
        }

        if (event.getFrequencia().isEmpty()) {
            holder.eventeprogress.setText(R.string.lembreteunico);
            holder.dateTXT.setVisibility(View.GONE);
        } else {
            holder.dateTXT.setVisibility(View.VISIBLE);
            String aa = event.getFrequencia().replace(";", ",");
            String bb = aa.substring(0, aa.length() - 1) + "";
            holder.dateTXT.setText(String.format(" %s", bb));
        }

        holder.Time.setText("Iniciou as " + event.getTIME() + " " + event.getDAY() + "/" + event.getMONTH() + "/" + event.getYEAR());
        holder.Event.setText(event.getEVENT());
        Calendar dayofWeekCalendar = Calendar.getInstance();
        dayofWeekCalendar.set(Integer.parseInt(event.getYEAR()), Integer.parseInt(event.getMONTH()) - 1, Integer.parseInt(event.getDAY()));
        List<String> diaSemana = new ArrayList<>();
        diaSemana.add("Dom");
        diaSemana.add("Seg");
        diaSemana.add("Ter");
        diaSemana.add("Qua");
        diaSemana.add("Qui");
        diaSemana.add("Sex");
        diaSemana.add("Sab");
        holder.deletbtton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Deseja apagar esse lembrete ?")
                    .setCancelable(true)
                    .setPositiveButton("Sim", (dialog, id) -> holder.delete(event, holder.getAdapterPosition()))
                    .setNegativeButton("Não", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();

        });
        Calendar timecalendario = Calendar.getInstance();
        timecalendario.setTime(ConvertStringTotime(event.getTIME()));
        if (!event.getDescricao().equals("")) {
            holder.descH.setText(event.getDescricao());
        } else {
            holder.descH.setVisibility(View.GONE);
        }
        if (event.getDuracao().equals(event.getAtualProgresso())) {
            holder.dateTXT.setTextColor(AppCompatResources.getColorStateList(context, R.color.cinza));
            holder.Time.setTextColor(AppCompatResources.getColorStateList(context, R.color.cinza));
            holder.eventeprogress.setTextColor(AppCompatResources.getColorStateList(context, R.color.cinza));
        }
    }


    private Date ConvertStringTotime(String eventDate) {
        Locale locale = new Locale("pt", "BR");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", locale);
        Date date = null;
        try {
            date = format.parse(eventDate);

        } catch (ParseException e) {
            System.out.println("fffffffffffff -> " + e);
        }
        return date;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class meuViewholdere extends RecyclerView.ViewHolder {

        TextView dateTXT, Event, Time, eventeprogress, descH;
        Button deletbtton;

        public meuViewholdere(@NonNull View itemView) {
            super(itemView);
            dateTXT = itemView.findViewById(R.id.eventeDateH);
            eventeprogress = itemView.findViewById(R.id.eventeprogress);
            Event = itemView.findViewById(R.id.eventeNameH);
            descH = itemView.findViewById(R.id.descH);
            Time = itemView.findViewById(R.id.eventeTimeH);
            deletbtton = itemView.findViewById(R.id.deleteventH);
        }

        private void delete(Events event, int pos) {

            bancolocal.getINSTANCE(context).deleteLembrete(event.getRequestCode());
            eventList.remove(pos);
            cancelAlarm(Integer.parseInt(event.getRequestCode()), event.getAtualProgresso(), event.getDuracao());
            CollectEventstask();
            if (TaskManager.events_today.isEmpty()) {
                if (TaskManager.RV_today.getVisibility() != View.GONE) {
                    TaskManager.iv_today.setVisibility(View.VISIBLE);
                }
            } else {
                TaskManager.iv_today.setVisibility(View.GONE);
            }
        }
    }


    public void cancelAlarm(int Requestcode, String progress, String duracao) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, alarmreceiverbroadcast.class);
        int progressoint = Integer.parseInt(progress);
        while (progressoint < Integer.parseInt(duracao)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(Requestcode + String.valueOf(progressoint)), myIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.cancel(pendingIntent);
            progressoint++;
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void CollectEventstask() {
        TaskManager.events_today.clear();
        try {
            TaskManager.events_today.addAll(bancolocal.getINSTANCE(context).get_Lembrete());
        } catch (Exception e) {
            System.out.println("eeeeeeeeee -> " + e);
        }
        TaskManager.RV_todayAdapter.notifyDataSetChanged();

    }


}







