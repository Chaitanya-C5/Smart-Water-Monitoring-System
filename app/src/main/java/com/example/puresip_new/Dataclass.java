package com.example.puresip_new;

public class Dataclass {

    String date, time, temp, ph, turb, purity, loc, uno;

    public Dataclass(String date, String time, String temp, String ph, String turb, String purity, String loc, String uno) {
        this.date = date;
        this.time = time;
        this.temp = temp;
        this.ph = ph;
        this.turb = turb;
        this.purity = purity;
        this.loc = loc;
        this.uno = uno;
    }

    public Dataclass(){}

    public String getUno() {
        return uno;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }

    public String getPh() {
        return ph;
    }

    public String getTurb() {
        return turb;
    }

    public String getPurity() {
        return purity;
    }

    public String getLoc() {
        return loc;
    }
}
