package com.example.marcinp.lokalizacjagps.BazaDanych;

/**
 * Created by MarcinP on 2015-05-22.
 */
public class TrasaWyznaczona {
    private int id;
    private int idTrasa;

    private double szerokosc;
    private double dlugosc;

    public TrasaWyznaczona() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTrasa() {
        return idTrasa;
    }

    public void setIdTrasa(int idTrasa) {
        this.idTrasa = idTrasa;
    }

    public double getSzerokosc() {
        return szerokosc;
    }

    public void setSzerokosc(double szerokosc) {
        this.szerokosc = szerokosc;
    }

    public double getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(double dlugosc) {
        this.dlugosc = dlugosc;
    }
}
