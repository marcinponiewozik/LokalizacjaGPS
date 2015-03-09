package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.marcinp.lokalizacjagps.ActivityClass.Biegnij;
import com.example.marcinp.lokalizacjagps.DBAdapter;
import com.example.marcinp.lokalizacjagps.KlasyJava.Trasa;
import com.example.marcinp.lokalizacjagps.KlasyJava.Wspolrzedne;
import com.example.marcinp.lokalizacjagps.R;

/**
 * Created by MarcinP on 2015-03-04.
 */
public class Menu extends Activity {
    private Button btnBiegnij,btnTrasy,btnWyniki;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

//        DBAdapter sql = new DBAdapter(context);
//        Wspolrzedne wspolrzedne = new Wspolrzedne();
//
//        Double a=22.222;
//        Double b=51.222;
//        for (int i = 0; i <30 ; i++) {
//            wspolrzedne.setId_Trasa(1);
//            wspolrzedne.setDlugosc(a+i);
//            wspolrzedne.setSzerokosc(b+i);
//            sql.dodajWspolrzedne(wspolrzedne);
//        }
        initUi();
    }

    public void initUi() {
        btnBiegnij = (Button) findViewById(R.id.btnBiegnij);
        btnTrasy = (Button) findViewById(R.id.btnTrasy);
        btnWyniki = (Button) findViewById(R.id.btnWyniki);

        btnBiegnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Biegnij.class);
                startActivity(intent);
            }
        });
        btnTrasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Trasy.class);
                startActivity(intent);
            }
        });
        btnWyniki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Wyniki.class);
                startActivity(intent);
            }
        });
    }
}
