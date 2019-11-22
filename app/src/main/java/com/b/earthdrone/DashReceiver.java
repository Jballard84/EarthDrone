package com.b.earthdrone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DashReceiver extends BroadcastReceiver    {
        private static final String TAG = "StartupReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Received broadcast intent: " + intent.getAction());
            if (Dash_Activity.clicked == true) {
                Map_Activity.moveMarker();
                Dash_Activity.morientation_text.setText(GlobalClass.mModel.getOrientation());
                Dash_Activity.mlatitude_text.setText(String.valueOf(GlobalClass.mModel.getLatitude()));
                Dash_Activity.mlongitude_text.setText(String.valueOf(GlobalClass.mModel.getLongitude()));
                Dash_Activity.mdistance_text.setText(GlobalClass.mModel.getDistance());

            }
        }
}
