package com.example.rachid.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import im.delight.android.ddp.ResultListener;

/**
 * Created by Rachid on 06/07/2016.
 */
public class ChangePasswordActivity  extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    public static Activity activity;

    private EditText mOldPasswordView;
    private EditText mNewPasswordView;
    private EditText mRepeatNewPasswordView;
    private Button mButtonChangePasword;

    private static MyNetwork myNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        activity = this;

        mOldPasswordView = (EditText) findViewById(R.id.changePassword_old_password);
        mNewPasswordView = (EditText) findViewById(R.id.changePassword_new_password);
        mRepeatNewPasswordView = (EditText) findViewById(R.id.changePassword_repeat_new_password);

        // AÑADIDO: BUTTON LISTENER
        // ----------------------------------------------------------------------------------------
        mButtonChangePasword = (Button) findViewById(R.id.changePassword_button_changePassword);
        mButtonChangePasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // AÑADIDO: CHANGE PASSWORD
    // ********************************************************************************************
    public void changePassword() {

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
        else if ((TextUtils.isEmpty(mNewPassword)) || (mNewPassword.length() <= 4)) {
            mNewPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mNewPasswordView;
            cancel = true;
        }
        // Check for a valid Repeat New Password and is same New Password
        else if ((TextUtils.isEmpty(mRepeatNewPassword)) || (mRepeatNewPassword.length() <= 4)) {
            mRepeatNewPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRepeatNewPasswordView;
            cancel = true;
        } else if (!mNewPassword.equals(mRepeatNewPassword)) {
            mRepeatNewPasswordView.setError(getString(R.string.error_password_not_same));
            focusView = mRepeatNewPasswordView;
            cancel = true;
        }

        if (cancel) {
            Log.i(TAG, "ENTRO A ChangePassword:changePassword: ERROR_PARAMETERS");

            // Show the errors
            focusView.requestFocus();
        } else {

            if ( !mOldPassword.equals(mNewPassword) ) {

                // MyNetwork : Change Password
                // ----------------------------------------------------------------------------
                myNetwork = new MyNetwork(TAG, activity);
                myNetwork.showProgressDialog();
                myNetwork.Connect();

                // Wait 2 sec to Connect
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

                                myNetwork.hideProgressDialog();
                                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "ENTRO A Settings:DialogChangePassword: NO_LOGGIN_IN");
                            }

                        } else {
                            myNetwork.hideProgressDialog();
                            Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "ENTRO A Settings:DialogChangePassword: COULD NOT CONNECT");
                        }

                    }
                }, 2000);

            }
            // ----------------------------------------------------------------------------
        }
    }

    // AÑADIDO: Cambiar Contraseña
    // --------------------------------------------------------------------------------------------
    /*
    private void setPassword(final String userID, final String newPassword) {

        myNetwork.setPassword(userID, newPassword);
        Log.i(TAG, "ENTRO A Settings:setPassword: PASSWORD CHANGED");

        myNetwork.Disconnect();
        Log.i(TAG, "ENTRO A Settings:setPassword: DISCONNECT");

        myNetwork.hideProgressDialog();
        Toast.makeText(activity, getString(R.string.password_update_succesfull), Toast.LENGTH_SHORT).show();
        Log.i(TAG, "ENTRO A Settings:changePassword: PASSWORD CHANGED");
    }
    */
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

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.password_update_succesfull), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ENTRO A Settings:changePassword: PASSWORD CHANGED");
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setChangePasswordResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Settings:changePassword: DISCONNECT");

                myNetwork.hideProgressDialog();
                if ( (error.equals("400")) && (reason.equals("Match failed")) ) {
                    Toast.makeText(activity, getString(R.string.error_the_current_password_is_not_correct), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_update_password), Toast.LENGTH_SHORT).show();
                }

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

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A Settings:changePassword:getChangePasswordResponse: COULD NOT LOGIN");
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
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