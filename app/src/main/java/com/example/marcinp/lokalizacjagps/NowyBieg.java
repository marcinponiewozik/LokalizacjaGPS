package com.example.marcinp.lokalizacjagps;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;

import com.example.marcinp.lokalizacjagps.KlasyJava.Czas;
import com.example.marcinp.lokalizacjagps.KlasyJava.Trasa;
import com.example.marcinp.lokalizacjagps.KlasyJava.Wspolrzedne;
import com.example.marcinp.lokalizacjagps.KlasyJava.Wynik;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarcinP on 2015-03-04.
 */

public class NowyBieg {
    Trasa trasa;
    List<Wspolrzedne> wspolrzednesList;
    boolean porazPierwszy;

    public NowyBieg() {
        wspolrzednesList = new ArrayList<Wspolrzedne>();
    }

    public NowyBieg(Trasa trasa, List<Wspolrzedne> wspolrzednesList, boolean porazPierwszy) {
        this.trasa = trasa;
        this.wspolrzednesList = wspolrzednesList;
        this.porazPierwszy = porazPierwszy;
    }

    public void dodajWspolrzedne(Location loc){
        Wspolrzedne temp = new Wspolrzedne();
        temp.setDlugosc(loc.getLongitude());
        temp.setSzerokosc(loc.getLatitude());
        temp.setId_Trasa(trasa.getId());
        wspolrzednesList.add(temp);
    }

    public void zakonczBieg(Long czas,Context context){

        int sekundy = (int) (czas/1000);
        int minuty = sekundy/60;
        int godziny = minuty/60;
        DBAdapter sql = new DBAdapter(context);
        sprawdzTrase(trasa.getId(),context);
        if(porazPierwszy){
            for (int i = 0; i < wspolrzednesList.size(); i++) {
                sql.dodajWspolrzedne(wspolrzednesList.get(i));
            }
        }

        Wynik wynik = new Wynik();
        wynik.setId_Trasa(trasa.getId());
        wynik.setGodziny(godziny);
        wynik.setMinuty(minuty);
        wynik.setSekundy(sekundy);
        sql.dodajWynik(wynik);

    }

    public void sprawdzTrase(int id,Context context){
        DBAdapter sql = new DBAdapter(context);
        Cursor c =sql.wezWyniki(id);
        if(c.getCount()!=0)
            porazPierwszy= false;
        else
            porazPierwszy = true;
    }



    public Trasa getTrasa() {
        return trasa;
    }

    public void setTrasa(Trasa trasa) {
        this.trasa = trasa;
    }

    public List<Wspolrzedne> getWspolrzednesList() {
        return wspolrzednesList;
    }

    public void setWspolrzednesList(List<Wspolrzedne> wspolrzednesList) {
        this.wspolrzednesList = wspolrzednesList;
    }

    public boolean isPorazPierwszy() {
        return porazPierwszy;
    }

    public void setPorazPierwszy(boolean porazPierwszy) {
        this.porazPierwszy = porazPierwszy;
    }
}
