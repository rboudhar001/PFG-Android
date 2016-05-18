package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rachid on 18/05/2016.
 */
public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultsActivity";
    public static Activity activity;

    private String place;
    private String date;

    private ListView list;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        activity = this;
        place = (String) getIntent().getExtras().getString("place");
        date = (String) getIntent().getExtras().getString("date");

        //AÑADIDO : SEARCH RESULTS
        // --------------------------------------------------------------------------------------------

        // ----------------------------------------------------------------------------------------
        list = (ListView) findViewById(R.id.searchResult_listView_events);

        Resources res = getResources();

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

        // ----------------------------------------------------------------------------------------
        showProgressDialog();
        if (date == null) { // Si el "date" es null, realizamos una busqueda solo por la localizacion
            listViewValues = MyNetwork.getAllEvents(this, place);
        } else {
            listViewValues = MyNetwork.getAllEvents(this, place, date);
        }
        hideProgressDialog();

        if (listViewValues == null) {
            listViewValues = new ArrayList<>();
        }
        // ----------------------------------------------------------------------------------------

        TextView no_events = (TextView) findViewById(R.id.searchResult_text_no_events);
        if (!listViewValues.isEmpty()) {
            no_events.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
        }
        else {
            no_events.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        }

        adapter = new EventsAdapter(EventsActivity.activity, listViewValues, res);
        list.setAdapter(adapter);
        // --------------------------------------------------------------------------------------------
    }

    //AÑADIDO : SEARCH RESULTS
    // --------------------------------------------------------------------------------------------



    // **********
    // FUNTIONS
    // **********
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
    // --------------------------------------------------------------------------------------------
}
