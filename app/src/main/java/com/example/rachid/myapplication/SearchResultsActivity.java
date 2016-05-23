package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rachid on 18/05/2016.
 */
public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultsActivity";
    public static Activity activity;

    private ListView mListView;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    private TextView mNoEventsView;
    //private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;

    private MyNetwork myNetwork;

    private Handler handler;
    private Runnable runnable;

    private String sPlace;
    private String sDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        activity = this;
        sPlace = getIntent().getExtras().getString("place");
        sDate = getIntent().getExtras().getString("date");

        //AÑADIDO : SEARCH RESULTS
        // ----------------------------------------------------------------------------------------

        // ----------------------------------------------------------------------------------------
        mProgressBar = (ProgressBar) findViewById(R.id.searchResult_progress);
        mNoEventsView = (TextView) findViewById(R.id.searchResult_text_no_events);
        mListView = (ListView) findViewById(R.id.searchResult_listView_events);

        final Resources res = getResources();

        //TODO: Recoger de la DB del servidor de Junguitu los eventos, y mostrar los que tengan el mismo "Place" y "Day" del formulario de busquedas

        // TEMPORAL
        // ----------------------------------------------------------------------------------------
        /*
        Event event_1 = new Event();
        event_1.setName("Evento numero 1");
        event_1.setPlace("Vitoria");
        event_1.setFirstDay("01/01/1990");
        event_1.setLastDay("31/12/2016");

        Event event_2 = new Event();
        event_2.setName("Evento numero 2");
        event_2.setPlace("Bilbao");
        event_2.setFirstDay("01/01/1990");
        event_2.setLastDay("31/12/2016");

        Event event_3 = new Event();
        event_3.setName("Evento numero 3");
        event_3.setPlace("Donostia");
        event_3.setFirstDay("01/01/1990");
        event_3.setLastDay("31/12/2016");

        listViewValues = new ArrayList<>();
        listViewValues.add(event_1);
        listViewValues.add(event_2);
        listViewValues.add(event_3);
        */
        // ----------------------------------------------------------------------------------------

        // Connect and Get data from Server
        // ----------------------------------------------------------------------------------------
        showProgressDialog();
        myNetwork = new MyNetwork(TAG, activity);
        myNetwork.Connect();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if ( myNetwork.isConnected() ) {

                    if ( myNetwork.isLoggedIn() ) {

                        if ( (sDate != null ) & (!sDate.isEmpty()) ) { // Buscamos por "Lugar" y "Fecha"
                            listViewValues = myNetwork.getAllEvents(sPlace, sDate);
                        } else { // Sino, buscamos solo por lugar
                            listViewValues = myNetwork.getAllEvents(sPlace);
                        }

                        Log.i(TAG, "ENTRO A SearchResults:onCreate: GET_EVENTS_SUCCESFULL");

                        myNetwork.Disconnect();
                        Log.i(TAG, "ENTRO A SearchResults:onCreate: DISCONNECT");

                        hideProgressDialog();
                        // ----------------------------------------------------------------------------------------

                        if (listViewValues == null) {
                            Log.i(TAG, "ENTRO A SearchResults:onCreate: NULL");
                            listViewValues = new ArrayList<>();
                        }

                        if (!listViewValues.isEmpty()) {

                            Log.i(TAG, "ENTRO A SearchResults:onCreate: NO_EMPTY");

                            mNoEventsView.setVisibility(View.INVISIBLE);
                            mListView.setVisibility(View.VISIBLE);
                        }
                        else {

                            Log.i(TAG, "ENTRO A SearchResults:onCreate: EMPTY");

                            mNoEventsView.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.INVISIBLE);
                        }

                        adapter = new EventsAdapter(EventsActivity.activity, listViewValues, res);
                        mListView.setAdapter(adapter);

                    }
                    else {
                        Log.i(TAG, "ENTRO A SearchResults:onCreate: NO_LOGGIN_IN");
                        hideProgressDialog();
                    }

                } else {
                    hideProgressDialog();
                    Log.i(TAG, "ENTRO A SearchResults:onCreate: NO_CONNECT");
                }

            }
        }, 3000);
        // ----------------------------------------------------------------------------------------

        /*
        // Connect and Get data from Server
        // ----------------------------------------------------------------------------------------
        showProgressDialog();
        myNetwork = new MyNetwork(TAG, activity);
        myNetwork.Connect();

        if (sDate == null) { // Si el "date" es null, realizamos una busqueda solo por la localizacion
            listViewValues = myNetwork.getAllEvents(sPlace);
        } else {
            listViewValues = myNetwork.getAllEvents(sPlace, sDate);
        }
        myNetwork.Disconnect();
        hideProgressDialog();
        // ----------------------------------------------------------------------------------------

        if (listViewValues == null) {
            listViewValues = new ArrayList<>();
        }

        TextView mNoEventsView = (TextView) findViewById(R.id.searchResult_text_no_events);
        if (!listViewValues.isEmpty()) {
            mNoEventsView.setVisibility(View.INVISIBLE);
            mListView.setVisibility(View.VISIBLE);
        }
        else {
            mNoEventsView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.INVISIBLE);
        }

        adapter = new EventsAdapter(EventsActivity.activity, listViewValues, res);
        mListView.setAdapter(adapter);
        */
        // --------------------------------------------------------------------------------------------
    }

    //AÑADIDO : SEARCH RESULTS
    // ********************************************************************************************

    // *****************
    // *** FUNCIONES ***
    // *****************
    // --------------------------------------------------------------------------------------------
    //
    private void startTime(int seconds) {
        // ---------------------------------------------------------------------------------------
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                endTime();
                //Toast.makeText(activity, "Impossible connect to server", Toast.LENGTH_SHORT).show();
                // ... Aqui lo que quieres ejecutar una vez pasados los 10 segundos ...
            }
        };

        handler.postDelayed(runnable, seconds); // 10 seg
        // ---------------------------------------------------------------------------------------
    }

    //
    private void endTime() {
        handler.removeCallbacks(runnable);
    }

    //
    private void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoEventsView.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }

    //
    private void hideProgressDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    // ********************************************************************************************
}
