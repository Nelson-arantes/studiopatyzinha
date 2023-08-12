package com.project.studiopatyzinha.pattern;

public class Events {
    String EVENT;
    String TIME;
    String DAY;
    String MONTH;
    String YEAR;
    String RequestCode;
    String WeekOfYear;
    String frequencia;
    String descricao;
    String duracao;
    String atualProgresso;
    String local;
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }



    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getAtualProgresso() {
        return atualProgresso;
    }

    public void setAtualProgresso(String atualProgresso) {
        this.atualProgresso = atualProgresso;
    }

    public Events(String EVENT, String RequestCode, String TIME, String DAY, String MONTH, String YEAR,
                  String WeekOfYear, String frequencia, String descricao, String duracao, String atualProgresso,String local) {
        this.EVENT = EVENT;
        this.TIME = TIME;
        this.atualProgresso = atualProgresso;
        this.duracao = duracao;
        this.local = local;
        this.descricao = descricao;
        this.DAY = DAY;
        this.MONTH = MONTH;
        this.YEAR = YEAR;
        this.WeekOfYear = WeekOfYear;
        this.frequencia = frequencia;
        this.RequestCode = RequestCode;

    }


    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getEVENT() {
        return EVENT;
    }

    public String getWeekOfYear() {
        return WeekOfYear;
    }

    public String getRequestCode() {
        return RequestCode;
    }

    public void setEVENT(String EVENT) {
        this.EVENT = EVENT;
    }

    public void setWeekOfYear(String WeekOfYear) {
        this.WeekOfYear = WeekOfYear;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public void setRequestCode(String requestCode) {
        this.RequestCode = requestCode;
    }

    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }
}