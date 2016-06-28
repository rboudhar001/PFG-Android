package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class PublishActivity extends AppCompatActivity {

    protected static final int REQUEST_CHECK_SETTINGS = 1000;
    private static final String TAG = "PublishActivity";
    public static Activity activity;

    //AÑADIDO: MENU
    // -----------------------------------------------------------------------------------------
    public static MyMenu myMenu;
    // -----------------------------------------------------------------------------------------

    private Button buttonPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        activity = this;

        // AÑADIDO : PUBLISH
        // ----------------------------------------------------------------------------------------
        buttonPublish = (Button) findViewById(R.id.publish_button_publish);
        buttonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redireccionar a la pagina de publicar evento
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sozialmusfest.scalingo.io/"));
                startActivity(browserIntent);
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
