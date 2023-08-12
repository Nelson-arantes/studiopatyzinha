package com.project.studiopatyzinha.pattern;

public class Addrespattern {
    String endereco_apelido;
    String endereco;
    String cep;
    String numero;

    public String getIdinterno() {
        return idinterno;
    }

    public void setIdinterno(String idinterno) {
        this.idinterno = idinterno;
    }

    String ifseleceted;
    String idinterno;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    public String getEndereco_apelido() {
        return endereco_apelido;
    }

    public void setEndereco_apelido(String endereco_apelido) {
        this.endereco_apelido = endereco_apelido;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getIfseleceted() {
        return ifseleceted;
    }

    public void setIfseleceted(String ifseleceted) {
        this.ifseleceted = ifseleceted;
    }

    public Addrespattern() {
    }

    public Addrespattern(String idinterno, String endereco, String endereco_apelido, String cep, String ifseleceted,String numero) {
        this.endereco = endereco;
        this.endereco_apelido = endereco_apelido;
        this.cep = cep;
        this.ifseleceted = ifseleceted;
        this.numero = numero;
        this.idinterno = idinterno;
    }
}