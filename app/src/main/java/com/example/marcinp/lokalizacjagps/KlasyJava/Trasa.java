package com.example.marcinp.lokalizacjagps.KlasyJava;

/**
 * Created by MarcinP on 2015-03-03.
 */
public class Trasa {
    int id;
    String nazwa;
    double dystans;

    public Trasa(int id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
    }

    public Trasa() {
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

    public double getDystans() {
        return dystans;
    }

    public void setDystans(double dystans) {
        this.dystans = dystans;
    }
}
