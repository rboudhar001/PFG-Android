package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

/**
 * Created by Rachid on 25/03/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    public static Activity activity;

    private Button buttonChangePassword;
    private Button buttonChangeLanguage;

    private static MyNetwork myNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        activity = this;

        //AÑADIDO: BUTTON CHANGE PASSWORD
        // ----------------------------------------------------------------------------------------
        buttonChangePassword = (Button) findViewById(R.id.settings_button_changePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Cambiar la contraseña de usuario
                startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
            }
        });
        //-----------------------------------------------------------------------------------------

        //AÑADIDO: BUTTON IDIOMA
        // ----------------------------------------------------------------------------------------
        buttonChangeLanguage = (Button) findViewById(R.id.settings_button_changeLanguage);
        buttonChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Cambiar el idioma de la aplicacion
                showAlertDialogChangeLanguage();
            }
        });
        //-----------------------------------------------------------------------------------------
    }

    // AÑADIDO: CHANGE LANGUAGE
    // ********************************************************************************************
    private void showAlertDialogChangeLanguage() {

        String english = getString(R.string.text_english);
        String spain = getString(R.string.text_spain);
        String basque = getString(R.string.text_basque);

        final CharSequence[] items = {english, spain, basque};
        final int itemDefaultSelect;

        // TODO: Obtener idioma de la App y poner opcion pre-seleccionada
        //Locale current = getResources().getConfiguration().locale;
        //String language = current.getDefault().getLanguage();
        String language = Locale.getDefault().getLanguage();
        Log.i(TAG, "ENTRO A Settings:dialogChangeLanguage:LANGUAGE APP: " + language);

        if ( language.equals("en") ) {
            itemDefaultSelect = 0;
        } else if ( language.equals("es") ) {
            itemDefaultSelect = 1;
        } else if ( language.equals("eu") ) {
            itemDefaultSelect = 2;
        } else {
            itemDefaultSelect = -1; // no tiene ninguna opcion seleccionada
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.settings_text_language));
        dialog.setSingleChoiceItems(items, itemDefaultSelect, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int item) {

                if (itemDefaultSelect != item) { // Si no se ha modificado nada, no actualizamos nada

                    //TODO: Cambiar el idioma de la App a la seleccionada por el usuario
                    // ----------------------------------------------------------------------------
                    String language = items[item].toString(); //Idioma seleccionado por el usuario
                    Log.i(TAG, "ENTRO A Settings:dialogChangeLanguage:LANGUAGE SELECTED: '" + language + "'");
                    Locale localizacion = null;

                    if (language.equals(getString(R.string.text_english))) {
                        Log.i(TAG, "ENTRO A Settings:dialogChangeLanguage:CHANGE TO ENGLISH: " + language);
                        localizacion = new Locale("en", "EN");
                        //localizacion = new Locale(Locale.ENGLISH);
                    } else if (language.equals(getString(R.string.text_spain))) {
                        Log.i(TAG, "ENTRO A Settings:dialogChangeLanguage:CHANGE TO SPAIN: " + language);
                        localizacion = new Locale("es", "ES");
                    } else if (language.equals(getString(R.string.text_basque))) {
                        Log.i(TAG, "ENTRO A Settings:dialogChangeLanguage:CHANGE TO BASQUE: " + language);
                        localizacion = new Locale("eu", "EU");
                    }

                    if (localizacion != null) {
                        Locale.setDefault(localizacion);
                        Configuration config = new Configuration();
                        config.locale = localizacion;
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    } else {
                        throw new IllegalArgumentException("It has not been detected the selected language");
                    }
                    // ----------------------------------------------------------------------------

                    // Guardamos el nuevo idioma de la App
                    // ---------------------------------------------------------------------------
                    Locale current = getResources().getConfiguration().locale;
                    String lan = Locale.getDefault().getLanguage();

                    MyState.getUser().setLanguage(lan);
                    MyDatabase.updateLanguage(TAG, activity, MyState.getUser());
                    // ---------------------------------------------------------------------------

                    // REFRESCAMOS LAS VENTANAS!!
                    // ---------------------------------------------------------------------------
                    if (MainActivity.activity != null) {
                        MainActivity.activity.finish();
                    }
                    if (EventsActivity.activity != null) {
                        EventsActivity.activity.finish();
                    }
                    ProfileActivity.activity.finish();

                    startActivity(new Intent(SettingsActivity.this, Inicializate.class));
                    startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                    finish();
                    // ---------------------------------------------------------------------------
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }
    // ********************************************************************************************

    //AÑADIDO: BOTON ATRAS
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //-----------------------------------------------------------------------------------------
}
