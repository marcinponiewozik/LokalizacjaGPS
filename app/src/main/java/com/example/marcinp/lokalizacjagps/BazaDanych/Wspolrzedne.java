package com.example.marcinp.lokalizacjagps.BazaDanych;

/**
 * Created by MarcinP on 2015-03-03.
 */
public class Wspolrzedne {
    int id;
    int id_Trasa;
    double dlugosc;
    double szerokosc;

    public Wspolrzedne() {
    }

    public Wspolrzedne(double dlugosc, double szerokosc, int id_Trasa) {
        this.dlugosc = dlugosc;
        this.szerokosc = szerokosc;
        this.id_Trasa = id_Trasa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_Trasa() {
        return id_Trasa;
    }

    public void setId_Trasa(int id_Trasa) {
        this.id_Trasa = id_Trasa;
    }

    public double getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(double dlugosc) {
        this.dlugosc = dlugosc;
    }

    public double getSzerokosc() {
        return szerokosc;
    }

    public void setSzerokosc(double szerokosc) {
        this.szerokosc = szerokosc;
    }
}
