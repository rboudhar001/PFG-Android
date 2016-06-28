package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rachid on 05/05/2016.
 */
public class EventsAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private String TAG;
    private Activity activity;

    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;

    Event tempValues = null;
    int i = 0;

    /*************  CustomAdapter Constructor *****************/
    public EventsAdapter(String T, Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        TAG = T;
        activity = a;
        data = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater ) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public ImageView image;
        public TextView name;
        public TextView place;
        public TextView firstDate;
        public TextView lastDate;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            view = inflater.inflate(R.layout.listview_event, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.listViewEvent_imageView_image);
            holder.name = (TextView) view.findViewById(R.id.listViewEvent_text_name);
            holder.place = (TextView) view.findViewById(R.id.listViewEvent_text_place);
            holder.firstDate = (TextView) view.findViewById(R.id.listViewEvent_text_firstDate);
            holder.lastDate = (TextView) view.findViewById(R.id.listViewEvent_text_lastDate);

            /************  Set holder with LayoutInflater ************/
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        if (data.size() > 0) {
            /***** Get each Model object from Arraylist ********/
            tempValues = null;
            tempValues = ( Event ) data.get( position );

            /************  Set Model values in Holder elements ***********/
            //holder.image.setImageResource(res.getIdentifier("com.androidexample.customlistview:drawable/" + tempValues.getPhoto(), null, null));
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int heightPixels = 300; //metrics.heightPixels / 4;
            int widthPixels = metrics.widthPixels - 24;

            //Log.i("EventsAdapter", "ENTRO A Events:getView: WIDTH-PIXELS: " + metrics.widthPixels);
            //Log.i("EventsAdapter", "ENTRO A Events:getView: HEIGHT-PIXELS: " + metrics.heightPixels);

            if ( (tempValues.getPhoto() != null) && (!tempValues.getPhoto().isEmpty()) ) {
                Picasso.with(activity).load(tempValues.getPhoto()).resize(widthPixels, heightPixels).into(holder.image);
            }

            holder.name.setText(tempValues.getName());
            holder.place.setText(tempValues.getPlace());
            holder.firstDate.setText(tempValues.getFirstDay());
            holder.lastDate.setText(tempValues.getLastDay());

            /******** Set Item Click Listner for LayoutInflater for each row *******/
            view.setOnClickListener(new OnItemClickListener(position));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "===== Row button clicked =====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            /****  Call onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            if (TAG.equals("SearchResultsActivity")) {
                ((SearchResultsActivity) SearchResultsActivity.activity).onItemClick(mPosition);

            } else if (TAG.equals("TabEvents")) {
                ((TabEvents) TabEvents.fragment).onItemClick(mPosition);

            } else if (TAG.equals("TabPublished")) {
                ((TabPublished) TabPublished.fragment).onItemClick(mPosition);

            } else if (TAG.equals("TabRegistered")) {
                ((TabRegistered) TabRegistered.fragment).onItemClick(mPosition);

            } else {
                Toast.makeText(activity, activity.getString(R.string.error_could_not_view_event), Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("EventsAdapter:onClick: Error, not detect what activity call");
            }
        }
    }
}
