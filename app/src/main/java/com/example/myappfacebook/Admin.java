package com.example.myappfacebook;

public class Admin {

    private String data;
    private String horari;

    public Admin(String data,String horari) {
        this.data = data;
        this.horari = horari;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }
}
