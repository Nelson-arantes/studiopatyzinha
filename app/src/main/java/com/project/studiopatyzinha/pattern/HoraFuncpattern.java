package com.project.studiopatyzinha.pattern;

public class HoraFuncpattern {
    private String almococomeco,almocofim,diasemana,entranotrabalhoas,iffolga,saidotrabalhoas;

    public String getAlmococomeco() {
        return almococomeco;
    }

    public void setAlmococomeco(String almococomeco) {
        this.almococomeco = almococomeco;
    }

    public String getAlmocofim() {
        return almocofim;
    }

    public void setAlmocofim(String almocofim) {
        this.almocofim = almocofim;
    }

    public String getDiasemana() {
        return diasemana;
    }

    public void setDiasemana(String diasemana) {
        this.diasemana = diasemana;
    }

    public String getEntranotrabalhoas() {
        return entranotrabalhoas;
    }

    public void setEntranotrabalhoas(String entranotrabalhoas) {
        this.entranotrabalhoas = entranotrabalhoas;
    }

    public String getIffolga() {
        return iffolga;
    }

    public void setIffolga(String iffolga) {
        this.iffolga = iffolga;
    }

    public String getSaidotrabalhoas() {
        return saidotrabalhoas;
    }

    public void setSaidotrabalhoas(String saidotrabalhoas) {
        this.saidotrabalhoas = saidotrabalhoas;
    }


    public HoraFuncpattern(String almococomeco, String almocofim, String diasemana, String entranotrabalhoas, String iffolga, String saidotrabalhoas) {
        this.almococomeco = almococomeco;
        this.almocofim = almocofim;
        this.diasemana = diasemana;
        this.entranotrabalhoas = entranotrabalhoas;
        this.iffolga = iffolga;
        this.saidotrabalhoas = saidotrabalhoas;
    }
}
