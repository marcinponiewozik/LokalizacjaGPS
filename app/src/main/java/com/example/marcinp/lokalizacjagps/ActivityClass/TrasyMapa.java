package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.DBAdapter;
import com.example.marcinp.lokalizacjagps.KlasyJava.Trasa;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by Marcin on 2015-03-06.
 */
public class TrasyMapa extends Activity implements OnMapReadyCallback {
    private MapFragment mapFragment;
    Context context = this;

    Trasa trasa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasy_mapy);
        DBAdapter sql = new DBAdapter(context);
        trasa = sql.wezTrase(getIntent().getExtras().getInt("id"));

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


        PolylineOptions rectOptions= wyznaczTrase(trasa.getId());
        Polyline polyline = googleMap.addPolyline(rectOptions);
        polyline.setColor(Color.BLUE);
        polyline.setWidth(3);
        polyline.setGeodesic(true);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(rectOptions.getPoints().get(0))
                .title("Poczatek trasy:"+trasa.getNazwa());
        Marker marker = googleMap.addMarker(markerOptions);
        MarkerOptions markerOptions2 = new MarkerOptions()
                .position(rectOptions.getPoints().get(rectOptions.getPoints().size()-1))
                .title("Koniec trasy:"+trasa.getNazwa());
        Marker marker2 = googleMap.addMarker(markerOptions2);

        LatLngBounds srodekTrasy = new LatLngBounds(rectOptions.getPoints().get(0),rectOptions.getPoints().get(0));

        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(srodekTrasy.getCenter(),13);
        googleMap.moveCamera(center);

    }

    public PolylineOptions wyznaczTrase(int id) {
        PolylineOptions rectOptions = new PolylineOptions();
        DBAdapter sql = new DBAdapter(context);
        Cursor c=sql.wezWSPOLRZEDNE(id);
        while (c.moveToNext()){
            double dlugosc=c.getDouble(0);
            double szerokosc = c.getDouble(1);
            rectOptions.add(new LatLng(szerokosc,dlugosc));
        }
        return rectOptions;
    }

}
