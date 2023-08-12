package com.project.studiopatyzinha.pattern;

public class Descpattern {
    String id,nome,valorreal,valorpercent;

    public String getValorpercent() {
        return valorpercent;
    }

    public void setValorpercent(String valorpercent) {
        this.valorpercent = valorpercent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValorreal() {
        return valorreal;
    }

    public void setValorreal(String valorreal) {
        this.valorreal = valorreal;
    }

    public Descpattern(String id, String nome, String valorreal, String valorpercent) {
        this.id = id;
        this.nome = nome;
        this.valorreal = valorreal;
        this.valorpercent = valorpercent;
    }
}
