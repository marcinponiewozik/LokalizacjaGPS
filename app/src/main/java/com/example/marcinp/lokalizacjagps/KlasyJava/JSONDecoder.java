package com.example.marcinp.lokalizacjagps.KlasyJava;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MarcinP on 2015-05-19.
 */
public class JSONDecoder {
    public List<List<HashMap<String,String>>> parse(JSONObject json){
        List<List<HashMap<String,String>>> trasy = new ArrayList<List<HashMap<String,String>>>() ;


        JSONArray routes;
        JSONArray legs;
        JSONArray steps;

        try {
            routes=json.getJSONArray("routes");

            System.out.println("Liczba tras:"+routes.length());
            for (int i = 0; i < routes.length(); i++) {
                JSONObject route = (JSONObject) routes.get(i);
                legs=route.getJSONArray("legs");

                List droga = new ArrayList<HashMap<String,String>>();
                System.out.println("Liczba legs:"+legs.length());
                for (int j = 0; j < legs.length() ; j++) {
                    JSONObject leg = (JSONObject) legs.get(j);
                    steps=leg.getJSONArray("steps");

                    for (int k = 0; k < steps.length(); k++) {
                        String polyline=null;
                        JSONObject step = (JSONObject)steps.get(k);
                        JSONObject polylineObject = step.getJSONObject("polyline");
                        String polylineString = polylineObject.get("points").toString();

                        List<LatLng> wspolrzedne = decodePoly(polylineString);
                        System.out.println("Liczba wspolrzednych:"+wspolrzedne.size());
                        for (int l = 0; l < wspolrzedne.size(); l++) {
                            HashMap<String,String> hashMap = new HashMap<String,String>();
                            hashMap.put("szerokosc", Double.toString(wspolrzedne.get(l).latitude));
                            hashMap.put("dlugosc", Double.toString(wspolrzedne.get(l).longitude));
                            System.out.println(wspolrzedne.get(l).latitude+":::::"+wspolrzedne.get(l).longitude);
                            droga.add(hashMap);
                        }
                    }
                    trasy.add(droga);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trasy;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
