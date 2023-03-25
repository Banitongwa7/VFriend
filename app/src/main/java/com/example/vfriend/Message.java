package com.example.vfriend;

public class Message {
    String message;
    String sendBy;
    public static String SENT_BY_ME = "moi";
    public static String SENT_BY_BOT = "bot";

    public Message(){

    }

    public Message(String message, String sendBy) {
        this.message = message;
        this.sendBy = sendBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }
}
