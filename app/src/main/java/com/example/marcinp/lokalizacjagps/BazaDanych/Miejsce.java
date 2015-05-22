package com.example.marcinp.lokalizacjagps.BazaDanych;

/**
 * Created by MarcinP on 2015-05-19.
 */
public class Miejsce {
    private int id;
    private String nazwa;
    private String informacjeDodatkowe;
    private double dlugosc;
    private double szerokosc;
    private int idKategoria;

    public Miejsce() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getInformacjeDodatkowe() {
        return informacjeDodatkowe;
    }

    public void setInformacjeDodatkowe(String informacjeDodatkowe) {
        this.informacjeDodatkowe = informacjeDodatkowe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIdKategoria() {
        return idKategoria;
    }

    public void setIdKategoria(int idKategoria) {
        this.idKategoria = idKategoria;
    }
}
