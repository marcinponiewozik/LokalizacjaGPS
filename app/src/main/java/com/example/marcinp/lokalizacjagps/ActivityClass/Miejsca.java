package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Kategoria;
import com.example.marcinp.lokalizacjagps.BazaDanych.Miejsce;
import com.example.marcinp.lokalizacjagps.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by MarcinP on 2015-05-19.
 */
public class Miejsca extends Activity implements OnMapReadyCallback{
    Context context = this;
    MapFragment map;
    EditText etSzukaj;

    List<Miejsce> listaMiejsc;


    DBAdapter sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miejsca);

        sql = new DBAdapter(context);
        listaMiejsc = new ArrayList<>();
        initLayout();
    }

    public void initLayout() {
        etSzukaj = (EditText) findViewById(R.id.etSzukaj);
        map =(MapFragment) getFragmentManager().findFragmentById(R.id.mapaMiejsc);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        wyswietlMiejsca();
        dodawanieMiejsc();
        zarzadzajMarkerami();
    }

    public void btnSzukaj(View v){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            Address address=null;
            List<Address> list= geocoder.getFromLocationName(etSzukaj.getText().toString(),1);
            if(!list.isEmpty())
                address=list.get(0);
            final LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            ustawKamere(latLng);

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Zapisac tą lokalizację?");
            alert.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    GoogleMap googleMap = map.getMap();
                    dialogDodajMiejsce(latLng, googleMap);
                }
            });

            alert.setNegativeButton("Nie",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alert.show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Problem z połączeniem", Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e){
            Toast.makeText(getApplicationContext(),"Błąd podczas dekodowania adresu", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Błąd podczas dekodowania adresu", Toast.LENGTH_SHORT).show();
        }
    }

    public void ustawKamere(LatLng latLng){
        GoogleMap googleMap = map.getMap();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
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
    public void dodawanieMiejsc(){
        final GoogleMap googleMap = map.getMap();
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                dialogDodajMiejsce(latLng, googleMap);
            }
        });
    }

    public void dialogDodajMiejsce(final LatLng latLng, final GoogleMap googleMap) {
        final List<Kategoria> kategoria = new ArrayList<Kategoria>();
        List<String> kategoriaStrings = new ArrayList<String>();
        final int[] idKategoriaSelected = new int[1];
        Cursor c = sql.getAllKategoria();
        while (c.moveToNext()){
            Kategoria k = new Kategoria();
            k.setId(c.getInt(0));
            k.setNazwa(c.getString(1));
            k.setZdjecie(c.getString(2));
            kategoria.add(k);
            kategoriaStrings.add(k.getNazwa());
        }
        final EditText etNazwaMiejsca;
        final EditText etInformacjeDodatkowe;
        Button btnDodaj;
        Spinner spnKategorie;

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_miejsce);
        dialog.setTitle("Dodaj miejsce");

        etNazwaMiejsca=(EditText) dialog.findViewById(R.id.etNazwaMiejsca);
        etInformacjeDodatkowe= (EditText) dialog.findViewById(R.id.etInformacjeDodatkowe);

        spnKategorie = (Spinner) dialog.findViewById(R.id.spnKategorie);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_dropdown_item,kategoriaStrings);
        spnKategorie.setAdapter(adapter);
        spnKategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idKategoriaSelected[0]  = (int) kategoria.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDodaj= (Button) dialog.findViewById(R.id.btnDodajMiejsce);
        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Miejsce m = new Miejsce();
                m.setNazwa(etNazwaMiejsca.getText().toString());
                m.setInformacjeDodatkowe(etInformacjeDodatkowe.getText().toString());
                m.setIdKategoria(idKategoriaSelected[0]);
                m.setDlugosc(latLng.longitude);
                m.setSzerokosc(latLng.latitude);

                sql.dodajMiejsce(m);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(m.getNazwa());
                String snippet = sql.wezKategoria(idKategoriaSelected[0]).getNazwa();
                markerOptions.snippet(snippet+"/n"+m.getInformacjeDodatkowe());
                markerOptions.position(new LatLng(m.getSzerokosc(),m.getDlugosc()));
                googleMap.addMarker(markerOptions);
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void zarzadzajMarkerami(){
        final GoogleMap googleMap = map.getMap();
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                Miejsce m = new Miejsce();

                TextView tvMarkerNazwa,tvMarkerKategoria,tvMarkerInfo,tvMarkerAdres;
                Button btnMarkerUsun,btnMarkerWyznaczTrase;

                m = sql.getMiejsceByNazwa(marker.getTitle());
                final Dialog dialog = new Dialog(context);
                dialog.setTitle("Marker:"+marker.getTitle());
                dialog.setContentView(R.layout.dialog_marker);
                tvMarkerNazwa = (TextView) dialog.findViewById(R.id.tvMarkerNazwa);
                tvMarkerInfo = (TextView) dialog.findViewById(R.id.tvMarkerInformacjeDodatkowe);
                tvMarkerKategoria = (TextView) dialog.findViewById(R.id.tvMarkerKategoria);
                tvMarkerAdres = (TextView) dialog.findViewById(R.id.tvMarkerAdres);

                tvMarkerNazwa.setText(m.getNazwa());
                tvMarkerKategoria.setText(sql.wezKategoria(m.getIdKategoria()).getNazwa());
                tvMarkerInfo.setText(m.getInformacjeDodatkowe());
                Geocoder adres = new Geocoder(context);
                try {
                    Address address=null;
                    List<Address> list= adres.getFromLocation(m.getSzerokosc(),m.getDlugosc(),1);
                    if(!list.isEmpty())
                        address=list.get(0);
                    tvMarkerAdres.setText(address.getAddressLine(0));
                } catch (IOException e) {
                    tvMarkerAdres.setText("Błąd podczas dekodowania współrzędnych");
                } catch (IllegalArgumentException e){
                    tvMarkerAdres.setText("Błąd podczas dekodowania współrzędnych");
                } catch (NullPointerException e){
                    tvMarkerAdres.setText("Błąd podczas dekodowania współrzędnych");
                }

                btnMarkerUsun = (Button) dialog.findViewById(R.id.btnMarkerUsun);
                final Miejsce finalM = m;
                btnMarkerUsun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle("Chcesz usunąc ten marker?");
                        alert.setPositiveButton("Tak",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                sql.usunMiejsce(finalM.getId());
                                marker.remove();
                                dialog.cancel();
                            }
                        });
                        alert.setNegativeButton("Nie",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        alert.show();
                    }
                });

                dialog.show();
            }
        });
    }
}
