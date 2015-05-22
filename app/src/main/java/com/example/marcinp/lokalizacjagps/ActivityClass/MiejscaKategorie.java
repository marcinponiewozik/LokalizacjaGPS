package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.marcinp.lokalizacjagps.R;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class MiejscaKategorie extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_miejsca_kategoria);

        TabHost tabHost = getTabHost();

        TabHost.TabSpec miejscaTab = tabHost.newTabSpec("Miejsca");
        miejscaTab.setIndicator("Miejsca");
        Intent miejsceIntent = new Intent(this, Miejsca.class);
        miejscaTab.setContent(miejsceIntent);

        TabHost.TabSpec kategorieTab = tabHost.newTabSpec("Kategorie");
        kategorieTab.setIndicator("Kategorie");
        Intent kategorieIntent = new Intent(this, Kategorie.class);
        kategorieTab.setContent(kategorieIntent);

        tabHost.addTab(miejscaTab);
        tabHost.addTab(kategorieTab);
    }
}
