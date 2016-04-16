package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

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

                /*
                Log.i(TAG, "ENTRO A M:buttonGeolocation:1");

                if (MyLocation.isObtainedLocation()) {

                    Log.i(TAG, "ENTRO A M:buttonGeolocation:2");

                    if (MainActivity.f != null) {
                        MainActivity.f.finish();
                    }
                    startActivity(new Intent(LocationActivity.this, EventsActivity.class));
                    finish();
                }
                */
            }
        });
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

                        /*
                        Log.i(TAG, "ENTRO A M:onActivityResult:3");

                        if (MyLocation.isObtainedLocation()) {

                            Log.i(TAG, "ENTRO A M:onActivityResult:4");

                            startActivity(new Intent(LocationActivity.this, EventsActivity.class));
                            finish();
                        }
                        */
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
