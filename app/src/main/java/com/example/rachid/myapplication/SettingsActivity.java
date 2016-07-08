package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

import im.delight.android.ddp.ResultListener;

/**
 * Created by Rachid on 25/03/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    public static Activity activity;

    private Button buttonChangePassword;
    private Button buttonChangeLanguage;

    private static MyNetwork myNetwork;

    private static ProgressDialog mProgressDialog;

    private static Handler handler;
    private static Runnable runnable;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
                showAlertDialogChangePassword();
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //AÑADIDO:
    // ----------------------------------------------------------------------------------------
    //
    private void showAlertDialogChangePassword() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.settings_changePassowrd));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_settings_change_password, null);
        dialog.setView(dialogView);

        final EditText mOldPasswordView = (EditText) dialogView.findViewById(R.id.dialog_old_password);
        final EditText mNewPasswordView = (EditText) dialogView.findViewById(R.id.dialog_new_password);
        final EditText mRepeatNewPasswordView = (EditText) dialogView.findViewById(R.id.dialog_repeat_new_password);

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Reset errors.
                mOldPasswordView.setError(null);
                mNewPasswordView.setError(null);
                mRepeatNewPasswordView.setError(null);

                // get values
                final String mOldPassword = mOldPasswordView.getText().toString();
                final String mNewPassword = mNewPasswordView.getText().toString();
                final String mRepeatNewPassword = mRepeatNewPasswordView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid Old Password
                if (TextUtils.isEmpty(mOldPassword)) {
                    mOldPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mOldPasswordView;
                    cancel = true;
                }
                // Check for a valid New Password
                if ((TextUtils.isEmpty(mNewPassword)) || (mNewPassword.length() <= 4)) {
                    mNewPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mNewPasswordView;
                    cancel = true;
                }
                // Check for a valid Repeat New Password and is same New Password
                if ((TextUtils.isEmpty(mRepeatNewPassword)) || (mRepeatNewPassword.length() <= 4)) {
                    mRepeatNewPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mRepeatNewPasswordView;
                    cancel = true;
                } else if (!mNewPassword.equals(mRepeatNewPassword)) {
                    mRepeatNewPasswordView.setError(getString(R.string.error_password_not_same));
                    focusView = mRepeatNewPasswordView;
                    cancel = true;
                }

                if (cancel) {
                    Log.i(TAG, "ENTRO A Settings:DialogChangePassword: ERROR_PARAMETERS");

                    // Show the errors
                    focusView.requestFocus();
                } else {

                    if (!mOldPassword.equals(mNewPassword)) {

                        // MyNetwork : Change Password
                        // ----------------------------------------------------------------------------
                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        // Wait 1 sec to Connect
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (myNetwork.isConnected()) {

                                    if (myNetwork.isLoggedIn()) {

                                        Log.i(TAG, "ENTRO A Settings:DialogChangePassword: SUCCESSFULLY CONNECT");
                                        // TODO: Actualizar la contraseña en la base de datos del servidor
                                        //setPassword(MyState.getUser().getID(), mNewPassword);
                                        changePassword(mOldPassword, mNewPassword);

                                    } else {
                                        myNetwork.Disconnect();
                                        Log.i(TAG, "ENTRO A Settings:DialogChangePassword: DISCONNECT");

                                        Log.i(TAG, "ENTRO A Settings:DialogChangePassword: NO_LOGGIN_IN");
                                        hideProgressDialog();
                                        Toast.makeText(getBaseContext(), getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Log.i(TAG, "ENTRO A Settings:DialogChangePassword: COULD NOT CONNECT");
                                    hideProgressDialog();
                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, 1000);

                    } else {

                        dialog.dismiss();
                    }
                    // ----------------------------------------------------------------------------
                }
            }
        });

        dialog.show();
    }

    // AÑADIDO: Cambiar Contraseña
    // --------------------------------------------------------------------------------------------
    private void setPassword(final String userID, final String newPassword) {

        myNetwork.setPassword(userID, newPassword);
        Log.i(TAG, "ENTRO A Settings:setPassword: PASSWORD CHANGED");

        myNetwork.Disconnect();
        Log.i(TAG, "ENTRO A Settings:setPassword: DISCONNECT");

        hideProgressDialog();
        Toast.makeText(activity, getString(R.string.password_update_succesfull), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ENTRO A Settings:changePassword: PASSWORD CHANGED");
    }
    // --------------------------------------------------------------------------------------------

    // AÑADIDO: Cambiar Contraseña
    // -------------------------------------------------------------------------------------------------------
    private void changePassword(final String oldPassword, final String newPassword) {

        // Inicializamos variable error a true
        MyError.setChangePasswordResponse(false);

        myNetwork.changePassword(oldPassword, newPassword, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setChangePasswordResponse(true);
                Log.i(TAG, "ENTRO A Settings:changePassword: SUCCESSFULLY: " + result);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Settings:changePassword: DISCONNECT");

                hideProgressDialog();
                Toast.makeText(activity, getString(R.string.password_update_succesfull), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ENTRO A Settings:changePassword: PASSWORD CHANGED");
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setChangePasswordResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Settings:changePassword: DISCONNECT");

                hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ENTRO A Settings:changePassword: COULD NOT LOGIN: " + error + " / " + reason + " / " + details);
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getChangePasswordResponse()) {
                    Log.i(TAG, "ENTRO A Settings:changePassword:getChangePasswordResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Settings:changePassword:getChangePasswordResponse: DISCONNECT");

                    hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A Settings:changePassword:getChangePasswordResponse: COULD NOT LOGIN");
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }
    // --------------------------------------------------------------------------------------------

    //
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
    //-----------------------------------------------------------------------------------------

    //AÑADIDO: BOTON ATRAS
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //-----------------------------------------------------------------------------------------

    // **********
    // FUNTIONS
    // **********
    // ----------------------------------------------------------------------------------------
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
        }
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rachid.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Settings Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.rachid.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    // ----------------------------------------------------------------------------------------
}
