package com.project.studiopatyzinha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.studiopatyzinha.Adapter.TaskClassAdapter;
import com.project.studiopatyzinha.Broadcast.alarmreceiverbroadcast;
import com.project.studiopatyzinha.banco.bancolocal;
import com.project.studiopatyzinha.pattern.Events;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskManager extends AppCompatActivity {
    String daysweek;
    public static RecyclerView RV_today;
    FloatingActionButton buttonfabAddEvento;
    View taskview;
    public static ArrayList<Events> events_today;
    Locale locale;
    SimpleDateFormat dateFormat, monthFormat, yearFormat;
    AlertDialog a;
    String alarmHour, alarmMinut;
    String numero_repeat_noification;
    boolean ifcontinue_Event = true;
    public static ImageView iv_today;
    TextInputEditText eventName, local_Evento;
    TextInputEditText descricao_Evento_ed;
    public static TaskClassAdapter RV_todayAdapter;
    AlertDialog.Builder builder;
    String hournow;
    TextView Eventtimeshow, eventdate, txt_quanttempo;
    String event_Time;
    ImageButton settime, setDate;
    Calendar c;
    ImageView addEvent;
    LinearLayout repetir_layout;
    CheckBox repetir_lembreteck, menotificar;
    Spinner number_spinner, spinner_repetir, meses_spinner;
    GridLayout Semana_layout;
    ConstraintLayout Mensal_layout, Anual_layout;
    CheckBox domck, segck, terck, quack, quick, sexck, sabck;
    NumberPicker number_picker_days_month;
    Toolbar toolbar_taskmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager);
        iv_today = findViewById(R.id.all_done_iv_today);
        toolbar_taskmanager = findViewById(R.id.toolbar_taskmanager);
        this.setSupportActionBar(toolbar_taskmanager);
        this.getSupportActionBar().setTitle("Meus Lembretes");
        toolbar_taskmanager.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar_taskmanager.setTitleTextColor(Color.WHITE);
        toolbar_taskmanager.setNavigationOnClickListener(v -> finish());

        buttonfabAddEvento = findViewById(R.id.buttonfabAddEvento);
        RV_today = findViewById(R.id.RV_today);
        events_today = new ArrayList<>();
        locale = new Locale("pt", "br");
        dateFormat = new SimpleDateFormat("dd", locale);
        monthFormat = new SimpleDateFormat("MM", locale);
        yearFormat = new SimpleDateFormat("yyyy", locale);
        RV_today.setVisibility(View.VISIBLE);
        buttonfabAddEvento.setOnClickListener(v -> {
            daysweek = "";
            numero_repeat_noification = "1";
            c = Calendar.getInstance();
            builder = new AlertDialog.Builder(v.getContext(), R.style.AlertDialogCustomfull);
            taskview = View.inflate(getBaseContext(), R.layout.newevent_layout, null);
            Eventtimeshow = taskview.findViewById(R.id.eventtime);
            local_Evento = taskview.findViewById(R.id.local_Evento);
            Semana_layout = taskview.findViewById(R.id.Semana_layout);
            Mensal_layout = taskview.findViewById(R.id.Mensal_layout);
            meses_spinner = taskview.findViewById(R.id.meses_spinner);
            Anual_layout = taskview.findViewById(R.id.Anual_layout);
            number_picker_days_month = taskview.findViewById(R.id.number_picker_days_month);
            eventdate = taskview.findViewById(R.id.eventdate);
            String diad = "";
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 10) {
                diad += "0";
            }
            diad += Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            String mesd = "";
            if ((Calendar.getInstance().get(Calendar.MONTH) + 1) < 10) {
                mesd += "0";
            }
            mesd += Calendar.getInstance().get(Calendar.MONTH) + 1;
            eventdate.setText(diad + "/" + mesd + "/" + Calendar.getInstance().get(Calendar.YEAR));
            setDate = taskview.findViewById(R.id.setDate);
            repetir_layout = taskview.findViewById(R.id.repetir_layout);
            domck = taskview.findViewById(R.id.domck);
            segck = taskview.findViewById(R.id.segck);
            terck = taskview.findViewById(R.id.terck);
            quack = taskview.findViewById(R.id.quack);
            quick = taskview.findViewById(R.id.quick);
            sexck = taskview.findViewById(R.id.sexck);
            sabck = taskview.findViewById(R.id.sabck);
            eventName = taskview.findViewById(R.id.eventeName);
            number_spinner = taskview.findViewById(R.id.number_spinner);
            spinner_repetir = taskview.findViewById(R.id.spinner_repetir);
            settime = taskview.findViewById(R.id.SetTime);
            repetir_lembreteck = taskview.findViewById(R.id.repetir_lembreteck);
            menotificar = taskview.findViewById(R.id.menotificar);
            txt_quanttempo = taskview.findViewById(R.id.txt_quanttempo);
            number_picker_days_month.setMinValue(1);
            number_picker_days_month.setMaxValue(31);
            number_picker_days_month.setValue(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            ArrayList<String> lista = new ArrayList<>();
            lista.add("Semanalmente");
            lista.add("Mensalmente");
            lista.add("Anualmente");
            ArrayAdapter<String> aa = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, lista);
            ArrayAdapter<String> bb = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, MainActivity.meses);
            meses_spinner.setAdapter(bb);
            meses_spinner.setSelection(Calendar.getInstance().get(Calendar.MONTH));
            spinner_repetir.setAdapter(aa);
            spinner_repetir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            txt_quanttempo.setText("Semanas");
                            Semana_layout.setVisibility(View.VISIBLE);
                            Mensal_layout.setVisibility(View.GONE);
                            Anual_layout.setVisibility(View.GONE);
                            break;
                        case 1:
                            txt_quanttempo.setText("Mes");
                            Mensal_layout.setVisibility(View.VISIBLE);
                            Semana_layout.setVisibility(View.GONE);
                            Anual_layout.setVisibility(View.GONE);
                            break;
                        case 2:
                            txt_quanttempo.setText("Anos");
                            Semana_layout.setVisibility(View.GONE);
                            Mensal_layout.setVisibility(View.VISIBLE);
                            Anual_layout.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
            initck();

            ImageView close_dialog_new_event = taskview.findViewById(R.id.close_dialog_new_event);
            close_dialog_new_event.setOnClickListener(v13 -> a.dismiss());
            descricao_Evento_ed = taskview.findViewById(R.id.descricao_Evento);
            addEvent = taskview.findViewById(R.id.addEvent);
            initSpinner();

            setDate.setOnClickListener(v1 -> {
                DatePickerDialog dialog = new DatePickerDialog(TaskManager.this);

                dialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                    String dia = "";
                    if (dayOfMonth < 10) {
                        dia += "0";
                    }
                    dia += dayOfMonth;
                    String mes = "";
                    if ((month + 1) < 10) {
                        mes += "0";
                    }
                    mes += (month + 1);
                    eventdate.setText(dia + "/" + mes + "/" + year);
                });
                dialog.show();
            });

            builder.setCancelable(true);

            hournow = "";
            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10) {
                hournow += "0";
            }
            hournow += Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":";
            if ((Calendar.getInstance().get(Calendar.MINUTE)) < 10) {
                hournow += "0";
            }
            hournow += (Calendar.getInstance().get(Calendar.MINUTE));
            repetir_lembreteck.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    Semana_layout.setVisibility(View.VISIBLE);
                    spinner_repetir.setVisibility(View.VISIBLE);
                    repetir_layout.setVisibility(View.VISIBLE);
                    spinner_repetir.setSelection(0);
                } else {
                    spinner_repetir.setVisibility(View.GONE);
                    Semana_layout.setVisibility(View.GONE);
                    Mensal_layout.setVisibility(View.GONE);
                    Anual_layout.setVisibility(View.GONE);
                    repetir_layout.setVisibility(View.GONE);
                }
            });
            settime.setOnClickListener(v1 -> {
                Calendar now = Calendar.getInstance();
                int hours = now.get(Calendar.HOUR_OF_DAY);
                int minuts = now.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(a.getWindow().getContext(),
                        (view1, hourOfDay, minute) -> {
                            alarmHour = "";
                            alarmMinut = "";
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            if (c.get(Calendar.HOUR_OF_DAY) < 10) {
                                alarmHour = "0";
                            }
                            alarmHour += c.get(Calendar.HOUR_OF_DAY);
                            if (c.get(Calendar.MINUTE) < 10) {
                                alarmMinut = "0";
                            }
                            alarmMinut += c.get(Calendar.MINUTE);
                            event_Time = alarmHour + ":" + alarmMinut;
                            Eventtimeshow.setText(event_Time);
                        }, hours, minuts, true);
                timePickerDialog.show();

            });
            Eventtimeshow.setText(hournow);

            if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10) {
                alarmHour = "0" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            } else {
                alarmHour = "" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            }
            if (Calendar.getInstance().get(Calendar.MINUTE) < 10) {
                alarmMinut = "0" + Calendar.getInstance().get(Calendar.MINUTE);
            } else {
                alarmMinut = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
            }


            addEvent.setOnClickListener(v1 -> {
                if (eventName.getText().toString().trim().isEmpty() && spinner_repetir.getSelectedItemPosition() == 0) {
                    eventName.setError(getApplicationContext().getString(R.string.empty_field));
                    ifcontinue_Event = false;
                } else {
                    ifcontinue_Event = true;
                }


                if (ifcontinue_Event) {
                    Calendar calendario1 = Calendar.getInstance();
                    calendario1.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmHour));
                    calendario1.set(Calendar.MINUTE, Integer.parseInt(alarmMinut));
                    calendario1.set(Integer.parseInt(eventdate.getText().toString().split("/")[2]), Integer.parseInt(eventdate.getText().toString().split("/")[1]) - 1, Integer.parseInt(eventdate.getText().toString().split("/")[0]));
                    SetAlarm(calendario1);
                    a.dismiss();
                }
            });
            builder.setView(taskview);
            builder.setCancelable(true);
            a = builder.create();
            if (taskview.getParent() != null) {
                ((ViewGroup) taskview.getParent()).removeAllViews();
            } else {
                a.setView(null);
                a.setView(taskview);
            }
            a.show();
        });
        events_today.addAll(bancolocal.getINSTANCE(getBaseContext()).get_Lembrete());
        RV_todayAdapter = new TaskClassAdapter(events_today);
        RV_today.setAdapter(RV_todayAdapter);
        RV_today.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
        if (events_today.isEmpty()) {
            iv_today.setVisibility(View.VISIBLE);
        }
    }

    private void initck() {

        domck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (daysweek.contains("Dom;")) {
                daysweek.replace("Dom;", "");
            } else {
                daysweek += "Dom;";
            }
        });
        segck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (daysweek.contains("Seg;")) {
                daysweek.replace("Seg;", "");
            } else {
                daysweek += "Seg;";
            }
        });
        terck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (daysweek.contains("Ter;")) {
                daysweek.replace("Ter;", "");
            } else {
                daysweek += "Ter;";
            }
        });
        quack.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (daysweek.contains("Qua;")) {
                daysweek.replace("Qua;", "");
            } else {
                daysweek += "Qua;";
            }
        });
        quick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (daysweek.contains("Qui;")) {
                daysweek.replace("Qui;", "");
            } else {
                daysweek += "Qui;";
            }
        });
        sexck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (daysweek.contains("Sex;")) {
                daysweek.replace("Sex;", "");
            } else {
                daysweek += "Sex;";
            }
        });
        sabck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (daysweek.contains("Sab;")) {
                daysweek.replace("Sab;", "");
            } else {
                daysweek += "Sab;";
            }
        });
    }

    private void initSpinner() {

        ArrayList<String> numberlist = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            numberlist.add(i + "");
        }
        ArrayAdapter<String> numberadaptern = new ArrayAdapter<>(getBaseContext(), R.layout.item_spinner, numberlist);
        number_spinner.setAdapter(numberadaptern);
    }


    public void SetAlarm(Calendar calendar) {
        int Requestcode = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(5));
        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Intent myIntent = new Intent(getBaseContext(), alarmreceiverbroadcast.class);
        myIntent.putExtra("id", String.valueOf(Requestcode));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), Requestcode, myIntent, PendingIntent.FLAG_IMMUTABLE);
        if (menotificar.isChecked()) {
            if (repetir_lembreteck.isChecked()) {
                numero_repeat_noification = number_spinner.getSelectedItem() + "";
                if (Semana_layout.getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < number_spinner.getSelectedItemPosition() + 2; i++) {
                        if (daysweek.contains("Dom")) {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(calendar.getTimeInMillis());
                            if (1 > calendar2.get(Calendar.DAY_OF_WEEK)) {
                                calendar2.add(Calendar.WEEK_OF_MONTH, 1);
                            }
                            calendar2.set(Calendar.DAY_OF_WEEK, 1);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);

                        }
                        if (daysweek.contains("Seg")) {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(calendar.getTimeInMillis());
                            if (2 > calendar2.get(Calendar.DAY_OF_WEEK)) {
                                calendar2.add(Calendar.WEEK_OF_MONTH, 1);
                            }
                            calendar2.set(Calendar.DAY_OF_WEEK, 2);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                        }
                        if (daysweek.contains("Ter")) {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(calendar.getTimeInMillis());
                            if (Calendar.TUESDAY > calendar2.get(Calendar.DAY_OF_WEEK)) {
                                calendar2.add(Calendar.WEEK_OF_MONTH, 1);
                            }
                            calendar2.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);

                        }
                        if (daysweek.contains("Qua")) {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(calendar.getTimeInMillis());
                            if (4 > calendar2.get(Calendar.DAY_OF_WEEK)) {
                                calendar2.add(Calendar.WEEK_OF_MONTH, 1);
                            }
                            calendar2.set(Calendar.DAY_OF_WEEK, 4);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                        }
                        if (daysweek.contains("Qui")) {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(calendar.getTimeInMillis());
                            if (5 > calendar2.get(Calendar.DAY_OF_WEEK)) {
                                calendar2.add(Calendar.WEEK_OF_MONTH, 1);
                            }
                            calendar2.set(Calendar.DAY_OF_WEEK, 5);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                        }
                        if (daysweek.contains("Sex")) {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(calendar.getTimeInMillis());
                            if (6 > calendar2.get(Calendar.DAY_OF_WEEK)) {
                                calendar2.add(Calendar.WEEK_OF_MONTH, 1);
                            }
                            calendar2.set(Calendar.DAY_OF_WEEK, 6);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                        }
                        if (daysweek.contains("Sab")) {
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTimeInMillis(calendar.getTimeInMillis());
                            if (7 > calendar2.get(Calendar.DAY_OF_WEEK)) {
                                calendar2.add(Calendar.WEEK_OF_MONTH, 1);
                            }
                            calendar2.set(Calendar.DAY_OF_WEEK, 7);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                        }
                        if (i > (number_spinner.getSelectedItemPosition() + 2)) {
                            calendar.add(Calendar.WEEK_OF_MONTH, 1);
                        }
                    }
                } else {
                    if (Mensal_layout.getVisibility() == View.VISIBLE) {
                        int diavalor = number_picker_days_month.getValue();
                        if (diavalor < calendar.get(Calendar.DAY_OF_MONTH) && calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
                            calendar.add(Calendar.MONTH, 1);
                        }
                        for (int i = 0; i < number_spinner.getSelectedItemPosition() + 1; i++) {
                            diavalor = number_picker_days_month.getValue();
                            while (diavalor > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                                diavalor--;
                            }
                            calendar.set(Calendar.DAY_OF_MONTH, diavalor);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                            calendar.add(Calendar.MONTH, 1);
                        }
                    }
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
        SaveEvent(eventName.getText() + "", calendar, daysweek, String.valueOf(Requestcode), numero_repeat_noification, local_Evento.getText().toString().trim());
    }

    private void SaveEvent(String event, Calendar calendario, String frequecy, String code, String duracao, String local) {
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH) + 1;
        Events evento = new Events(event, code, String.format("%02d", hora) + ":" + String.format("%02d", minuto), String.format("%02d", dia), String.format("%02d", mes), String.valueOf(calendario.get(Calendar.YEAR)),
                String.valueOf(calendario.get(Calendar.WEEK_OF_YEAR)), frequecy, descricao_Evento_ed.getText().toString(), duracao, "0", local);
        bancolocal.getINSTANCE(getBaseContext()).addLembrete(evento);
        TaskManager.events_today.add(evento);
        TaskManager.RV_todayAdapter.notifyDataSetChanged();


    }


