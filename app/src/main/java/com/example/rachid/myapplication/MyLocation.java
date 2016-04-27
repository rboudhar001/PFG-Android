package com.example.rachid.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rachid on 14/04/2016.
 */
public abstract class MyLocation {

    // Variables
    private static String TAG;
    private static Activity activity;
    private static int REQUEST_CHECK_SETTINGS;

    private static GoogleApiClient mGoogleApiClient;
    private static LocationManager locationManager;
    private static LocationListener locationListener;

    private static boolean obtainedLocation = false;

    private static ProgressDialog mProgressDialog;

    // Funciones
    public static void location_function(String T, Activity A, int R) {

        TAG = T;
        activity = A;
        REQUEST_CHECK_SETTINGS = R;

        Log.i(TAG, "ENTRO A M:location_function:0");

        mGoogleApiClient = new GoogleApiClient
                .Builder(activity)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        locationChecker(mGoogleApiClient, activity);
    }

    /**
     * Prompt user to enable GPS and Location Services
     */
    public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {

        Log.i(TAG, "ENTRO A M:locationChecker:0");

        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //builder.setNeedBle(true); //Bluetooth
        builder.setAlwaysShow(true); //GPS

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        Log.i(TAG, "ENTRO A M:locationChecker:1");

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                Log.i(TAG, "ENTRO A M:locationChecker:2");

                final Status status = result.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        Log.i(TAG, "ENTRO A M:locationChecker:3");

                        showProgressDialog();
                        getLocation();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user a dialog.

                        Log.i(TAG, "ENTRO A M:locationChecker:4");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        Log.i(TAG, "ENTRO A M:locationChecker:5");

                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    public static void getLocation() {

        Log.i(TAG, "ENTRO A M:getLocation:0");

        locationManager = (LocationManager) activity.getSystemService(activity.getApplicationContext().LOCATION_SERVICE);

        if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) { // Si esta activo el GPS

            Log.i(TAG, "ENTRO A M:getLocation:1");

            // Define a listener that responds to location updates
            locationListener = new LocationListener() {

                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.

                    Log.i(TAG, "ENTRO A M:getLocation:2");

                    try {
                        Geocoder geoCoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
                        List<Address> list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        if ((list != null) ){
                            if (list.size() > 0) {
                                Address address = list.get(0);
                                String city = address.getLocality();

                                Log.i(TAG, "ENTRO A M:getLocation:3");

                                if (city == null) {
                                    Log.i(TAG, "ENTRO A M:getLocation:city es NULL");
                                }
                                else {
                                    Log.i(TAG, "ENTRO A M:getLocation:city NULL STRING?: " + city);
                                }

                                if (city != null) {
                                    Log.i(TAG, "ENTRO A M:getLocation:4");

                                    if (MyState.getUser().getLocation() == null) {
                                        Log.i(TAG, "ENTRO A M:getLocation:getLocation es NULL");
                                    }
                                    else {
                                        Log.i(TAG, "ENTRO A M:getLocation:getLocation es NULL STRING?: " + MyState.getUser().getLocation());
                                    }

                                    if(!city.equals(MyState.getUser().getLocation())) {
                                        Log.i(TAG, "ENTRO A M:getLocation:5");
                                        MyDatabase.insertLocation(TAG, activity, city);

                                        MyState.getUser().setLocation(city);
                                        // --------------------------------------------------------------------------------
                                        if (MyState.getExistsLocation()) { // Si ya existia la localizacion actualizamos el Drawer navigation
                                            if (MainActivity.activity != null) {
                                                Log.i(TAG, "ENTRO A MyLocation:EditLocation:0");
                                                MainActivity.myMenu.updateLocation();
                                            }
                                            if (EventsActivity.activity != null){
                                                Log.i(TAG, "ENTRO A MyLocation:EditLocation:1");
                                                EventsActivity.myMenu.updateLocation();
                                            }
                                        }
                                        // --------------------------------------------------------------------------------
                                        MyState.setExistsLocation(true);
                                    }
                                    obtainedLocation = true;
                                }

                                if (mGoogleApiClient != null) {
                                    mGoogleApiClient.disconnect();
                                }

                                PackageManager packageManager = activity.getApplicationContext().getPackageManager();
                                if (packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                                        activity.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                                    locationManager.removeUpdates(locationListener);
                                }

                                hideProgressDialog();
                                if (obtainedLocation) {
                                    if (EventsActivity.activity != null) {
                                        activity.finish();
                                    }
                                    else {
                                        MainActivity.activity.finish();
                                        activity.startActivity(new Intent(activity, EventsActivity.class));
                                        activity.finish();
                                    }
                                } else {
                                    Toast.makeText(activity.getBaseContext(), "Impossible to detect the location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.i(TAG, "ENTRO A M:getLocation:6");
                }

                public void onProviderEnabled(String provider) {
                    Log.i(TAG, "ENTRO A M:getLocation:7");
                }

                public void onProviderDisabled(String provider) {
                    Log.i(TAG, "ENTRO A M:getLocation:8");
                }
            };

            String locationProvider;
            if ( isNetworkConnected() ) { // Si la red esta activa
                // Use NETWORK location data:
                locationProvider = LocationManager.NETWORK_PROVIDER;
                Log.i(TAG, "ENTRO A M:BUSCANDO_POR_RED");

                PackageManager packageManager = activity.getApplicationContext().getPackageManager();
                if (packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                        activity.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "ENTRO A M:getLocation:10");
                    locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
                }
            } else{
                hideProgressDialog();
                Toast.makeText(activity.getBaseContext(), "Not network connected, impossible to detect the location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static void delete(String T, Activity A){

        MyDatabase.deleteLocation(T, A);

        MyState.getUser().setLocation(null);
        MyState.setExistsLocation(false);
    }

    public static boolean isObtainedLocation() {
        return obtainedLocation;
    }

    public static void disconnectGoogleApiClient(){
        mGoogleApiClient.disconnect();
    }

    private static void showProgressDialog() {
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage(activity.getString(R.string.loading));
        //mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        //mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //mProgressDialog.setContentView(R.layout.activity_location);

        mProgressDialog.show();
    }

    private static void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
}
