package com.b.earthdrone;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.VideoView;
import android.widget.MediaController;
import android.net.Uri;
import java.net.URISyntaxException;

public class Live_Activity extends AppCompatActivity {
    private Button mLive_button;
    private Button mDash_button;
    private Button mControl_button;
    private Button mMap_button;
    private VideoView mView;
    private WebView mWebView;

    public static final String ROBOT_VIEW = "com.b.earthdrone.SHOW_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_view);
        mWebView=(WebView) findViewById(R.id.WebView);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.loadUrl( "http://10.123.21.91:8081");
        //Uri vidUri = Uri.parse(vidAddress);
        //mView.setVideoURI(vidUri);
        //MediaController vidControl = new MediaController(this);
        //vidControl.setAnchorView(mView);
        //mView.setMediaController(vidControl);
       // mView.start();

        mMap_button = (Button) findViewById(R.id.button1);
        mMap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                //do I need to kill the poll service
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


        mDash_button = (Button) findViewById(R.id.button3);
        mDash_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {
                finish();
                openDash_Activity();
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