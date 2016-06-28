package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

                                        // TODO: Actualizar la contraseña en la base de datos del servidor
                                        changePassword(mOldPassword, mNewPassword);

                                    } else {
                                        myNetwork.Disconnect();
                                        Log.i(TAG, "ENTRO A Settings:DialogChangePassword: DISCONNECT");

                                        Log.i(TAG, "ENTRO A Settings:DialogChangePassword: NO_LOGGIN_IN");
                                        hideProgressDialog();
                                        Toast.makeText(getBaseContext(), getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");
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
    // -------------------------------------------------------------------------------------------------------
    private void changePassword(final String oldPassword, final String newPassword) {

        // Inicializamos variable error a true
        MyError.setChangePasswordResponse(false);

        myNetwork.changePassword(oldPassword, newPassword, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setChangePasswordResponse(true);

                Log.i(TAG, "ENTRO A Settings:changePassword: SUCCESSFULLY: " + result);

                //TODO: Obtener los datos del usuario
                /*
                // --------------------------------------------------------------------------------
                String[] pieces = result.split("\"");
                String id = pieces[3];
                Log.i(TAG, "ENTRO A Login:loginUser:ID: " + id);

                getUser(id);
                // --------------------------------------------------------------------------------
                */

                Log.i(TAG, "ENTRO A Settings:changePassword: PASSWORD CHANGED");

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Settings:changePassword: DISCONNECT");

                hideProgressDialog();
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setChangePasswordResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Settings:changePassword: DISCONNECT");

                /*
                if ((error.equals("403") && (reason.equals("User not found")))) {
                    Toast.makeText(activity, getString(R.string.error_user_not_exists), Toast.LENGTH_LONG).show();
                } else if ((error.equals("403") && (reason.equals("Incorrect password")))) {
                    Toast.makeText(activity, getString(R.string.error_password_is_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_LONG).show();
                }
                */

                Log.i(TAG, "ENTRO A Settings:changePassword: COULD NOT LOGIN: " + error + " / " + reason + " / " + details);
                hideProgressDialog();
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getLoginResponse()) {
                    Log.i(TAG, "ENTRO A Settings:changePassword:getChangePasswordResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Settings:changePassword:getChangePasswordResponse: DISCONNECT");

                    Log.i(TAG, "ENTRO A Settings:changePassword:getChangePasswordResponse: COULD NOT LOGIN");
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }
    // -------------------------------------------------------------------------------------------------------

    //
    private void showAlertDialogChangeLanguage() {

        String spain = getString(R.string.text_spain);
        String english = getString(R.string.text_english);
        String basque = getString(R.string.text_basque);

        final CharSequence[] items = {spain, english, basque};
        final int itemDefaultSelect;

        // TODO: Obtener idioma del sistema y poner opcion preseleccionada
        itemDefaultSelect = 0;

        /*
        String gender = MyState.getUser().getGender();
        if (gender != null) {
            if (gender.equals(male)) {
                itemDefaultSelect = 0;
            } else if (gender.equals(female)) {
                itemDefaultSelect = 1;
            } else if (gender.equals(other)) {
                itemDefaultSelect = 2;
            } else {
                itemDefaultSelect = -1; // no tiene ninguna opcion seleccionada
            }
        }
        else {
            itemDefaultSelect = -1; // no tiene ninguna opcion seleccionada
        }
        */

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.profile_text_gender));
        dialog.setSingleChoiceItems(items, itemDefaultSelect, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int item) {

                if (itemDefaultSelect != item) { // Si no se ha modificado nada, no actualizamos nada

                    //TODO: Cambiar el idioma del sistema al seleccionado por el usuario
                    //items[item].toString(); //Idioma seleccionado por el usuario
                }

                dialog.dismiss();
            }
        });
        dialog.show();
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
    // ----------------------------------------------------------------------------------------
}
