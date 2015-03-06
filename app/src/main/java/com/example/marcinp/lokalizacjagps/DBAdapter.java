package com.example.marcinp.lokalizacjagps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.marcinp.lokalizacjagps.KlasyJava.Trasa;
import com.example.marcinp.lokalizacjagps.KlasyJava.Wspolrzedne;
import com.example.marcinp.lokalizacjagps.KlasyJava.Wynik;

import java.util.List;

/**
 * Created by MarcinP on 2015-03-03.
 */
public class DBAdapter extends SQLiteOpenHelper {
    public DBAdapter(Context context) {
        super(context, "geolokalizacja.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE Table TRASA(" +
                "id INTEGER PRIMARY KEY Autoincrement," +
                "nazwa TEXT," +
                "dystans DOUBLE);" +
                "");
        db.execSQL(
                "CREATE Table WYNIK(" +
                        "id INTEGER PRIMARY KEY Autoincrement," +
                        "id_TRASA INTEGER," +
                        "czas DOUBLE," +
                        "FOREIGN KEY(id_TRASA) REFERENCES TRASA(id)" +
                        ");" +
                        "");
        db.execSQL(
                "CREATE Table WSPOLRZEDNE(" +
                        "id INTEGER PRIMARY KEY Autoincrement," +
                        "id_TRASA INTEGER," +
                        "dlugosc DOUBLE," +
                        "szerokosc DOUBLE," +
                        "FOREIGN KEY(id_TRASA) REFERENCES TRASA (id)" +
                        ");" +
                        "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void dodajTrase(Trasa trasa){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("nazwa",trasa.getNazwa());
        cv.put("dystans",trasa.getDystans());
        sql.insert("TRASA",null,cv);
        sql.close();
    }
    public void usunTrase(int id){
        SQLiteDatabase sql = getWritableDatabase();
        String[] argumenty = {""+id};
        sql.delete("TRASA","id=?",argumenty);
        sql.close();
    }

    public Trasa wezTrase(String nazwa){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","nazwa","dystans"};
        String[] argumenty = {""+nazwa};
        Cursor c = sql.query("TRASA",kolumny,"nazwa=?",argumenty,null,null,null);
        Trasa trasa = new Trasa();
        while (c.moveToNext()){
            trasa.setId(c.getInt(0));
            trasa.setNazwa(c.getString(1));
            trasa.setDystans(c.getDouble(2));
        }
        return trasa;
    }

    public Cursor getAllTrasy(){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","nazwa","dystans"};
        Cursor c = sql.query("TRASA",kolumny,null,null,null,null,null);
        return  c;
    }
    public void dodajWynik(Wynik wynik){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("id_Trasa",wynik.getId_Trasa());
        cv.put("czas",wynik.getCzas());
        sql.insert("WYNIK", null, cv);
        sql.close();
    }
    public void usunWynik(int id){
        SQLiteDatabase sql = getWritableDatabase();
        String[] argumenty = {""+id};
        sql.delete("WYNIK","id=?",argumenty);
        sql.close();
    }

    public void dodajWspolrzedne(Wspolrzedne wsp){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("id_Trasa",wsp.getId_Trasa());
        cv.put("dlugosc",wsp.getDlugosc());
        cv.put("szerokosc",wsp.getSzerokosc());
        sql.insert("WSPOLRZEDNE",null,cv);
        sql.close();
    }
    public void usunWspolrzedne(int id){
        SQLiteDatabase sql = getWritableDatabase();
        String[] argumenty = {""+id};
        sql.delete("WSPOLRZEDNE","id=?",argumenty);
        sql.close();
    }

    public Cursor getAllWspolrzedne(){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","id_TRASA","dlugosc","szerokosc"};
        Cursor c = sql.query("WSPOLRZEDNE",kolumny,null,null,null,null,null);
        return  c;
    }

}
