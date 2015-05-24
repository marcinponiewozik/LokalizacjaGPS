package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marcinp.lokalizacjagps.BazaDanych.DBAdapter;
import com.example.marcinp.lokalizacjagps.BazaDanych.Kategoria;
import com.example.marcinp.lokalizacjagps.ListView.Item;
import com.example.marcinp.lokalizacjagps.ListView.ListViewAdapter;
import com.example.marcinp.lokalizacjagps.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class Kategorie extends Activity {

    ListView lvKategorie;

    List<Kategoria> listaKategorii;

    final Context context = this;
    List<Item> itemList;
    DBAdapter sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kategorie);
        sql = new DBAdapter(getApplicationContext());
        initView();
    }

    public void btnDodajKategorie(View v){
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dodaj_kategorie);
        dialog.setTitle("Nowa Kategoria");

        Button btnDodaj = (Button) dialog.findViewById(R.id.btnDodaj);
        final EditText etNazwaKategorii = (EditText) dialog.findViewById(R.id.etNazwaKategorii);

        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNazwaKategorii.getText().equals(""))
                    Toast.makeText(getApplicationContext(),"Wpisz nazwę Kategorii",Toast.LENGTH_SHORT).show();
                else{
                    Kategoria kategoria = new Kategoria();
                    kategoria.setNazwa(etNazwaKategorii.getText().toString());
                    kategoria.setZdjecie(null);

                    sql.dodajKategoria(kategoria);
                    initListViewAdapter();
                    dialog.cancel();
                }
            }
        });

        dialog.show();
    }
    public void initView() {
        lvKategorie = (ListView) findViewById(R.id.lvKategorie);
        initListViewAdapter();

        lvKategorie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context,KategorieMapa.class);
                intent.putExtra("id_KATEGORIA",listaKategorii.get(i).getId());
                startActivity(intent);
            }
        });

        lvKategorie.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Usunąć kategorię?");
                alert.setPositiveButton("Tak",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sql.usunKategoria(listaKategorii.get(index).getId());
                        initListViewAdapter();
                    }
                });
                alert.setNegativeButton("Nie",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alert.show();
                return false;
            }
        });
    }

    public void initListViewAdapter() {
        listaKategorii = new ArrayList<>();
        itemList= new ArrayList<>();
        Cursor c=sql.getAllKategoria();
        while( c.moveToNext()){
            Kategoria temp =new Kategoria();
            temp.setId(c.getInt(0));
            temp.setNazwa(c.getString(1));
            temp.setZdjecie(c.getString(2));

            Item i = new Item();
            i.setNazwa(temp.getNazwa());

            int liczbaMiejsc = sql.getAllMiejsceByKategoria(temp.getId()).getCount();
            System.out.println("PPPPP:"+liczbaMiejsc);
            i.setLiczbaMiejsc(String.valueOf(liczbaMiejsc));
            i.setZdjecie(null);
            itemList.add(i);
            listaKategorii.add(temp);
        }
        Item[] tempList = new Item[itemList.size()];
        tempList = itemList.toArray(tempList);
        ListViewAdapter adapter = new ListViewAdapter(context, R.layout.row_list,tempList);

        lvKategorie.setAdapter(adapter);
    }
}
