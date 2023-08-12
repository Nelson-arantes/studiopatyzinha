package com.project.studiopatyzinha.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.project.studiopatyzinha.TaskManager;
import com.project.studiopatyzinha.banco.bancolocal;
import com.project.studiopatyzinha.services.taskservice;

public class alarmreceiverbroadcast extends BroadcastReceiver {
    public alarmreceiverbroadcast() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent i) {
        String atu = i.getStringExtra("atu");
        String notid = i.getStringExtra("id");
        if(atu!=null){
            TaskManager.events_today.clear();
            TaskManager.events_today.addAll(bancolocal.getINSTANCE(context).get_Lembrete());
            TaskManager.RV_todayAdapter.notifyDataSetChanged();
        }else{
            Intent myIntent = new Intent(context, taskservice.class);
            myIntent.putExtra("notid", notid);
            context.startService(myIntent);
        }
    }


}
