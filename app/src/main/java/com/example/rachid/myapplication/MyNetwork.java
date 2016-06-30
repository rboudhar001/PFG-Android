package com.example.rachid.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

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
    public void loginUserWithGoogle(String google_id, ResultListener listener) {

        // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
        // ---------------------------------------------------------------------------------------
        myMeteor.loginUserWithGoogle(google_id, listener);
        // ---------------------------------------------------------------------------------------
    }

    //
    public void loginUserWithFacebook(String facebook_id, ResultListener listener) {

        // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
        // ---------------------------------------------------------------------------------------
        myMeteor.loginUserWithFacebook(facebook_id, listener);
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

    //
    public void setUsername(String userID, String newUsername) {

        // TODO: Cambia en la DB del servidor la contraseña del usuario.
        // ---------------------------------------------------------------------------------------
        myMeteor.setUsername(userID, newUsername);
        // ---------------------------------------------------------------------------------------
    }

    //
    public void changePassword(String oldPassword, String newPassword, ResultListener listener) {

        // TODO: Cambia en la DB del servidor la contraseña del usuario.
        // ---------------------------------------------------------------------------------------
        myMeteor.changePassword(oldPassword, newPassword, listener);
        // ---------------------------------------------------------------------------------------
    }
    // ********************************************************************************************


    // USUARIOS : Acceso a la DB de usuarios
    // ********************************************************************************************
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

    //
    public void registerUserEvent(User user, String nameEvent) {

        // TODO: Apuntar al usuario el evento
        // ------------------------------------------------------------------------------------
        myMeteor.registerUserEvent(user, nameEvent);
        // ------------------------------------------------------------------------------------
    }

    //
    public void unregisterUserEvent(User user, String nameEvent) {

        // TODO: Desapuntar del usuario el evento
        // ------------------------------------------------------------------------------------
        myMeteor.unregisterUserEvent(user, nameEvent);
        // ------------------------------------------------------------------------------------
    }
    // ********************************************************************************************

    // EVENTOS
    // ********************************************************************************************
    //
    // Obtiene un evento por la id
    //
    public Event getEventWithID(String id) {

        if (id != null) {
            // TODO: Guarda en la DB del servidor al usuario, devuelve el ID que se le ha dado en la DB del servidor.
            // ---------------------------------------------------------------------------------------
            return myMeteor.getEventWithID(id);
            // ---------------------------------------------------------------------------------------
        }

        return null;
    }

    //
    // Obtiene todos los eventos con la misma id del ArrayList
    //
    public ArrayList<Event> getAllEvents(ArrayList<String> ids){

        ArrayList<Event> list = null;

        if (ids != null) {

            // TODO: Obtiene todos los eventos cuyo valor "place" sea igual a "location"
            // ------------------------------------------------------------------------------------
            list = myMeteor.getAllEvents(ids);
            // ------------------------------------------------------------------------------------
        }

        return list;
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
    // ********************************************************************************************
}
