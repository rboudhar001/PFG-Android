package com.example.rachid.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublishActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //AÑADIDO: PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private NavigationView navHeaderMain;
    private View navViewHeaderMain;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textUserLocation;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN
        // -----------------------------------------------------------------------------------------
        if (true) {

            navHeaderMain = (NavigationView) findViewById(R.id.nav_view);
            navViewHeaderMain = navHeaderMain.inflateHeaderView(R.layout.nav_header_main);

            // TODO: Mirar si existe (foto, nombre, email) del usuario en local, sino traerlo desde el servidor.
            circleImageProfile = (CircleImageView) navViewHeaderMain.findViewById(R.id.circle_image_profile);
            //circleImageProfile.setBackground(/*Imagen tipo DRAWANBLE*/);

            //AÑADIDO: BASE DE DATOS
            // ----------------------------------------------------------------------------------------
            // ----------------------------------------------------------------------------------------

            // AÑADIDO: CLICK EVENT - CIRCLE_IMAGE_PROFILE
            // ----------------------------------------------------------------------------------------
            circleImageProfile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PublishActivity.this, ProfileActivity.class));
                }
            });
            //-----------------------------------------------------------------------------------------
        }
        // -----------------------------------------------------------------------------------------

        //AÑADIDO
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

    //AÑADIDO
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
            startActivity(new Intent(PublishActivity.this, MainActivity.class));
        } else if (id == R.id.login) {
            startActivity(new Intent(PublishActivity.this, LoginActivity.class));
        } else if (id == R.id.sign_up) {
            startActivity(new Intent(PublishActivity.this, SignUpActivity.class));
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
