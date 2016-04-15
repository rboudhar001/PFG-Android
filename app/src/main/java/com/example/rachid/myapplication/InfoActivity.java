package com.example.rachid.myapplication;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rachid on 25/03/2016.
 */
public class InfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "InfoActivity";
    private final Activity activity = this;

    //AÑADIDO: STATE
    // -----------------------------------------------------------------------------------------
    State state = new State();
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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

    // AÑADIDO LISTA
    // ----------------------------------------------------------------------------------------

    // ----------------------------------------------------------------------------------------

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

        if (id == R.id.main_page) {
            if (state.getExistsLocation()) {
                startActivity(new Intent(InfoActivity.this, EventsActivity.class));
            } else {
                startActivity(new Intent(InfoActivity.this, MainActivity.class));
            }
        } else if (id == R.id.account) {
            if (state.getLoged()) {
                startActivity(new Intent(InfoActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(InfoActivity.this, LoginActivity.class));
            }
        } else if (id == R.id.publish) {
            startActivity(new Intent(InfoActivity.this, PublishActivity.class));
        } else if (id == R.id.search) {
            startActivity(new Intent(InfoActivity.this, SearchActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-----------------------------------------------------------------------------------------
}
