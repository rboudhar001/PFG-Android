package com.example.rachid.myapplication;

import android.app.Activity;
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

        // AÑADIDO: BUTTON
        // ****************************************************************************************
        mUserNotLoggedView = (TextView) findViewById(R.id.showEvent_text_user_not_logged);
        mButtonRegister = (Button) findViewById(R.id.showEvent_button_register);

        // ESTADO
        // ---------------------------------------------------------------------------------------
        if (MyState.getLoged()) { // Si el usuario esta logeado

            connectAndDo("getFestivalsAssisted", null);

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
        // ---------------------------------------------------------------------------------------

        // LISTENER
        // ---------------------------------------------------------------------------------------
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MyState.getLoged()) {
                    final TextView asistentes = mAssistantsView;
                    connectAndDo("registerOrUnregister", asistentes);
                }
            }
        });
        // ----------------------------------------------------------------------------------------
        // ****************************************************************************************
    }

    public void connectAndDo(final String hacer, final TextView asistentes) {

        if ( MyNetwork.isNetworkConnected(activity) ) {

            myNetwork = new MyNetwork(TAG, activity);
            myNetwork.showProgressDialog();
            myNetwork.Connect();

            // Wait 1 seconds to Connect
            // -----------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if ( myNetwork.isConnected() ) {

                        if (myNetwork.isLoggedIn()) {
                            Log.i(TAG, "ENTRO A ShowEvent:sButtonRegister: SUCCESSFULLY CONNECT");

                            subscribeAndDo(hacer, asistentes);

                        } else {
                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A ShowEvent:sButtonRegister: DISCONNECT");

                            myNetwork.hideProgressDialog();
                            Toast.makeText(getBaseContext(), getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "ENTRO A ShowEvent:sButtonRegister: NO_LOGGIN_IN");
                        }

                    } else {
                        myNetwork.hideProgressDialog();
                        Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "ENTRO A ShowEvent:onCreate: NO_CONNECT");
                    }
                }
            }, 1000);
            // -----------------------------------------------------------------------------------

        } else {
            Log.i(TAG, "ENTRO A Profile:connectAndDo:Connect: ERROR_NETWORK");
            Toast.makeText(activity, getString(R.string.error_not_network), Toast.LENGTH_SHORT).show();
        }
    }

    public void subscribeAndDo(final String hacer, final TextView asistentes) {

        // Inicializamos variable error a true
        MyError.setSubscribeResponse(false);

        String subscriptionId = myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                MyError.setSubscribeResponse(true);
                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: SUCCESSFULLY SUBSCRIBE");

                // Obtenermos al usuario del servidor
                final User userServer = myNetwork.getUserWithId(MyState.getUser().getID());

                // Wait 1 seconds
                // ----------------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Actualizamos el usuario en la DB
                        MyDatabase.updateUser(TAG, activity, userServer);
                        // Actualizamos el usuario en el Sistema
                        MyState.setUser(userServer);

                        if ( hacer.equals("getFestivalsAssisted") ) {
                            //Actualizamos la ventana con el estado del boton
                            updateButton();
                            myNetwork.hideProgressDialog();

                        } else if ( hacer.equals("registerOrUnregister") ) {
                            //Registramos o des-registramos del usuario este evento
                            registerOrUnregisterUserEvent(userServer, asistentes);

                        } else {
                            throw new IllegalArgumentException("ShowEvent:SubscribeAndDo: It is not detected method");
                        }

                    }
                }, 1000);
                // ----------------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSubscribeResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A ShowEvent:Subscribe: DISCONNECT");

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ENTRO A ShowEvent:Subscribe: COULD NOT SUBSCRIBE");
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getSubscribeResponse()) {
                    Log.i(TAG, "ENTRO A ShowEvent:Subscribe:getSubscribeResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A ShowEvent:Subscribe:getSubscribeResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A ShowEvent:Subscribe:getSubscribeResponse: COULD NOT SUBSCRIBE");
                }

            }
        }, 5000);
        // ------------------------------------------------------------------------------------
    }

    //
    public void updateButton() {

        // Darle estilo al boton "Apuntarse" o "Desapuntarse" segun si esta o no registrado el usuario a este evento
        if ( thisUserRegisteredInThisEvent() ) {
            mButtonRegister.setBackgroundColor(getResources().getColor(R.color.ROJO));
            mButtonRegister.setText(getString(R.string.showEvent_text_unregister));
        }
        else {
            mButtonRegister.setBackgroundColor(getResources().getColor(R.color.VERDE));
            mButtonRegister.setText(getString(R.string.showEvent_text_register));
        }
    }

    // AÑADIDO: Caso de uso "Apuntarse" o "Desapuntarse"
    // --------------------------------------------------------------------------------------------
    //
    public void registerOrUnregisterUserEvent(final User userServer, final TextView asistentes) {

        String button_state = mButtonRegister.getText().toString();

        if ( thisUserRegisteredInThisEvent() ) {
            Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterOnEvent: USER_ARE_REGISTERED");

            // Comprobar posible error: por si el usuario se ha desapuntado desde la pagina web y no se ha cambiado el estado del boton en el transcurso
            if ( TextUtils.equals(button_state, getString(R.string.showEvent_text_unregister)) ) { // Si el boton es igual a des-registrarse

                // TODO: Des-Registrar a este usuario en este evento
                myNetwork.unregisterUserEvent(userServer, sEvent);

                // Wait 1 second
                // --------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Eliminamos este evento al usuario (DB Local)
                        MyDatabase.unregisterUserEvent(TAG, activity, userServer, sEvent.getName());

                        // Eliminamos este evento al usuario (System)
                        userServer.getfestivalsAssisted().remove(sEvent.getName());
                        Log.i(TAG, "ENTRO A ShowEvent:RegisterOrUnregisterUserEvent: FESTIVALS_ASSISTED: " + userServer.getfestivalsAssisted());

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

                        myNetwork.hideProgressDialog();
                    }

                }, 1000);
                // --------------------------------------------------------------------------------

            } else {
                // Actualizar estado del boton
                mButtonRegister.setBackgroundColor(getResources().getColor(R.color.ROJO));
                mButtonRegister.setText(getString(R.string.showEvent_text_unregister));

                int aux = sEvent.getAssistants() + 1;
                sEvent.setAssistants(aux);
                asistentes.setText("" + sEvent.getAssistants());

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_you_are_already_unregistered), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterOnEvent: USER_NOT_ARE_REGISTERED");

            // Comprobar posible error: por si el usuario se ha apuntado desde la pagina web y no se ha cambiado el estado del boton en el transcurso
            if ( TextUtils.equals(button_state, getString(R.string.showEvent_text_register)) ) { // Si el boton es igual a registrarse

                //TODO: Comprobamos en VIVO si el evento no esta completo
                final Event eventServer = myNetwork.getEventWithID(sEvent.getID());

                // Wait 1 second
                // ----------------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (eventServer.getAssistants() < eventServer.getCapacity()) {

                            // TODO: Registrar a este usuario en este evento
                            myNetwork.registerUserEvent(userServer, sEvent);

                            // Wait 1 second
                            // ----------------------------------------------------------------------------------------
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // Añadimos este evento al usuario (DB Local)
                                    MyDatabase.registerUserEvent(TAG, activity, userServer, sEvent.getName());

                                    // Añadimos este evento al usuario (System)
                                    //Log.i(TAG, "ENTRO A MyMeteor:RegisterUserEvent: FESTIVALS_ASSISTED_OLD: " + userServer.getfestivalsAssisted());
                                    //userServer.getfestivalsAssisted().add(sEvent.getName());
                                    //Log.i(TAG, "ENTRO A ShowEvent:RegisterOrUnregisterUserEvent: FESTIVALS_ASSISTED_NEW: " + userServer.getfestivalsAssisted());

                                    // Actualizar estado del boton
                                    mButtonRegister.setBackgroundColor(getResources().getColor(R.color.ROJO));
                                    mButtonRegister.setText(getString(R.string.showEvent_text_unregister));

                                    int aux = sEvent.getAssistants() + 1;
                                    sEvent.setAssistants(aux);
                                    asistentes.setText("" + sEvent.getAssistants());

                                    if (EventsActivity.activity != null) {
                                        EventsActivity.activity.finish();
                                    }

                                    myNetwork.hideProgressDialog();
                                }

                            }, 1000);
                            // ----------------------------------------------------------------------------------------

                        } else {
                            Toast.makeText(activity, getString(R.string.error_event_complete), Toast.LENGTH_SHORT).show();
                        }
                    }

                }, 1000);
                // ----------------------------------------------------------------------------------------

            } else {
                // Actualizar estado del boton
                mButtonRegister.setBackgroundColor(getResources().getColor(R.color.VERDE));
                mButtonRegister.setText(getString(R.string.showEvent_text_register));

                int aux = sEvent.getAssistants() - 1;
                sEvent.setAssistants(aux);
                asistentes.setText("" + sEvent.getAssistants());

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_you_are_already_registered), Toast.LENGTH_SHORT).show();
            }
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
}
