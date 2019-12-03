package com.b.earthdrone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;

import static com.b.earthdrone.Map_Activity.ROBOT_POSTION_MOVE_MARKER;

public class Dash_Activity extends AppCompatActivity  {
    private Button mLive_button;
    private Button mDash_button;
    private Button mControl_button;
    private Button mMap_button;
    private Button mconnection_button;
    public static boolean clicked;
    public static TextView mlatitude_text;
    public  static TextView mlongitude_text;
    public static  TextView mdistance_text;
    public  static TextView morientation_text;
    public static TextView mbattery_text;
    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";
    private Handler mHandler;
    private static  BroadcastReceiver dashReceiver = new DashReceiver();
    /**
     * Need to find out if I need a poll service in this activity if I have one running in map activity
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_view);

        mbattery_text=(TextView)findViewById(R.id.mbattery_text);
        mlatitude_text=(TextView)findViewById(R.id.lat);
        mlongitude_text=(TextView)findViewById(R.id.lon);
        mdistance_text=(TextView)findViewById(R.id.distance_text);
        morientation_text=(TextView)findViewById(R.id.orien);

        mbattery_text.setText("");
        morientation_text.setText("");
        mlatitude_text.setText("");
        mlongitude_text.setText("");
        mdistance_text.setText("");
        //Intent intent = new Intent(this, LiveViewPollService.class);
        //startService(intent);
        //LiveViewPollService.setServiceAlarm(this,true);{
            //moveMarker();
          //  startService(intent);
        //}

        //BroadcastReceiver dashReceiver = new DashReceiver();
        //IntentFilter dashfilter = new IntentFilter(ROBOT_POSTION_MOVE_MARKER);
        //this.registerReceiver(dashReceiver,dashfilter);


       // dashReceiver = new DashReceiver();
        IntentFilter dashfilter = new IntentFilter();
        dashfilter.addAction(ROBOT_POSTION_MOVE_MARKER);// add any actions you want
        registerReceiver(dashReceiver , dashfilter);

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
                clicked = true;
                mbattery_text.setText(String.valueOf(GlobalClass.mModel.getBattery()) + "%");
                morientation_text.setText(GlobalClass.mModel.getOrientation() + "°");
                mlatitude_text.setText(String.valueOf(GlobalClass.mModel.getLatitude()));
                mlongitude_text.setText(String.valueOf(GlobalClass.mModel.getLongitude()));
                mdistance_text.setText(GlobalClass.mModel.getDistance());
                //morientation_text.postDelayed(mUpdate, 0);
                //mlatitude_text.postDelayed(mUpdate, 0);
               // mlongitude_text.postDelayed(mUpdate, 0);
               // mdistance_text.postDelayed(mUpdate, 0);


                }
            });

        if (clicked == true) {
            mconnection_button.setBackgroundColor(getResources().getColor(R.color.green));
            mconnection_button.setText(getResources().getText(R.string.connected));

            mbattery_text.setText(String.valueOf(GlobalClass.mModel.getBattery()) + "%");
            morientation_text.setText(GlobalClass.mModel.getOrientation() + "°");
            mlatitude_text.setText(String.valueOf(GlobalClass.mModel.getLatitude()));
            mlongitude_text.setText(String.valueOf(GlobalClass.mModel.getLongitude()));
            mdistance_text.setText(GlobalClass.mModel.getDistance());
            //morientation_text.postDelayed(mUpdate, 0);
            //mlatitude_text.postDelayed(mUpdate, 0);
            //mlongitude_text.postDelayed(mUpdate, 0);
            //mdistance_text.postDelayed(mUpdate, 0);


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

    public void onPause() {
        super.onPause();

        unregisterReceiver(dashReceiver);
    }
    public void onResume() {
        super.onResume();

        IntentFilter dashfilter = new IntentFilter();
        dashfilter.addAction(ROBOT_POSTION_MOVE_MARKER);
        registerReceiver(dashReceiver,dashfilter);
    }

/*
    private Runnable mUpdate = new Runnable() {
        public void run() {
            morientation_text.setText(GlobalClass.mModel.getOrientation());
            mlatitude_text.setText(String.valueOf(GlobalClass.mModel.getLatitude()));
            mlongitude_text.setText(String.valueOf(GlobalClass.mModel.getLongitude()));
            mdistance_text.setText(GlobalClass.mModel.getDistance());
            mdistance_text.postDelayed(this, 1000);

        }
    };

 */






}