package com.example.myappfacebook;

public class ProdSeleccionats {
    private String NameProd;
    private String TimeProd;
    private String PriceProd;

    public ProdSeleccionats(String nameProd) {
        NameProd = nameProd;
    }

    public String getNameProd() {
        return NameProd;
    }

    public void setNameProd(String nameProd) {
        NameProd = nameProd;
    }

    public String getTimeProd() {
        return TimeProd;
    }

    public void setTimeProd(String timeProd) {
        TimeProd = timeProd;
    }

    public String getPriceProd() {
        return PriceProd;
    }

    public void setPriceProd(String priceProd) {
        PriceProd = priceProd;
    }
}
