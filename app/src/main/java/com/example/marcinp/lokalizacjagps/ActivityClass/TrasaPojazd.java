package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
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
import com.example.marcinp.lokalizacjagps.BazaDanych.Trasa;
import com.example.marcinp.lokalizacjagps.BazaDanych.TrasaPrzebyta;
import com.example.marcinp.lokalizacjagps.BazaDanych.TrasaWyznaczona;
import com.example.marcinp.lokalizacjagps.KlasyJava.JSONDecoder;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class TrasaPojazd extends Activity implements OnMapReadyCallback{
    Button btnWyznacz;
    TextView tvLiczba;
    int liczba = 0;
    MapFragment map;
    DBAdapter sql;
    Context context=this;

    List<Marker> markerList;

    LocationManager locationManager;
    LocationListener locationListener;
    Location loc;
    Location poprzednia;

    List<LatLng> trasaWyznaczona;
    List<LatLng> trasaPrzebyta;

    double dystansWyznaczony=0;
    double dystansPrzebyty=0;

    Date rozpoczecie = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wyznacz_trase);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                loc = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch (status){
                    case GpsStatus.GPS_EVENT_FIRST_FIX:
                        Toast.makeText(context, "GPS gotowy do uzycia", Toast.LENGTH_SHORT).show();
                        break;
                    case GpsStatus.GPS_EVENT_STARTED:
                        Toast.makeText(context,"GPS started...",Toast.LENGTH_SHORT).show();
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED:
                        Toast.makeText(context,"GPS stoped",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);

        sql= new DBAdapter(context);
        markerList = new ArrayList<>();
        initView();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        final Date zakonczenie = new Date();
        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Zapisać tą trasę?");
        alert.setPositiveButton("Tak",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Trasa trasa = new Trasa();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String temp = sdf.format(new Date());

                Geocoder adres = new Geocoder(context);
                Address address=null;

                LatLng first = trasaWyznaczona.get(0);
                LatLng last = trasaWyznaczona.get(trasaWyznaczona.size()-1);
                List<Address> list= null;

                String adresPoczatek="";
                try {
                    list = adres.getFromLocation(first.latitude,first.longitude,1);
                    if(!list.isEmpty()) {
                        address = list.get(0);
                        adresPoczatek=address.getAddressLine(0);
                    }
                } catch (IOException e) {
                    Toast.makeText(context, "Problem z internetem", Toast.LENGTH_SHORT);
                }
                String adresKoniec = "";
                try {
                    list = adres.getFromLocation(last.latitude,last.longitude,1);
                    if(!list.isEmpty()) {
                        address = list.get(0);
                        adresKoniec=address.getAddressLine(0);
                    }
                } catch (IOException e) {
                    Toast.makeText(context, "Problem z internetem", Toast.LENGTH_SHORT);
                }

                String nazwaTrasy = temp + "|"+adresPoczatek+"--->"+adresKoniec;
                trasa.setNazwa(nazwaTrasy);
                trasa.setDystansPrzebyty(dystansPrzebyty);
                trasa.setDystansWyznaczony(dystansWyznaczony);

                long czas;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = null;
                Date date2 = null;

                String d1 = dateFormat.format(rozpoczecie);
                String d2 = dateFormat.format(zakonczenie);
                try {
                    date1 = dateFormat.parse(d1);
                    date2 = dateFormat.parse(d2);

                    czas = date2.getTime() - date1.getTime();
                    trasa.setCzasPrzebyty(czas);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sql.dodajTrase(trasa);

                trasa = sql.wezTrasePoNazwie(nazwaTrasy);

                for (int j = 0; j < trasaPrzebyta.size(); j++) {
                    TrasaPrzebyta t = new TrasaPrzebyta();
                    t.setIdTrasa(trasa.getId());
                    t.setSzerokosc(trasaPrzebyta.get(j).latitude);
                    t.setDlugosc(trasaPrzebyta.get(j).longitude);

                    sql.dodajTrasaPrzebyta(t);
                }

                for (int j = 0; j < trasaWyznaczona.size(); j++) {
                    TrasaWyznaczona t = new TrasaWyznaczona();
                    t.setIdTrasa(trasa.getId());
                    t.setSzerokosc(trasaWyznaczona.get(j).latitude);
                    t.setDlugosc(trasaWyznaczona.get(j).longitude);

                    sql.dodajTrasaWyznaczona(t);
                }

                trasaPrzebyta = new ArrayList<LatLng>();
                trasaWyznaczona = new ArrayList<LatLng>();
                rozpoczecie = null;
                Toast.makeText(context,"Trasa została zapisana",Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Nie",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context,"Trasa nie została zapisana",Toast.LENGTH_SHORT).show();
            }
        });

        if(rozpoczecie!=null)
            alert.show();
    }

    public void initView(){
        btnWyznacz = (Button) findViewById(R.id.btnWyznaczTrase);
        tvLiczba = (TextView) findViewById(R.id.tvLiczbaPunktow);

        map = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaWyznacz);
        map.getMapAsync(this);

        setBtnWyznacz();
    }

    public Uri uriGooglMapsNavigation(){
        String koniec = "dir/"+loc.getLatitude()+","+loc.getLongitude()+"/";
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

                if(loc!=null){
                    Marker m = markerList.get(markerList.size()-1);
                    String url = JSONUrl(new LatLng(loc.getLatitude(), loc.getLongitude()), m.getPosition());
                    PobieranieJSON pobieranieJSON = new PobieranieJSON();
                    pobieranieJSON.execute(url);
                    ustawGPS();
                    rozpoczecie = new Date();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uriGooglMapsNavigation());
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context,"GPS jest nieaktywny",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void ustawGPS(){
        trasaPrzebyta = new ArrayList<>();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                double dystans = 0;
                if(poprzednia!=null)
                    dystans = location.distanceTo(poprzednia);

                dystansPrzebyty = dystansPrzebyty +dystans;
                trasaPrzebyta .add(latLng);
                poprzednia=location;
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,10, locationListener);


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
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
                if (jestNaLiscie(marker)) {
                    markerList.remove(marker);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker());
                    liczba--;
                    tvLiczba.setText(String.valueOf(liczba));
                    if (liczba == 0)
                        btnWyznacz.setEnabled(false);
                } else {
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

    //tworzenie adresu url trasy
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
    //pobieranie trasy dojazdu w pliku JSON z podanego url
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

    //Pobieranie JSONA w nowym watku| po zakonczeniu tworzenie nowego watku w ktorym bedzie przetwarzany plik JSON
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

    // Klasa AsynTask przetwarzajaca plik JSON
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

                Location temp = null;
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("szerokosc"));
                    double lng = Double.parseDouble(point.get("dlugosc"));

                    LatLng position = new LatLng(lat, lng);
                    Location location=new Location(locationManager.GPS_PROVIDER);
                    location.setLatitude(position.latitude);
                    location.setLongitude(position.longitude);

                    double dystans = 0;
                    if(temp!=null)
                        dystans = location.distanceTo(temp);

                    dystansWyznaczony = dystansWyznaczony + dystans;
                    trasaWyznaczona.add(position);
                    temp = location;
                }
            }
        }
    }
}
