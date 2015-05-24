package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Trasa;
import com.example.marcinp.lokalizacjagps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class TrasaHistoria extends Activity {

    ListView lv;
    DBAdapter sql;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_listviev);

        sql = new DBAdapter(context);
        intiListView();
    }

    public void intiListView() {

        List<String> nazwyTras = new ArrayList<>();
        final List<Trasa> trasy = new ArrayList<>();
        Cursor c = sql.getAllTrasy();
        while (c.moveToNext()){
            Trasa t = new Trasa();
            t.setId(c.getInt(0));
            t.setNazwa(c.getString(1));
            t.setDystansPrzebyty(c.getDouble(2));
            t.setDystansWyznaczony(c.getDouble(3));
            t.setCzasPrzebyty(c.getLong(4));
            t.setCzasWyznaczony(c.getLong(5));

            trasy.add(t);
            nazwyTras.add(t.getNazwa());
        }
        lv = (ListView) findViewById(R.id.lvTrasy);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,nazwyTras);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id_TRASA = trasy.get(i).getId();
                Intent intent = new Intent(context,TrasaMapa.class);
                intent.putExtra("id_TRASA",id_TRASA);
                startActivity(intent);
            }
        });
    }
}
