package com.project.studiopatyzinha.pattern;

public class Servicepattern {
    String descricao;
    String diaspromocao;
    String ft;
    String funcs;
    String id;
    String ifpromocao;
    String nome;
    String selected;
    String tempo;
    String valor;
    String valorp;

    public Servicepattern() {}
    public Servicepattern(String nome, String tempo, String valor, String ifpromocao, String valorp, String diaspromocao, String id, String descricao, String ft, String funcs, String selected) {
        this.nome = nome;
        this.tempo = tempo;
        this.valor = valor;
        this.ifpromocao = ifpromocao;
        this.valorp = valorp;
        this.diaspromocao = diaspromocao;
        this.id = id;
        this.descricao = descricao;
        this.ft = ft;
        this.funcs = funcs;
        this.selected = selected;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFuncs() {
        return funcs;
    }

    public void setFuncs(String funcs) {
        this.funcs = funcs;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFt() {
        return ft;
    }

    public void setFt(String ft) {
        this.ft = ft;
    }

    public String getDesccricao() {
        return descricao;
    }




    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getIfpromocao() {
        return ifpromocao;
    }

    public void setIfpromocao(String ifpromocao) {
        this.ifpromocao = ifpromocao;
    }

    public String getValorp() {
        return valorp;
    }

    public void setValorp(String valorp) {
        this.valorp = valorp;
    }

    public String getDiaspromocao() {
        return diaspromocao;
    }

    public void setDiaspromocao(String diaspromocao) {
        this.diaspromocao = diaspromocao;
    }


}
