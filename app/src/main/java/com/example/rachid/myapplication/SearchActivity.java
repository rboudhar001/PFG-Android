package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Rachid on 25/03/2016.
 */
public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "SearchActivity";
    public static Activity activity;

    //AÑADIDO: MENU
    // -----------------------------------------------------------------------------------------
    public static MyMenu myMenu;
    // -----------------------------------------------------------------------------------------

    private AutoCompleteTextView mPlaceView;
    private EditText mDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        // AÑADIDO : SEARCH
        // ----------------------------------------------------------------------------------------
        mPlaceView = (AutoCompleteTextView) findViewById(R.id.search_place);

        mDateView = (EditText) findViewById(R.id.search_date);
        mDateView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                Log.i(TAG, "ENTRO A Search:onEditorAction:0");

                if (id == R.id.search_button_keyboard_search || id == EditorInfo.IME_NULL) {

                    Log.i(TAG, "ENTRO A Search:onEditorAction:1");

                    attemptSearch();

                    return true;
                }
                return false;
            }
        });

        Button mSearchButton = (Button) findViewById(R.id.search_button_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ENTRO A Login:onClick:0");
                attemptSearch();
                Log.i(TAG, "ENTRO A Login:onClick:1");
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO : SEARCH
    // ----------------------------------------------------------------------------------------
    /**
     * Attempts to search the events specified by the search form
     * If there are form errors (invalid place, day, etc.), the
     * errors are presented and no actual search attempt is made.
     */
    private void attemptSearch() {

        Log.i(TAG, "ENTRO A Login:attemptLogin:0");

        // Reset errors.
        mPlaceView.setError(null);
        mDateView.setError(null);

        // Store values at the time of the search attempt.
        String place = mPlaceView.getText().toString();
        String date = mDateView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid place, if the place entered one.
        if (TextUtils.isEmpty(place)) {
            mPlaceView.setError(getString(R.string.error_field_required));
            focusView = mPlaceView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt search and focus the first form field with an error.

            Log.i(TAG, "ENTRO A Search:attemptSearch:2");

            focusView.requestFocus();
        } else {

            Log.i(TAG, "ENTRO A Search:attemptSearch:3");

            Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
            intent.putExtra("place", place);
            intent.putExtra("date", date);
            startActivity(intent);
        }
    }
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
            if (MyState.getExistsLocation()) {
                startActivity(new Intent(SearchActivity.this, EventsActivity.class));
            } else {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
            }
        } else if (id == R.id.account) {
            if (MyState.getLoged()) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(SearchActivity.this, AccountActivity.class));
            }
        } else if (id == R.id.publish) {
            startActivity(new Intent(SearchActivity.this, PublishActivity.class));
        } else if (id == R.id.info) {
            startActivity(new Intent(SearchActivity.this, InfoActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-----------------------------------------------------------------------------------------
}
