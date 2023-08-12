package com.project.studiopatyzinha.pattern;

import android.content.Context;

import java.util.Locale;

public class Price {
    double semdisconto;
    int parcelas;
    public boolean setemdesconto;
    double comDisconto;

    public Price(double semdisconto, int parcelas, boolean setemdesconto, double comDisconto) {
        this.semdisconto = semdisconto;
        this.parcelas = parcelas;
        this.setemdesconto = setemdesconto;
        this.comDisconto = comDisconto;
    }

    public String getDesconto(Context context){
        return String.format(Locale.GERMAN,"%s %.2f","R$ ", semdisconto);
    }
    public String getDiscount(Context context){
        return String.format(Locale.GERMAN,"%s %.2f","R$ ", comDisconto);
    }
    public String getPercentNumber(){
        float percent = (float)((semdisconto-comDisconto)/ semdisconto)*100;
        return String.format("%s%%", Integer.valueOf(String.valueOf((int) percent)));    }

}



