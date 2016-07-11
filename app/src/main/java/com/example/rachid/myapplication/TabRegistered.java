package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import im.delight.android.ddp.SubscribeListener;

/**
 * Created by Rachid on 27/04/2016.
 */
public class TabRegistered extends Fragment {

    private static final String TAG = "TabRegistered";
    public static Fragment fragment;
    public static Activity activity;

    private ListView mListView;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    private TextView mLoginForSee;
    private TextView mNoEventsView;
    //private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;

    private MyNetwork myNetwork;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_registered, container, false);
        Log.i(TAG, "ENTRO A TabRegistered:onCreateView: CREATE_NEW_INSTANCE");

        fragment = this;
        activity = this.getActivity();

        mProgressBar = (ProgressBar) view.findViewById(R.id.tabRegistered_progress);
        mLoginForSee = (TextView) view.findViewById(R.id.tabRegistered_text_loginForSee);
        mNoEventsView = (TextView) view.findViewById(R.id.tabRegistered_text_no_events);
        mListView = (ListView) view.findViewById(R.id.tabRegistered_listView_events);

        //TODO: Recoger de la DB del servidor de Junguitu los eventos.
        // TEMPORAL
        // ----------------------------------------------------------------------------------------
        /*
        Event event_3 = new Event();
        event_3.setName("Evento numero 3");
        event_3.setPlace("Donostia");
        event_3.setFirstDay("01/01/1990");
        event_3.setLastDay("31/12/2016");

        listViewValues.add(event_3);
        */
        // ----------------------------------------------------------------------------------------

        if ( MyState.getLoged() ) {

            //TODO: Obtener los datos del usuario ACTUALES
            connectAndDo();

        } else {
            showMsgLoginForSee();
        }

        return view;
    }

    public void connectAndDo() {

        if ( MyNetwork.isNetworkConnected(activity) ) {

            myNetwork = new MyNetwork(TAG, activity);

            if (myNetwork.isConnected()) {
                myNetwork.Disconnect();
            }

            showProgressDialog();
            myNetwork.Connect();

            // Wait 2 seconds to Connect
            // -----------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if ( myNetwork.isConnected() ) {

                        if (myNetwork.isLoggedIn()) {
                            Log.i(TAG, "ENTRO A TabRegistered:connectAndDo: SUCCESSFULLY CONNECT");

                            subscribeAndDo();

                        } else {
                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A TabRegistered:connectAndDo: DISCONNECT");

                            showMsg( false );
                            showMsgNoEvents();
                            hideProgressDialog();
                            //Toast.makeText(getBaseContext(), getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(EventsActivity.activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "ENTRO A TabRegistered:connectAndDo: NO_LOGGIN_IN");
                        }

                    } else {
                        showMsg( false );
                        showMsgNoEvents();
                        hideProgressDialog();
                        //Toast.makeText(EventsActivity.activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "ENTRO A TabRegistered:connectAndDo: NO_CONNECT");
                    }
                }
            }, 2000);
            // -----------------------------------------------------------------------------------

        } else {
            showMsg( false );
            showMsgNoEvents();
            //Toast.makeText(EventsActivity.activity, getString(R.string.error_not_network), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "ENTRO A TabRegistered:connectAndDo: ERROR_NETWORK");
        }
    }

    public void subscribeAndDo() {

        // Inicializamos variable error a true
        MyError.setSubscribeResponse(false);

        String subscriptionId = myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                MyError.setSubscribeResponse(true);
                Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo: SUCCESSFULLY SUBSCRIBE");

                //TODO: Obtenermos al Usuario ACTUALIZADO del servidor
                final User userServer = myNetwork.getUserWithId(MyState.getUser().getID());
                Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo: GET_USER_SERVER");

                // Wait 1 seconds
                // ----------------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Actualizamos el usuario en la DB
                        MyDatabase.updateUser(TAG, activity, userServer);
                        // Actualizamos el usuario en el Sistema
                        MyState.setUser(userServer);

                        listViewValues = myNetwork.getAllEvents( userServer.getfestivalsAssisted() );
                        Log.i(TAG, "ENTRO A TabRegistered:onCreateView: GET_EVENTS_SUCCESFULL");

                        // Wait 1 seconds
                        // ----------------------------------------------------------------------------------------
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Actualizamos la ventana con los eventos
                                updateWindow();

                                myNetwork.Disconnect();
                                Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo: DISCONNECT");

                                hideProgressDialog();
                            }
                        }, 1000);
                        // ----------------------------------------------------------------------------------------

                    }
                }, 1000);
                // ----------------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSubscribeResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo: DISCONNECT");

                showMsg( false );
                showMsgNoEvents();
                hideProgressDialog();
                //Toast.makeText(EventsActivity.activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo: COULD NOT SUBSCRIBE");
            }
        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getSubscribeResponse()) {
                    Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo:getSubscribeResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo:getSubscribeResponse: DISCONNECT");

                    showMsg( false );
                    showMsgNoEvents();
                    hideProgressDialog();
                    //Toast.makeText(EventsActivity.activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A TabRegistered:subscribeAndDo:getSubscribeResponse: COULD NOT SUBSCRIBE");
                }
            }
        }, 5000);
        // ------------------------------------------------------------------------------------
    }

    public void updateWindow() {

        Resources res = EventsActivity.activity.getResources();

        if (listViewValues == null) {
            Log.i(TAG, "ENTRO A TabRegistered:updateWindow:listViewValues: NULL");
            listViewValues = new ArrayList<>();
        }

        if ( !listViewValues.isEmpty() ) {
            Log.i(TAG, "ENTRO A TabRegistered:updateWindow:listViewValues: NO_EMPTY");

            showList();
        } else {
            Log.i(TAG, "ENTRO A TabRegistered:updateWindow:listViewValues: EMPTY");

            showMsg( true );
            showMsgNoEvents();
        }

        //Log.i(TAG, "ENTRO A TabRegistered:onCreateView:FRAGMENT.ACTIVITY: " + fragment.getActivity());
        //Log.i(TAG, "ENTRO A TabRegistered:onCreateView:ACTIVITY: " + EventsActivity.activity);
        //adapter = new EventsAdapter(fragment.getActivity(), listViewValues, res);
        adapter = new EventsAdapter(TAG, EventsActivity.activity, listViewValues, res);
        mListView.setAdapter(adapter);
    }

    // *****************
    // *****************  This function used by adapter ****************/
    // *****************
    public void onItemClick(int mPosition) {
        Event tempValues = listViewValues.get(mPosition);
        //Toast.makeText(EventsActivity.activity, "Event Name: " + tempValues.getName(), Toast.LENGTH_LONG).show();
        //Toast.makeText(EventsActivity.activity, "Event ID: " + tempValues.getID(), Toast.LENGTH_LONG).show();

        Log.i(TAG, "ENTRO A TabRegistered:onItemClick:POSITION: " + mPosition);
        Log.i(TAG, "ENTRO A TabRegistered:onItemClick:EVENT: " + tempValues);

        // TODO: Al clickear un evento, mostrarlo en la ventana de ShowEventActivity
        Intent intent = new Intent(EventsActivity.activity, ShowEventActivity.class);
        intent.putExtra("event", tempValues); // tempValues es el evento seleccionado por el usuario
        startActivity(intent);
    }

    // *****************
    // *** FUNCIONES ***
    // *****************
    // --------------------------------------------------------------------------------------------
    //
    public void showMsg( boolean connection ) {
        try {
            if ( !connection ) {
                mNoEventsView.setText( getString(R.string.msg_no_connection) );
            } else {
                mNoEventsView.setText( getString(R.string.tabRegistered_text_no_events) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    public void showMsgLoginForSee() {
        mLoginForSee.setVisibility(View.VISIBLE);
        mNoEventsView.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }
    //
    public void showMsgNoEvents() {
        mLoginForSee.setVisibility(View.INVISIBLE);
        mNoEventsView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }
    //
    public void showList() {
        mLoginForSee.setVisibility(View.INVISIBLE);
        mNoEventsView.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.VISIBLE);
    }

    // PROGRESS DIALOG
    private void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLoginForSee.setVisibility(View.INVISIBLE);
        mNoEventsView.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }
    //
    private void hideProgressDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    // --------------------------------------------------------------------------------------------
}
