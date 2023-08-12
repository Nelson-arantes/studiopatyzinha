package com.project.studiopatyzinha.pattern;

public class MessageModel {


    String uId;
    String message;
    String messageId;
    String status;
    String ft;

    public String getFt() {
        return ft;
    }

    public void setFt(String ft) {
        this.ft = ft;
    }

    public MessageModel() {
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    String chatId;
    long timesStamp;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public MessageModel(String uId, String message, String messageId, String status, String chatId, long timesStamp, String ft) {
        this.uId = uId;//idpessoa -> quem enviou a mesagem
        this.message = message;//o texto da mesagem
        this.chatId = chatId;//sender+receiver
        this.status = status;//visualizada | recebida | enviada | não enviada
        this.messageId = messageId;// id da mensagem
        this.timesStamp = timesStamp;// hora da mesagem
        this.ft = ft;// foto
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getTimesStamp() {
        return timesStamp;
    }

    public void setTimesStamp(long timesStamp) {
        this.timesStamp = timesStamp;
    }
}

