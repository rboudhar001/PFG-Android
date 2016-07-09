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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rachid on 27/04/2016.
 */
public class TabRegistered extends Fragment {

    private static final String TAG = "TabRegistered";
    public static Fragment fragment;

    private ListView mListView;
    private EventsAdapter adapter;
    private ArrayList<Event> listViewValues;

    private TextView mNoEventsView;
    //private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;

    private MyNetwork myNetwork;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_registered, container, false);

        fragment = this;

        Log.i(TAG, "ENTRO A TabRegistered:onCreateView: CREATE_NEW_INSTANCE");

        // ----------------------------------------------------------------------------------------
        mProgressBar = (ProgressBar) view.findViewById(R.id.tabRegistered_progress);
        mNoEventsView = (TextView) view.findViewById(R.id.tabRegistered_text_no_events);
        mListView = (ListView) view.findViewById(R.id.tabRegistered_listView_events);

        final Resources res = getResources();

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

        // Connect and Get data from Server
        // ----------------------------------------------------------------------------------------
        if ( MyNetwork.isNetworkConnected(fragment.getActivity()) ) {

            myNetwork = new MyNetwork(TAG, fragment.getActivity());
            showProgressDialog();
            myNetwork.Connect();

            //Wait 1 sec to Connect
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if ( myNetwork.isConnected() ) {

                            listViewValues = myNetwork.getAllEvents(MyState.getUser().getfestivalsAssisted());
                            Log.i(TAG, "ENTRO A TabRegistered:onCreateView: GET_EVENTS_SUCCESFULL");

                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A TabRegistered:onCreateView: DISCONNECT");

                            hideProgressDialog();
                            // ----------------------------------------------------------------------------------------

                            if (listViewValues == null) {
                                Log.i(TAG, "ENTRO A TabRegistered:onCreateView:listViewValues: NULL");
                                listViewValues = new ArrayList<>();
                            }

                            if (!listViewValues.isEmpty()) {

                                Log.i(TAG, "ENTRO A TabRegistered:onCreateView:listViewValues: NO_EMPTY");

                                mNoEventsView.setVisibility(View.INVISIBLE);
                                mListView.setVisibility(View.VISIBLE);
                            } else {

                                Log.i(TAG, "ENTRO A TabRegistered:onCreateView:listViewValues: EMPTY");

                                mNoEventsView.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.INVISIBLE);
                            }

                            //Log.i(TAG, "ENTRO A TabRegistered:onCreateView:FRAGMENT.ACTIVITY: " + fragment.getActivity());
                            //Log.i(TAG, "ENTRO A TabRegistered:onCreateView:ACTIVITY: " + EventsActivity.activity);
                            //adapter = new EventsAdapter(fragment.getActivity(), listViewValues, res);
                            adapter = new EventsAdapter(TAG, EventsActivity.activity, listViewValues, res);
                            mListView.setAdapter(adapter);

                    } else {
                        Log.i(TAG, "ENTRO A TabRegistered:onCreateView: NO_CONNECT");
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

        Log.i(TAG, "ENTRO A TabRegistered:onItemClick:POSITION: " + mPosition);
        Log.i(TAG, "ENTRO A TabRegistered:onItemClick:EVENT: " + tempValues);

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
        mListView.setVisibility(View.INVISIBLE);
    }

    //
    private void hideProgressDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
    // --------------------------------------------------------------------------------------------
}
