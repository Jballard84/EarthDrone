package com.b.earthdrone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Map_Activity extends AppCompatActivity {

private Button mLive_button;
private Button mDash_button;
private Button mControl_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        mLive_button = (Button) findViewById(R.id.Live_button);
        mLive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.live_view);
            }
        });

        mControl_button = (Button) findViewById(R.id.Control_button);
        mControl_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.control_view);
            }
        });

        mDash_button = (Button) findViewById(R.id.Dash_button);
        mDash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.dash_view);
            }
        });
    }


}
