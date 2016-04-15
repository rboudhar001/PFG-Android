package com.example.rachid.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

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

    private static State state = new State();

    private static GoogleApiClient mGoogleApiClient;
    private static LocationManager locationManager;
    private static LocationListener locationListener;
    private static Location location;

    private static boolean obtainedLocation = false;

    // Funciones
    public static void location_function(String T, Activity A, int R) {

        TAG = T;
        activity = A;
        REQUEST_CHECK_SETTINGS = R;

        Log.i(TAG, "ENTRO A M:location_function:0");

        locationManager = (LocationManager) activity.getSystemService(activity.getApplicationContext().LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // Si el GPS esta activo
            getLocation();
        } else { // Si no, solicitar activar GPS
            mGoogleApiClient = new GoogleApiClient
                    .Builder(activity)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();

            locationChecker(mGoogleApiClient, activity);
        }
    }

    /**
     * Prompt user to enable GPS and Location Services
     * @param mGoogleApiClient
     * @param activity
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
                final LocationSettingsStates state = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        Log.i(TAG, "ENTRO A M:locationChecker:3");

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

                        if ((list != null) & (list.size() > 0)) {
                            Address address = list.get(0);
                            String city = address.getLocality();

                            Log.i(TAG, "ENTRO A M:getLocation:3");

                            if ( (city != null) & (city != state.getUser().getLocation()) ) {

                                Log.i(TAG, "ENTRO A M:getLocation:4");
                                MyDatabase.insertLocation(TAG, activity, city);

                                state.getUser().setLocation(city);
                                state.setExistsLocation(true);
                            }

                            if (mGoogleApiClient != null) {
                                mGoogleApiClient.disconnect();
                            }

                            PackageManager packageManager = activity.getApplicationContext().getPackageManager();
                            if (packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                                    activity.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                                locationManager.removeUpdates(locationListener);
                            }

                            activity.startActivity(new Intent(activity, EventsActivity.class));
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
            if ( locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) { // Si la red esta activa
                // Use NETWORK location data:
                locationProvider = LocationManager.NETWORK_PROVIDER;
            } else{
                // Use GPS location data:
                locationProvider = LocationManager.GPS_PROVIDER;
            }

            Log.i(TAG, "ENTRO A M:getLocation:9");

            PackageManager packageManager = activity.getApplicationContext().getPackageManager();
            if (packageManager.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                    activity.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "ENTRO A M:getLocation:10");

                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
            }

            location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {

                try {
                    Geocoder geoCoder = new Geocoder(activity.getApplicationContext(), Locale.getDefault());
                    List<Address> list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if ((list != null) & (list.size() > 0)) {
                        Address address = list.get(0);
                        String city = address.getLocality();

                        Log.i(TAG, "ENTRO A M:getLocation:11");

                        if ( city != null ) {

                            Log.i(TAG, "ENTRO A M:getLocation:12");

                            MyDatabase.insertLocation(TAG, activity, city);

                            state.getUser().setLocation(city);
                            state.setExistsLocation(true);

                            obtainedLocation = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (mGoogleApiClient != null) {
                    mGoogleApiClient.disconnect();
                }

                locationManager.removeUpdates(locationListener);
                activity.startActivity(new Intent(activity, EventsActivity.class));
            }
        }
    }

    public static void delete(String T, Activity A){

        MyDatabase.deleteLocation(T, A);

        state.getUser().setLocation(null);
        state.setExistsLocation(false);
    }

    public static boolean isObtainedLocation() {
        return obtainedLocation;
    }

    public static void disconnectGoogleApiClient(){
        mGoogleApiClient.disconnect();
    }
}
