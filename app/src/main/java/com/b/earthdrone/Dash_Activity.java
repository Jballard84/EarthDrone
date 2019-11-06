package com.b.earthdrone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    private TextView morientation_text;
    private static final String url = "jdbc:mariadb://10.123.21.91:3306/myDB";
    private static final String user = "BallardPi";
    private static final String pass = "BallardPi";


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
        String ori="";

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
                try {
                    Connection conn = DriverManager.getConnection(url,user,pass);
                    System.out.println("Database connection success");

                    String orientation = "Database Connection Successful\n";
                    String latitude="";
                    String longitude="";
                    String distance="";
                    Statement st1 = conn.createStatement();
                    ResultSet or = st1.executeQuery("select distinct Heading from Test;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    ResultSetMetaData rsmd1 = or.getMetaData();
                    orientation = or.getString(1).toString() + ",";

                    Statement st2 = conn.createStatement();
                    ResultSet lat = st2.executeQuery("select distinct Heading from Test;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    ResultSetMetaData rsmd2 = lat.getMetaData();
                    latitude = or.getString(1).toString() + ",";

                    Statement st3 = conn.createStatement();
                    ResultSet lon = st3.executeQuery("select distinct Heading from Test;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    ResultSetMetaData rsmd3 = lon.getMetaData();
                    longitude = or.getString(1).toString() + ",";

                    Statement st4 = conn.createStatement();
                    ResultSet dis = st4.executeQuery("select distinct Heading from Test;");//pulls the value that is saved in the heading column which is then associated to the orientation text view
                    ResultSetMetaData rsmd4 = or.getMetaData();
                    distance = dis.getString(1).toString() + ",";

                    //while (or.next()) {
                      //  orientation += or.getString(1).toString() + "\n";
                    //}
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
            int size_of_string = result.length();
            String temp ="";
            String orientation = "";
            String latitude="";
            String longitude="";
            String distance="";
            for (int i=0; i<size_of_string;i++){

                if(temp == null){// check to see if it is null first
                    temp=String.valueOf(result.charAt(i));
                }
                else{// now search threw the string till you find a ,
                    if (result.charAt(i)==','){

                        if (orientation==null) {
                            orientation = temp;
                            temp = "";
                        } //found orientaion now need to find latitude
                        else if(latitude==null){
                            latitude = temp;
                            temp="";
                        } //found latitude now need to find longitude
                        else if(longitude==null){
                            longitude=temp;
                            temp="";
                        }// found longitude now need to find distance
                        else{
                            distance= temp;
                        }// end of the string and there should not be anything left
                    }// end of if the character at the index is a coma
                    else{
                        temp = temp+String.valueOf(result.charAt(i));
                    }// the character is not a coma so addd to the string
                }

                // now search threw the string till you find a ,

            }

            morientation_text.setText(orientation);
            mlatitude_text.setText(latitude);
            mlongitude_text.setText(longitude);
            mdistance_text.setText(distance);


        }

    }//mytask

}