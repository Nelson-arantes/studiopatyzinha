package com.project.studiopatyzinha.pattern;

public class categoriasLC {
    String nome;
    String legenda;
    String valor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    int color;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public categoriasLC(String nome, String legenda, String valor, int color) {
        this.nome = nome;
        this.legenda = legenda;
        this.valor = valor;
        this.color = color;
    }
    public categoriasLC(String id, String nome, String legenda, String valor, int color) {
        this.id = id;
        this.nome = nome;
        this.legenda = legenda;
        this.valor = valor;
        this.color = color;
    }
}
