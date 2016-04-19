package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Rachid on 16/04/2016.
 */
public class LocationActivity extends AppCompatActivity {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "LocationActivity";
    private final Activity activity = this;

    //AÑADIDO: LOCATION
    // -----------------------------------------------------------------------------------------
    private Button buttonLocation;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //AÑADIDO: LOCATION
        // ----------------------------------------------------------------------------------------
        buttonLocation = (Button) findViewById(R.id.location_button_location);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "ENTRO A M:buttonGeolocation:0");
                MyLocation.location_function(TAG, activity, REQUEST_CHECK_SETTINGS);
            }
        });
        // ----------------------------------------------------------------------------------------

        //AÑADIDO: LIST OF CITYS
        // ----------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO: LOCATION
    // ----------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A M:onActivityResult:0");

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:

                Log.i(TAG, "ENTRO A M:onActivityResult:1");

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        Log.i(TAG, "ENTRO A M:onActivityResult:2");
                        MyLocation.location_function(TAG, activity, REQUEST_CHECK_SETTINGS);
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        MyLocation.disconnectGoogleApiClient();
                        break;
                    default:
                        MyLocation.disconnectGoogleApiClient();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //MyLocation.disconnectGoogleApiClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MyLocation.disconnectGoogleApiClient();
    }
    // ----------------------------------------------------------------------------------------
}
