package com.example.marcinp.lokalizacjagps.BazaDanych;

/**
 * Created by MarcinP on 2015-05-19.
 */
public class  Kategoria {

    String nazwa;
    int id;
    String zdjecie;

    public Kategoria() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZdjecie() {
        return zdjecie;
    }

    public void setZdjecie(String zdjecie) {
        this.zdjecie = zdjecie;
    }
}
