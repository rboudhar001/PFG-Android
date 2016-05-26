package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
        }
        // ----------------------------------------------------------------------------------------

        // OPEN MainActivity OR EventsActivity
        // ----------------------------------------------------------------------------------------
        if (MyState.getExistsLocation()) { // Si existe la localizacion, abrir la ventana de EventsActivity

            Log.i(TAG, "ENTRO A Inicializate:3: " + MyState.getLoged());

            startActivity(new Intent(Inicializate.this, EventsActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Cambiar la animacion
            this.finish();

        } else { // Sino, abrir la ventana MainActivity

            Log.i(TAG, "ENTRO A Inicializate:4: " + MyState.getLoged());
            startActivity(new Intent(Inicializate.this, MainActivity.class));
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // Cambiar la animacion
            this.finish();
        }
        // ----------------------------------------------------------------------------------------
    }
}
