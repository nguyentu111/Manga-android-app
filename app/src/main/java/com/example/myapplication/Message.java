package com.example.myapplication;

import java.util.Date;
import java.text.*;
public class Message {
    private String messageText;
    private String messageUser;
    private long messageTime;
    public Message(String messageUser, String messageText) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        // Initialize to current time
        messageTime = new Date().getTime();
    }
    public Message(String messageUser, String messageText, long time) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        // Initialize to current time
        this.messageTime = time;
    }
    public Message(){
    }
    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    public String getMessageUser() {
        return messageUser;
    }
    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }
    public long getMessageTime() {
        return messageTime;
    }
    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
