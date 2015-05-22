package com.example.marcinp.lokalizacjagps.ListView;

import android.widget.ImageView;

/**
 * Created by MarcinP on 2015-05-21.
 */
public class Item {
    private String nazwa;
    private String liczbaMiejsc;
    private ImageView zdjecie;


    public Item() {
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getLiczbaMiejsc() {
        return liczbaMiejsc;
    }

    public void setLiczbaMiejsc(String liczbaMiejsc) {
        this.liczbaMiejsc = liczbaMiejsc;
    }

    public ImageView getZdjecie() {
        return zdjecie;
    }

    public void setZdjecie(ImageView zdjecie) {
        this.zdjecie = zdjecie;
    }
}
