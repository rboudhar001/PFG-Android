package com.example.rachid.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Rachid on 17/04/2016.
 */
public class ForgottenPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgottenPasswordActivity";
    public static Activity activity;

    // VARIABLES
    // --------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        activity = this;

        // AÑADIDO:
        // ----------------------------------------------------------------------------------------
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: BUTTON LISTENER
        // ----------------------------------------------------------------------------------------

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
