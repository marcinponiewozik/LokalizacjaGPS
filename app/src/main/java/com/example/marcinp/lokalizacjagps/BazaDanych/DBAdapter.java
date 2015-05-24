package com.example.marcinp.lokalizacjagps.BazaDanych;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                "dystansPrzebyty DOUBLE," +
                "dystansWyznaczony DOUBLE," +
                "czasPrzebyty INTEGER,"+
                "czasWyznaczony INTEGER);" +
                "");

        db.execSQL(
                "CREATE Table TRASAPRZEBYTA(" +
                "id INTEGER PRIMARY KEY Autoincrement," +
                "id_TRASA INTEGER," +
                "szerokosc DOUBLE," +
                "dlugosc DOUBLE," +
                "FOREIGN KEY(id_TRASA) REFERENCES TRASA(id));" +
                "");

        db.execSQL(
                "CREATE Table TRASAWYZNACZONA(" +
                        "id INTEGER PRIMARY KEY Autoincrement," +
                        "id_TRASA INTEGER," +
                        "szerokosc DOUBLE," +
                        "dlugosc DOUBLE," +
                        "FOREIGN KEY(id_TRASA) REFERENCES TRASA(id));" +
                        "");

        db.execSQL(
                "CREATE Table WYNIK(" +
                        "id INTEGER PRIMARY KEY Autoincrement," +
                        "id_TRASA INTEGER," +
                        "godziny INTEGER," +
                        "minuty INTEGER," +
                        "sekundy INTEGER," +
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
        db.execSQL(
                "CREATE Table KATEGORIA(" +
                        "id INTEGER PRIMARY KEY Autoincrement," +
                        "nazwa TEXT," +
                        "zdjecie TEXT" +
                        ");" +
                        "");
        db.execSQL(
                "CREATE Table MIEJSCE(" +
                        "id INTEGER PRIMARY KEY Autoincrement," +
                        "nazwa TEXT," +
                        "info TEXT," +
                        "dlugosc DOUBLE," +
                        "szerokosc DOUBLE," +
                        "id_KATEGORIA INTEGER,"+
                        "FOREIGN KEY(id_KATEGORIA) REFERENCES KATEGORIA (id) ON DELETE CASCADE" +
                        ");" +
                        "");
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //Kategoria
    public void dodajKategoria(Kategoria kategoria){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nazwa", kategoria.getNazwa());
        cv.put("zdjecie",kategoria.getZdjecie());
        sql.insert("KATEGORIA",null,cv);
        sql.close();
    }
    public void usunKategoria(int id){
        SQLiteDatabase sql = getWritableDatabase();
        String[] argumenty = {""+id};
        sql.delete("Kategoria","id=?",argumenty);
        sql.close();
    }
    public Kategoria wezKategoria(int id){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","nazwa","zdjecie",};
        String[] argumenty = {""+id};
        Cursor c = sql.query("KATEGORIA",kolumny,"id=?",argumenty,null,null,null);
        Kategoria kategoria = new Kategoria();
        while (c.moveToNext()){
            kategoria.setId(c.getInt(0));
            kategoria.setNazwa(c.getString(1));
            kategoria.setZdjecie(c.getString(2));
        }
        return kategoria;
    }
    public Cursor getAllKategoria(){
        SQLiteDatabase sql = getReadableDatabase();

        String[] kolumny = {"id","nazwa","zdjecie"};
        Cursor c = sql.query("KATEGORIA",kolumny,null,null,null,null,null);
        return  c;
    }
    //Miejsca
    public void dodajMiejsce(Miejsce miejsce){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nazwa",miejsce.getNazwa());
        cv.put("info",miejsce.getInformacjeDodatkowe());
        cv.put("dlugosc",miejsce.getDlugosc());
        cv.put("szerokosc",miejsce.getSzerokosc());
        cv.put("id_KATEGORIA",miejsce.getIdKategoria());
        sql.insert("Miejsce",null,cv);
        sql.close();
    }
    public void usunMiejsce(int id){
        SQLiteDatabase sql = getWritableDatabase();
        String[] argumenty = {""+id};
        sql.delete("Miejsce","id=?",argumenty);
        sql.close();
    }
    public Miejsce wezMiejsce(int id){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","nazwa","info","dlugosc","szerokosc","id_KATEGORIA"};
        String[] argumenty = {""+id};
        Cursor c = sql.query("Miejsce",kolumny,"id=?",argumenty,null,null,null);
        Miejsce miejsce = new Miejsce();
        while (c.moveToNext()){
            miejsce.setId(c.getInt(0));
            miejsce.setNazwa(c.getString(1));
            miejsce.setInformacjeDodatkowe(c.getString(2));
            miejsce.setDlugosc(c.getDouble(3));
            miejsce.setSzerokosc(c.getDouble(4));
            miejsce.setIdKategoria(c.getInt(5));
        }
        return miejsce;
    }
    public Cursor getAllMiejsce(){
        SQLiteDatabase sql = getReadableDatabase();

        String[] kolumny = {"id","nazwa","info","dlugosc","szerokosc","id_KATEGORIA"};
        Cursor c = sql.query("MIEJSCE",kolumny,null,null,null,null,null);
        return  c;
    }
    public Cursor getAllMiejsceByKategoria(int id_KATEGORIA){
        SQLiteDatabase sql = getReadableDatabase();

        String[] kolumny = {"id","nazwa","info","dlugosc","szerokosc","id_KATEGORIA"};
        String[] argumenty = {""+id_KATEGORIA};
        Cursor c = sql.query("MIEJSCE",kolumny,"id_KATEGORIA=?",argumenty,null,null,null);
        return  c;
    }
    public Miejsce getMiejsceByNazwa(String nazwa){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","nazwa","info","dlugosc","szerokosc","id_KATEGORIA"};
        String[] argumenty = {""+nazwa};
        Cursor c = sql.query("MIEJSCE",kolumny,"nazwa=?",argumenty,null,null,null);
        Miejsce m = new Miejsce();
        while (c.moveToNext()){
            m.setId(c.getInt(0));
            m.setNazwa(c.getString(1));
            m.setInformacjeDodatkowe(c.getString(2));
            m.setDlugosc(c.getDouble(3));
            m.setSzerokosc(c.getDouble(4));
            m.setIdKategoria(c.getInt(5));
        }
        return m;
    }
    //Trasa
    public void dodajTrase(Trasa trasa){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("nazwa",trasa.getNazwa());
        cv.put("dystansPrzebyty",trasa.getDystansPrzebyty());
        cv.put("dystansWyznaczony", trasa.getDystansWyznaczony());
        cv.put("czasPrzebyty", trasa.getCzasPrzebyty());
        cv.put("czasWyznaczony", trasa.getCzasWyznaczony());
        sql.insert("TRASA",null,cv);
        sql.close();
    }
    public void usunTrase(int id){
        SQLiteDatabase sql = getWritableDatabase();
        String[] argumenty = {""+id};
        sql.delete("TRASA","id=?",argumenty);
        sql.close();
    }
    public Trasa wezTrase(int id){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","nazwa","dystansPrzebyty","dystansWyznaczony","czasPrzebyty","czasWyznaczony"};
        String[] argumenty = {""+id};
        Cursor c = sql.query("TRASA",kolumny,"id=?",argumenty,null,null,null);
        Trasa trasa = new Trasa();
        while (c.moveToNext()){
            trasa.setId(c.getInt(0));
            trasa.setNazwa(c.getString(1));
            trasa.setDystansPrzebyty(c.getDouble(2));
            trasa.setDystansWyznaczony(c.getDouble(3));
            trasa.setCzasPrzebyty(c.getLong(4));
            trasa.setCzasWyznaczony(c.getLong(5));
        }
        return trasa;
    }
    public Trasa wezTrasePoNazwie(String nazwa){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","nazwa","dystansPrzebyty","dystansWyznaczony","czasPrzebyty","czasWyznaczony"};
        String[] argumenty = {""+nazwa};
        Cursor c = sql.query("TRASA",kolumny,"nazwa=?",argumenty,null,null,null);
        Trasa trasa = new Trasa();
        while (c.moveToNext()){
            trasa.setId(c.getInt(0));
            trasa.setNazwa(c.getString(1));
            trasa.setDystansPrzebyty(c.getDouble(2));
            trasa.setDystansWyznaczony(c.getDouble(3));
            trasa.setCzasPrzebyty(c.getLong(4));
            trasa.setCzasWyznaczony(c.getLong(5));
        }
        return trasa;
    }
    public Cursor getAllTrasy(){
        SQLiteDatabase sql = getReadableDatabase();

        String[] kolumny = {"id","nazwa","dystansPrzebyty","dystansWyznaczony","czasPrzebyty","czasWyznaczony"};
        Cursor c = sql.query("TRASA",kolumny,null,null,null,null,null);
        return  c;
    }
    //TrasaPrzebyta
    public void dodajTrasaPrzebyta(TrasaPrzebyta trasa){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("id_TRASA",trasa.getIdTrasa());
        cv.put("szerokosc",trasa.getSzerokosc());
        cv.put("dlugosc",trasa.getDlugosc());

        sql.insert("TRASAPRZEBYTA",null,cv);
        sql.close();
    }
    public Cursor trasaPrzebytaByIdTrasa(int id){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","szerokosc","dlugosc"};
        String[] argumenty = {""+id};
        Cursor c = sql.query("TRASAPRZEBYTA",kolumny,"id_TRASA=?",argumenty,null,null,null);
        return c;
    }

    //TrasaWyznaczona
    public void dodajTrasaWyznaczona(TrasaWyznaczona trasa){
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put("id_TRASA",trasa.getIdTrasa());
        cv.put("szerokosc",trasa.getSzerokosc());
        cv.put("dlugosc",trasa.getDlugosc());

        sql.insert("TRASAWYZNACZONA",null,cv);
        sql.close();
    }
    public Cursor trasaWyznaczonaByIdTrasa(int id){
        SQLiteDatabase sql = getReadableDatabase();
        String[] kolumny = {"id","szerokosc","dlugosc"};
        String[] argumenty = {""+id};
        Cursor c = sql.query("TRASAWYZNACZONA",kolumny,"id_TRASA=?",argumenty,null,null,null);
        return c;
    }

}
