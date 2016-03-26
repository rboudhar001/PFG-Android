package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.content.Intent;
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
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
// ----------------------------------------------------------------------------------------

// AÑADIDOS: GOOGLE
// ----------------------------------------------------------------------------------------
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.Plus;
// ----------------------------------------------------------------------------------------


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    private boolean isLoged = false;

    //AÑADIDO: PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private NavigationView navHeaderMain;
    private View navViewHeaderMain;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textUserLocation;
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: GOOGLE
    // ----------------------------------------------------------------------------------------
    private static final String TAG = "MainActivity";
    private static final int RC_GOOGLE = 9001;
    private GoogleApiClient mGoogleApiClient;
    // ----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AÑADIDO: GOOGLE
        // ----------------------------------------------------------------------------------------
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: Comprobar si el usuario esta logeado
        // -----------------------------------------------------------------------------------------
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            isLoged = true;
        }
        // -----------------------------------------------------------------------------------------

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN
        // -----------------------------------------------------------------------------------------
        if (isLoged) {

            navHeaderMain = (NavigationView) findViewById(R.id.nav_view);
            navViewHeaderMain = navHeaderMain.inflateHeaderView(R.layout.nav_header_main);

            // TODO: Mirar si existe (foto, nombre, email) del usuario en local, sino traerlo desde el servidor.
            circleImageProfile = (CircleImageView) navViewHeaderMain.findViewById(R.id.circle_image_profile);
            //circleImageProfile.setBackground(/*Imagen tipo DRAWANBLE*/);

            textUserName = (TextView) navViewHeaderMain.findViewById(R.id.text_user_name);
            textUserName.setText("DETECTADA SESIÓN");

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO: GOOGLE
    // ----------------------------------------------------------------------------------------
    /*
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            GoogleSignInResult result = opr.get();
            GoogleSignInAccount acct = result.getSignInAccount();

            // Get account information
            textUserName = (TextView) navViewHeaderMain.findViewById(R.id.text_user_name);
            textUserName.setText(acct.getDisplayName());

            textUserEmail = (TextView) navViewHeaderMain.findViewById(R.id.text_user_email);
            textUserEmail.setText(acct.getEmail());

            textUserLocation = (TextView) navViewHeaderMain.findViewById(R.id.text_user_location);
            textUserLocation.setText("PEDIR_LOCALIZACIÓN");
        }
    }
    */

    /*
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();

                // Get account information
                textUserName = (TextView) navViewHeaderMain.findViewById(R.id.text_user_name);
                textUserName.setText(acct.getDisplayName());

                textUserEmail = (TextView) navViewHeaderMain.findViewById(R.id.text_user_email);
                textUserEmail.setText(acct.getEmail());

                textUserLocation = (TextView) navViewHeaderMain.findViewById(R.id.text_user_location);
                textUserLocation.setText("PEDIR_LOCALIZACIÓN");
            }
        }
    }
    */

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
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

        if (id == R.id.login) {
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
    // ----------------------------------------------------------------------------------------
}
