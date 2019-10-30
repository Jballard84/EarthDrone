package com.b.earthdrone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map_Activity extends AppCompatActivity implements OnMapReadyCallback {

private Button mLive_button;
private Button mDash_button;
private Button mControl_button;
private Button mMap_button;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        Intent intent = new Intent(this,Live_Activity.class );
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // public Location getMyLocation
        // Add a marker in Sydney and move the camera
        LatLng UNCA_Quad = new LatLng(35.616314, -82.56732);
        mMap.addMarker(new MarkerOptions().position(UNCA_Quad).title("Marker in Asheville quad"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(UNCA_Quad));

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.616314,-82.56732), 10));
        new CountDownTimer(3000, 1000) {
            public void onFinish() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.616314,-82.56732), 10));

                // Execute your code here
            }


            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
        new CountDownTimer(4000, 1000) {
            public void onFinish() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.616314,-82.56732), 80));
                // Execute your code here
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }


}
