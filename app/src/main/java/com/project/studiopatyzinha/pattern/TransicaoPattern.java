package com.project.studiopatyzinha.pattern;

public class TransicaoPattern {
    String id,valor,data,operacao;

    public TransicaoPattern(String id, String valor, String data, String operacao) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.operacao = operacao;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
}
