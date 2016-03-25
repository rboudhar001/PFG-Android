package com.example.rachid.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

//AÑADIDO
import android.widget.FrameLayout;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import android.support.design.widget.NavigationView;

//AÑADIDO: GOOGLE
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //AÑADIDO: CIRCLE_IMAGE_PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private NavigationView navHeaderMain;
    private View navViewHeaderMain;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textUserLocation;
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: GOOGLE // POSIBLEMENTE BORRAR ESTO
    // -----------------------------------------------------------------------------------------
    private GoogleApiClient client;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN
        // -----------------------------------------------------------------------------------------
        if (true) { // Si el usuario esta logeado
            navHeaderMain = (NavigationView) findViewById(R.id.nav_view);
            navViewHeaderMain = navHeaderMain.inflateHeaderView(R.layout.nav_header_main);

            // TODO: Mirar si existe (foto, nombre, email) del usuario en local, sino traerlo desde el servidor.
            circleImageProfile = (CircleImageView) navViewHeaderMain.findViewById(R.id.circle_image_profile);
            //circleImageProfile.setBackground(/*Imagen tipo DRAWANBLE*/);

            textUserName = (TextView) navViewHeaderMain.findViewById(R.id.text_user_name);
            textUserName.setText("");

            textUserEmail = (TextView) navViewHeaderMain.findViewById(R.id.text_user_email);
            textUserEmail.setText("");

            textUserLocation = (TextView) navViewHeaderMain.findViewById(R.id.text_user_location);
            textUserLocation.setText("");

            // AÑADIDO: CLICK EVENT - CIRCLE_IMAGE_PROFILE
            // ----------------------------------------------------------------------------------------
            circleImageProfile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            });
            //-----------------------------------------------------------------------------------------
        }
        // -----------------------------------------------------------------------------------------

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //AÑADIDO: GOOGLE
        // ----------------------------------------------------------------------------------------
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        //-----------------------------------------------------------------------------------------
    }

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
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (id == R.id.login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.sign_up) {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
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

    /*
    // AÑADIDO: OPCIONES
    // --------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // --------------------------------------------------------------------------------------------
    */

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rachid.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rachid.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
