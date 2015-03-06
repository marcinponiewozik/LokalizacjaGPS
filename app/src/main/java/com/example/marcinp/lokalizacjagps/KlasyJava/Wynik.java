package com.example.marcinp.lokalizacjagps.KlasyJava;

/**
 * Created by MarcinP on 2015-03-03.
 */
public class Wynik {
    int id;
    int id_Trasa;
    double czas;

    public Wynik(int id_Trasa, double czas, int id) {
        this.id_Trasa = id_Trasa;
        this.czas = czas;
        this.id = id;
    }

    public Wynik() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCzas() {
        return czas;
    }

    public void setCzas(double czas) {
        this.czas = czas;
    }

    public int getId_Trasa() {
        return id_Trasa;
    }

    public void setId_Trasa(int id_Trasa) {
        this.id_Trasa = id_Trasa;
    }
}
