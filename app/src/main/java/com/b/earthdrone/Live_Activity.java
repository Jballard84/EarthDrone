package com.b.earthdrone;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.MediaController;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URISyntaxException;

public class Live_Activity extends AppCompatActivity {
    private Button mLive_button;
    private Button mDash_button;
    private Button mControl_button;
    private Button mMap_button;
    private VideoView mView;
    private VideoView mvideoView;
    private WebView mWebView;

    public static final String ROBOT_VIEW = "com.b.earthdrone.SHOW_NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_view);


        mvideoView=(VideoView) findViewById(R.id.videoView) ;

       // mWebView=(WebView) findViewById(R.id.WebView);
        //mWebView.getSettings().setLoadWithOverviewMode(true);
        //mWebView.getSettings().setUseWideViewPort(true);
        //mWebView.loadUrl( "http://10.123.21.91:8081");
        Uri vidUri = Uri.parse("http://10.123.21.91:8081");
        mvideoView.setVideoURI(vidUri);
        //MediaController vidControl = new MediaController(this);
        //vidControl.setAnchorView(mView);
        //mView.setMediaController(vidControl);
        mvideoView.start();
        //Picasso.get().load("http://10.123.21.91:8081").into(imageView);
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





    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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