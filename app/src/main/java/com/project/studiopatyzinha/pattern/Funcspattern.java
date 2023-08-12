package com.project.studiopatyzinha.pattern;

public class Funcspattern {
    String nome,percutal,dias_alario,id;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPercutal() {
        return percutal;
    }

    public void setPercutal(String percutal) {
        this.percutal = percutal;
    }

    public String getDias_alario() {
        return dias_alario;
    }

    public void setDias_alario(String dias_alario) {
        this.dias_alario = dias_alario;
    }

    public Funcspattern(String nome, String percutal, String dias_alario,String id) {
        this.nome = nome;
        this.percutal = percutal;
        this.dias_alario = dias_alario;
        this.id = id;
    }
}
