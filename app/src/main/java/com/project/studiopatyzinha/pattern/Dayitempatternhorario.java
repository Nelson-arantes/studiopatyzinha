package com.project.studiopatyzinha.pattern;

public class Dayitempatternhorario {
    String dayweek;
    String ifselected;
    String abreas;
    String fechas;
    String iffechado;
    String almocoinicio;
    String almocofim;

    public Dayitempatternhorario() {
    }

    public String getIfselected() {
        return ifselected;
    }

    public void setIfselected(String ifselected) {
        this.ifselected = ifselected;
    }

    public Dayitempatternhorario(String dayweek, String ifselected, String abreas, String fechas, String iffechado, String almocoinicio, String almocofim) {
        this.dayweek = dayweek;
        this.ifselected = ifselected;
        this.abreas = abreas;
        this.fechas = fechas;
        this.iffechado = iffechado;
        this.almocoinicio = almocoinicio;
        this.almocofim = almocofim;
    }

    public String getAbreas() {
        return abreas;
    }

    public void setAbreas(String abreas) {
        this.abreas = abreas;
    }

    public String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }

    public String getIffechado() {
        return iffechado;
    }

    public void setIffechado(String iffechado) {
        this.iffechado = iffechado;
    }

    public String getAlmocoinicio() {
        return almocoinicio;
    }

    public void setAlmocoinicio(String almocoinicio) {
        this.almocoinicio = almocoinicio;
    }

    public String getAlmocofim() {
        return almocofim;
    }

    public void setAlmocofim(String almocofim) {
        this.almocofim = almocofim;
    }

    public String getBackcolor() {
        return ifselected;
    }

    public void setBackcolor(String backcolor) {
        this.ifselected = backcolor;
    }

    public String getDayweek() {
        return dayweek;
    }

    public void setDayweek(String dayweek) {
        this.dayweek = dayweek;
    }

}
