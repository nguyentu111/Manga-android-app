package com.example.myapplication;

import java.util.Date;
import java.text.*;
public class Message {
    private String message;
    private Date time;

    private int id;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSameTime(Date time) {
        if (time.getYear() == this.time.getYear()) {
            if (time.getMonth() == this.time.getMonth()) {
                if (time.getDate() == this.time.getDate()) {
                    if (time.getHours() == this.time.getHours()) {
                        if (time.getMinutes() == this.time.getMinutes()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String getTimeString() {
        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm");
        return ft.format(time);
    }

    public Date getTime() {
        return time;
    }

    public int getId() { return id; }

    public Message(String message, int id) {
        this.message = message;
        this.time = new Date();
        this.id = id;
    }
}
