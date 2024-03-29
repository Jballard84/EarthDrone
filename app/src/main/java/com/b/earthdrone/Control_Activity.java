package com.b.earthdrone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Control_Activity extends AppCompatActivity {
    private Button mLive_button;
    private Button mDash_button;
    private Button mControl_button;
    private Button mMap_button;

    private Button mLeftButton;
    private Button mRightButton;
    private Button mUpButton;
    private Button mDownButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLeftButton = (Button) findViewById(R.id.LEFT_ARROW);
        mRightButton = (Button) findViewById(R.id.RIGHT_ARROW);
        mUpButton = (Button) findViewById(R.id.UP_ARROW);
        mDownButton = (Button) findViewById(R.id.DOWN_ARROW);
        setContentView(R.layout.control_view);
        mMap_button = (Button) findViewById(R.id.button1);
        mMap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                finish();

            }
        });

        mDash_button = (Button) findViewById(R.id.button2);
        mDash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                finish();
                openDash_Activity();
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
