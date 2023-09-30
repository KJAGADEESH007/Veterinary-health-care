package com.example.vetcare;

public class Notifications
{

    private String msg,title;

    public Notifications() { }

    public Notifications(String msg, String title) {
        this.msg = msg;
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

