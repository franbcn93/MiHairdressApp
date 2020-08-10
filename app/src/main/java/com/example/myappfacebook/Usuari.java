package com.example.myappfacebook;

public class Usuari {

    private String data;
    private String email;
    private String horari;
    private String preu;
    private String temps;

    public Usuari(String data,String email,String horari,String preu,String temps) {
        this.data = data;
        this.email = email;
        this.horari = horari;
        this.preu = preu;
        this.temps = temps;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHorari() {
        return horari;
    }

    public void setHorari(String horari) {
        this.horari = horari;
    }

    public String getPreu() {
        return preu;
    }

    public void setPreu(String preu) {
        this.preu = preu;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }
}
