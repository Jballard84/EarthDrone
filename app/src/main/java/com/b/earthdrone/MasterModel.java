package com.b.earthdrone;

import android.widget.TextView;

import java.sql.Connection;
import java.util.Observable;

public class MasterModel extends Observable {

    private   Connection conn;
    private String orientation = "";
    private String latitude="";
    private String longitude="";
    private String distance="";
    private TextView mlatitude_text;
    private TextView mlongitude_text;
    private TextView mdistance_text;
    private TextView morientation_text;




    void setOrientation(String s){
        this.orientation = s;
        notifyObservers();
    }
    void setLatitude(String s){
        this.latitude=s;
        notifyObservers();
    }
    void setLongitude(String s){
        this.longitude=s;
        notifyObservers();
    }
    void setDistance(String s){
        this.distance=s;
        notifyObservers();
    }
    void setConn(Connection c){
        this.conn=c;
        notifyObservers();
    }
    String getOrientation(){
        return orientation;
    }
    String getLatitude(){
        return latitude;
    }
    String getLongitude(){
        return longitude;
    }

    String getDistance(){
        return distance;
    }
    Connection getConn(){
        return conn;
    }



}



