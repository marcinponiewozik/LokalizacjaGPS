package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Trasa;
import com.example.marcinp.lokalizacjagps.NowyBieg;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by MarcinP on 2015-03-04.
 */
public class Rozpocznij extends Activity implements OnMapReadyCallback {
    final Context context = this;
    private MapFragment mapFragment;

    private TextView tvNazwaTrasy, tvDystans, tvTimer;

    private Trasa aktualnaTrasa;
    DBAdapter sql;

    Location ostatniaLokalizacja = null;
    int odleglosc=0;
    LocationManager locationManager;
    LocationListener locationListener;

    NowyBieg nowy;

    List<String> listaTras;
    private long startTime = 0L,czasWMilisekundach = 0L,czasBuff = 0L,czasUpdated = 0L;

    Timer timer;
    TimerTask timerTask;
    int GPSczas,GPSodleglosc;

    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glowna);

        sql = new DBAdapter(context);
        listaTras = new ArrayList<String>();
        ustawGUI();
        pokazDialog();


    }

    public void pokazDialog() {
        ustawListe();

        final Dialog dialog = new Dialog(Rozpocznij.this);
        dialog.setContentView(R.layout.dialog_listviev);
        dialog.setTitle("Wybierz trase");
        ListView lv = (ListView) dialog.findViewById(R.id.lvTrasy);
        ArrayAdapter adapter = new ArrayAdapter(Rozpocznij.this,android.R.layout.simple_list_item_activated_1,listaTras);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aktualnaTrasa= sql.wezTrasePoNazwie(listaTras.get(position));
                aktualizujUI();
                dialog.cancel();
                ustawMape();
            }
        });

        dialog.show();
    }

    public void rysujTrase(GoogleMap map){

        Cursor c = sql.wezWSPOLRZEDNE(aktualnaTrasa.getId());

        PolylineOptions options = new PolylineOptions();
        while( c.moveToNext() ){
            double dlugosc=c.getDouble(0);
            double szerokosc = c.getDouble(1);

            options.add(new LatLng(szerokosc,dlugosc));
        }

        Polyline polyline=map.addPolyline(options);
        polyline.setColor(Color.BLUE);
        polyline.setWidth(3);
        polyline.setGeodesic(true);

        if(options.getPoints().size()>1)
            ustawKamere(options.getPoints().get(0));
    }
    public void ustawMape() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void ustawGUI() {
        tvNazwaTrasy = (TextView) findViewById(R.id.tvNazwa);
        tvDystans = (TextView) findViewById(R.id.tvDystans);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        aktualnaTrasa = new Trasa();

    }
    public void ustawGPS() {
        if(aktualnaTrasa.getRodzaj() == 1){
            GPSczas = 5000;
            GPSodleglosc = 5;
        }
        else {
            GPSczas = 10000;
            GPSodleglosc = 15;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(ostatniaLokalizacja != null) {
                odleglosc += Math.round(ostatniaLokalizacja.distanceTo(location));
                }
                tvDystans.setText(odleglosc+"");
                nowy.dodajWspolrzedne(location);

                ostatniaLokalizacja= location;
                ustawKamere(new LatLng(location.getLatitude(),location.getLongitude()));
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPSczas,GPSodleglosc, locationListener);
    }
    public void ustawListe(){
        Cursor c = sql.getAllTrasy();
        List<String> list = new ArrayList<String>();
        while( c.moveToNext()) {
            list.add(c.getString(1));
        }
        listaTras = new ArrayList<String>();
        listaTras = list;
    }

    public void aktualizujUI() {
        tvNazwaTrasy.setText(aktualnaTrasa.getNazwa());
        tvDystans.setText(""+aktualnaTrasa.getDystans());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        rysujTrase(googleMap);
    }


    public void startTimer() {
        timer = new Timer();
        initTimer();
        timer.schedule(timerTask,0,1000);
    }
    public void stopTimer(){
        if(timer != null){
            timer.cancel();
            timer=null;
        }
    }
    public void initTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        czasWMilisekundach = SystemClock.uptimeMillis() - startTime;

                        czasUpdated = czasBuff + czasWMilisekundach;

                        int sekundy = (int) (czasUpdated / 1000);
                        int minuty = sekundy/60;
                        int godziny = minuty/60;
                        sekundy = sekundy % 60;

                        tvTimer.setText(godziny+":"+minuty+":"+
                                String.format("%02d", sekundy));
                    }
                });
            }
        };
    }
    public void btnStart(View v){
        nowy = new NowyBieg();
        nowy.setTrasa(aktualnaTrasa);
        ustawGPS();

        startTime = SystemClock.uptimeMillis();
        startTimer();
    }
    public void btnStop(View v){
        stopTimer();

        nowy.zakonczBieg(czasUpdated, context,odleglosc);

        locationManager.removeUpdates(locationListener);
    }

    public void ustawKamere(LatLng latLang){
        GoogleMap map = mapFragment.getMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang,5));
    }
}
