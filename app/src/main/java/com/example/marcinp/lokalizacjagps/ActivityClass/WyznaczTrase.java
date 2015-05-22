package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.marcinp.lokalizacjagps.KlasyJava.JSONDecoder;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.internal.ma;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
 * Created by MarcinP on 2015-05-19.
 */
public class WyznaczTrase extends Activity implements OnMapReadyCallback{

    private MapFragment mapa;
    ArrayList<LatLng> markers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wyznacz_trase);
        markers = new ArrayList<LatLng>();

        mapa = (MapFragment) getFragmentManager().findFragmentById(R.id.mapaWyznacz);
        mapa.getMapAsync(this);


    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        String poczatek = "origin="+origin.latitude+","+origin.longitude;

        String koniec = "destination="+dest.latitude+","+dest.longitude;
        String url = "https://maps.googleapis.com/maps/api/directions/json?"+poczatek+"&"+koniec;
        System.out.println("URL: "+url);
        return url;
    }
    //Pobieranie JSON-a z podanego URL
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

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLang) {
                if (markers.size() == 2) {
                    markers.clear();
                    googleMap.clear();
                }
                markers.add(latLang);
                MarkerOptions options = new MarkerOptions();
                options.position(latLang);
                googleMap.addMarker(options);

                if (markers.size() == 2) {
                    String url = getDirectionsUrl(markers.get(0), markers.get(1));

                    PobieranieJSON pobieranieJSON = new PobieranieJSON();
                    pobieranieJSON.execute(url);
                }
            }
        });
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
            RysujTrase jsonTask = new RysujTrase();

            // parsowanie json-a w nowym watku
            jsonTask.execute(result);
        }
    }

    // Klasa AsynTask pobierająca dane o trasie w formacie JSON  i wyswietlająca trase na mapie
    private class RysujTrase extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>>{


        // Czynności w tyle
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... json) {

            JSONObject jsonObject;
            JSONDecoder decoder = new JSONDecoder();
            List<List<HashMap<String, String>>> drogi = null;

            try{
                jsonObject = new JSONObject(json[0]);
                drogi = decoder.parse(jsonObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return drogi;
        }

        // Czynności wykonane na koniec w glównym wątku
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            List<LatLng> points;
            PolylineOptions lineOptions = null;

            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("szerokosc"));
                    double lng = Double.parseDouble(point.get("dlugosc"));

                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);

            }

            // rysowanie trasy
            GoogleMap googleMap = mapa.getMap();
            googleMap.addPolyline(lineOptions);
        }
    }
}
