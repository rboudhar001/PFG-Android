package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationSettingsStates;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rachid on 13/04/2016.
 */
public class EventsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "EventsActivity";
    private final Activity activity = this;

    public static Activity f;

    //AÑADIDO: STATE
    // -----------------------------------------------------------------------------------------
    State state = new State();
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        f = this;

        //AÑADIDO: LOGIN
        // ----------------------------------------------------------------------------------------
        Log.i(TAG, "ENTRO A L:getLoged: " + state.getLoged());

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

                        Log.i(TAG, "ENTRO A L:getLoged:EMAIL: " + state.getUser().getEmail() + ", LOCATION: " + state.getUser().getLocation());
                    }
                }
                c.close();
                db.close();
            }
        }
        // ----------------------------------------------------------------------------------------

        //AÑADIDO MENU - LOGIN - LOCATION
        // ----------------------------------------------------------------------------------------
        MyMenu.onCreate(TAG, activity, REQUEST_CHECK_SETTINGS);
        // ----------------------------------------------------------------------------------------

        //AÑADIDO MENU
        // ----------------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //-----------------------------------------------------------------------------------------
    }

    //AÑADIDO MENU
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
                startActivity(new Intent(EventsActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(EventsActivity.this, LoginActivity.class));
            }
        } else if (id == R.id.publish) {
            startActivity(new Intent(EventsActivity.this, PublishActivity.class));
        } else if (id == R.id.search) {
            startActivity(new Intent(EventsActivity.this, SearchActivity.class));
        } else if (id == R.id.info) {
            startActivity(new Intent(EventsActivity.this, InfoActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-----------------------------------------------------------------------------------------
}
