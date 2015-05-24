package com.example.marcinp.lokalizacjagps.ListView2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.ActivityClass.Kategorie;
import com.example.marcinp.lokalizacjagps.ActivityClass.Monitoruj;
import com.example.marcinp.lokalizacjagps.ListView.Item;
import com.example.marcinp.lokalizacjagps.R;

import java.text.DecimalFormat;

/**
 * Created by Marcin on 2015-05-23.
 */
public class ListViewAdapterMiejsca extends ArrayAdapter {
    Context context;
    int resource;
    ItemMiejsce objects[] = null;
    public ListViewAdapterMiejsca(Context context, int resource, ItemMiejsce[] objects) {
        super(context, resource, objects);

        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView==null) {
            LayoutInflater inflater = ((Monitoruj) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }
        ItemMiejsce item = objects[position];

        TextView tvIdMiejsca = (TextView) convertView.findViewById(R.id.tvIdMiejsca);
        TextView tvNazwa = (TextView) convertView.findViewById(R.id.tvNazwaMiejsca);
        TextView tvodleglosc = (TextView) convertView.findViewById(R.id.tvOdlegloscDoMiejsca);
        float d = item.getOdleglosc()/1000;
        tvNazwa.setText(item.getNazwa());
        tvodleglosc.setText(String.format("%.3f",d)+"km");
        tvIdMiejsca.setText(String.valueOf(item.getId()));

        return convertView;
    }
}
