package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

// Location
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.LocationSettingsStates;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
// ----------------------------------------------------------------------------------------

// AÑADIDOS: GOOGLE
// ----------------------------------------------------------------------------------------
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.ResultCallback;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
// ----------------------------------------------------------------------------------------


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "MainActivity";
    private final Activity activity = this;

    public static Activity f;

    //AÑADIDO: STATE
    // -----------------------------------------------------------------------------------------
    private State state = new State();
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private NavigationView navHeader;
    private View navViewHeader;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textUserLocation;
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: LOCATION
    // -----------------------------------------------------------------------------------------
    private Button buttonGeolocation;
    private ImageButton imageButtonUpdateLocation;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        f = this;

        //AÑADIDO: LOGIN
        // ----------------------------------------------------------------------------------------
        Log.i(TAG, "ENTRO A M:getLoged: " + state.getLoged());

        if (!state.getLoged()) {
            //Abrimos la base de datos
            DBActivity mDB_Activity = new DBActivity(this, null);
            SQLiteDatabase db = mDB_Activity.getReadableDatabase();
            if (db != null) {
                Cursor c = db.rawQuery("SELECT * FROM Users", null);
                if (c.moveToFirst()) {
                    if (c.getString(1) != null) {
                        state.setLoged(true); // Usuario logeado
                        if (c.getString(7) != null) {
                            state.setExistsLocation(true); // Usuario con localizacion
                        }
                        state.setUser(new User(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7)));

                        Log.i(TAG, "ENTRO A M:getLoged:EMAIL: " + state.getUser().getEmail() + ", LOCATION: " + state.getUser().getLocation());
                    }
                }
                c.close();
                db.close();
            }
        }
        // ----------------------------------------------------------------------------------------

        // OPEN MainActivity OR EventsActivity
        // ----------------------------------------------------------------------------------------
        if (state.getExistsLocation()) { // Si existe la localizacion pasar a la ventana de eventos
            startActivity(new Intent(MainActivity.this, EventsActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Cambiar la animacion
            finish();
        }
        setContentView(R.layout.activity_main);
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN or NAV_HEADER_LOGIN
        // ----------------------------------------------------------------------------------------
        if (state.getLoged()) { // Si el usuario esta con sesion iniciada

            navHeader = (NavigationView) findViewById(R.id.nav_view);
            navViewHeader = navHeader.inflateHeaderView(R.layout.nav_header_login);

            circleImageProfile = (CircleImageView) navViewHeader.findViewById(R.id.circle_image_profile);
            Picasso.with(getApplicationContext()).load(state.getUser().getUrlImageProfile()).into(circleImageProfile);

            textUserName = (TextView) navViewHeader.findViewById(R.id.text_user_name);
            textUserName.setText(state.getUser().getName());

            textUserEmail = (TextView) navViewHeader.findViewById(R.id.text_user_email);
            textUserEmail.setText(state.getUser().getEmail());

            textUserLocation = (TextView) navViewHeader.findViewById(R.id.text_user_location);
            textUserLocation.setText("SIN LOCALIZACIÓN");

            // AÑADIDO: CLICK EVENT - CIRCLE_IMAGE_PROFILE
            // ------------------------------------------------------------------------------------
            circleImageProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            });
            //-------------------------------------------------------------------------------------
        }
        // ----------------------------------------------------------------------------------------

        //AÑADIDO: MENU
        // ----------------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ----------------------------------------------------------------------------------------

        //AÑADIDO: LOCATION
        // ----------------------------------------------------------------------------------------
        buttonGeolocation = (Button) findViewById(R.id.button_geolocation);
        buttonGeolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "ENTRO A M:buttonGeolocation:0");

                MyLocation.location_function(TAG, activity, REQUEST_CHECK_SETTINGS);

                Log.i(TAG, "ENTRO A M:buttonGeolocation:1");

                if (MyLocation.isObtainedLocation()) {

                    Log.i(TAG, "ENTRO A M:buttonGeolocation:2");

                    startActivity(new Intent(MainActivity.this, EventsActivity.class));
                    finish();
                }
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO: LOCATION
    // ----------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A M:onActivityResult:0");

        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:

                Log.i(TAG, "ENTRO A M:onActivityResult:1");

                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        Log.i(TAG, "ENTRO A M:onActivityResult:2");

                        MyLocation.location_function(TAG, activity, REQUEST_CHECK_SETTINGS);

                        Log.i(TAG, "ENTRO A M:onActivityResult:3");

                        if (MyLocation.isObtainedLocation()) {

                            Log.i(TAG, "ENTRO A M:onActivityResult:4");

                            startActivity(new Intent(MainActivity.this, EventsActivity.class));
                            finish();
                        }
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

    //AÑADIDO: MENU
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account) {
            if (state.getLoged()) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        } else if (id == R.id.publish) {
            startActivity(new Intent(MainActivity.this, PublishActivity.class));
        } else if (id == R.id.search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        } else if (id == R.id.info) {
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    // ----------------------------------------------------------------------------------------
}
