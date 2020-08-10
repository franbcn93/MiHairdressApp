package com.example.myappfacebook;

public class Client {
    private String email;
    private String sexe;
    private String telefon;

    public Client(String email,String telefon,String sexe) {
        this.email = email;
        this.sexe = sexe;
        this.telefon = telefon;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
