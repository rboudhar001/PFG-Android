package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Rachid on 16/04/2016.
 */
public class LocationActivity extends AppCompatActivity {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "LocationActivity";
    private final Activity activity = this;

    private ListView list;

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

        //AÑADIDO: LIST OF COUNTRIES
        // ----------------------------------------------------------------------------------------
        list = (ListView) findViewById(R.id.location_listView_country);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();

        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( (country.length() > 0) && (!countries.contains(country)) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        list.setAdapter(adapter);
        // ----------------------------------------------------------------------------------------

        //AÑADIDO: LISTENER LIST-VIEW
        // ----------------------------------------------------------------------------------------
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                String city = ((TextView) arg1).getText().toString();
                //Toast.makeText(getBaseContext(), "Item: " + city, Toast.LENGTH_LONG).show();

                MyDatabase.insertLocation(TAG, activity, city);

                MyState.getUser().setLocation(city);
                // --------------------------------------------------------------------------------
                if (MyState.getExistsLocation()) { // Si ya existia la localizacion actualizamos el Drawer navigation
                    if (MainActivity.activity != null) {
                        MainActivity.myMenu.updateLocation();
                    }
                    if (EventsActivity.activity != null){
                        EventsActivity.myMenu.updateLocation();
                    }
                }
                // --------------------------------------------------------------------------------
                MyState.setExistsLocation(true);

                finish();
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
