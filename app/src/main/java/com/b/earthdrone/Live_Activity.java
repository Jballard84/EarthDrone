package com.b.earthdrone;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Live_Activity extends AppCompatActivity {
    private Button mMap_button;
    private Button mControl_button;
    private Button mDash_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_view);
        mControl_button = (Button) findViewById(R.id.Control_button);
        mControl_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.control_view);
            }
        });

        mMap_button = (Button) findViewById(R.id.Live_button);
        mMap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.map_view);
            }
        });


        mDash_button = (Button) findViewById(R.id.Live_button);
        mDash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.dash_view);
            }
        });
    }


}