package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class PublishActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "PublishActivity";
    public static Activity activity;

    //AÑADIDO: MENU
    // -----------------------------------------------------------------------------------------
    public static MyMenu myMenu;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        activity = this;
        myMenu = new MyMenu(activity);

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN or NAV_HEADER_LOGIN
        // ----------------------------------------------------------------------------------------
        if (MyState.getLoged()) { // Si el usuario esta con sesion iniciada, cargamos el nav_header_login
            myMenu.loadHeaderLogin();
        }
        else if (MyState.getExistsLocation()) {
            myMenu.loadHeaderLocation();
        }
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

        if (id == R.id.main_page) {
            if (MyState.getExistsLocation()) {
                startActivity(new Intent(PublishActivity.this, EventsActivity.class));
            } else {
                startActivity(new Intent(PublishActivity.this, MainActivity.class));
            }
        } else if (id == R.id.account) {
            if (MyState.getLoged()) {
                startActivity(new Intent(PublishActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(PublishActivity.this, AccountActivity.class));
            }
        } else if (id == R.id.search) {
            startActivity(new Intent(PublishActivity.this, SearchActivity.class));
        } else if (id == R.id.info) {
            startActivity(new Intent(PublishActivity.this, InfoActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-----------------------------------------------------------------------------------------
}
