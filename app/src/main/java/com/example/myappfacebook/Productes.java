package com.example.myappfacebook;

public class Productes {
    private String nomProducte;
    private String preuProducte;
    private String tempsEmpleat;

    public Productes(String nomProducte,String preuProducte,String tempsEmpleat) {
        this.nomProducte = nomProducte;
        this.preuProducte = preuProducte;
        this.tempsEmpleat = tempsEmpleat;
    }

    public String getNomProducte() {
        return nomProducte;
    }

    public void setNomProducte(String nomProducte) {
        this.nomProducte = nomProducte;
    }

    public String getPreuProducte() {
        return preuProducte;
    }

    public void setPreuProducte(String preuProducte) {
        this.preuProducte = preuProducte;
    }

    public String getTempsEmpleat() {
        return tempsEmpleat;
    }

    public void setTempsEmpleat(String tempsEmpleat) {
        this.tempsEmpleat = tempsEmpleat;
    }
}
