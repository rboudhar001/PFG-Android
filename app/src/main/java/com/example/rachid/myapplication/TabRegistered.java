package com.example.rachid.myapplication;

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
public class TabRegistered extends Fragment {

    private static final String TAG = "TabRegistered";
    public static TabRegistered tabRegistered;

    private ListView list;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_registered, container, false);

        tabRegistered = this;

        // ----------------------------------------------------------------------------------------
        list = (ListView) view.findViewById(R.id.tabRegistered_listView_events);

        //TODO: Recoger de la DB del servidor de Junguitu los eventos.
        Resources res = getResources();

        // TEMPORAL
        /*
        // ----------------------------------------------------------------------------------------
        Event event_3 = new Event();
        event_3.setName("Evento numero 3");
        event_3.setPlace("Donostia");
        event_3.setFirstDate("01/01/1990");
        event_3.setLastDate("31/12/2016");

        listViewValues.add(event_3);
        // ----------------------------------------------------------------------------------------
        */

        TextView no_events = (TextView) view.findViewById(R.id.tabRegistered_text_no_events);
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
