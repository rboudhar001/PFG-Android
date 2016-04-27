package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;
// ----------------------------------------------------------------------------------------

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    public static Activity activity;

    //AÑADIDO: MENU
    // -----------------------------------------------------------------------------------------
    public static MyMenu myMenu;
    // -----------------------------------------------------------------------------------------

    private Button buttonUseLocation;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AÑADIDO: LOGIN
        // ----------------------------------------------------------------------------------------
        Log.i(TAG, "ENTRO A M:getLoged_1: " + MyState.getLoged());
        Log.i(TAG, "ENTRO A M:getExistsLocation_1: " + MyState.getExistsLocation());

        if ( (!MyState.getLoged()) || (!MyState.getExistsLocation()) ){ // Si no estoy logeado o no tengo la localizacion
            // Intentar inicializar de la DB
            MyDatabase.inicializate(TAG, this);
        }
        // ----------------------------------------------------------------------------------------

        // OPEN MainActivity OR EventsActivity
        // ----------------------------------------------------------------------------------------
        if (MyState.getExistsLocation()) { // Si existe la localizacion pasar a la ventana de eventos

            Log.i(TAG, "ENTRO A Main:FINISH:0");

            startActivity(new Intent(MainActivity.this, EventsActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Cambiar la animacion

            Log.i(TAG, "ENTRO A Main:FINISH:1");

            activity = null;

            this.finish();
            return;
            //Log.i(TAG, "ENTRO A Main:FINISH:2");
        }
        activity = this;
        myMenu = new MyMenu(activity);

        setContentView(R.layout.activity_main);
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN or NAV_HEADER_LOGIN
        // ----------------------------------------------------------------------------------------
        if (MyState.getLoged()) { // Si el usuario esta con sesion iniciada, cargamos el nav_header_login
            myMenu.loadHeaderLogin();
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
            // ---------------------------------------------------------------------------------------
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
            // ---------------------------------------------------------------------------------------
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
