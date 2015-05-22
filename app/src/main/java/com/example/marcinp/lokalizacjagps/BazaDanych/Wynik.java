package com.example.marcinp.lokalizacjagps.BazaDanych;

/**
 * Created by MarcinP on 2015-03-03.
 */
public class Wynik {
    int id;
    int id_Trasa;
    int godziny;
    int minuty;
    int sekundy;


    public Wynik(int id_Trasa, int godziny, int minuty, int sekundy) {
        this.id_Trasa = id_Trasa;
        this.godziny = godziny;
        this.minuty = minuty;
        this.sekundy = sekundy;
    }

    public Wynik() {
    }

    public int getGodziny() {
        return godziny;
    }

    public void setGodziny(int godziny) {
        this.godziny = godziny;
    }

    public int getMinuty() {
        return minuty;
    }

    public void setMinuty(int minuty) {
        this.minuty = minuty;
    }

    public int getSekundy() {
        return sekundy;
    }

    public void setSekundy(int sekundy) {
        this.sekundy = sekundy;
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
}
