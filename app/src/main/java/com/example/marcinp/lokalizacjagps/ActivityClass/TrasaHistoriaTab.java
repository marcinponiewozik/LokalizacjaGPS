package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.marcinp.lokalizacjagps.R;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class TrasaHistoriaTab extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_miejsca_kategoria);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec pieszoTab = tabHost.newTabSpec("Pieszo");
        pieszoTab.setIndicator("Pieszo");
        Intent pieszoIntent = new Intent(this, TrasaPieszo.class);
        pieszoTab.setContent(pieszoIntent);

        TabHost.TabSpec pojazdTab = tabHost.newTabSpec("Pojazd");
        pojazdTab.setIndicator("Pojazd");
        Intent pojazdIntent = new Intent(this, TrasaPojazd.class);
        pojazdTab.setContent(pojazdIntent);

        TabHost.TabSpec historiaTab = tabHost.newTabSpec("Historia");
        historiaTab.setIndicator("Historia");
        Intent historiaIntent = new Intent(this, TrasaHistoria.class);
        historiaTab.setContent(historiaIntent);

        tabHost.addTab(pieszoTab);
        tabHost.addTab(pojazdTab);
        tabHost.addTab(historiaTab);
    }
}
