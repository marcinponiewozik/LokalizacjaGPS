package com.example.marcinp.lokalizacjagps;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;

import com.example.marcinp.lokalizacjagps.KlasyJava.Trasa;
import com.example.marcinp.lokalizacjagps.KlasyJava.Wspolrzedne;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarcinP on 2015-03-04.
 */

public class NowyBieg {
    Trasa trasa;
    List<Wspolrzedne> wspolrzednesList;
    boolean porazPierwszy;
    double czas;

    public NowyBieg() {
        wspolrzednesList = new ArrayList<Wspolrzedne>();
    }

    public NowyBieg(Trasa trasa, List<Wspolrzedne> wspolrzednesList, boolean porazPierwszy) {
        this.trasa = trasa;
        this.wspolrzednesList = wspolrzednesList;
        this.porazPierwszy = porazPierwszy;
    }

    public void dodajWspolrzedne(Location loc){
        Wspolrzedne temp = new Wspolrzedne(loc.getLongitude(),loc.getLatitude(),trasa.getId());

        wspolrzednesList.add(temp);
    }

    public void zakonczBieg(double czas,Context context){
        this.czas = czas;
//        sprawdzTrase(trasa.getId(),context);
//        if (porazPierwszy){
            DBAdapter sql = new DBAdapter(context);
            for (int i = 0; i <wspolrzednesList.size() ; i++) {
                sql.dodajWspolrzedne(wspolrzednesList.get(i));

            }
        //}

    }

    public void sprawdzTrase(int id,Context context){
        DBAdapter sql = new DBAdapter(context);
        Cursor c =sql.getAllTrasy();
        while (c.moveToNext())
        {
            if(id == c.getInt(0))
            {
                porazPierwszy = false;
                break;
            }
        }
    }

    public double getCzas() {
        return czas;
    }

    public void setCzas(double czas) {
        this.czas = czas;
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
