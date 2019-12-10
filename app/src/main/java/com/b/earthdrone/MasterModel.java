package com.b.earthdrone;

import android.widget.TextView;

import java.sql.Connection;
import java.text.NumberFormat;
import java.util.Observable;
import java.util.Observer;

public class MasterModel extends Observable {

    private   Connection conn;
    private String orientation = "";
    private double latitude=0;
    private double longitude=0;
    private int battery=0;
    private String distance="";
    private TextView mlatitude_text;
    private TextView mlongitude_text;
    private TextView mdistance_text;
    private TextView morientation_text;


    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    void setOrientation(String s){
        this.orientation = s;
        notifyObservers(s);
    }
    void setLatitude(double d){
        this.latitude=d;
        notifyObservers(d);
    }
    void setLongitude(double d){
        this.longitude=d;
        notifyObservers(d);
    }
    void setBattery(int i){
        this.battery=i;
    }
    void setDistance(String s){
        this.distance=s;
        notifyObservers(s);
    }
    void setConn(Connection c){
        this.conn=c;
        notifyObservers(c);
    }
    String getOrientation(){
        return orientation;
    }
    double getLatitude(){
        return latitude;
    }
    double getLongitude(){
        return longitude;
    }
    int getBattery(){ return battery; }

    String getDistance(){ return distance; }
    Connection getConn(){
        return conn;
    }



}



