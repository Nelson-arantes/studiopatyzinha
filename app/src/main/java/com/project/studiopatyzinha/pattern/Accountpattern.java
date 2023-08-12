package com.project.studiopatyzinha.pattern;

import java.util.Map;

public class Accountpattern {

    Map<String, Object> barbersposts;
    String cargo;

    public String getUltimopagamento() {
        return ultimopagamento;
    }

    public void setUltimopagamento(String ultimopagamento) {
        this.ultimopagamento = ultimopagamento;
    }

    String ultimopagamento;
    String dia_salario;
    String email;
    Map<String, Object> followersid;
    String followersquant;
    String frasefilosofica;
    String grupoAbaixo;

    public String getIfgerente() {
        return ifgerente;
    }

    public void setIfgerente(String ifgerente) {
        this.ifgerente = ifgerente;
    }

    String ifgerente;
    Map<String, Object> horario;
    String id;
    String imgUri;
    String nome;
    Map<String, Object> opinioes;
    Map<String, Object> horasdiaspagamentos;//map para ver o horario para disparar o pagamento do funcionário
    String percentual;
    String quantpost;
    String selected;
    String formapagamento;
    String senha;

    public Map<String, Object> getHorasdiaspagamentos() {
        return horasdiaspagamentos;
    }

    public void setHorasdiaspagamentos(Map<String, Object> horasdiaspagamentos) {
        this.horasdiaspagamentos = horasdiaspagamentos;
    }


    public String getFormapagamento() {
        return formapagamento;
    }

    public void setFormapagamento(String formapagamento) {
        this.formapagamento = formapagamento;
    }


    public Map<String, Object> getBarbersposts() {
        return barbersposts;
    }

    public void setBarbersposts(Map<String, Object> barbersposts) {
        this.barbersposts = barbersposts;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDia_salario() {
        return dia_salario;
    }

    public void setDia_salario(String dia_salario) {
        this.dia_salario = dia_salario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getFollowersid() {
        return followersid;
    }

    public void setFollowersid(Map<String, Object> followersid) {
        this.followersid = followersid;
    }

    public String getFollowersquant() {
        return followersquant;
    }

    public void setFollowersquant(String followersquant) {
        this.followersquant = followersquant;
    }

    public String getFrasefilosofica() {
        return frasefilosofica;
    }

    public void setFrasefilosofica(String frasefilosofica) {
        this.frasefilosofica = frasefilosofica;
    }

    public String getGrupoAbaixo() {
        return grupoAbaixo;
    }

    public void setGrupoAbaixo(String grupoAbaixo) {
        this.grupoAbaixo = grupoAbaixo;
    }

    public Map<String, Object> getHorario() {
        return horario;
    }

    public void setHorario(Map<String, Object> horario) {
        this.horario = horario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public Map<String, Object> getOpinioes() {
        return opinioes;
    }

    public void setOpinioes(Map<String, Object> opinioes) {
        this.opinioes = opinioes;
    }

    public String getPercentual() {
        return percentual;
    }

    public void setPercentual(String percentual) {
        this.percentual = percentual;
    }

    public String getQuantpost() {
        return quantpost;
    }

    public void setQuantpost(String quantpost) {
        this.quantpost = quantpost;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Accountpattern() {
    }

    public Accountpattern(Map<String, Object> barbersposts, String cargo, String dia_salario, String email, Map<String, Object> followersid, String followersquant, String frasefilosofica, String grupoAbaixo, Map<String, Object> horario, String id, String imgUri, String nome, Map<String, Object> opinioes, String percentual, String quantpost, String selected, String senha, Map<String, Object> horasdiaspagamentos, String ultimopagamento,String ifgerente) {
        this.barbersposts = barbersposts;
        this.ifgerente = ifgerente;
        this.cargo = cargo;
        this.dia_salario = dia_salario;
        this.email = email;
        this.followersid = followersid;
        this.followersquant = followersquant;
        this.frasefilosofica = frasefilosofica;
        this.grupoAbaixo = grupoAbaixo;
        this.horario = horario;
        this.id = id;
        this.imgUri = imgUri;
        this.nome = nome;
        this.opinioes = opinioes;
        this.percentual = percentual;
        this.quantpost = quantpost;
        this.selected = selected;
        this.senha = senha;
        this.horasdiaspagamentos = horasdiaspagamentos;
        this.ultimopagamento = ultimopagamento;
    }

}
