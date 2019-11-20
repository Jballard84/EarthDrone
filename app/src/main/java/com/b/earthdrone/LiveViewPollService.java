package com.b.earthdrone;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import static com.b.earthdrone.Map_Activity.ROBOT_POSTION_MOVE_MARKER;

public class LiveViewPollService extends IntentService {
    private static final String TAG = "LiveViewPollService";
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
        return new Intent(context, LiveViewPollService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = LiveViewPollService.newIntent(context);
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
        Intent i = LiveViewPollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public LiveViewPollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {// this is where we will change the code to view images from the database
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
        sendBroadcast(new Intent(Live_Activity.ROBOT_VIEW));//this send a broadcast to the system and when it gets the message it moves the marker


    }





}