//    private void atunextalrm() {
//        Calendar secundario = Calendar.getInstance();
//        if (ifminute.getText().toString().contains("minuto")) {
//            secundario.add(Calendar.HOUR_OF_DAY, frequencia);
//        } else {
//            secundario.add(Calendar.MINUTE, frequencia);
//        }
//        String mes = "";
//        if ((secundario.get(Calendar.MONTH) + 1) < 9) {
//            mes += "0" + (secundario.get(Calendar.MONTH) + 1);
//        } else {
//            mes += (secundario.get(Calendar.MONTH) + 1);
//        }
//        String dia = "";
//        if (secundario.get(Calendar.DAY_OF_MONTH) < 10) {
//            dia += "0" + secundario.get(Calendar.DAY_OF_MONTH);
//        } else {
//            dia += secundario.get(Calendar.DAY_OF_MONTH);
//        }
//        String hora = "";
//        if (secundario.get(Calendar.HOUR_OF_DAY) < 10) {
//            hora += "0" + secundario.get(Calendar.HOUR_OF_DAY);
//        } else {
//            hora += secundario.get(Calendar.HOUR_OF_DAY);
//        }
//        String minuto = "";
//        if (secundario.get(Calendar.MINUTE) < 10) {
//            minuto += "0" + secundario.get(Calendar.MINUTE);
//        } else {
//            minuto += secundario.get(Calendar.MINUTE);
//        }

//        String datacerta = "Proximo lembrete ";
//        if (Integer.parseInt(dia) > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
//            datacerta += "\nAmanhã as: " + hora + ":" + minuto;
//
//        } else {
//            datacerta += "as: " + hora + ":" + minuto;
//        }
//    }


//    private boolean testeData(int frequencia, int tipotempo) {
//        boolean antes;
//        try {
//            Calendar cal2 = Calendar.getInstance();
//            cal2.setTimeInMillis(c.getTimeInMillis());
//            cal2.add(tipotempo, frequencia);
//            Date datateste = cal2.getTime();
//            Calendar now = Calendar.getInstance();
//            cal2.set(Calendar.SECOND, 0);
//            cal2.set(Calendar.MILLISECOND, 0);
//            now.set(Calendar.SECOND, 0);
//            now.set(Calendar.MILLISECOND, 0);
//            Date now2 = now.getTime();
//            if (datateste.compareTo(now2) < 0) {// passado
//                throw new IOException();
//            } else {
//                antes = false;
//            }
//
//        } catch (IOException e) {
//            antes = true;
//        }
//        return antes;
//    }

}