package com.example.marcinp.lokalizacjagps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Trasa;
import com.example.marcinp.lokalizacjagps.BazaDanych.Wspolrzedne;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;


public class Glowna extends FragmentActivity implements OnMapReadyCallback {

    private MapFragment mapFragment;

    private TextView tvNazwaTrasy, tvNajlepszyWynik, tvTimer, tvLogi;
    private Button btnNowaTrasa, btnStaraTrasa, btnStart, btnStop;

    private List<Wspolrzedne> listaWspolrzednych;

    private Trasa aktualnaTrasa;
    DBAdapter sql;

    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glowna);

        ustawMape();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_glowna, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ustawMape() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void aktualizujUI(Trasa trasa) {
        tvNazwaTrasy.setText(trasa.getNazwa());
        tvNajlepszyWynik.setText("0");
        tvTimer.setText("0:00");
    }

    public void dodajGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double szerokosc = location.getLatitude();
                double dlugosc = location.getLongitude();

                String wyswietl = szerokosc + "||" + dlugosc + "\n";

                tvLogi.setText(tvLogi.getText() + wyswietl);

                Wspolrzedne wspolrzedne = new Wspolrzedne();
                //wspolrzedne.setId_Trasa(1);
                wspolrzedne.setSzerokosc(szerokosc);
                wspolrzedne.setDlugosc(dlugosc);

                //sql.dodajWspolrzedne(wspolrzedne);
                //listaWspolrzednych.add(wspolrzedne);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, locationListener);
    }
}
