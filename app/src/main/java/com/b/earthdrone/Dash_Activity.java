package com.b.earthdrone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Dash_Activity extends AppCompatActivity {
    private Button mLive_button;
    private Button mDash_button;
    private Button mControl_button;
    private Button mMap_button;
    private Button mconnection_button;
    private static boolean clicked;
    private TextView mlatitude_text;
    private TextView mlongitude_text;
    private TextView mdistance_text;
    private static TextView morientation_text;
    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    private String orientation = "";
    private String latitude="";
    private String longitude="";
    private String distance="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_view);
        mlatitude_text=(TextView)findViewById(R.id.lat);
        mlongitude_text=(TextView)findViewById(R.id.lon);
        mdistance_text=(TextView)findViewById(R.id.distance_text);
        morientation_text=(TextView)findViewById(R.id.orien);



        mMap_button = (Button) findViewById(R.id.button1);
        mMap_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    finish();

                }
            });

        mControl_button = (Button) findViewById(R.id.button2);
        mControl_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    finish();
                    openControl_Activity();
                }
            });

        mLive_button = (Button) findViewById(R.id.button3);
        mLive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {
                finish();
                openLive_Activity();
                }
            });

        mconnection_button = (Button) findViewById(R.id.connection_button);
        mconnection_button.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                mconnection_button.setBackgroundColor(getResources().getColor(R.color.green));
                mconnection_button.setText(getResources().getText(R.string.connected));
                final MyTask myTask = new MyTask();
                myTask.execute();


                    clicked = true;
                }
            });

        if (clicked == true) {
            mconnection_button.setBackgroundColor(getResources().getColor(R.color.green));
            mconnection_button.setText(getResources().getText(R.string.connected));
            MyTask myTask = new MyTask();
            myTask.execute();



            }


    }
    public void openControl_Activity() {
        Intent intent = new Intent(this, Control_Activity.class);
        startActivity(intent);
    }
    public void openDash_Activity() {
        Intent intent = new Intent(this, Dash_Activity.class);
        startActivity(intent);
    }

    public void openMap_Activity() {
        Intent intent = new Intent(this,Map_Activity.class );
        startActivity(intent);
    }
    public void openLive_Activity() {
        Intent intent = new Intent(this,Live_Activity.class );
        startActivity(intent);
    }

   private class MyTask extends AsyncTask<String,Void,String> {
        String res = "";
        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                try {
                    if(conn == null){
                        conn = DriverManager.getConnection(url,user,pass);
                        System.out.println("Database connection success");
                    }
                    else {
                        System.out.println("Database is connected");
                    }

                    Statement st1 = conn.createStatement();
                    ResultSet or = st1.executeQuery("select distinct Heading from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    or.next();
                    ResultSetMetaData rsmd1 = or.getMetaData();
                    orientation = or.getString(1).toString() ;

                    Statement st2 = conn.createStatement();
                    ResultSet lat = st2.executeQuery("select distinct Latitude from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    lat.next();
                    ResultSetMetaData rsmd2 = lat.getMetaData();
                    latitude = lat.getString(1).toString() ;

                    Statement st3 = conn.createStatement();
                    ResultSet lon = st3.executeQuery("select distinct Longitude from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    lon.next();
                    ResultSetMetaData rsmd3 = lon.getMetaData();
                    longitude = lon.getString(1).toString();

                    Statement st4 = conn.createStatement();
                    ResultSet dis = st4.executeQuery("select distinct Speed from Test Limit 1;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    dis.next();
                    ResultSetMetaData rsmd4 = or.getMetaData();
                    distance = dis.getString(1).toString();

                    res = orientation+latitude+longitude+distance;
                } catch (Exception e) {
                    e.printStackTrace();
                    res = e.toString();
                }

                StringBuilder sb= new StringBuilder();


                return res;

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            System.out.println("Data base selection success");


            return null;
        }

        protected void onPostExecute(String result) {
            morientation_text.setText(orientation);
            mlatitude_text.setText(latitude);
            mlongitude_text.setText(longitude);
            mdistance_text.setText(distance);
        }

    }//mytask




}