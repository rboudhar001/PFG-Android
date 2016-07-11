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

import im.delight.android.ddp.ResultListener;
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

        Log.i(TAG, "ENTRO A ShowEvent:onCreate:EVENT: " + sEvent);
        Log.i(TAG, "ENTRO A ShowEvent:onCreate:PLACE: " + sPlace);
        Log.i(TAG, "ENTRO A ShowEvent:onCreate:DATE: " + sDate);

        // AÑADIDO: VISUALIZAR EVENTO
        // ----------------------------------------------------------------------------------------
        mImageView = (ImageView) findViewById(R.id.showEvent_image_event);

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = 600; //metrics.heightPixels / 4;
        int widthPixels = metrics.widthPixels - 8;

        Log.i(TAG, "ENTRO A ShowEvent:onCreate: PHOTO: '" + sEvent.getPhoto() + "'");
        if ( (!TextUtils.isEmpty(sEvent.getPhoto())) && (!TextUtils.equals(sEvent.getPhoto(), "/img/noimgFestival.png")) ) {
            Log.i(TAG, "ENTRO A ShowEvent:onCreate: FESTIVAL_PHOTO");

            Picasso.with(activity).load( sEvent.getPhoto() ).resize(widthPixels, heightPixels).into(mImageView);
        } else {
            Log.i(TAG, "ENTRO A ShowEvent:onCreate: DEFAULT_PHOTO");

            Picasso.with(activity).load( "http://sozialmusfest.scalingo.io/img/noimgFestival.png" ).resize(widthPixels, heightPixels).into( mImageView );
        }

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
            Log.i(TAG, "ENTRO A ShowEvent:onCreate: ACTIVATE_BUTTON");
            // NO mostrar mensaje y habilitar boton
            mUserNotLoggedView.setVisibility(View.INVISIBLE);
            mButtonRegister.setActivated(true);
            mButtonRegister.setEnabled(true);

            // Actualizamos estado del boton "Apuntarse" o "Desapuntarse"
            updateButton(MyState.getUser(), sEvent);

            // Actualizamos los festivales_assistidos del usuario actuales del servidor
            connectAndDo("getFestivalsAssisted", null);

        } else {
            Log.i(TAG, "ENTRO A ShowEvent:onCreate: NOT_ACTIVATE_BUTTON");
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
                    final TextView textAssistants = mAssistantsView;
                    connectAndDo("registerOrUnregister", textAssistants);
                }
            }
        });
        // ----------------------------------------------------------------------------------------
        // ****************************************************************************************
    }

    // CONNECT
    // ****************************************************************************************
    public void connectAndDo(final String hacer, final TextView textAssistants) {

        if ( MyNetwork.isNetworkConnected(activity) ) {

            myNetwork = new MyNetwork(TAG, activity);

            if (myNetwork.isConnected()) {
                myNetwork.Disconnect();
            }

            myNetwork.showProgressDialog();
            myNetwork.Connect();

            // Wait 2.5 seconds to Connect
            // -----------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if ( myNetwork.isConnected() ) {

                        if (myNetwork.isLoggedIn()) {
                            Log.i(TAG, "ENTRO A ShowEvent:connectAndDo: SUCCESSFULLY CONNECT");

                            subscribeAndDo(hacer, textAssistants);

                        } else {
                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A ShowEvent:connectAndDo: DISCONNECT");

                            myNetwork.hideProgressDialog();
                            //Toast.makeText(getBaseContext(), getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                            Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "ENTRO A ShowEvent:connectAndDo: NO_LOGGIN_IN");
                        }

                    } else {
                        myNetwork.hideProgressDialog();
                        Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "ENTRO A ShowEvent:connectAndDo: NO_CONNECT");
                    }
                }
            }, 2500);
            // -----------------------------------------------------------------------------------

        } else {
            Log.i(TAG, "ENTRO A Profile:connectAndDo: ERROR_NETWORK");
            Toast.makeText(activity, getString(R.string.error_not_network), Toast.LENGTH_SHORT).show();
        }
    }
    // ****************************************************************************************

    // SUBSCRIBE
    // ****************************************************************************************
    public void subscribeAndDo(final String hacer, final TextView textAssistants) {

        // Inicializamos variable error a true
        MyError.setSubscribeResponse(false);

        String subscriptionId = myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                MyError.setSubscribeResponse(true);
                Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo: SUCCESSFULLY SUBSCRIBE");

                //TODO: Obtenermos al Usuario ACTUALIZADO del servidor
                final User userServer = myNetwork.getUserWithId(MyState.getUser().getID());
                Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo: GET_USER_SERVER");

                //TODO: Obtenermos el Evento ACTUALIZADO del servidor
                final Event eventServer = myNetwork.getEventWithID( sEvent.getID() );
                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: GET_EVENT_SERVER");

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
                            updateButton(userServer, eventServer);

                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo: DISCONNECT");

                            myNetwork.hideProgressDialog();

                        } else if ( hacer.equals("registerOrUnregister") ) {
                            //Registramos o des-registramos del usuario este evento
                            registerOrUnregisterUserEvent(userServer, eventServer, textAssistants);

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
                Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo: DISCONNECT");

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo: COULD NOT SUBSCRIBE");
            }
        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getSubscribeResponse()) {
                    Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo:getSubscribeResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo:getSubscribeResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A ShowEvent:subscribeAndDo:getSubscribeResponse: COULD NOT SUBSCRIBE");
                }
            }
        }, 5000);
        // ------------------------------------------------------------------------------------
    }
    // ****************************************************************************************

    // AÑADIDO: Caso de uso "Apuntarse" o "Desapuntarse"
    // ****************************************************************************************
    public void registerOrUnregisterUserEvent(final User userServer, final Event eventServer, final TextView textAssistants) {

        final String button_state = mButtonRegister.getText().toString();

        if ( thisUserRegisteredInThisEvent(userServer, eventServer) ) {
            Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: USER_ARE_REGISTERED");

            // Comprobar posible error: por si el usuario se ha desapuntado desde la pagina web y no se ha cambiado el estado del boton en el transcurso
            if ( TextUtils.equals(button_state, getString(R.string.showEvent_text_unregister)) ) { // Si el boton es igual a des-registrarse

                // TODO: Des-Registrar a este usuario en este evento
                // --------------------------------------------------------------------
                ArrayList<String> festivales_asistidos = userServer.getfestivalsAssisted();
                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent:Unregister: FESTIVALS_ASSISTED_OLD: " + festivales_asistidos);
                if ( festivales_asistidos != null ) {
                    userServer.getfestivalsAssisted().remove( eventServer.getName() );
                }
                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent:Unregister: FESTIVALS_ASSISTED_NEW: " + festivales_asistidos);
                // --------------------------------------------------------------------

                // TODO: Actualizar el usuario en el servidor
                updateUserOnNetworkAndDo("Unregister", userServer, eventServer, textAssistants);

            } else {
                updateButton(userServer, eventServer);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: DISCONNECT");

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_you_are_already_unregistered), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: USER_NOT_ARE_REGISTERED");

            // Comprobar posible error: por si el usuario se ha apuntado desde la pagina web y no se ha cambiado el estado del boton en el transcurso
            if ( TextUtils.equals(button_state, getString(R.string.showEvent_text_register)) ) { // Si el boton es igual a registrarse

                if (eventServer.getAssistants() < eventServer.getCapacity()) {

                    // TODO: Registrar a este usuario en este evento
                    // --------------------------------------------------------------------
                    ArrayList<String> festivales_asistidos = userServer.getfestivalsAssisted();
                    Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent:Register: FESTIVALS_ASSISTED_OLD: " + festivales_asistidos);
                    if ( festivales_asistidos == null ) {
                        userServer.setfestivalsAssisted( new ArrayList<String>() );
                        userServer.getfestivalsAssisted().add( eventServer.getName() );
                    } else {
                        if ( !festivales_asistidos.contains(eventServer.getName() )) {
                            userServer.getfestivalsAssisted().add( eventServer.getName() );
                        }
                    }
                    Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent:Register: FESTIVALS_ASSISTED_NEW: " + festivales_asistidos);
                    Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent:Register: FESTIVALS_ASSISTED_USER: " + userServer.getfestivalsAssisted());
                    // --------------------------------------------------------------------

                    // TODO: Actualizar el usuario en el servidor
                    updateUserOnNetworkAndDo("Register", userServer, eventServer, textAssistants);

                } else {
                    updateButton(userServer, eventServer);

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_event_complete), Toast.LENGTH_SHORT).show();
                }

            } else {
                updateButton(userServer, eventServer);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A ShowEvent:registerOrUnregisterUserEvent: DISCONNECT");

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_you_are_already_registered), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //
    public void updateUserOnNetworkAndDo(final String hacer, final User userServer, final Event eventServer, final TextView textAssistants) {

        // Inicializamos variable error a true
        MyError.setUpdateUserResponse(false);

        myNetwork.updateUser(userServer, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setUpdateUserResponse(true);
                Log.i(TAG, "ENTRO A ShowEvent:updateUserOnNetworkAndDo: SUCCESSFULLY UPDATE USER");

                if (hacer.equals("Register")) {

                    //TODO: Aumentar en 1 el numero de asistentes de este evento
                    eventServer.setAssistants((eventServer.getAssistants() + 1));
                    Log.i(TAG, "ENTRO A ShowEvent:updateUserOnNetworkAndDo:Register: ASSISTANTS: " + eventServer.getAssistants());

                    //TODO: Actualizar el evento en el servidor
                    updateEventOnNetwork(userServer, eventServer, textAssistants, 0);

                } else if (hacer.equals("Unregister")) {

                    //TODO: Disminuir en 1 el numero de asistentes de este evento
                    eventServer.setAssistants((eventServer.getAssistants() - 1));
                    Log.i(TAG, "ENTRO A ShowEvent:updateUserOnNetworkAndDo:Unregister: ASSISTANTS: " + eventServer.getAssistants());

                    //TODO: Actualizar el evento en el servidor
                    updateEventOnNetwork(userServer, eventServer, textAssistants, 0);

                } else {
                    throw new IllegalArgumentException("ShowEvent:updateUserOnNetworkAndDo: It is not detected method");
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setUpdateUserResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A ShowEvent:updateUserOnNetworkAndDo: DISCONNECT");

                myNetwork.hideProgressDialog();
                //Toast.makeText(activity, getString(R.string.error_could_not_update_user), Toast.LENGTH_LONG).show();
                Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_LONG).show();
                Log.i(TAG, "ENTRO A ShowEvent:updateUserOnNetworkAndDo: COULD NOT UPDATE: " + error + " / " + reason + " / " + details);
            }
        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getUpdateUserResponse()) {
                    Log.i(TAG, "ShowEvent:updateUserOnNetworkAndDo:getUpdateUserResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A ShowEvent:updateUserOnNetworkAndDo:getUpdateUserResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A ShowEvent:updateUserOnNetworkAndDo:getUpdateUserResponse: COULD NOT SUBSCRIBE");
                }
            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    public void updateEventOnNetwork(final User userServer, final Event eventServer, final TextView textAssistants, final int retry) {

        // Inicializamos variable error a true
        MyError.setUpdateEventResponse(false);

        myNetwork.updateEvent(eventServer, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setUpdateEventResponse(true);
                Log.i(TAG, "ENTRO A ShowEvent:updateEventOnNetwork: SUCCESSFULLY UPDATE EVENT");

                // Actualizamos el usuario en la DB (local)
                MyDatabase.updateUser(TAG, activity, userServer);
                // Actualizamos el usuario en el Sistema
                MyState.setUser(userServer);
                Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork: USER_FESTIVALS_ASSISTED: " + userServer.getfestivalsAssisted());

                // Actualizamos el texto de numero de asistentes
                mAssistantsView.setText("" + eventServer.getAssistants());
                Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork: EVENT_ASSISTANTS: " + eventServer.getAssistants());

                // Actualizar estado del boton
                updateButton(userServer, eventServer);

                if (EventsActivity.activity != null) {
                    EventsActivity.activity.finish();
                }

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork: DISCONNECT");

                myNetwork.hideProgressDialog();
            }

            @Override
            public void onError(String error, String reason, String details) {
                //MyError.setUpdateEventResponse(true);

                //myNetwork.Disconnect();
                //Log.i(TAG, "ENTRO A ShowEvent:updateEventOnNetwork: DISCONNECT");

                //myNetwork.hideProgressDialog();
                //Toast.makeText(activity, getString(R.string.error_could_not_update_user), Toast.LENGTH_LONG).show();
                //Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_LONG).show();
                Log.i(TAG, "ENTRO A ShowEvent:updateEventOnNetwork: COULD_NOT_UPDATE_EVENT: " + error + " / " + reason + " / " + details);
            }
        });

        // Wait 5 seconds, si no responde en este tiempo, REINTENTAR!!
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getUpdateEventResponse()) {
                    Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork:getUpdateEventResponse: TIMES_EXPIRED");

                    //TODO: Retry again the update
                    if (retry <= 3) {
                        Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork:getUpdateEventResponse: RETRY_NUMBER: " + retry);
                        updateEventOnNetwork(userServer, eventServer, textAssistants, (retry + 1));

                    } else {
                        myNetwork.Disconnect();
                        Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork:getUpdateEventResponse: DISCONNECT");

                        myNetwork.hideProgressDialog();
                        Toast.makeText(activity, getString(R.string.fatal_error_to_update_assistant_on_event), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork:getUpdateEventResponse: FATAL_ERROR!!!!!!!!! COULD NOT UPDATE EVENT: (The user have register the event, but event (assistants) not are update)!!!!");
                    }
                }
            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    public void updateButton(final User user, final Event event) {

        // Darle estilo al boton "Apuntarse" o "Desapuntarse" segun si esta o no registrado el usuario a este evento
        if ( thisUserRegisteredInThisEvent(user, event) ) {
            mButtonRegister.setBackgroundColor(getResources().getColor(R.color.ROJO));
            mButtonRegister.setText(getString(R.string.showEvent_text_unregister));
        }
        else {
            mButtonRegister.setBackgroundColor(getResources().getColor(R.color.VERDE));
            mButtonRegister.setText(getString(R.string.showEvent_text_register));
        }

        // Actualizamos el texto de numero de asistentes
        mAssistantsView.setText("" + event.getAssistants());
        Log.i(TAG, "ENTRO a ShowEvent:updateEventOnNetwork: EVENT_ASSISTANTS: " + event.getAssistants());
    }

    //
    public boolean thisUserRegisteredInThisEvent(final User user, final Event event) {
        ArrayList<String> festivales_asistidos = user.getfestivalsAssisted();
        Log.i(TAG, "ENTRO a ShowEvent:thisUserRegisteredInThisEvent: FESTIVALES_ASISTIDOS: " + festivales_asistidos);

        if (festivales_asistidos != null) {
            for (String name : festivales_asistidos) {
                if (sEvent.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    //AÑADIDO: BOTON ATRAS
    // --------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {

        // Abrimos la ventana de la cual provenimos
        if ( !TextUtils.isEmpty(sPlace) ) {
            Log.i(TAG, "ENTRO a ShowEvent:onBackPressed: OPEN_SEARCH_WINDOW");

            Intent intent = new Intent(ShowEventActivity.this, SearchResultsActivity.class);
            intent.putExtra("place", sPlace);
            intent.putExtra("date", sDate);
            startActivity(intent);
        } else {
            Log.i(TAG, "ENTRO a ShowEvent:onBackPressed: OPEN_EVENTS_WINDOW");

            startActivity(new Intent(ShowEventActivity.this, EventsActivity.class));
        }

        this.finish();
    }
    //---------------------------------------------------------------------------------------------
}
