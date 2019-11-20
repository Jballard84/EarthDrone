package com.b.earthdrone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.sql.SQLNonTransientConnectionException;
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
    private static final String url = "jdbc:mariadb://10.123.21.91:3306/EarthDrone";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    private static TextView mlatitude_text;
    private static TextView mlongitude_text;
    private static TextView mdistance_text;
    private static TextView morientation_text;
    private static double robotlat = 35.615992;
    private static double robotlong = -82.566879;
    private static LatLng newlatLng = new LatLng(robotlat, robotlong);
    private static GoogleMap mMap;
    private static Marker robotPosition;
    private static boolean marker = false;
    private PolylineOptions robotFence;
    private static String latitude;
    private static String longitude;
    private static String orientation;
    private static String distance;
    private static Connection conn = GlobalClass.mModel.getConn();
    private static final String TAG = "PollService";
    public static final String ROBOT_POSTION_MOVE_MARKER = "com.b.earthdrone.SHOW_NOTIFICATION";
    private static final long POLL_INTERVAL_MS = TimeUnit.SECONDS.toMillis(1);
    private static  BroadcastReceiver MapReceiver = new StartupReceiver();
    static final LatLng geofenceCorner1 = new LatLng(35.616759, -82.566081);
    static final LatLng geofenceCorner2 = new LatLng(35.615992, -82.566879);
    static final LatLng geofenceCorner3 = new LatLng(35.615243, -82.565706);
    static final LatLng geofenceCorner4 = new LatLng(35.615999, -82.564857);

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
        mapFragment.getMapAsync(this);// want to put a flag so I know when the map is ready

        Intent intent = new Intent(this, PollService.class);
        startService(intent);

        //BroadcastReceiver br = new StartupReceiver();
        IntentFilter mapfilter = new IntentFilter(ROBOT_POSTION_MOVE_MARKER);
        this.registerReceiver(MapReceiver,mapfilter);
        PollService.setServiceAlarm(this,true);{
            //moveMarker();
            startService(intent);
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
    private Runnable mUpdate = new Runnable() {
        public void run() {

            moveMarker();

        }
    };
    public void onPause() {
        super.onPause();

        unregisterReceiver(MapReceiver);
    }
    public void onResume() {
        super.onResume();

        IntentFilter mapfilter = new IntentFilter();
        mapfilter.addAction(ROBOT_POSTION_MOVE_MARKER);
        registerReceiver(MapReceiver,mapfilter);
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
    private static void moveMarker(double lat, double lon) {
        robotlat = lat;
        robotlong = lon;
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

    public static void moveMarker() {
        if (latitude != null) {
            robotlat = GlobalClass.mModel.getLatitude();
            robotlong = GlobalClass.mModel.getLongitude();

        }
        if (mMap != null) {

            newlatLng = new LatLng(robotlat, robotlong);

            System.out.printf("%f %f", robotlat, robotlong);
            robotPosition.setPosition(newlatLng);
            //end of if the marker is already there
            //robotPosition.remove();
            //robotPosition = mMap.addMarker(new MarkerOptions().position(newlatLng).title("Robot"));

            System.out.println("Database is not connected but map is created");

            }//end of if map is created

        else{
            System.out.println("Database is not connected and map is not created");
        }


    }

    /**
     * this creates the map and geofence and places a marker in a default location until it connects to the datatbase
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng robotMarkerPosition = new LatLng(35.615965, -82.566009);     //this is position of the robots marker
        //final  LatLng newlatLng = new LatLng(robotlat, robotlong);
        //final Marker robotPositionnew  = mMap.addMarker(new MarkerOptions().position(newlatLng).title("Robot"));

        //to change how big the robots image is, change these next 2 variables
        int imageWidth = 50;
        int imageHeight = 50;

        BitmapDrawable drawableImage = (BitmapDrawable)getResources().getDrawable(R.drawable.walle);
        Bitmap b = drawableImage.getBitmap();
        Bitmap robotMarkerImage = Bitmap.createScaledBitmap(b,imageWidth,imageHeight,false);
        robotPosition = mMap.addMarker(
                new MarkerOptions()
                .position(robotMarkerPosition)
                .title("Robot")
                .icon(BitmapDescriptorFactory.fromBitmap(robotMarkerImage))
                );
        //robotPosition.setIcon(BitmapDescriptorFactory.fromResource(R.id.robotIcon));
        robotFence = new PolylineOptions()
                .add(
                        geofenceCorner1,
                        geofenceCorner2,
                        geofenceCorner3,
                        geofenceCorner4,
                        geofenceCorner1);
        Polyline polyline = mMap.addPolyline(robotFence.color(Color.RED));
        LatLng UNCA_Quad = new LatLng(35.615965, -82.566009);
        robotPosition.setPosition(UNCA_Quad);
        marker = true;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UNCA_Quad, 18));
        mMap.setMyLocationEnabled(true);   //COMMENTED THIS OUT TO WORK ON PHONE
        newlatLng = new LatLng(35.615992, -82.566879);

        Marker user_marker1 = mMap.addMarker(new MarkerOptions()
                .position(geofenceCorner1)
                .title("Corner 1")
                .draggable(true));
        user_marker1.setPosition(geofenceCorner1);
        Marker user_marker2 = mMap.addMarker(new MarkerOptions()
                .position(geofenceCorner2)
                .title("Corner 2")
                .draggable(true));
        user_marker2.setPosition(geofenceCorner2);
        Marker user_marker3 = mMap.addMarker(new MarkerOptions()
                .position(geofenceCorner3)
                .title("Corner 3")
                .draggable(true));
        user_marker3.setPosition(geofenceCorner3);
        Marker user_marker4 = mMap.addMarker(new MarkerOptions()
                .position(geofenceCorner4)
                .title("Corner 4")
                .draggable(true));
        user_marker4.setPosition(geofenceCorner4);

        LatLng getPositionMarker1 = user_marker1.getPosition();
        LatLng getPositionMarker2 = user_marker2.getPosition();
        LatLng getPositionMarker3 = user_marker3.getPosition();
        LatLng getPositionMarker4 = user_marker4.getPosition();
        /*
        System.out.println("Marker1 Position: " + getPositionMarker1 + "\n");
        System.out.println("Marker2 Position: " + getPositionMarker2 + "\n");
        System.out.println("Marker3 Position: " + getPositionMarker3 + "\n");
        System.out.println("Marker4 Position: " + getPositionMarker4 + "\n");
        */

    }



    public static class PollService extends IntentService {


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

        public boolean isServiceAlarmOn(Context context) {
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
            //this is where I think I should put my code but dont know for sure
            Log.i(TAG, "Recieved an intent" + intent);
            String res = "";


               try {
                   Class.forName("org.mariadb.jdbc.Driver");
                   try {
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
                           ResultSet lat = st2.executeQuery("select distinct Lat from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                           lat.next();
                           ResultSetMetaData rsmd2 = lat.getMetaData();
                           latitude = lat.getString(1);

                           GlobalClass.mModel.setLatitude(Double.valueOf(latitude));

                           Statement st3 = conn.createStatement();
                           ResultSet lon = st3.executeQuery("select distinct Lon from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                           lon.next();
                           ResultSetMetaData rsmd3 = lon.getMetaData();
                           longitude = lon.getString(1).toString();
                           GlobalClass.mModel.setLongitude(Double.valueOf(longitude));

                           Statement st4 = conn.createStatement();
                           ResultSet dis = st4.executeQuery("select distinct Battery from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                           dis.next();
                           ResultSetMetaData rsmd4 = or.getMetaData();
                           distance = dis.getString(1).toString();
                           GlobalClass.mModel.setDistance(distance);
                       }catch (SQLNonTransientConnectionException e) {
                           e.printStackTrace();
                           System.out.println("Data base connection was not a success");
                       }

                   } catch (Exception e) {
                       e.printStackTrace();

                   }

                   StringBuilder sb = new StringBuilder();


               } catch (ClassNotFoundException ex) {
                   ex.printStackTrace();
               }



           // System.out.println("Data base selection success");

           sendBroadcast(new Intent(ROBOT_POSTION_MOVE_MARKER));//this send a broadcast to the system and when it gets the message it moves the marker


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
}
/*


     // myTask gets all of the variables for the dashboard

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

 */