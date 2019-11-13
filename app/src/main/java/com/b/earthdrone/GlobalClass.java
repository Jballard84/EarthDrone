package com.b.earthdrone;

import android.widget.TextView;

import java.sql.Connection;

public class GlobalClass {

    public static Connection conn;
    public static String orientation = "";
    public static String latitude="";
    public static String longitude="";
    public static String distance="";
    public static TextView mlatitude_text;
    public static TextView mlongitude_text;
    public static TextView mdistance_text;
    public static TextView morientation_text;


    public static boolean HasChanged(String ori,String lat,String lon,String dis){
        if(ori != orientation){
           return true;
        }
        else if (lat != latitude){
            return true;
        }
        return false;

    }
}
