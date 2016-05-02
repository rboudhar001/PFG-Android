package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Rachid on 25/03/2016.
 */
public class InfoActivity extends AppCompatActivity {

    private static final String TAG = "InfoActivity";
    public static Activity activity;

    private Button buttonHelp;
    private Button buttonTermsOfService;
    private Button buttonPrivacyPolicies;
    private Button buttonVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        activity = this;

        // AÑADIDO: BUTTON LISTENERS
        // ----------------------------------------------------------------------------------------
        buttonHelp = (Button) findViewById(R.id.info_button_help);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redireccionar a la pagina de Junguitu
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        buttonTermsOfService = (Button) findViewById(R.id.info_button_terms_of_service);
        buttonTermsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redireccionar a la pagina de Junguitu
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        buttonPrivacyPolicies = (Button) findViewById(R.id.info_button_privacy_policy);
        buttonPrivacyPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redireccionar a la pagina de Junguitu
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        buttonVersion = (Button) findViewById(R.id.info_button_version);
        buttonVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, VersionActivity.class));
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO: BOTON ATRAS
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //-----------------------------------------------------------------------------------------
}
