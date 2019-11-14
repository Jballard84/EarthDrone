package com.b.earthdrone;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static String latitude;
    private static String longitude;
    private static String orientation;
    private static String distance;
    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    private static Connection conn=GlobalClass.mModel.getConn();
    private static final long POLL_INTERVAL_MS = TimeUnit.SECONDS.toMillis(1);

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG,"Recieved an intent"+ intent);
        String res = "";


        try {
            Class.forName("org.mariadb.jdbc.Driver");
            try {
                if (conn == null) {
                    conn = DriverManager.getConnection(url, user, pass);
                    GlobalClass.mModel.setConn(conn);
                    System.out.println("Database connection success");
                } else {
                    System.out.println("Database is connected");

                }

                Statement st1 = conn.createStatement();
                ResultSet or = st1.executeQuery("select distinct Heading from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                or.next();
                ResultSetMetaData rsmd1 = or.getMetaData();
                orientation = or.getString(1).toString();
                GlobalClass.mModel.setOrientation(orientation);

                Statement st2 = conn.createStatement();
                ResultSet lat = st2.executeQuery("select distinct Latitude from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                lat.next();
                ResultSetMetaData rsmd2 = lat.getMetaData();
                latitude = lat.getString(1).toString();
                GlobalClass.mModel.setLatitude(latitude);

                Statement st3 = conn.createStatement();
                ResultSet lon = st3.executeQuery("select distinct Longitude from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                lon.next();
                ResultSetMetaData rsmd3 = lon.getMetaData();
                longitude = lon.getString(1).toString();
                GlobalClass.mModel.setLongitude(longitude);

                Statement st4 = conn.createStatement();
                ResultSet dis = st4.executeQuery("select distinct Speed from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                dis.next();
                ResultSetMetaData rsmd4 = or.getMetaData();
                distance = dis.getString(1).toString();
                GlobalClass.mModel.setDistance(distance);

                res = orientation + latitude + longitude + distance;
            } catch (Exception e) {
                e.printStackTrace();
                res = e.toString();
            }

            StringBuilder sb = new StringBuilder();




        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.println("Data base selection success");



        //this is where I will do my code but dont know how


    }


/*
    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;

*/
}