package com.project.studiopatyzinha.pattern;

public class UserPattern {
    String img_user;
    String name_user;
    String last_message_user;
    String time_last_message_user;
    String quant_new_message;
    String statuaLasMessage;

    public String getStatuaLasMessage() {
        return statuaLasMessage;
    }

    public void setStatuaLasMessage(String statuaLasMessage) {
        this.statuaLasMessage = statuaLasMessage;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    String idUser;

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getLast_message_user() {
        return last_message_user;
    }

    public String getTime_last_message_user() {
        return time_last_message_user;
    }

    public void setTime_last_message_user(String time_last_message_user) {
        this.time_last_message_user = time_last_message_user;
    }

    public void setLast_message_user(String last_message_user) {
        this.last_message_user = last_message_user;
    }
public UserPattern(){

}

    public String getQuant_new_message() {
        return quant_new_message;
    }

    public void setQuant_new_message(String quant_new_message) {
        this.quant_new_message = quant_new_message;
    }

    public UserPattern(String img_user, String name_user, String last_message_user, String idUser, String time_last_message_user, String quant_new_message) {
        this.img_user = img_user;
        this.quant_new_message = quant_new_message;
        this.name_user = name_user;
        this.idUser = idUser;
        this.time_last_message_user = time_last_message_user;
        this.last_message_user = last_message_user;
    }
}
