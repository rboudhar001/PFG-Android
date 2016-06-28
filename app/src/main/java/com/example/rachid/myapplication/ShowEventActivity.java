package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import im.delight.android.ddp.SubscribeListener;

/**
 * Created by Rachid on 23/05/2016.
 */
public class ShowEventActivity extends AppCompatActivity {

    private static final String TAG = "ShowEventActivity";
    public static Activity activity;

    private Event sEvent;
    private String sPlace;
    private String sDate;

    // EVENTO
    // --------------------------------------------------------------------------------------------
    private ImageView mImageView;
    private TextView mNameView;
    private TextView mDescriptionView;
    private TextView mPlaceView;
    private TextView mFirstDayView;
    private TextView mLastDayView;
    private TextView mSalesView;
    private TextView mContactNumberView;
    private TextView mWebPageView;
    private TextView mCreatorView;
    private TextView mAssistantsView;
    private TextView mCapacityView;

    private TextView mUserNotLoggedView;
    private Button mButtonRegister;
    // --------------------------------------------------------------------------------------------

    private ProgressDialog mProgressDialog;
    private MyNetwork myNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        activity = this;
        sEvent = (Event) getIntent().getExtras().getSerializable("event");
        sPlace = getIntent().getExtras().getString("place");
        sDate = getIntent().getExtras().getString("date");

        Log.i(TAG, "ENTRO A ShowEvent:onCreate:PLACE: " + sPlace);
        Log.i(TAG, "ENTRO A ShowEvent:onCreate:DATE: " + sDate);

        // AÑADIDO: VISUALIZAR EVENTO
        // ----------------------------------------------------------------------------------------
        mImageView = (ImageView) findViewById(R.id.showEvent_image_event);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = 600; //metrics.heightPixels / 4;
        int widthPixels = metrics.widthPixels - 8;
        Picasso.with(activity).load(sEvent.getPhoto()).resize(widthPixels, heightPixels).into(mImageView);

        mNameView = (TextView) findViewById(R.id.showEvent_textEdit_name);
        mNameView.setText(sEvent.getName());

        mDescriptionView = (TextView) findViewById(R.id.showEvent_textEdit_description);
        mDescriptionView.setText(sEvent.getDescription());

        mPlaceView = (TextView) findViewById(R.id.showEvent_textEdit_place);
        mPlaceView.setText(sEvent.getPlace());

        mFirstDayView = (TextView) findViewById(R.id.showEvent_textEdit_firstDate);
        mFirstDayView.setText(sEvent.getFirstDay());

        mLastDayView = (TextView) findViewById(R.id.showEvent_textEdit_lastDate);
        mLastDayView.setText(sEvent.getLastDay());

        mSalesView = (TextView) findViewById(R.id.showEvent_textEdit_sales);
        mSalesView.setText(sEvent.getSales());

        mContactNumberView = (TextView) findViewById(R.id.showEvent_textEdit_contact_number);
        mContactNumberView.setText("" + sEvent.getContact_number());

        mWebPageView = (TextView) findViewById(R.id.showEvent_textEdit_webpage);
        mWebPageView.setText(sEvent.getWebpage());

        mCreatorView = (TextView) findViewById(R.id.showEvent_textEdit_creator);
        mCreatorView.setText(sEvent.getCreator());

        mAssistantsView = (TextView) findViewById(R.id.showEvent_textEdit_assistants);
        mAssistantsView.setText("" + sEvent.getAssistants());

        mCapacityView = (TextView) findViewById(R.id.showEvent_textEdit_capacity);
        mCapacityView.setText("" + sEvent.getCapacity());
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: BUTTON LISTENER
        // ----------------------------------------------------------------------------------------
        mUserNotLoggedView = (TextView) findViewById(R.id.showEvent_text_user_not_logged);
        mButtonRegister = (Button) findViewById(R.id.showEvent_button_register);

