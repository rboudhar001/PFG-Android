package com.example.rachid.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Rachid on 18/04/2016.
 */
public abstract class MyNetwork {

    private static String TAG = "MyNetwork";
    private static MyMeteor myMeteor;

    // USUARIOS
    // ********************************************************************************************
    //
    public static String signupUser(Activity activity, User mUser) {

        if (mUser != null) {

            // TODO: Accede al servidor y guarda en la DB este nuevo usuario, devuelve el ID que se le ha dado en la DB del servidor.
            // ---------------------------------------------------------------------------------------
            myMeteor = new MyMeteor(activity);
            myMeteor.Connect();
            String id = myMeteor.addUser(mUser);
            myMeteor.Disconnect();
            // ---------------------------------------------------------------------------------------

            return id;
        }

        return null;
    }

    //
    public static User loginUser(Activity activity, String mEmail, String mPassword) {

        // TODO: Accede al servidor y solicita que se le de el usuario con el email y password pasados como parametros.
        // ---------------------------------------------------------------------------------------
        myMeteor = new MyMeteor(activity);
        myMeteor.Connect();
        User user = myMeteor.getUser(mEmail, mPassword);
        myMeteor.Disconnect();
        // ---------------------------------------------------------------------------------------

        return user;
    }

    //
    public static boolean updateUser(Activity activity, User mUser) {

        if (mUser != null) {

            // TODO: Accede al servidor y actualiza en la DB el usuario pasado como parametro
            // ------------------------------------------------------------------------------------
            myMeteor = new MyMeteor(activity);
            myMeteor.Connect();
            myMeteor.updateUser(mUser);
            myMeteor.Disconnect();
            // ------------------------------------------------------------------------------------

            return true;
        }

        return false;
    }
    // ********************************************************************************************

    // EVENTOS
    // ********************************************************************************************
    //
    public static ArrayList<Event> getAllEvents(Fragment fragment, String location){

        ArrayList<Event> list = null;

        if (location != null) {

            // TODO: Accede al servidor y obtiene todos los eventos cuyo valor "place" sea igual a "location"
            // ------------------------------------------------------------------------------------
            myMeteor = new MyMeteor(fragment);
            myMeteor.Connect();

            if (myMeteor.isConnected()) {
                if (myMeteor.isLoggedIn()) {
                    list = myMeteor.getAllEvents(location);
                }
                else {
                    Log.i(TAG, "ENTRO A MyNetwork:getAllEvents:NO_LOGGED_IN");
                }

                myMeteor.Disconnect();
            }
            else {
                Log.i(TAG, "ENTRO A MyNetwork:getAllEvents:NO_CONNECT");
            }
            // ------------------------------------------------------------------------------------
        }

        return list;
    }

    //
    public static ArrayList<Event> getRegisteredEvents(Fragment fragment, String userID) {

        ArrayList<Event> list = null;

        if (userID != null) {

            // TODO: Accede al servidor y obtiene los eventos en los que se ha registrado el usuario "userID".
            // ------------------------------------------------------------------------------------
            myMeteor = new MyMeteor(fragment);
            myMeteor.Connect();

            if (myMeteor.isConnected()) {
                if (myMeteor.isLoggedIn()) {
                    list = myMeteor.getRegisteredEvents(userID);
                }
                else {
                    Log.i(TAG, "ENTRO A MyNetwork:getRegisteredEvents:NO_LOGGED_IN");
                }

                myMeteor.Disconnect();
            }
            else {
                Log.i(TAG, "ENTRO A MyNetwork:getRegisteredEvents:NO_CONNECT");
            }
            // ------------------------------------------------------------------------------------
        }

        return list;
    }

    //
    public static ArrayList<Event> getPublishedEvents(Fragment fragment, String userID) {

        ArrayList<Event> list = null;

        if (userID != null) {

            // TODO: Accede al servidor y obtiene los eventos publicados por el usuario "userID".
            // ------------------------------------------------------------------------------------
            myMeteor = new MyMeteor(fragment);
            myMeteor.Connect();

            if (myMeteor.isConnected()) {
                if (myMeteor.isLoggedIn()) {
                    list = myMeteor.getPublishedEvents(userID);
                }
                else {
                    Log.i(TAG, "ENTRO A MyNetwork:getPublishedEvents:NO_LOGGED_IN");
                }

                myMeteor.Disconnect();
            }
            else {
                Log.i(TAG, "ENTRO A MyNetwork:getPublishedEvents:NO_CONNECT");
            }
            // ------------------------------------------------------------------------------------
        }

        return list;
    }

    // ********************************************************************************************
}
