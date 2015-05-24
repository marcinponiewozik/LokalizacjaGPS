package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        initUi();
    }

    public void initUi() {
        btnBiegnij = (Button) findViewById(R.id.btnBiegnij);
        btnTrasy = (Button) findViewById(R.id.btnTrasy);
        btnWyniki = (Button) findViewById(R.id.btnWyniki);

        btnBiegnij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Monitoruj.class);
                startActivity(intent);
            }
        });
        btnTrasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrasaHistoriaTab.class);
                startActivity(intent);
            }
        });
        btnWyniki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MiejscaKategorie.class);
                startActivity(intent);
            }
        });
    }
}
