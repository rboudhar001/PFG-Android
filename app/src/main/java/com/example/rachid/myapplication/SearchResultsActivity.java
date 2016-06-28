package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

        // Wait 1 sec to Connect
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if ( myNetwork.isConnected() ) {

                    if ( (sDate != null ) & (!sDate.isEmpty()) ) { // Buscamos por "Lugar" y "Fecha"
                        listViewValues = myNetwork.getAllEvents(sPlace, sDate);
                    } else { // Sino, buscamos solo por lugar
                        listViewValues = myNetwork.getAllEvents(sPlace);
                    }
                    Log.i(TAG, "ENTRO A SearchResults:onCreate: GET_EVENTS_SUCCESFULL");

                    // Wait 1 second
                    // ----------------------------------------------------------------------------------------
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            // --------------------------------------------------------------------
                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A SearchResults:onCreate: DISCONNECT");

                            hideProgressDialog();
                            // --------------------------------------------------------------------

                            if (listViewValues == null) {
                                Log.i(TAG, "ENTRO A SearchResults:onCreate: NULL");
                                listViewValues = new ArrayList<>();
                            }

                            if (!listViewValues.isEmpty()) {

                                Log.i(TAG, "ENTRO A SearchResults:onCreate: NO_EMPTY");

                                mNoEventsView.setVisibility(View.INVISIBLE);
                                mListView.setVisibility(View.VISIBLE);
                            } else {

                                Log.i(TAG, "ENTRO A SearchResults:onCreate: EMPTY");

                                mNoEventsView.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.INVISIBLE);
                            }

                            adapter = new EventsAdapter(TAG, SearchResultsActivity.this, listViewValues, res);
                            mListView.setAdapter(adapter);

                        }

                    }, 1000);
                    // ----------------------------------------------------------------------------------------

                } else {
                    hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A SearchResults:onCreate: NO_CONNECT");
                }

            }
        }, 1000);
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO : SEARCH RESULTS
    // --------------------------------------------------------------------------------------------
    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition) {
        Event tempValues = listViewValues.get(mPosition);
        //Toast.makeText(EventsActivity.activity, "Event Name: " + tempValues.getName(), Toast.LENGTH_LONG).show();
        //Toast.makeText(EventsActivity.activity, "Event ID: " + tempValues.getID(), Toast.LENGTH_LONG).show();

        // TODO: Al clickear un evento, mostrarlo en la ventana de ShowEventActivity
        Intent intent = new Intent(SearchResultsActivity.this, ShowEventActivity.class);
        intent.putExtra("event", tempValues); // tempValues es el evento seleccionado por el usuario
        intent.putExtra("place", sPlace);
        intent.putExtra("date", sDate);
        startActivity(intent);
    }
    // --------------------------------------------------------------------------------------------

    //AÑADIDO: BOTON ATRAS
    // --------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //---------------------------------------------------------------------------------------------

    // *****************
    // *** FUNCIONES ***
    // *****************
    // --------------------------------------------------------------------------------------------
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
