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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.DBAdapter;
import com.example.marcinp.lokalizacjagps.KlasyJava.Trasa;
import com.example.marcinp.lokalizacjagps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarcinP on 2015-03-04.
 */
public class Trasy extends Activity {
    TextView tvNazwaTrasy;
    Spinner spnTrasy;
    ListView lvWyniki;
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
        tvNazwaTrasy = (TextView) findViewById(R.id.tvTrasa);
        spnTrasy = (Spinner) findViewById(R.id.spnTrasy);
        lvWyniki = (ListView) findViewById(R.id.lvWyniki);
        initSpinner();
        spnTrasy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                DBAdapter sql = new DBAdapter(context);
                aktualnaTrasa=sql.wezTrase( adapterView.getItemAtPosition(pos).toString());
                tvNazwaTrasy.setText("Wyniki dla trasy:" + aktualnaTrasa.getNazwa());
                initListView(aktualnaTrasa);
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
    public void initListView(Trasa trasa){
        List<String> list = new ArrayList<String>();
        DBAdapter sql = new DBAdapter(context);
        Cursor c = sql.wezWyniki(trasa.getId());
        while (c.moveToNext()){
            list.add(c.getString(2));
        }
    }
    public void pokazMape(View v){
        Intent intent = new Intent(context,TrasyMapa.class);
        intent.putExtra("id",aktualnaTrasa.getId());
        startActivity(intent);
    }
}
