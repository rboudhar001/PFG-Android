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
public class TabPublished extends Fragment {

    private static final String TAG = "TabPublished";
    public static TabPublished tabPublished;

    private ListView list;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_published ,container, false);

        tabPublished = this;

        // ----------------------------------------------------------------------------------------
        list = (ListView) view.findViewById(R.id.tabPublished_listView_events);

        //TODO: Recoger de la DB del servidor de Junguitu los eventos.
        Resources res = getResources();

        // TEMPORAL
        // ----------------------------------------------------------------------------------------
        /*
        Event event_1 = new Event();
        event_1.setName("Evento numero 1");
        event_1.setPlace("Vitoria");
        event_1.setFirstDay("01/01/1990");
        event_1.setLastDay("31/12/2016");

        listViewValues.add(event_1);
        */

        listViewValues = MyNetwork.getPublishedEvents(tabPublished, MyState.getUser().getID());
        if (listViewValues == null) {
            listViewValues = new ArrayList<>();
        }
        // ----------------------------------------------------------------------------------------

        TextView no_events = (TextView) view.findViewById(R.id.tabPublished_text_no_events);
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