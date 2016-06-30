package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Rachid on 02/05/2016.
 */
public class Inicializate extends AppCompatActivity {

    private static final String TAG = "Inicializate";
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        //AÑADIDO: Comprobar para actualizar el estado si no estoy logeado
        // ----------------------------------------------------------------------------------------
        Log.i(TAG, "ENTRO A Inicializate:1: " + MyState.getLoged());

        if ( (!MyState.getLoged()) || (!MyState.getExistsLocation()) ){ // Si no estoy logeado o no tengo la localizacion

            Log.i(TAG, "ENTRO A Inicializate:2: " + MyState.getLoged());

            // Intentar inicializar de la DB
            MyDatabase.inicializate(TAG, this);

            // Actualizamos la App al idioma elegido por el usuario
            // ------------------------------------------------------------------------------------
            String language = MyState.getUser().getLanguage();
            Locale localizacion;

            if (language.equals("es")) {
                Log.i(TAG, "ENTRO A Inicializate:onCreate:CHANGE TO SPAIN: " + language);
                localizacion = new Locale("es", "ES");
            } else if (language.equals("eu")) {
                Log.i(TAG, "ENTRO A Inicializate:onCreate:CHANGE TO BASQUE: " + language);
                localizacion = new Locale("eu", "EU");
            } else {
                Log.i(TAG, "ENTRO A Inicializate:onCreate:CHANGE TO ENGLISH: " + language);
                localizacion = new Locale("en", "EN");
            }

            Locale.setDefault(localizacion);
            Configuration config = new Configuration();
            config.locale = localizacion;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            // ------------------------------------------------------------------------------------
        }
        // ----------------------------------------------------------------------------------------

        // OPEN MainActivity OR EventsActivity
        // ----------------------------------------------------------------------------------------
        if (MyState.getExistsLocation()) { // Si existe la localizacion, abrir la ventana de EventsActivity
            Log.i(TAG, "ENTRO A Inicializate:3: " + MyState.getLoged());

            //if (EventsActivity.activity != null) {
            //    EventsActivity.activity.finish();
            //}
            startActivity(new Intent(Inicializate.this, EventsActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Cambiar la animacion
            this.finish();

        } else { // Sino, abrir la ventana MainActivity
            Log.i(TAG, "ENTRO A Inicializate:4: " + MyState.getLoged());

            //if (MainActivity.activity != null) {
            //    MainActivity.activity.finish();
            //}
            startActivity(new Intent(Inicializate.this, MainActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Cambiar la animacion
            this.finish();
        }
        // ----------------------------------------------------------------------------------------
    }
}
