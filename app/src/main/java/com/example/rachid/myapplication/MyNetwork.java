package com.example.rachid.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rachid on 18/04/2016.
 */
public class MyNetwork {

    // *****************
    // *** VARIABLES ***
    // *****************
    private final String TAG;
    private Activity activity;
    private Fragment fragment;

    private MyMeteor myMeteor;


    // *******************
    // *** CONSTRUCTOR ***
    // *******************
    public MyNetwork(String TAG, Activity activity) {
        this.TAG = TAG;
        this.activity = activity;

        myMeteor = new MyMeteor(activity);
    }

    public MyNetwork(String TAG, Fragment fragment) {
        this.TAG = TAG;
        this.fragment = fragment;

        myMeteor = new MyMeteor(fragment);
    }

    // *******************
    // *** FUNCIONES ***
    // *******************

    // CONNECT
    // ********************************************************************************************
    //
    public void Connect() {
        myMeteor.Connect();
    }

    //
    public void Disconnect() {
        myMeteor.Disconnect();
    }

    //
    public boolean isConnected() {
        return myMeteor.isConnected();
    }

    //
    public boolean isLoggedIn() {
        return myMeteor.isConnected();
    }
    // ********************************************************************************************

    // USUARIOS
    // ********************************************************************************************
    //
    public String signupUser(User mUser) {

        if (mUser != null) {

            // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
            // ---------------------------------------------------------------------------------------
            return myMeteor.addUser(mUser);
            // ---------------------------------------------------------------------------------------
        }

        return null;
    }

    //
    public User loginUser(String mEmail, String mPassword) {

        if ( (mEmail != null) && (mPassword != null) ) {
            // TODO: Solicita que se le de el usuario con el email y password pasados como parametros.
            // ---------------------------------------------------------------------------------------
            return myMeteor.getUser(mEmail, mPassword);
            // ---------------------------------------------------------------------------------------
        }

        return null;
    }

    //
    public boolean updateUser(User mUser) {

        if (mUser != null) {

            // TODO: Actualiza en la DB el usuario pasado como parametro
            // ------------------------------------------------------------------------------------
            myMeteor.updateUser(mUser);
            // ------------------------------------------------------------------------------------
            return true;
        }

        return false;
    }

    //
    public void changePassword(String id, String oldPassword, String newPassword) {

        // TODO: Actualiza en la DB del servidor la nueva contraseña del usuario
        // ---------------------------------------------------------------------------------------
        myMeteor.changePassword(id, oldPassword, newPassword);
        // ---------------------------------------------------------------------------------------
    }
    // ********************************************************************************************

    // EVENTOS
    // ********************************************************************************************
    //
    // Obtiene todos los eventos de una localizacion <FRAGMENT>
    //
    public ArrayList<Event> getAllEvents(String location){

        ArrayList<Event> list = null;

        if (location != null) {

            // TODO: Obtiene todos los eventos cuyo valor "place" sea igual a "location"
            // ------------------------------------------------------------------------------------
            list = myMeteor.getAllEvents(location);
            // ------------------------------------------------------------------------------------
        }

        return list;
    }

    //
    // Obtiene todos los eventos de una localizacion de un dia concreto
    //
    public ArrayList<Event> getAllEvents(String location, String date){

        ArrayList<Event> list = null;

        if (location != null) {

            // TODO: Accede al servidor y obtiene todos los eventos de la localizacion y fecha pasados como parametros.
            // ------------------------------------------------------------------------------------
            list = myMeteor.getAllEvents(location, date);
            // ------------------------------------------------------------------------------------
        }

        return list;
    }

    //
    // Obtiene todos los eventos a los que el usuario se ha registrado
    //
    public ArrayList<Event> getRegisteredEvents(String userID) {

        ArrayList<Event> list = null;

        if (userID != null) {

            // TODO: Accede al servidor y obtiene los eventos en los que se ha registrado el usuario "userID".
            // ------------------------------------------------------------------------------------
            list = myMeteor.getRegisteredEvents(userID);
            // ------------------------------------------------------------------------------------
        }

        return list;
    }

    //
    // Obtiene todos los eventos que el usuario a publicado
    //
    public ArrayList<Event> getPublishedEvents(String userID) {

        ArrayList<Event> list = null;

        if (userID != null) {

            // TODO: Accede al servidor y obtiene los eventos publicados por el usuario "userID".
            // ------------------------------------------------------------------------------------
            list = myMeteor.getPublishedEvents(userID);
            // ------------------------------------------------------------------------------------
        }

        return list;
    }
    // ********************************************************************************************
}
