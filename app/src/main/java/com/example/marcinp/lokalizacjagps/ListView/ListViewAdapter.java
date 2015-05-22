package com.example.marcinp.lokalizacjagps.ListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.ActivityClass.Kategorie;
import com.example.marcinp.lokalizacjagps.R;

import java.util.List;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class ListViewAdapter extends ArrayAdapter<Item>{

    Context context;
    int resource;
    Item objects[] = null;
    public ListViewAdapter(Context context, int resource, Item[] objects) {
        super(context, resource, objects);

        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView==null) {
            LayoutInflater inflater = ((Kategorie) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }
        Item item = objects[position];

        TextView tvNazwaKategorii = (TextView) convertView.findViewById(R.id.tvNazwaKategoria);
        TextView tvLiczba = (TextView) convertView.findViewById(R.id.tvLiczbaMiejsc);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivZdjecie);

        tvNazwaKategorii.setText(item.getNazwa());
        tvLiczba.setText(item.getLiczbaMiejsc());
        imageView.setImageResource(R.drawable.star_big_on);

        return convertView;
    }
}
