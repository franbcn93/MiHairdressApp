package com.example.myappfacebook;

public class Hora {

    private String nombre;
    private String clicked;
    //private String artista;
    //private int imagen;

    public Hora(){

    }

    public Hora(String nombre,String click) {
        this.nombre = nombre;
        this.clicked = click;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClicked() {
        return clicked;
    }

    public void setClicked(String clicked) {
        this.clicked = clicked;
    }

}