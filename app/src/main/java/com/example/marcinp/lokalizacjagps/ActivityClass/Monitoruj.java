package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Miejsce;
import com.example.marcinp.lokalizacjagps.ListView2.ItemMiejsce;
import com.example.marcinp.lokalizacjagps.ListView2.ListViewAdapterMiejsca;
import com.example.marcinp.lokalizacjagps.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Marcin on 2015-05-23.
 */
public class Monitoruj extends Activity {

    Context context = this;

    ListView lvMiejsca;
    RadioButton rbPieszo, rbPojazd;
    RadioGroup rgTryb;

    Switch switchwlwyl;

    LocationManager manager;
    LocationListener listener;

    RadioButton trybWybrany = null;

    List<String> nazwyMiejsc;
    List<Miejsce> miejsca;

    DBAdapter sql;

    Location mojaLokalizacja = null;
    Location locationtemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoruj);

        nazwyMiejsc = new ArrayList<>();
        miejsca = new ArrayList<>();
        sql = new DBAdapter(context);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        initLayout();
    }


    public void initLayout() {
        lvMiejsca = (ListView) findViewById(R.id.lvMiejsca);
        rgTryb = (RadioGroup) findViewById(R.id.rgTryb);
        rbPieszo = (RadioButton) rgTryb.findViewById(R.id.rbPieszo);
        rbPojazd = (RadioButton) rgTryb.findViewById(R.id.rbPojazd);
        switchwlwyl = (Switch) findViewById(R.id.switchwlwyl);

        initRadioGroup();
        intiSwitch();
    }

    public void intiSwitch() {
        switchwlwyl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                System.out.println("SWITCH" + b);
                if (b == true) {
                    initLvMiejsca();
                    rbPieszo.setEnabled(true);
                    rbPojazd.setEnabled(true);
                }
                else {
                    rbPojazd.setChecked(false);
                    rbPieszo.setChecked(false);
                    rbPieszo.setEnabled(false);
                    rbPojazd.setEnabled(false);
                    manager.removeUpdates(listener);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
                    lvMiejsca.setAdapter(adapter);
                }
            }
        });
    }

    public void initRadioGroup() {
        rgTryb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rbPieszo.getId())
                    trybWybrany = rbPieszo;
                else
                    trybWybrany = rbPojazd;

                System.out.println("WYBRANY:" + trybWybrany.getText());

                initGPS();
            }
        });
    }

    public void initGPS() {
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mojaLokalizacja = location;
                initLvMiejsca();
                System.out.println("LOKALIZACJA:" + mojaLokalizacja);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (trybWybrany == rbPieszo)
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 15, listener);
        else
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 30, listener);


    }

    public void initLvMiejsca() {
        miejsca = new ArrayList<>();
        int odleglosc;
        if (trybWybrany == rbPieszo)
            odleglosc = 1000;
        else
            odleglosc = 10000;

        Cursor c = sql.getAllMiejsce();


        if (mojaLokalizacja != null) {
            List<ItemMiejsce> items = new ArrayList<>();
            while (c.moveToNext()) {
                Miejsce m = new Miejsce();
                double lng = c.getDouble(3);
                m.setDlugosc(lng);
                double lat = c.getDouble(4);
                m.setSzerokosc(lat);
                System.out.println("BAZA:" + lat + "|" + lng);
                locationtemp = new Location(LocationManager.GPS_PROVIDER);
                locationtemp.setLongitude(lng);
                locationtemp.setLatitude(lat);


                float distans = mojaLokalizacja.distanceTo(locationtemp);
                if (distans < odleglosc) {
                    m.setId(c.getInt(0));
                    m.setNazwa(c.getString(1));
                    m.setInformacjeDodatkowe(c.getString(2));
                    miejsca.add(m);

                    ItemMiejsce item = new ItemMiejsce();
                    item.setNazwa(m.getNazwa());
                    item.setOdleglosc(distans);
                    item.setId(m.getId());
                    items.add(item);
                }
            }

            ItemMiejsce[] temp = new ItemMiejsce[items.size()];
            temp = items.toArray(temp);
            Arrays.sort(temp, new Comparator<ItemMiejsce>() {
                @Override
                public int compare(ItemMiejsce i1, ItemMiejsce i2) {
                    return (int) (i1.getOdleglosc() - i2.getOdleglosc());
                }
            });
            ListViewAdapterMiejsca adapter = new ListViewAdapterMiejsca(context, R.layout.row_miejsce, temp);
            lvMiejsca.setAdapter(adapter);
        }
    }
}