        if (MyState.getLoged()) { // Si el usuario esta logeado

            // Darle estilo al boton "Apuntarse" o "Desapuntarse" segun si esta o no registrado el usuario a este evento
            if (thisUserRegisteredInThisEvent()) {
                mButtonRegister.setBackgroundColor( getResources().getColor(R.color.ROJO) );
                mButtonRegister.setText(getString(R.string.showEvent_text_unregister));
            }
            else {
                mButtonRegister.setBackgroundColor( getResources().getColor(R.color.VERDE) );
                mButtonRegister.setText(getString(R.string.showEvent_text_register));
            }

            // NO mostrar mensaje y habilitar boton
            mUserNotLoggedView.setVisibility(View.INVISIBLE);
            mButtonRegister.setActivated(true);
            mButtonRegister.setEnabled(true);
        } else {
            // Mostrar mensaje y NO habilitar boton
            mUserNotLoggedView.setVisibility(View.VISIBLE);
            mButtonRegister.setActivated(false);
            mButtonRegister.setEnabled(false);
        }

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MyState.getLoged()) {
                    if (sEvent.getAssistants() < sEvent.getCapacity()) {

                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        final TextView asistentes = mAssistantsView;

                        // Wait 1 seconds to Connect
                        // ------------------------------------------------------------------------------------
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if ( myNetwork.isConnected() ) {
                                    registerOrUnregisterOnEvent(asistentes);
                                } else {
                                    hideProgressDialog();
                                    Log.i(TAG, "ENTRO A ShowEvent:onCreate: NO_CONNECT");
                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                }

                            }
                        }, 1000);
                        // ------------------------------------------------------------------------------------

                    }
                    else {
                        Toast.makeText(activity, getString(R.string.error_event_complete), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // AÑADIDO: Caso de uso "Apuntarse" o "Desapuntarse"
    // --------------------------------------------------------------------------------------------
    public void registerOrUnregisterOnEvent(final TextView asistentes) {

        Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterOnEvent: LOGGED");

        // Inicializamos variable error a true
        MyError.setSubscribeResponse(false);

        String subscriptionId = myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                MyError.setSubscribeResponse(true);

                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: SUCCESSFULLY SUBSCRIBE");
                registerOrUnregisterUserEvent(asistentes);
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSubscribeResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: DISCONNECT");

                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: COULD NOT SUBSCRIBE");
                //Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }

        });

        // Wait 6 seconds, si no responde en este tiempo, cerrar.
        // ------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if ( !MyError.getSubscribeResponse() ) {
                    Log.i(TAG, "ENTRO A Account:loginOrSignupUser:getSubscribeResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:loginOrSignupUser:getSubscribeResponse: DISCONNECT");

                    Log.i(TAG, "ENTRO A Account:loginOrSignupUser:getSubscribeResponse: COULD NOT SUBSCRIBE");
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            }
        }, 6000);
        // ------------------------------------------------------------------------------------
    }
    // --------------------------------------------------------------------------------------------

    // AÑADIDO: FUNCIONES
    // --------------------------------------------------------------------------------------------
    //
    public void registerOrUnregisterUserEvent(final TextView asistentes) {

        if (thisUserRegisteredInThisEvent()) {
            Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterOnEvent: USER_ARE_REGISTERED");

            // TODO: Des-Registrar a este usuario en este evento
            myNetwork.unregisterUserEvent(MyState.getUser(), sEvent.getName());

            // Wait 1 second
            // ----------------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Eliminamos este evento al usuario (DB Local)
                    MyDatabase.unregisterUserEvent(TAG, activity, MyState.getUser(), sEvent.getName());

                    // Eliminamos este evento al usuario (System)
                    MyState.getUser().getfestivalsAssisted().remove(sEvent.getName());

                    // Actualizar estado del boton
                    mButtonRegister.setBackgroundColor(getResources().getColor(R.color.VERDE));
                    mButtonRegister.setText(getString(R.string.showEvent_text_register));

                    int aux = sEvent.getAssistants() - 1;
                    sEvent.setAssistants(aux);
                    asistentes.setText("" + aux);

                    // Cerramos la ventana de la cual provenimos para actualizar
                    if ( (sPlace != null) && (!TextUtils.isEmpty(sPlace)) ) {
                        SearchResultsActivity.activity.finish();
                    } else {
                        EventsActivity.activity.finish();
                    }

                    hideProgressDialog();
                }

            }, 1000);
            // ----------------------------------------------------------------------------------------
        }
        else {
            Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterOnEvent: USER_NOT_ARE_REGISTERED");

            // TODO: Registrar a este usuario en este evento
            myNetwork.registerUserEvent(MyState.getUser(), sEvent.getName());

            // Wait 1 second
            // ----------------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Añadimos este evento al usuario (DB Local)
                    MyDatabase.registerUserEvent(TAG, activity, MyState.getUser(), sEvent.getName());

                    // Añadimos este evento al usuario (System)
                    MyState.getUser().getfestivalsAssisted().add(sEvent.getName());

                    // Actualizar estado del boton
                    mButtonRegister.setBackgroundColor(getResources().getColor(R.color.ROJO));
                    mButtonRegister.setText(getString(R.string.showEvent_text_unregister));

                    int aux = sEvent.getAssistants() + 1;
                    sEvent.setAssistants(aux);
                    asistentes.setText("" + sEvent.getAssistants());

                    if (EventsActivity.activity != null) {
                        EventsActivity.activity.finish();
                    }

                    hideProgressDialog();
                }

            }, 1000);
            // ----------------------------------------------------------------------------------------
        }
    }

    //
    public boolean thisUserRegisteredInThisEvent() {
        ArrayList<String> festivales_asistidos = MyState.getUser().getfestivalsAssisted();
        for (String name : festivales_asistidos) {
            if (sEvent.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }
    // --------------------------------------------------------------------------------------------

    //AÑADIDO: BOTON ATRAS
    // --------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {

        // Abrimos la ventana de la cual provenimos
        if ( (sPlace != null) && (!TextUtils.isEmpty(sPlace)) ) {
            Intent intent = new Intent(ShowEventActivity.this, SearchResultsActivity.class);
            intent.putExtra("place", sPlace);
            intent.putExtra("date", sDate);
            startActivity(intent);
        } else {
            startActivity(new Intent(ShowEventActivity.this, EventsActivity.class));
        }

        this.finish();
    }
    //---------------------------------------------------------------------------------------------

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
