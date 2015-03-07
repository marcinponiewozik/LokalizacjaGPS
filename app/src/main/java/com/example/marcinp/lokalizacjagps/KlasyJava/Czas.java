package com.example.marcinp.lokalizacjagps.KlasyJava;

/**
 * Created by Marcin on 2015-03-07.
 */
public class Czas {
    int godziny;
    int minuty;
    int sekundy;

    public String czasSlownie(){
        if( godziny == 0){
            return  minuty +" min "+sekundy+" sek";
        }
        else
            return  godziny+" godz "+minuty +" min "+sekundy+" sek";
    }
    public Czas() {
    }

    public Czas(int godziny, int minuty, int sekundy) {
        this.godziny = godziny;
        this.minuty = minuty;
        this.sekundy = sekundy;
    }

    public int getGodziny() {
        return godziny;
    }

    public void setGodziny(int godziny) {
        this.godziny = godziny;
    }

    public int getMinuty() {
        return minuty;
    }

    public void setMinuty(int minuty) {
        this.minuty = minuty;
    }

    public int getSekundy() {
        return sekundy;
    }

    public void setSekundy(int sekundy) {
        this.sekundy = sekundy;
    }
}
