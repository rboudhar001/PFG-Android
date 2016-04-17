package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
// ----------------------------------------------------------------------------------------

// AÑADIDOS: GOOGLE
// ----------------------------------------------------------------------------------------

//import com.google.android.gms.location.LocationListener;

// ----------------------------------------------------------------------------------------


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "MainActivity";
    private final Activity activity = this;

    public static Activity f;

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
    private Button buttonUseLocation;
    private ImageButton imageButtonUpdateLocation;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        f = this;

        //AÑADIDO: LOGIN
        // ----------------------------------------------------------------------------------------
        Log.i(TAG, "ENTRO A M:getLoged_1: " + MyState.getLoged());
        Log.i(TAG, "ENTRO A M:getExistsLocation_1: " + MyState.getExistsLocation());

        if ( (!MyState.getLoged()) || (!MyState.getExistsLocation()) ){
            //Abrimos la base de datos
            DBActivity mDB_Activity = new DBActivity(this, null);
            SQLiteDatabase db = mDB_Activity.getReadableDatabase();
            if (db != null) {
                Cursor c = db.rawQuery("SELECT * FROM Users", null);
                if (c.moveToFirst()) {

                    Log.i(TAG, "ENTRO A M:getLoged:EMAIL: " + c.getString(1) + ", LOCATION: " + c.getString(7));

                    if (c.getString(1) != null) {
                        Log.i(TAG, "ENTRO A M:getLoged: OBTENER_EMAIL");
                        MyState.setLoged(true); // Usuario logeado
                    }
                    if (c.getString(7) != null) {
                        Log.i(TAG, "ENTRO A M:getExistsLocation: OBTENER_LOCALIZACION");
                        MyState.setExistsLocation(true); // Usuario con localizacion
                    }
                    MyState.setUser(new User(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7)));
                }
                c.close();
                db.close();
            }
        }
        // ----------------------------------------------------------------------------------------

        Log.i(TAG, "ENTRO A M:getLoged_2: " + MyState.getLoged());
        Log.i(TAG, "ENTRO A M:getExistsLocation_2: " + MyState.getExistsLocation());

        // OPEN MainActivity OR EventsActivity
        // ----------------------------------------------------------------------------------------
        if (MyState.getExistsLocation()) { // Si existe la localizacion pasar a la ventana de eventos
            startActivity(new Intent(MainActivity.this, EventsActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Cambiar la animacion
            finish();
        }
        setContentView(R.layout.activity_main);
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN or NAV_HEADER_LOGIN
        // ----------------------------------------------------------------------------------------
        if (MyState.getLoged()) { // Si el usuario esta con sesion iniciada

            navHeader = (NavigationView) findViewById(R.id.nav_view);
            navViewHeader = navHeader.inflateHeaderView(R.layout.nav_header_login);

            circleImageProfile = (CircleImageView) navViewHeader.findViewById(R.id.circle_image_profile);
            Picasso.with(getApplicationContext()).load(MyState.getUser().getUrlImageProfile()).into(circleImageProfile);

            textUserName = (TextView) navViewHeader.findViewById(R.id.text_user_name);
            textUserName.setText(MyState.getUser().getName());

            textUserEmail = (TextView) navViewHeader.findViewById(R.id.text_user_email);
            textUserEmail.setText(MyState.getUser().getEmail());

            textUserLocation = (TextView) navViewHeader.findViewById(R.id.text_user_location);
            textUserLocation.setText("___________________");

            // AÑADIDO: CLICK EVENT - CIRCLE_IMAGE_PROFILE
            // ------------------------------------------------------------------------------------
            circleImageProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            });
            //-------------------------------------------------------------------------------------

            //AÑADIDO: BUTTON_UPDATE_LOCATION
            // ----------------------------------------------------------------------------------------
            imageButtonUpdateLocation = (ImageButton) findViewById(R.id.nav_imageButton_update_location);
            imageButtonUpdateLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, LocationActivity.class));
                }
            });
            // ----------------------------------------------------------------------------------------
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

        //AÑADIDO: BUTTON_USE_LOCATION
        // ----------------------------------------------------------------------------------------
        buttonUseLocation = (Button) findViewById(R.id.main_button_use_location);
        buttonUseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });
        // ----------------------------------------------------------------------------------------
    }

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
            if (MyState.getLoged()) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));
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
