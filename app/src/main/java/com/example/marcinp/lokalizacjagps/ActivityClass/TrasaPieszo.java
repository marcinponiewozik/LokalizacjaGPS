package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Kategoria;
import com.example.marcinp.lokalizacjagps.KlasyJava.JSONDecoder;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class TrasaPieszo extends Activity implements OnMapReadyCallback {
    Button btnWyznacz;
    TextView tvLiczba;
    int liczba = 0;
    MapFragment map;
    DBAdapter sql;
    Context context=this;

    List<Marker> markerList;

    LocationManager locationManager;
    LocationListener locationListener;

    List<LatLng> trasaWyznaczona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wyznacz_trase);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sql= new DBAdapter(context);
        markerList = new ArrayList<>();
        initView();
    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void initView(){
        btnWyznacz = (Button) findViewById(R.id.btnWyznaczTrase);
        tvLiczba = (TextView) findViewById(R.id.tvLiczbaPunktow);

        map = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaWyznacz);
        map.getMapAsync(this);

        setBtnWyznacz();
    }

    public Uri createUri(){
        Marker markerTemp = markerList.get(0);
        LatLng destination = markerTemp.getPosition();
        //
        String koniec = "dir/51.2616799,22.5079104/";
        String url = "https://www.google.pl/maps/"+koniec;
        for(int i = 0 ; i <markerList.size();i++){
            Marker m = markerList.get(i);
            url = url+"'";
            LatLng waypoint = m.getPosition();
            url = url+waypoint.latitude+","+waypoint.longitude+"'/";
        }
        System.out.println("URL:"+url);

        return Uri.parse(url);
    }
    public void setBtnWyznacz(){
        btnWyznacz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Marker m = markerList.get(markerList.size()-1);

                    String url = JSONUrl(new LatLng(loc.getLatitude(), loc.getLongitude()), m.getPosition());
//                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, createUri());
//                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                    startActivity(intent);
//                    ustawGPS();

                    PobieranieJSON pobieranieJSON = new PobieranieJSON();
                    pobieranieJSON.execute(url);
                }
                else{
                    Toast.makeText(context,"GPS jest nieaktywny",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void ustawGPS(){

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("LOCATION:"+location);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        wyswietlMiejsca();
        zarzadzajMarkerami();
    }
    public void wyswietlMiejsca(){
        GoogleMap googleMap = map.getMap();
        Cursor c = sql.getAllMiejsce();
        while (c.moveToNext()){
            double dlugosc=c.getDouble(3);
            double szerokosc = c.getDouble(4);
            String nazwa = c.getString(1);
            String info = c.getString(2);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(szerokosc,dlugosc));
            markerOptions.title(nazwa);
            Kategoria k = new Kategoria();
            k=sql.wezKategoria(c.getInt(5));
            markerOptions.snippet(k.getNazwa()+"\n"+info);

            googleMap.addMarker(markerOptions);
        }
    }
    public void zarzadzajMarkerami(){
        GoogleMap googleMap = map.getMap();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(jestNaLiscie(marker)) {
                    markerList.remove(marker);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker());
                    liczba--;
                    tvLiczba.setText(String.valueOf(liczba));
                    if (liczba ==0)
                        btnWyznacz.setEnabled(false);
                }
                else {
                    markerList.add(marker);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    liczba++;
                    tvLiczba.setText(String.valueOf(liczba));
                    btnWyznacz.setEnabled(true);
                }
                return true;
            }
        });
    }
    public boolean jestNaLiscie(Marker marker){
        for(Marker m:markerList){
            if(m.equals(marker))
                return true;
        }
        return false;
    }

    private String JSONUrl(LatLng origin,LatLng dest){

        String poczatek = "origin="+origin.latitude+","+origin.longitude;

        String koniec = "destination="+dest.latitude+","+dest.longitude;
        String url = "https://maps.googleapis.com/maps/api/directions/json?"+poczatek+"&"+koniec+"&waypoints=";

        for (int i = 0; i <markerList.size()-1 ; i++) {
            Marker m = markerList.get(i);
            LatLng p = m.getPosition();
            String point="";
            if(i==0)
                point = p.latitude+","+p.longitude;
            else
                point = "|"+p.latitude+","+p.longitude;

            url= url+point;
        }
        System.out.println("URL: "+url);
        return url;
    }
    private String downloadUrl(String urlJSON) throws IOException {
        String data = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(urlJSON);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    //Pobieranie JSONA w nowym watku
    private class PobieranieJSON extends AsyncTask<String, Void, String> {

        // Pobieranie w oddzielnym wątku
        @Override
        protected String doInBackground(String... url) {
            String data = null;

            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Czynnosci wykonane na koniec
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //Tworzenie nowego watku
            DekodowanieJSON jsonTask = new DekodowanieJSON();

            // parsowanie json-a w nowym watku
            jsonTask.execute(result);
        }
    }

    // Klasa AsynTask pobierająca dane o trasie w formacie JSON  i wyswietlająca trase na mapie
    private class DekodowanieJSON extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {


        // Czynności w tyle
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... json) {

            JSONObject jsonObject;
            JSONDecoder decoder = new JSONDecoder();
            List<List<HashMap<String, String>>> drogi = null;

            try {
                jsonObject = new JSONObject(json[0]);
                drogi = decoder.parse(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drogi;
        }

        // Czynności wykonane na koniec w glównym wątku
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            for (int i = 0; i < result.size(); i++) {
                trasaWyznaczona = new ArrayList<LatLng>();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("szerokosc"));
                    double lng = Double.parseDouble(point.get("dlugosc"));

                    LatLng position = new LatLng(lat, lng);

                    trasaWyznaczona.add(position);
                }
            }

            //sql.doda
        }
    }
}
