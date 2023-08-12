package com.project.studiopatyzinha.pattern;

public class Opinionpattern {
    String date;
    String opinion;
    String iduser;//pego o nome e a foto
    String id;
    String nomeuser;

    public Opinionpattern(String date, String opinion, String iduser, String id, String nomeuser, String grade, String ftuser) {
        this.date = date;
        this.opinion = opinion;
        this.iduser = iduser;
        this.id = id;
        this.nomeuser = nomeuser;
        this.grade = grade;
        this.ftuser = ftuser;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    String grade;
    String ftuser;


    public String getNomeuser() {
        return nomeuser;
    }

    public void setNomeuser(String nomeuser) {
        this.nomeuser = nomeuser;
    }

    public String getFtuser() {
        return ftuser;
    }

    public void setFtuser(String ftuser) {
        this.ftuser = ftuser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Opinionpattern() {
    }


}
