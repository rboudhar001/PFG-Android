package com.example.rachid.myapplication;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
    public ArrayList<Event> listViewValues = new ArrayList<Event>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_events, container, false);

        tabEvents = this;

        // ----------------------------------------------------------------------------------------
        list = (ListView) view.findViewById(R.id.tabEvents_listView_events);

        //TODO: Recoger de la DB del servidor de Junguitu los eventos.
        //Resources res = MyNetwork.getEvents();

        // TEMPORAL
        Resources res = null;

        adapter = new EventsAdapter(EventsActivity.activity, listViewValues, res);
        list.setAdapter(adapter);
        // ----------------------------------------------------------------------------------------

        return view;
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition) {
        Event tempValues = ( Event ) listViewValues.get(mPosition);

        // SHOW ALERT
        Toast.makeText(EventsActivity.activity, "Event Name: " + tempValues.getName(), Toast.LENGTH_LONG).show();

        //TODO: Redireccionar a la pagina para visualizar el evento seleccionado de la lista.
    }
}