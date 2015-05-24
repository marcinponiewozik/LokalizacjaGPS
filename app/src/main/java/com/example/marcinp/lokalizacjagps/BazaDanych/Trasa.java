package com.example.marcinp.lokalizacjagps.BazaDanych;

/**
 * Created by MarcinP on 2015-03-03.
 */
public class Trasa {
    int id;
    String nazwa;
    double dystansWyznaczony;
    double dystansPrzebyty;

    long czasPrzebyty;
    long czasWyznaczony;
    public Trasa(int id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
    }

    public Trasa() {
    }

    public long getCzasPrzebyty() {
        return czasPrzebyty;
    }

    public void setCzasPrzebyty(long czasPrzebyty) {
        this.czasPrzebyty = czasPrzebyty;
    }

    public long getCzasWyznaczony() {
        return czasWyznaczony;
    }

    public void setCzasWyznaczony(long czasWyznaczony) {
        this.czasWyznaczony = czasWyznaczony;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public double getDystansWyznaczony() {
        return dystansWyznaczony;
    }

    public void setDystansWyznaczony(double dystansWyznaczony) {
        this.dystansWyznaczony = dystansWyznaczony;
    }

    public double getDystansPrzebyty() {
        return dystansPrzebyty;
    }

    public void setDystansPrzebyty(double dystansPrzebyty) {
        this.dystansPrzebyty = dystansPrzebyty;
    }
}
