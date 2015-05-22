package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Trasa;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by MarcinP on 2015-03-04.
 */
public class Trasy extends Activity {
    TextView tvRodzaj,tvDystans,tvNajlepszyWynik,tvLiczbaWynikow,tvPoczatekTrasy,tvKoniecTrasy;
    Spinner spnTrasy;
    Context context = this;
    Trasa aktualnaTrasa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasy);
        aktualnaTrasa = new Trasa();
        initGUI();
    }

    public void initGUI(){
        spnTrasy = (Spinner) findViewById(R.id.spnTrasy);
        tvRodzaj = (TextView) findViewById(R.id.textView2);
        tvLiczbaWynikow = (TextView) findViewById(R.id.textView3);
        tvNajlepszyWynik = (TextView) findViewById(R.id.textView4);
        tvPoczatekTrasy = (TextView) findViewById(R.id.textView5);
        tvKoniecTrasy = (TextView) findViewById(R.id.textView6);
        tvDystans = (TextView) findViewById(R.id.tvDystans);
        initSpinner();
        spnTrasy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                DBAdapter sql = new DBAdapter(context);
                aktualnaTrasa=sql.wezTrasePoNazwie(adapterView.getItemAtPosition(pos).toString());
                if(sql.wezWyniki(aktualnaTrasa.getId()).getCount()==0){
                    tvDystans.setText("Dystans: brak danych");
                    tvNajlepszyWynik.setText("Najlepszy wynik: brak danych");
                    tvPoczatekTrasy.setText("Początek trasy: brak danych");
                    tvKoniecTrasy.setText("Koniec trasy: brak danych");
                }
                else{
                    Cursor c=sql.wezWyniki(aktualnaTrasa.getId());
                    c.moveToPosition(sql.najlepszyWynik(aktualnaTrasa.getId()));
                    String wynik = c.getInt(2)+"godz"+c.getInt(3)+"min"+c.getInt(4)+"sek";
                    c=sql.wezWSPOLRZEDNE(aktualnaTrasa.getId());

                    Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                    List<Address> list = null;
                    try {
                        c.moveToFirst();
                        LatLng poczatek = new LatLng(c.getDouble(1),c.getDouble(0));
                        list = geocoder.getFromLocation(poczatek.latitude,poczatek.longitude,1);
                        tvPoczatekTrasy.setText("Początek trasy: "+ list.get(0).getAddressLine(0)+"("+list.get(0).getAddressLine(1)+")");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    tvDystans.setText("Dystans: "+aktualnaTrasa.getDystans()/1000 +"km");
                    tvNajlepszyWynik.setText("Najlepszy wynik: " +wynik);

                    try {
                        LatLng koniec = new LatLng(c.getDouble(1),c.getDouble(0));
                        c.moveToLast();
                        list = geocoder.getFromLocation(koniec.latitude,koniec.longitude,1);
                        tvKoniecTrasy.setText("Koniec trasy: "+ list.get(0).getAddressLine(0)+"("+list.get(0).getAddressLine(1)+")");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                tvLiczbaWynikow.setText("Liczba wyników:"+sql.wezWyniki(aktualnaTrasa.getId()).getCount());

                if(aktualnaTrasa.getRodzaj()==0){
                    tvRodzaj.setText("Rodzaj: Auto");
                }
                else
                    tvRodzaj.setText("Rodzaj: Pieszo");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initSpinner(){

        List<String> list = new ArrayList<String>();
        DBAdapter sql = new DBAdapter(context);
        Cursor c = sql.getAllTrasy();
        while (c.moveToNext())
        {
            list.add(c.getString(1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,list);
        spnTrasy.setAdapter(adapter);
    }
    public void pokazMape(View v){
        Intent intent = new Intent(context,TrasyMapa.class);
        intent.putExtra("id",aktualnaTrasa.getId());
        startActivity(intent);
    }
    public void nowaTrasa(View v){
        final Dialog dialog = new Dialog(Trasy.this);
        dialog.setContentView(R.layout.dodaj_trase);
        dialog.setTitle("Podaj nazwe Trasy");
        final EditText etNazwa = (EditText)dialog.findViewById(R.id.etPodajNazwe);
        final RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.rgRodzaj);

        Button btnDodaj = (Button) dialog.findViewById(R.id.btnDodaj);

        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trasa nowaTrasa = new Trasa();
                nowaTrasa.setNazwa(etNazwa.getText().toString());

                RadioButton rb = (RadioButton) radioGroup.findViewById(R.id.radioButton);
                int rodzaj;

                if(rb.getId()==radioGroup.getCheckedRadioButtonId())
                    rodzaj = 0;
                else
                    rodzaj = 1;

                nowaTrasa.setRodzaj(rodzaj);
                DBAdapter sql = new DBAdapter(Trasy.this);
                sql.dodajTrase(nowaTrasa);
                dialog.cancel();
                initSpinner();
            }
        });

        dialog.show();
    }

    public void porownaj(View v){
        Intent intent = new Intent(this,Porownanie.class);
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"+aktualnaTrasa.getId());
        intent.putExtra("id",aktualnaTrasa.getId());
        startActivity(intent);
    }
}
