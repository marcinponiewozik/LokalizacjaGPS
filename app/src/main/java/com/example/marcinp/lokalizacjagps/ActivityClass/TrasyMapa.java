package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.example.marcinp.lokalizacjagps.DBAdapter;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Marcin on 2015-03-06.
 */
public class TrasyMapa extends Activity implements OnMapReadyCallback {
    private MapFragment mapFragment;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasy_mapa);
        initMapFragment();
    }



    public void initMapFragment() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaTrasy);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        int id = getIntent().getExtras().getInt("id");
        PolylineOptions rectOptions= wyznaczTrase(id);
        googleMap.addPolyline(rectOptions);

    }

    public PolylineOptions wyznaczTrase(int id) {
        PolylineOptions rectOptions = new PolylineOptions();
        DBAdapter sql = new DBAdapter(context);
        Cursor c=sql.wezWSPOLRZEDNE(id);
        while (c.moveToNext()){
            double dlugosc=c.getDouble(0);
            double szerokosc = c.getDouble(1);
            rectOptions.add(new LatLng(dlugosc,szerokosc));
        }
        return rectOptions;
    }

}
