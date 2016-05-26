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

import javax.xml.transform.Result;

import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;

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

    // METEOR : CONNECT
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

    // USUARIOS : Llamada a funciones de la API de Meteor
    // ********************************************************************************************
    //
    public void signupUser(String mUserName, String mEmail, String mPassword, ResultListener listener) {

        // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
        // ---------------------------------------------------------------------------------------
        myMeteor.signupUser(mUserName, mEmail, mPassword, listener);
        // ---------------------------------------------------------------------------------------
    }

    public String subscribe(final String subscriptionName) {
        return myMeteor.subscribe(subscriptionName);
    }

    //
    public String subscribe(final String subscriptionName, final Object[] params, final SubscribeListener listener) {
        return myMeteor.subscribe(subscriptionName, params, listener);
    }

    //
    public void loginUser(String mEmail, String mPassword, ResultListener listener) {

        // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
        // ---------------------------------------------------------------------------------------
        myMeteor.loginUser(mEmail, mPassword, listener);
        // ---------------------------------------------------------------------------------------
    }

    //
    public void Logout(ResultListener listener) {
        myMeteor.Logout(listener);
    }

    //
    public void Logout() {
        myMeteor.Logout();
    }
    // ********************************************************************************************


    // USUARIOS : Acceso a la DB de usuarios
    // ********************************************************************************************
    //
    public void addUser(User mUser) {

        // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
        // ---------------------------------------------------------------------------------------
        myMeteor.addUser(mUser);
        // ---------------------------------------------------------------------------------------
    }

    //
    public User getUserWithId(String id) {

        // TODO: Obtiene un usuario por id
        // ---------------------------------------------------------------------------------------
        return myMeteor.getUserWithId(id);
        // ---------------------------------------------------------------------------------------
    }

    //
    public User getUserWithFacebook(String facebook_id) {

        // TODO: Obtiene un usuario por el facebook_id
        // ---------------------------------------------------------------------------------------
        return myMeteor.getUserWithFacebook(facebook_id);
        // ---------------------------------------------------------------------------------------
    }

    //
    public User getUserWithGoogle(String google_id) {

        // TODO: Obtiene un usuario por el google_id
        // ---------------------------------------------------------------------------------------
        return myMeteor.getUserWithGoogle(google_id);
        // ---------------------------------------------------------------------------------------
    }

    //
    public void updateUser(User user) {

        // TODO: Actualiza en la DB el usuario pasado como parametro
        // ------------------------------------------------------------------------------------
        myMeteor.updateUser(user);
        // ------------------------------------------------------------------------------------
    }
    // ********************************************************************************************

    // EVENTOS
    // ********************************************************************************************
    //
    public String getEventID(String nameEvent) {

        if (nameEvent != null) {

            // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
            // ---------------------------------------------------------------------------------------
            return myMeteor.getEventID(nameEvent);
            // ---------------------------------------------------------------------------------------
        }

        return null;
    }

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
