package com.project.studiopatyzinha.pattern;

public class Horapattern {
    String hora;
    boolean ocupado;
String selected;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean getOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public Horapattern(String hora, boolean ocupado,String selected) {
        this.selected = selected;
        this.hora = hora;
        this.ocupado = ocupado;
    }
}
