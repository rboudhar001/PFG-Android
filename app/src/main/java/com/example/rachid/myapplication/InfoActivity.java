package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
                //TODO: Redireccionar a la pagina de AYUDA
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sozialmusfest.scalingo.io/"));
                startActivity(browserIntent);
            }
        });

        buttonTermsOfService = (Button) findViewById(R.id.info_button_terms_of_service);
        buttonTermsOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redireccionar a la pagina de TERMINOS DE SERVICIO
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sozialmusfest.scalingo.io/services"));
                startActivity(browserIntent);
            }
        });

        buttonPrivacyPolicies = (Button) findViewById(R.id.info_button_privacy_policy);
        buttonPrivacyPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redireccionar a la pagina de POLITICA DE PRIVACIDAD
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sozialmusfest.scalingo.io/privacy"));
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
