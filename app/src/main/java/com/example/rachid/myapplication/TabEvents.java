package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rachid on 27/04/2016.
 */
public class TabEvents extends Fragment {

    private static final String TAG = "TabEvents";
    public static TabEvents tabEvents;

    private ListView list;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_events, container, false);

        tabEvents = this;

        // ----------------------------------------------------------------------------------------
        list = (ListView) view.findViewById(R.id.tabEvents_listView_events);

        Resources res = getResources();

        //TODO: Recoger de la DB del servidor de Junguitu los eventos.
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

        listViewValues.add(event_1);
        listViewValues.add(event_2);
        listViewValues.add(event_3);
        */

        listViewValues = MyNetwork.getAllEvents(tabEvents, MyState.getUser().getLocation());
        // ----------------------------------------------------------------------------------------

        TextView no_events = (TextView) view.findViewById(R.id.tabEvents_text_no_events);
        if (listViewValues.isEmpty()) {
            no_events.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
        }
        else {
            no_events.setVisibility(View.INVISIBLE);
            list.setVisibility(View.VISIBLE);
        }

        adapter = new EventsAdapter(EventsActivity.activity, listViewValues, res);
        list.setAdapter(adapter);

        return view;
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition) {
        Event tempValues = listViewValues.get(mPosition);

        // SHOW ALERT
        Toast.makeText(EventsActivity.activity, "Event Name: " + tempValues.getName(), Toast.LENGTH_LONG).show();

        //TODO: Redireccionar a la pagina para visualizar el evento seleccionado de la lista.
    }
}