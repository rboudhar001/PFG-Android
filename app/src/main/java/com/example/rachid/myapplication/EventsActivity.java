package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Rachid on 13/04/2016.
 */
public class EventsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "EventsActivity";
    public static Activity activity;

    //AÑADIDO: MENU
    // --------------------------------------------------------------------------------------------
    public static MyMenu myMenu;
    // --------------------------------------------------------------------------------------------

    //AÑADIDO: MENU
    // --------------------------------------------------------------------------------------------
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPageAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    // --------------------------------------------------------------------------------------------

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        activity = this;
        myMenu = new MyMenu(activity);

        Log.i(TAG, "ENTRO A Events:getLoged: " + MyState.getLoged());
        Log.i(TAG, "ENTRO A Events:getExistsLocation: " + MyState.getExistsLocation());

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN or NAV_HEADER_LOGIN
        // ----------------------------------------------------------------------------------------
        if (MyState.getLoged()) { // Si el usuario esta con sesion iniciada, cargamos el nav_header_login
            myMenu.loadHeaderLogin();
        } else if (MyState.getExistsLocation()) {
            myMenu.loadHeaderLocation();
        }
        // ----------------------------------------------------------------------------------------

        //AÑADIDO MENU
        // ----------------------------------------------------------------------------------------
        mToolbar = (Toolbar) findViewById(R.id.events_toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //-----------------------------------------------------------------------------------------

        // AÑADIDO: SLIDING TAB
        //-----------------------------------------------------------------------------------------
        CharSequence Titles[] = {getString(R.string.text_published), getString(R.string.text_events), getString(R.string.text_registered)};
        int NumOfTabs = 3;

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        mViewPageAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, NumOfTabs);

        // Assigning ViewPager View and setting the adapter
        mViewPager = (ViewPager) findViewById(R.id.events_viewpager);
        mViewPager.setAdapter(mViewPageAdapter);
        mViewPager.setCurrentItem(1); // 0 = Publish, 1 = Events, 3 = Registered, z

        // Assiging the Sliding Tab Layout View
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.events_slidingtabs);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.BLANCO));
        //mSlidingTabLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.AZUL_CLARO_APP));
        mSlidingTabLayout.setDividerColors(getResources().getColor(R.color.AZUL_CLARO_APP));

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        /*
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        */

        // Setting the ViewPager For the SlidingTabsLayout
        mSlidingTabLayout.setViewPager(mViewPager);

        // When change tab (SlidingTabLayout), update Activity title.
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        setTitle(getString(R.string.title_activity_published));
                        break;
                    case 1:
                        setTitle(getString(R.string.title_activity_events));
                        break;
                    case 2:
                        setTitle(getString(R.string.title_activity_registered));
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int pos) {
                // TODO Auto-generated method stub
            }
        });
    }

    //AÑADIDO MENU
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
                startActivity(new Intent(EventsActivity.this, ProfileActivity.class));
            } else {
                startActivity(new Intent(EventsActivity.this, AccountActivity.class));
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

    //AÑADIDO: OPTIONS
    // --------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            this.recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // --------------------------------------------------------------------------------------------
}
