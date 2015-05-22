package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.*;
import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Trasa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarcinP on 2015-04-13.
 */
public class Porownanie extends Activity {
    TextView tvTrasa1,tvTrasa2;
    TextView tvRodzaj1,tvRodzaj2;
    TextView tvWynik1, tvWynik2;
    TextView tvOdleglosc1, tvOdleglosc2;
    TextView tvSrednia1, tvSrednia2;

    Spinner spinnerTrasaDoPorownania;

    List<Trasa> listaTras;

    Trasa trasa1,trasa2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.porownanie);

        Bundle extras = getIntent().getExtras();
        DBAdapter sql = new DBAdapter(this);
        trasa1 = new Trasa();
        trasa2 = new Trasa();

        trasa1 = sql.wezTrase(getIntent().getIntExtra("id",0));
        initGUI();
        updateTrasa1(trasa1);

    }

    public void initGUI() {
        listaTras = new ArrayList<>();
        spinnerTrasaDoPorownania = (Spinner) findViewById(R.id.spinnerTrasaDoPorownania);
        List<String> items = new ArrayList<>();

        DBAdapter db = new DBAdapter(this);

        Cursor c = db.getAllTrasy();
        while(c.moveToNext()){
            Trasa temp = new Trasa();
            temp.setId(c.getInt(0));
            temp.setNazwa(c.getString(1));
            temp.setDystans(c.getDouble(2));
            if(temp.getId()!=trasa1.getId()) {
                listaTras.add(temp);
                items.add(temp.getNazwa());
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,items);

        spinnerTrasaDoPorownania.setAdapter(adapter);
        spinnerTrasaDoPorownania.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trasa2 = listaTras.get(position);
                updateTrasa2(trasa2);
                porownaj(trasa1,trasa2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvTrasa1 = (TextView) findViewById(R.id.tvNazwaTrasy1);
        tvTrasa2 = (TextView) findViewById(R.id.tvNazwaTrasy2);

        tvRodzaj1 = (TextView) findViewById(R.id.tvRodzaj1);
        tvRodzaj2 = (TextView) findViewById(R.id.tvRodzaj2);

        tvWynik1 = (TextView) findViewById(R.id.tvLiczbaWynikow1);
        tvWynik2 = (TextView) findViewById(R.id.tvLiczbaWynikow2);

        tvOdleglosc1 = (TextView) findViewById(R.id.tvOdleglosc1);
        tvOdleglosc2 = (TextView) findViewById(R.id.tvOdleglosc2);

        tvSrednia1 = (TextView) findViewById(R.id.tvPredkosc1);
        tvSrednia2 = (TextView) findViewById(R.id.tvPredkosc2);
    }

    public void updateTrasa2(Trasa trasa){
        tvTrasa2.setText(trasa.getNazwa());
        tvOdleglosc2.setText(Double.toString(trasa.getDystans()));
        if(trasa.getRodzaj()==0)
            tvRodzaj2.setText("Auto");
        else
            tvRodzaj2.setText("Pieszo");

        DBAdapter db = new DBAdapter(this);

        tvWynik2.setText(db.liczbaWynikow(trasa.getId())+"");
        tvSrednia2.setText("0");
    }
    public void updateTrasa1(Trasa trasa){
        tvTrasa1.setText(trasa.getNazwa());
        tvOdleglosc1.setText(Double.toString(trasa.getDystans()));
        if(trasa.getRodzaj()==0)
            tvRodzaj1.setText("Auto");
        else
            tvRodzaj1.setText("Pieszo");

        DBAdapter db = new DBAdapter(this);

        tvWynik1.setText(db.liczbaWynikow(trasa.getId())+"");
        tvSrednia1.setText("0");
    }

    public void porownaj(Trasa trasa1, Trasa trasa2){
        RelativeLayout c1,d1,e1;
        RelativeLayout c2,d2,e2;

        c1=(RelativeLayout)findViewById(R.id.c1);
        c2=(RelativeLayout)findViewById(R.id.c2);

        e1=(RelativeLayout)findViewById(R.id.e1);
        e2=(RelativeLayout)findViewById(R.id.e2);

        if(trasa1.getDystans()>trasa2.getDystans()){
            c1.setBackgroundColor(Color.RED);
            c2.setBackgroundColor(Color.GREEN);
        }
        else {
            c2.setBackgroundColor(Color.RED);
            c1.setBackgroundColor(Color.GREEN);
        }


    }
}
