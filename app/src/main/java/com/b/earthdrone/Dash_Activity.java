package com.b.earthdrone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Dash_Activity extends AppCompatActivity {
    private Button mLive_button;
    private Button mDash_button;
    private Button mControl_button;
    private Button mMap_button;
    private Button mconnection_button;
    private static boolean clicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_view);
        mMap_button= (Button) findViewById(R.id.button1);
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

        mconnection_button = (Button)findViewById(R.id.connection_button);
        mconnection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mconnection_button.setBackgroundColor(getResources().getColor(R.color.green));
                mconnection_button.setText(getResources().getText(R.string.connected));
                clicked = true;
            }
        });

        if(clicked == true){
            mconnection_button.setBackgroundColor(getResources().getColor(R.color.green));
            mconnection_button.setText(getResources().getText(R.string.connected));
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



}