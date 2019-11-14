package com.b.earthdrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;


public class Map_Activity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {


    private Button mLive_button;
    private Button mDash_button;
    private Button mControl_button;
    private Button mMap_button;
    private LatLngBounds fenceCenter;
    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    private static TextView mlatitude_text;
    private static TextView mlongitude_text;
    private static TextView mdistance_text;
    private static TextView morientation_text;
    private static double robotlat = 35.615992;
    private static double robotlong = -82.566879;
    private static LatLng newlatLng = new LatLng(robotlat, robotlong);
    private GoogleMap mMap;
    private Marker robotPosition;
    private boolean marker = false;
    private PolylineOptions robotFence;
    private static String latitude;
    private static String longitude;
    private static String orientation;
    private static String distance;
    private static Connection conn = GlobalClass.mModel.getConn();
    private static final String TAG = "PollService";

    private static final long POLL_INTERVAL_MS = TimeUnit.SECONDS.toMillis(1);

    /**
     * For this to work I have to poll the variables to see if they changed I also have to run a Poll service to grab the data from the database what I dont understand is why I made the mastermodel private and
     * now I can not reference them when I run my task
     *
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        //mlatitude_text=(TextView)findViewById(R.id.lat);
        //mlongitude_text=(TextView)findViewById(R.id.lon);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = new Intent(this, PollService.class);
        startService(intent);


        if (mMap != null) {
            //Marker robotPositionold = mMap.addMarker(new MarkerOptions().position(newlatLng).title("Robot"));
            moveMarker();
        }
        mLive_button = (Button) findViewById(R.id.button1);
        mLive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openLive_Activity();
            }
        });

        mControl_button = (Button) findViewById(R.id.button2);
        mControl_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openControl_Activity();
            }
        });

        mDash_button = (Button) findViewById(R.id.button3);
        mDash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDash_Activity();
            }
        });
    }

    public void openLive_Activity() {
        Intent intent = new Intent(this, Live_Activity.class);
        startActivity(intent);
    }

    public void openControl_Activity() {
        Intent intent = new Intent(this, Control_Activity.class);
        startActivity(intent);
    }

    public void openDash_Activity() {
        Intent intent = new Intent(this, Dash_Activity.class);
        startActivity(intent);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }


    /**
     * this updates the position of the marker on the map
     */
    public void moveMarker(double lat, double lon) {
        robotlat = Double.parseDouble(latitude);
        robotlong = Double.parseDouble(longitude);
        if (mMap != null) {
            newlatLng = new LatLng(robotlat, robotlong);
            if (marker != false) {
                robotPosition.setPosition(newlatLng);
            }//end of if the marker is already there
            robotPosition = mMap.addMarker(new MarkerOptions().position(newlatLng).title("Robot"));
        }//end of if map is created

    }

    /**
     * this updates the position of the marker by calling myTask
     */

    public void moveMarker() {
        robotlat = Double.parseDouble(latitude);
        robotlong = Double.parseDouble(longitude);
        if (mMap != null) {
            newlatLng = new LatLng(robotlat, robotlong);
            if (marker != false) {
                robotPosition.setPosition(newlatLng);
            }//end of if the marker is already there
            robotPosition = mMap.addMarker(new MarkerOptions().position(newlatLng).title("Robot"));
        }//end of if map is created

    }

    /**
     * this creates the map and geofence and places a marker in a default location until it connects to the datatbase
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng latLng = new LatLng(35.615965, -82.566009);
        //final  LatLng newlatLng = new LatLng(robotlat, robotlong);
        //final Marker robotPositionnew  = mMap.addMarker(new MarkerOptions().position(newlatLng).title("Robot"));
        robotPosition = mMap.addMarker(new MarkerOptions().position(latLng).title("Robot"));
        robotFence = new PolylineOptions()
                .add(
                        new LatLng(35.616759, -82.566081),
                        new LatLng(35.615992, -82.566879),
                        new LatLng(35.615243, -82.565706),
                        new LatLng(35.615999, -82.564857),
                        new LatLng(35.616759, -82.566081));
        Polyline polyline = mMap.addPolyline(robotFence.color(Color.RED));
        LatLng UNCA_Quad = new LatLng(35.615965, -82.566009);
        robotPosition.setPosition(UNCA_Quad);
        marker = true;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UNCA_Quad, 18));
        mMap.setMyLocationEnabled(true);
        newlatLng = new LatLng(35.615992, -82.566879);
    }

    /**
     * myTask gets all of the variables for the dashboard
     */
    public static class MyTask extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected String doInBackground(String... strings) {
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


                return res;

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            System.out.println("Data base selection success");


            return null;
        }

        protected void onPostExecute(String result) {
            morientation_text.setText(orientation);
            // GlobalClass.morientation_text=morientation_text;
            mlatitude_text.setText(latitude);
            //GlobalClass.mlatitude_text= mlatitude_text;
            mlongitude_text.setText(longitude);
            //GlobalClass.mlongitude_text=mlongitude_text;
            mdistance_text.setText(distance);
            //GlobalClass.mdistance_text= mdistance_text;
        }

    }//mytask

    /*


        public class DbPollService extends IntentService {

            public DbPollService(String name) {
                super(name);
            }

            @Override
            protected void onHandleIntent(@Nullable Intent intent) {
                MyTask myTask = new MyTask();
                myTask.execute();
                moveMarker();

            }
        }

     */
    public class PollService extends IntentService {


        public  Intent newIntent(Context context) {
            return new Intent(context, com.b.earthdrone.PollService.class);
        }

        public  void setServiceAlarm(Context context, boolean isOn) {
            Intent i = com.b.earthdrone.PollService.newIntent(context);
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

        public  boolean isServiceAlarmOn(Context context) {
            Intent i = com.b.earthdrone.PollService.newIntent(context);
            PendingIntent pi = PendingIntent
                    .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
            return pi != null;
        }

        public PollService() {
            super(TAG);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.i(TAG, "Recieved an intent" + intent);
            Map_Activity.MyTask myTask = new Map_Activity.MyTask();
            myTask.execute();


            //this is where I will do my code but dont know how


        }

    }
}
