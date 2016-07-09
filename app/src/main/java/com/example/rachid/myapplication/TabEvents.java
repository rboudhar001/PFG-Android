package com.example.rachid.myapplication;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rachid on 27/04/2016.
 */
public class TabEvents extends Fragment {

    private static final String TAG = "TabEvents";
    public static Fragment fragment;

    private ListView mListView;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    private TextView mNoEventsView;
    private Button mButtonRetry;

    //private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;

    private MyNetwork myNetwork;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.tab_events, container, false);
        fragment = this;

        Log.i(TAG, "ENTRO A TabEvents:onCreateView: CREATE_NEW_INSTANCE");

        // ----------------------------------------------------------------------------------------
        mProgressBar = (ProgressBar) view.findViewById(R.id.tabEvents_progress);
        mNoEventsView = (TextView) view.findViewById(R.id.tabEvents_text_no_events);
        mButtonRetry = (Button) view.findViewById(R.id.tabEvents_button_retry);
        mListView = (ListView) view.findViewById(R.id.tabEvents_listView_events);

        //AÑADIDO: BUTTON_USE_LOCATION
        // ----------------------------------------------------------------------------------------
        mButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.getActivity().recreate();
            }
        });
        // ----------------------------------------------------------------------------------------

        final Resources res = getResources();

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

        Event event_4 = new Event();
        event_4.setName("Evento numero 4");
        event_4.setPlace("Madrid");
        event_4.setFirstDay("01/01/1990");
        event_4.setLastDay("31/12/2016");

        Event event_5 = new Event();
        event_5.setName("Evento numero 5");
        event_5.setPlace("Barcelona");
        event_5.setFirstDay("01/01/1990");
        event_5.setLastDay("31/12/2016");

        Event event_6 = new Event();
        event_6.setName("Evento numero 6");
        event_6.setPlace("Sevilla");
        event_6.setFirstDay("01/01/1990");
        event_6.setLastDay("31/12/2016");

        listViewValues = new ArrayList<>();
        listViewValues.add(event_1);
        listViewValues.add(event_2);
        listViewValues.add(event_3);
        listViewValues.add(event_4);
        listViewValues.add(event_5);
        listViewValues.add(event_6);
        */
        // ----------------------------------------------------------------------------------------

        // Connect and Get data from Server
        // ----------------------------------------------------------------------------------------
        if ( MyNetwork.isNetworkConnected(fragment.getActivity()) ) {

            myNetwork = new MyNetwork(TAG, fragment);
            showProgressDialog();
            myNetwork.Connect();

            // Wait 1 sec to Connect
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if ( myNetwork.isConnected() ) {

                            listViewValues = myNetwork.getAllEvents(MyState.getUser().getLocation());
                            Log.i(TAG, "ENTRO A TabEvents:onCreateView: GET_EVENTS_SUCCESFULL");

                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A TabEvents:onCreateView: DISCONNECT");

                            hideProgressDialog();
                            // ----------------------------------------------------------------------------------------

                            if (listViewValues == null) {
                                Log.i(TAG, "ENTRO A TabEvents:onCreateView:listViewValues: NULL");
                                listViewValues = new ArrayList<>();
                            }

                            if ( !listViewValues.isEmpty() ) {
                                Log.i(TAG, "ENTRO A TabEvents:onCreateView:listViewValues: NO_EMPTY");
                                mNoEventsView.setVisibility(View.INVISIBLE);
                                mButtonRetry.setVisibility(View.INVISIBLE);
                                mListView.setVisibility(View.VISIBLE);

                            } else {
                                Log.i(TAG, "ENTRO A TabEvents:onCreateView:listViewValues: EMPTY");
                                mNoEventsView.setVisibility(View.VISIBLE);
                                mButtonRetry.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.INVISIBLE);

                            }

                            //Log.i(TAG, "ENTRO A TabEvents:onCreateView:FRAGMENT.ACTIVITY: " + fragment.getActivity());
                            //Log.i(TAG, "ENTRO A TabEvents:onCreateView:ACTIVITY: " + EventsActivity.activity);
                            //adapter = new EventsAdapter(fragment.getActivity(), listViewValues, res);
                            adapter = new EventsAdapter(TAG, EventsActivity.activity, listViewValues, res);
                            mListView.setAdapter(adapter);

                    } else {
                        Log.i(TAG, "ENTRO A TabEvents:onCreateView: NO_CONNECT");
                        Toast.makeText(fragment.getActivity(), getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }

                }
            }, 1000);
            // ----------------------------------------------------------------------------------------
        } else {
            Log.i(TAG, "ENTRO A Profile:connectAndDo:Connect: ERROR_NETWORK");
            Toast.makeText(fragment.getContext(), getString(R.string.error_not_network), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition) {
        Event tempValues = listViewValues.get(mPosition);
        //Toast.makeText(EventsActivity.activity, "Event Name: " + tempValues.getName(), Toast.LENGTH_LONG).show();
        //Toast.makeText(EventsActivity.activity, "Event ID: " + tempValues.getID(), Toast.LENGTH_LONG).show();

        Log.i(TAG, "ENTRO A TabEvent:onItemClick:POSITION: " + mPosition);
        Log.i(TAG, "ENTRO A TabEvent:onItemClick:EVENT: " + tempValues);

        // TODO: Al clickear un evento, mostrarlo en la ventana de ShowEventActivity
        Intent intent = new Intent(fragment.getActivity(), ShowEventActivity.class);
        intent.putExtra("event", tempValues); // tempValues es el evento seleccionado por el usuario
        startActivity(intent);
    }

    // *****************
    // *** FUNCIONES ***
    // *****************
    // --------------------------------------------------------------------------------------------
    //
    private void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoEventsView.setVisibility(View.INVISIBLE);
        mButtonRetry.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.INVISIBLE);
    }

    //
    private void hideProgressDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    // --------------------------------------------------------------------------------------------
}