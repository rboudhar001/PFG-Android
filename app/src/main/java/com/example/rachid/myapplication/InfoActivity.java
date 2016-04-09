package com.example.rachid.myapplication;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rachid on 25/03/2016.
 */
public class InfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //AÑADIDO: STATE
    // -----------------------------------------------------------------------------------------
    State state = new State();
    // -----------------------------------------------------------------------------------------

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
        setContentView(R.layout.activity_info);

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN
        // -----------------------------------------------------------------------------------------
        if (state.getState()) { // Si el usuario esta con sesion iniciada

            navHeaderMain = (NavigationView) findViewById(R.id.nav_view);
            navViewHeaderMain = navHeaderMain.inflateHeaderView(R.layout.nav_header_main);

            //AÑADIDO: BASE DE DATOS
            // ----------------------------------------------------------------------------------------
            //Abrimos la base de datos
            DBActivity mDB_Activity = new DBActivity(this, null);

            SQLiteDatabase db = mDB_Activity.getReadableDatabase();
            if (db != null) {
                Cursor c = db.rawQuery("SELECT * FROM Users", null);
                if (c.moveToFirst()) {
                    circleImageProfile = (CircleImageView) navViewHeaderMain.findViewById(R.id.circle_image_profile);
                    Picasso.with(getApplicationContext()).load(c.getString(6)).into(circleImageProfile);

                    textUserName = (TextView) navViewHeaderMain.findViewById(R.id.text_user_name);
                    textUserName.setText(c.getString(3));

                    textUserEmail = (TextView) navViewHeaderMain.findViewById(R.id.text_user_email);
                    textUserEmail.setText(c.getString(1));

                    textUserLocation = (TextView) navViewHeaderMain.findViewById(R.id.text_user_location);
                    textUserLocation.setText("PEDIR_LOCALIZACIÓN");
                }
                c.close();
                db.close();
            }
            // ----------------------------------------------------------------------------------------

            // AÑADIDO: CLICK EVENT - CIRCLE_IMAGE_PROFILE
            // ----------------------------------------------------------------------------------------
            circleImageProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(InfoActivity.this, ProfileActivity.class));
                }
            });
            //-----------------------------------------------------------------------------------------
        }
        // -----------------------------------------------------------------------------------------

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
            startActivity(new Intent(InfoActivity.this, MainActivity.class));
        } else if (id == R.id.account) {
            if (state.getState()) {
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
