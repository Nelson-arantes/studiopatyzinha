package com.project.studiopatyzinha.pattern;

import java.util.ArrayList;
import java.util.Map;

public class PostPattern {
    String commentsQuant;
    String date;
    String desc;
    String id;
    Map<String, Object> likesids;
    String likesquants, nomeid;
    Accountpattern pessoa;
    String photo;
    boolean selflike;
    String tag;
    String views;
    Map<String,Object> notificados;

    public ArrayList<Comments_pattern> getComentarios() {
        return comentarios;
    }

    public void setComentarios(ArrayList<Comments_pattern> comentarios) {
        this.comentarios = comentarios;
    }

    ArrayList<Comments_pattern> comentarios;

    public PostPattern(String commentsQuant, String date, String desc, String id, Map<String, Object> likesids, String likesquants, String nomeid, Accountpattern pessoa, String photo, boolean selflike, String tag, String views, Map<String, Object> notificados,ArrayList<Comments_pattern> comentarios) {
        this.commentsQuant = commentsQuant;
        this.date = date;
        this.comentarios = comentarios;
        this.desc = desc;
        this.id = id;
        this.likesids = likesids;
        this.likesquants = likesquants;
        this.nomeid = nomeid;
        this.pessoa = pessoa;
        this.photo = photo;
        this.selflike = selflike;
        this.tag = tag;
        this.views = views;
        this.notificados = notificados;
    }

    public PostPattern() {
    }

    public void setCommentsQuant(String commentsQuant) {
        this.commentsQuant = commentsQuant;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setLikesids(Map<String, Object> likesids) {
        this.likesids = likesids;
    }

    public void setLikesquants(String likesquants) {
        this.likesquants = likesquants;
    }

    public void setNomeid(String nomeid) {
        this.nomeid = nomeid;
    }

    public void setPessoa(Accountpattern pessoa) {
        this.pessoa = pessoa;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public void setSelflike(boolean selflike) {
        this.selflike = selflike;
    }
    public void setNotificados(Map<String, Object> notificados) {
        this.notificados = notificados;
    }





    public boolean isSelflike() {
        return selflike;
    }

    public String getTag() {
        return tag;
    }

    public String getViews() {
        return views;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getLikesids() {
        return likesids;
    }

    public String getLikesquants() {
        return likesquants;
    }

    public String getNomeid() {
        return nomeid;
    }

    public Accountpattern getPessoa() {
        return pessoa;
    }
    public Map<String, Object> getNotificados() {        return notificados;
    }


    public String getPhoto() {
        return photo;
    }
    public String getDate() {
        return date;
    }

    public String getCommentsQuant() {
        return commentsQuant;
    }

    public String getDesc() {
        return desc;
    }

}
