package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.DBAdapter;
import com.example.marcinp.lokalizacjagps.KlasyJava.Trasa;
import com.example.marcinp.lokalizacjagps.NowyBieg;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by MarcinP on 2015-03-04.
 */
public class Biegnij extends Activity implements OnMapReadyCallback {
    final Context context = this;
    private MapFragment mapFragment;

    private TextView tvNazwaTrasy, tvDystans, tvTimer, tvLogi;

    private Spinner spnTrasy;

    private Trasa aktualnaTrasa;
    DBAdapter sql;

    LocationManager locationManager;
    LocationListener locationListener;

    NowyBieg nowy;

    List<String> listaTras;
    private long startTime = 0L,czasWMilisekundach = 0L,czasBuff = 0L,czasUpdated = 0L;

    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glowna);

        sql = new DBAdapter(context);
        tvLogi = (TextView) findViewById(R.id.tvLog);
        listaTras = new ArrayList<String>();
        ustawGUI();
        ustawMape();
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

        spnTrasy = (Spinner) findViewById(R.id.spnWybierzTrase);

        ustawAdapterSpinnera();
        spnTrasy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aktualnaTrasa = sql.wezTrasePoNazwie(listaTras.get(position));
                aktualizujUI(aktualnaTrasa);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void ustawGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                nowy.dodajWspolrzedne(location);
                tvLogi.setText(tvLogi.getText().toString()+location.getLatitude()+"||"+location.getLongitude()+"\n");
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,5, locationListener);
    }
    public void ustawAdapterSpinnera(){
        DBAdapter sql = new DBAdapter(context);
        Cursor c = sql.getAllTrasy();
        List<String> list = new ArrayList<String>();
        while( c.moveToNext()) {
            list.add(c.getString(1));
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,list);
        spnTrasy.setAdapter(adapter);
        listaTras = new ArrayList<String>();
        listaTras = list;
    }

    public void aktualizujUI(Trasa aktualnaTrasa) {
        tvNazwaTrasy.setText(aktualnaTrasa.getNazwa());
        tvDystans.setText(""+aktualnaTrasa.getDystans());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
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

    public void btnDodajTrase(View v){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dodaj_trase);
        dialog.setTitle("Wpisz nazwe trasy");

        final EditText etPodajNazwe = (EditText) dialog.findViewById(R.id.etPodajNazwe);
        Button btnDodaj = (Button) dialog.findViewById(R.id.btnDodaj);
        final Trasa nowa = new Trasa();
        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowa.setDystans(0);
                nowa.setNazwa(etPodajNazwe.getText().toString());
                sql.dodajTrase(nowa);
                ustawAdapterSpinnera();
                dialog.cancel();
            }
        });

        dialog.show();
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

        nowy.zakonczBieg(czasUpdated, context);

        locationManager.removeUpdates(locationListener);
    }
}
