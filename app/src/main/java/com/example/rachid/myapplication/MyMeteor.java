package com.example.rachid.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.Query;
import im.delight.android.ddp.db.memory.InMemoryDatabase;

/**
 * Created by Rachid on 18/04/2016.
 *
 * Android-DDP:
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 */
public class MyMeteor implements MeteorCallback {

    // *****************
    // *** VARIABLES ***
    // *****************
    private static String TAG = "MyMeteor";
    private Activity activity;
    private Fragment fragment;
    private Meteor mMeteor;

    // Añadido: Collection Names
    // -------------------------------------------------------------------------------------------
    private String Users = "users";
    private String Events = "festivals";
    // -------------------------------------------------------------------------------------------

    // *******************
    // *** CONSTRUCTOR ***
    // *******************
    public MyMeteor(Activity a){

        activity = a;

        // enable logging of internal events for the library
        Meteor.setLoggingEnabled(true);

        // create a new instance
        //mMeteor = new Meteor(activity, "ws://example.meteor.com/websocket");
        mMeteor = new Meteor(activity, "ws://sozialmusfest.scalingo.io/websocket", new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);
    }

    public MyMeteor(Fragment f){

        fragment = f;

        // enable logging of internal events for the library
        Meteor.setLoggingEnabled(true);

        // create a new instance
        //mMeteor = new Meteor(fragment.getContext(), "ws://example.meteor.com/websocket");
        mMeteor = new Meteor(fragment.getContext(), "ws://sozialmusfest.scalingo.io/websocket", new InMemoryDatabase());

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);
    }

    // *****************
    // *** FUNCIONES ***
    // *****************

    // METEOR : CONNECT
    // ********************************************************************************************
    public void Connect(){
        // establish the connection
        mMeteor.connect();
    }

    public boolean isConnected(){
        return mMeteor.isConnected();
    }

    public boolean isLoggedIn(){
        return mMeteor.isLoggedIn();
    }

    public void Disconnect(){
        mMeteor.disconnect();
    }

    public void onConnect(boolean signedInAutomatically) {

        Log.i(TAG, "ENTRO A MyMeteor:onConnect: Connected");
        Log.i(TAG, "ENTRO A MyMeteor:onConnect: Is logged in: " + mMeteor.isLoggedIn());
        Log.i(TAG, "ENTRO A MyMeteor:onConnect: User ID: " + mMeteor.getUserId());

        if (signedInAutomatically) {
            Log.i(TAG, "ENTRO A MyMeteor:onConnect: Successfully logged in automatically");
        }
        /*
        else {
            Log.i(TAG, "ENTRO A MyMeteor:onConnect: NOT Successfully logged in automatically");

            // sign in to the server
            mMeteor.loginWithUsername("sozial", "Sozial_PFG_2016", new ResultListener() {
            //mMeteor.loginWithUsername("USER", "PASSWORD", new ResultListener() {

                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: Successfully logged in: " + result);

                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: Is logged in: " + mMeteor.isLoggedIn());
                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: User ID: " + mMeteor.getUserId());
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: Could not log in: " + error + " / " + reason + " / " + details);

                    // sign up for a new account
                    mMeteor.registerAndLogin("sozial", "sozial.mus.fest@gmail.com", "Sozial_PFG_2016", new ResultListener() {
                    //mMeteor.registerAndLogin("USER", "EMAIL", "PASSWORD", new ResultListener() {

                        @Override
                        public void onSuccess(String result) {
                            Log.i(TAG, "ENTRO A MyMeteor:onConnect:registerAndLogin: Successfully registered: " + result);
                        }

                        @Override
                        public void onError(String error, String reason, String details) {
                            Log.i(TAG, "ENTRO A MyMeteor:onConnect:registerAndLogin: Could not register: " + error + " / " + reason + " / " + details);
                        }

                    });
                }

            });
        }
        */

        /*
        // subscribe to data from the server
        String subscriptionId = mMeteor.subscribe("publicMessages");

        // unsubscribe from data again (usually done later or not at all)
        mMeteor.unsubscribe(subscriptionId);
        */

        /*
        // insert data into a collection
        Map<String, Object> insertValues = new HashMap<String, Object>();
        insertValues.put("_id", "my-key");
        insertValues.put("some-number", 3);
        mMeteor.insert("my-collection", insertValues);

        // update data in a collection
        Map<String, Object> updateQuery = new HashMap<String, Object>();
        updateQuery.put("_id", "my-key");
        Map<String, Object> updateValues = new HashMap<String, Object>();
        updateValues.put("_id", "my-key");
        updateValues.put("some-number", 5);
        mMeteor.update("my-collection", updateQuery, updateValues);

        // remove data from a collection
        mMeteor.remove("my-collection", "my-key");

        // call an arbitrary method
        mMeteor.call("myMethod");
        */

    }

    public void onDisconnect() {
        System.out.println("Disconnected");
        mMeteor.removeCallback(this);
    }

    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        // parse the JSON and manage the data yourself (not recommended)
        // or
        // enable a database (see section "Using databases to manage data") (recommended)
    }

    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        // parse the JSON and manage the data yourself (not recommended)
        // or
        // enable a database (see section "Using databases to manage data") (recommended)
    }

    public void onDataRemoved(String collectionName, String documentID) {
        // parse the JSON and manage the data yourself (not recommended)
        // or
        // enable a database (see section "Using databases to manage data") (recommended)
    }

    public void onException(Exception e) {
    }
    // ********************************************************************************************

    // USUARIOS : Llamada a funciones de la API de Meteor
    // ********************************************************************************************
    //
    public void signupUser(String mUserName, String mEmail, String mPassword, ResultListener listener) {
        // sign up for a new account
        mMeteor.registerAndLogin(mUserName, mEmail, mPassword, listener);
    }

    //
    public String subscribe(final String subscriptionName) {
        return mMeteor.subscribe(subscriptionName);
    }

    //
    public String subscribe(final String subscriptionName, final Object[] params, final SubscribeListener listener) {
        return mMeteor.subscribe(subscriptionName, params, listener);
    }

    //
    public void loginUser(String email, String password, ResultListener listener) {

        if ( ((email == null) || (TextUtils.isEmpty(email))) || ((password == null) || (TextUtils.isEmpty(password))) ) {
            throw new IllegalArgumentException("MyMeteor:loginUser: You must provide either a email and password");
        }

        mMeteor.loginWithEmail(email, password, listener);
    }

    // -------------------------------------------------------------------------------------------------------
    public void loginUserWithGoogle(String google_id, ResultListener listener) {
        if ( (google_id == null) || (TextUtils.isEmpty(google_id)) ) {
            throw new IllegalArgumentException("MyMeteor:loginUserWithGoogle: You must provide either a Google ID");
        }

        final Map<String, Object> authData = new HashMap<String, Object>();
        authData.put("loginHint", google_id);

        mMeteor.call("login", new Object[]{authData}, listener);
    }
    // ------------------------------------------------------------------------------------------------------

    // -------------------------------------------------------------------------------------------------------
    public void loginUserWithFacebook(String facebook_id, ResultListener listener) {
        if ( (facebook_id == null) || (TextUtils.isEmpty(facebook_id)) ) {
            throw new IllegalArgumentException("MyMeteor:loginUserWithGoogle: You must provide either a Facebook ID");
        }

        final Map<String, Object> authData = new HashMap<String, Object>();
        authData.put("loginHint", facebook_id);

        mMeteor.call("login", new Object[]{authData}, listener);
    }
    // ------------------------------------------------------------------------------------------------------

    //
    public void Logout(ResultListener listener) {
        mMeteor.logout(listener);
    }

    //
    public void Logout() {
        mMeteor.logout();
    }

    //
    public void setUsername(final String userId, final String newUsername) {
        if ( (userId == null) || (newUsername == null) ) {
            throw new IllegalArgumentException("MyMeteor:setUsername: You must provide either a userId and newUsername");
        }

        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("userId", userId);
        data.put("newUsername", newUsername);

        mMeteor.call("update", new Object[]{ data });
    }

    //
    public void changePassword(final String oldPassword, final String newPassword, ResultListener listener) {
        if ( (oldPassword == null) || (newPassword == null) ) {
            throw new IllegalArgumentException("You must provide either a Passwords");
        }

        final Map<String, Object> authData = new HashMap<String, Object>();
        authData.put("oldPassword", oldPassword);
        authData.put("newPassword", newPassword);

        mMeteor.call("changePassword", new Object[]{authData}, listener);
    }
    // ********************************************************************************************


    // USUARIOS : Acceso a la DB de usuarios
    // ********************************************************************************************
    //
    /*
    public void addUser(User user) {

        // Inserting new user into a collection "Users"
        // ----------------------------------------------------------------------------------------
        Map<String, Object> values = new HashMap<String, Object>();
        //values.put("id", "id"); //No añadimos la ID, se genera en el server
        values.put("email", user.getEmail());
        values.put("username", user.getUsername());
        //values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("surname", user.getSurname());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("place", user.getPlace());
        values.put("music_style", user.getMusicStyle());
        values.put("image", user.getImage());

        mMeteor.insert(Users, values);
        // ----------------------------------------------------------------------------------------
    }
    */

    //
    public User getUserWithId(String id) {

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Users);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:Collection: " + collection);

        // Get Document ID is "wjQvNQ6sGjzLMDyiJ"
        //Document document = collection.getDocument("001");

        // Get of Collection "Users" where Age of all users is equal to 30
        //Query query = collection.whereEqual("age", 30);

        // Get of Collection "Users" where ID of user is "001", your name.
        //Object field = document.getField("name");

        // Get of Collection "Users" where Age of all users is equal to 30
        Document document = collection.getDocument(id);

        //Query query = collection.whereEqual("_id", id);
        //Document document = query.findOne();
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:Document: " + document);


        // VER TODOS LOS CAMPOS DEL ODCUMENT
        // ----------------------------------------------------------------------------------------
        /*
        String[] fields = document.getFieldNames();
        for (String field : fields) {

            Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:FIELD: " + field);
        }
        */
        // ----------------------------------------------------------------------------------------

        // COMO SE GUARDAN LOS DATOS EN LA DB DEL SERVIDOR
        // ----------------------------------------------------------------------------------------
        /*
        emails=
        [
        {address=mim@gmail.com, verified=false}
        ]
        username=mim,
        name=,
        surname=,
        gender=,
        birthday=,
        place=,
        music_style=,
        image=,

        createdAt={$date=1464178903761},

        followers=[],
        following=[],

        festivals_created=[],
        festivals_assisted=[],
        */
        // ----------------------------------------------------------------------------------------

        //String id = document.getId();

        //String email = (String) document.getField("email");
        ArrayList emails = (ArrayList) document.getField("emails");//ArrayList de {LinkedHashMap}
        LinkedHashMap email_complete = (LinkedHashMap) emails.get(0); // email_complete = LinkedHashMap = {(String)address (boolean)verified}
        String email = (String) email_complete.get("address");

        String user_name = (String) document.getField("username");
        String password = null; //(String) document.getField("password");
        String name = (String) document.getField("name");
        String surname = (String) document.getField("surname");
        String gender = (String) document.getField("gender");
        String birthday= (String) document.getField("birthday");
        String place = (String) document.getField("place");
        String music_style= (String) document.getField("music_style");
        String image = (String) document.getField("image");

        String google_id = null; //(String) document.getField("google_id");
        String facebook_id = null; //(String) document.getField("facebook_id");

        ArrayList festivals_created = (ArrayList) document.getField("festivals_created");
        ArrayList festivals_assisted = (ArrayList) document.getField("festivals_assisted");

        String location = MyState.getUser().getLocation();
        String language = MyState.getUser().getLanguage();

        // IMPRIMIR LOS VALORES RECOGIDOS DE LA DB DEL SERVIDOR
        // ----------------------------------------------------------------------------------------
        /*
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:ID: " + id);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:EMAIL: " + email);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:USER_NAME: " + user_name);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:PASSWORD: " + password);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:NAME: " + name);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:SURNAME: " + surname);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:GENDER: " + gender);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:BIRTHDAY: " + birthday);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:PLACE: " + place);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:MUSIC_STYLE: " + music_style);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:IMAGE: " + image);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:GOOGLE_ID: " + google_id);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:FACEBOOK_ID: " + facebook_id);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:FESTIVALS_CREATED: " + festivals_created);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:FESTIVALS_ASSISTED: " + festivals_assisted);
        */
        // ----------------------------------------------------------------------------------------

        return new User(id, email, user_name, password, name, surname, gender, birthday, place,
                music_style, image, google_id, facebook_id, festivals_created, festivals_assisted, location, language);
    }

    //
    public User getUserWithFacebook(String facebook_id ) {

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Users);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithFacebook:Collection: " + collection);

        // Get of Collection "Users" where Age of all users is equal to 30
        Query query = collection.whereEqual("facebook_id", facebook_id);
        Document document = query.findOne();
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithFacebook:Document: " + document);

        // ----------------------------------------------------------------------------------------
        String id = document.getId();

        ArrayList emails = (ArrayList) document.getField("emails");//ArrayList de {LinkedHashMap}
        LinkedHashMap email_complete = (LinkedHashMap) emails.get(0); // email_complete = LinkedHashMap = {(String)address (boolean)verified}
        String email = (String) email_complete.get("address");

        String user_name = (String) document.getField("username");
        String password = null; //(String) document.getField("password");
        String name = (String) document.getField("name");
        String surname = (String) document.getField("surname");
        String gender = (String) document.getField("gender");
        String birthday= (String) document.getField("birthday");
        String place = (String) document.getField("place");
        String music_style= (String) document.getField("music_style");
        String image = (String) document.getField("image");

        String google_id = null; //(String) document.getField("google_id");
        //String facebook_id = null; //(String) document.getField("facebook_id");

        ArrayList festivals_created = (ArrayList) document.getField("festivals_created");
        ArrayList festivals_assisted = (ArrayList) document.getField("festivals_assisted");

        String location = MyState.getUser().getLocation();
        String language = MyState.getUser().getLanguage();
        // ----------------------------------------------------------------------------------------

        return new User(id, email, user_name, password, name, surname, gender, birthday, place,
                music_style, image, google_id, facebook_id, festivals_created, festivals_assisted, location, language);
    }

    //
    public User getUserWithGoogle(String google_id) {

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Users);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithGoogle:Collection: " + collection);

        // Get of Collection "Users" where Age of all users is equal to 30
        Query query = collection.whereEqual("google_id", google_id);
        Document document = query.findOne();
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithGoogle:Document: " + document);

        // ----------------------------------------------------------------------------------------
        String id = document.getId();

        ArrayList emails = (ArrayList) document.getField("emails");//ArrayList de {LinkedHashMap}
        LinkedHashMap email_complete = (LinkedHashMap) emails.get(0); // email_complete = LinkedHashMap = {(String)address (boolean)verified}
        String email = (String) email_complete.get("address");

        String user_name = (String) document.getField("username");
        String password = null; //(String) document.getField("password");
        String name = (String) document.getField("name");
        String surname = (String) document.getField("surname");
        String gender = (String) document.getField("gender");
        String birthday= (String) document.getField("birthday");
        String place = (String) document.getField("place");
        String music_style= (String) document.getField("music_style");
        String image = (String) document.getField("image");

        //String google_id = null; //(String) document.getField("google_id");
        String facebook_id = null; //(String) document.getField("facebook_id");

        ArrayList festivals_created = (ArrayList) document.getField("festivals_created");
        ArrayList festivals_assisted = (ArrayList) document.getField("festivals_assisted");

        String location = MyState.getUser().getLocation();
        String language = MyState.getUser().getLanguage();
        // ----------------------------------------------------------------------------------------

        return new User(id, email, user_name, password, name, surname, gender, birthday, place,
                music_style, image, google_id, facebook_id, festivals_created, festivals_assisted, location, language);
    }

    //
    public void updateUser(User user) {

        // QUERY: ID of user that update
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("_id", user.getID());

        // Use the command "$set" for each value
        Map<String, Object> set = new HashMap<String, Object>();

        // Values of User
        Map<String, Object> values = new HashMap<String, Object>();

        // ------------------------
        //EJEMPLO $SET
        //$set:{"nombre":"mom"}
        // ------------------------

        // ACTUALIZACION DE LOS CAMPOS
        // ****************************************************************************************
        // ID
        //values.put("_id", user.getID());

        // EMAIL
        // ---------------------------------------------------
        LinkedHashMap<String, java.io.Serializable> email_complete = new LinkedHashMap<String, java.io.Serializable>();
        email_complete.put("address", user.getEmail());
        email_complete.put("verified", false);

        ArrayList<LinkedHashMap<String, java.io.Serializable>> emails = new ArrayList<LinkedHashMap<String, java.io.Serializable>>();
        emails.add(email_complete);

        //value = new HashMap<String, Object>();
        values.put("emails", emails);
        //params.put("$set", value);
        // ---------------------------------------------------

        // USER_NAME
        //value = new HashMap<String, Object>();
        values.put("username", user.getUsername());
        //params.put("$set", value);

        // PASSWORD
        //value = new HashMap<String, Object>();
        //value.put("password", user.getPassword());
        //params.put("$set", value);

        // NAME
        //value = new HashMap<String, Object>();
        values.put("name", user.getName());
        //params.put("$set", value);

        // SURNAME
        //value = new HashMap<String, Object>();
        values.put("surname", user.getSurname());
        //params.put("$set", value);

        // GENDER
        //value = new HashMap<String, Object>();
        values.put("gender", user.getGender());
        //params.put("$set", value);

        // BIRTHDAY
        //value = new HashMap<String, Object>();
        values.put("birthday", user.getBirthday());
        //params.put("$set", value);

        // PLACE
        //value = new HashMap<String, Object>();
        values.put("place", user.getPlace());
        //params.put("$set", value);

        // MUSIC_STYLE
        //value = new HashMap<String, Object>();
        values.put("music_style", user.getMusicStyle());
        //params.put("$set", value);

        // IMAGE
        //value = new HashMap<String, Object>();
        values.put("image", user.getImage());
        //params.put("$set", value);

        // GOOGLE_ID
        //value = new HashMap<String, Object>();
        values.put("google_id", user.getGoogle_id());
        //params.put("$set", value);

        // FACEBOOK_ID
        //value = new HashMap<String, Object>();
        values.put("facebook_id", user.getFacebook_id());
        //params.put("$set", value);

        // FESTIVALS_CREATED
        //value = new HashMap<String, Object>();
        values.put("festivals_created", user.getFestivalsCreated());
        //params.put("$set", value);

        // FESTIVALS_ASSISTED
        //value = new HashMap<String, Object>();
        values.put("festivals_assisted", user.getfestivalsAssisted());
        //params.put("$set", value);
        // ****************************************************************************************

        set.put("$set", values);

        mMeteor.update(Users, query, set);
    }

    //
    public void registerUserEvent(User user, String nameEvent) {

        // Get ID of user that update
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("_id", user.getID());

        // Updating user into a collection "Users"
        Map<String, Object> values = new HashMap<String, Object>();
        ArrayList festivales_asistidos = user.getfestivalsAssisted();
        festivales_asistidos.add(nameEvent);
        values.put("festivals_assisted", festivales_asistidos);

        mMeteor.update(Users, query, values);
    }

    //
    public void unregisterUserEvent(User user, String nameEvent) {

        // Get ID of user that update
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("_id", user.getID());

        // Updating user into a collection "Users"
        Map<String, Object> values = new HashMap<String, Object>();
        ArrayList festivales_asistidos = user.getfestivalsAssisted();
        festivales_asistidos.remove(nameEvent);
        values.put("festivals_assisted", festivales_asistidos);

        mMeteor.update(Users, query, values);
    }
    // ********************************************************************************************

    // AÑADIDO: EVENTS
    // ********************************************************************************************
    //
    public String addEvent(Event event) {

        // Inserting new event into a collection "Events"
        // ----------------------------------------------------------------------------------------
        Map<String, Object> values = new HashMap<String, Object>();
        //values.put("_id", "my-id");
        values.put("photo", event.getPhoto());
        values.put("name", event.getName());
        values.put("description", event.getDescription());

        values.put("place", event.getPlace());
        values.put("firstDay", event.getFirstDay());
        values.put("lastDay", event.getLastDay());

        values.put("capacity", event.getCapacity());
        values.put("assistants", event.getAssistants());

        values.put("sales", event.getSales());
        values.put("webpage", event.getWebpage());
        values.put("contact_number", event.getContact_number());

        values.put("creator", event.getCreator());

        mMeteor.insert(Events, values);
        // ----------------------------------------------------------------------------------------

        // Get ID event of the new event
        // ----------------------------------------------------------------------------------------
        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Events);

        // Get of Collection "Users" where Age of all users is equal to 30
        Query query = collection.whereEqual("name", event.getName());
        Document document = query.findOne();

        return document.getId();
        // ----------------------------------------------------------------------------------------
    }

    //
    public Event getEventWithID(String id) {

        // ----------------------------------------------------------------------------------------
        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Events"
        Collection collection = database.getCollection(Events);
        Log.i(TAG, "ENTRO A MyMeteor:getEventWithID:Collection: " + collection);

        // Get of Collection "Events" where id is "id"
        Document document = collection.getDocument(id);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:Document: " + document);

        //String id = document.getId();
        String photo = (String) document.getField("photo");
        String name = (String) document.getField("name");
        String description = (String) document.getField("descripton");

        String place = (String) document.getField("place");
        String firstDay = (String) document.getField("firstDay");
        String lastDay = (String) document.getField("lastDay");

        int capacity = (int) document.getField("capacity");
        int assistants = (int) document.getField("assistants");

        String sales = (String) document.getField("sales");
        String webpage = (String) document.getField("webpage");
        int contact_number = (int) document.getField("contact_number");

        String creator = (String) document.getField("creator");

        return new Event(id, photo, name, description, place, firstDay, lastDay, capacity,
                assistants, sales, webpage, contact_number, creator);
        // ----------------------------------------------------------------------------------------
    }

    //
    public ArrayList<Event> getAllEvents(ArrayList<String> nameEvents /*Las ids son los nombres de los eventos*/) {

        int capacity, assistants, contact_number;
        String id, photo, name, description, place, firstDay, lastDay, sales, webpage, creator;

        Event event;
        ArrayList<Event> list = new ArrayList<>();

        Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:mMeteor: " + mMeteor);

        try {
            // Get Database
            Database database = mMeteor.getDatabase();
            Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:database: " + database);

            // Get Collection name is "Events"
            Collection collection = database.getCollection(Events);
            Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:collection: " + collection);

            Query query;
            Document document;
            // Get of Collection all "Events" content in "ids"
            for (String nameEvent : nameEvents) {

                query = collection.whereEqual("name", nameEvent);
                document = query.findOne();
                Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:ID_EVENT: " + nameEvent);
                Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:DOCUMENT: " + document);

                id = document.getId();
                photo = (String) document.getField("photo");
                name = (String) document.getField("name");
                description = (String) document.getField("description");

                place = (String) document.getField("place");
                firstDay = (String) document.getField("firstDay");
                lastDay = (String) document.getField("lastDay");

                capacity = (int) document.getField("capacity");
                assistants = (int) document.getField("assistants");

                sales = (String) document.getField("sales");
                webpage = (String) document.getField("webpage");

                // Esto es asi, porque en la DB del server, a veces este valor se guarda como "int" y otras como "string"
                // -------------------------------------------------------------------------------
                Object object = document.getField("contact_number");
                //Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:Contact_Number CLASS?: " + o.getClass());
                if (object instanceof String) {
                    if ( (!TextUtils.isEmpty((String)object)) ) {
                        contact_number = Integer.parseInt( (String) object );
                    } else {
                        contact_number = 0;
                    }
                } else if(object instanceof Integer) {
                    contact_number = (int) object;
                } else {
                    contact_number = -1; //ERROR ...
                }
                // -------------------------------------------------------------------------------

                creator = (String) document.getField("creator");

                event = new Event(id, photo, name, description, place, firstDay, lastDay, capacity,
                        assistants, sales, webpage, contact_number, creator);

                list.add(event);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //
    public ArrayList<Event> getAllEvents(String location) {

        int capacity, assistants, contact_number;
        String id, photo, name, description, place, firstDay, lastDay, sales, webpage, creator;

        Event event;
        ArrayList<Event> list = new ArrayList<>();

        Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:mMeteor: " + mMeteor);

        try {

            // Get Database
            Database database = mMeteor.getDatabase();

            Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:database: " + database);

            // Get Collection name is "Events"
            Collection collection = database.getCollection(Events);

            Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:collection: " + collection);

            // Get of Collection "Events" where "place" of all events is equal to "location"
            //Query query = collection.whereEqual("place", location);

            // Create a list of events
            //Document[] documents = query.find();

            Document[] documents = collection.find();

            for (Document doc : documents) {

                place = (String) doc.getField("place");
                //if (place.contains(location)) {

                id = doc.getId();

                photo = (String) doc.getField("photo");
                name = (String) doc.getField("name");

                Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:NAME: " + name);
                Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:IMAGE: " + photo);

                description = (String) doc.getField("description");

                //place = (String) doc.getField("place");
                firstDay = (String) doc.getField("firstDay");
                lastDay = (String) doc.getField("lastDay");

                //
                //Object o = doc.getField("capacity");
                //Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:Capacity CLASS?: " + o.getClass());
                //

                capacity = (int) doc.getField("capacity");
                assistants = (int) doc.getField("assistants");

                sales = (String) doc.getField("sales");
                webpage = (String) doc.getField("webpage");

                // Esto es asi, porque en la DB del server, a veces este valor se guarda como "int" y otras como "string"
                // -------------------------------------------------------------------------------
                Object object = doc.getField("contact_number");
                //Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:Contact_Number CLASS?: " + o.getClass());
                if (object instanceof String) {
                    if ( (!TextUtils.isEmpty((String)object)) ) {
                        contact_number = Integer.parseInt( (String) object );
                    } else {
                        contact_number = 0;
                    }
                } else if(object instanceof Integer) {
                    contact_number = (int) object;
                } else {
                    contact_number = -1; //ERROR ...
                }
                // -------------------------------------------------------------------------------

                creator = (String) doc.getField("creator");

                event = new Event(id, photo, name, description, place, firstDay, lastDay, capacity,
                        assistants, sales, webpage, contact_number, creator);

                list.add(event);
                //}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //
    public ArrayList<Event> getAllEvents(String location, String date) {

        int capacity, assistants, contact_number;
        String id, photo, name, description, place, firstDay, lastDay, sales, webpage, creator;

        Date mDate, mFirstDay, mLastDate;
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            mDate = format.parse(date);
        }
        catch (Exception e) {
            mDate = null;
            e.printStackTrace();
        }

        Event event;
        ArrayList<Event> list = new ArrayList<>();

        try {

            // Get Database
            Database database = mMeteor.getDatabase();

            // Get Collection name is "Events"
            Collection collection = database.getCollection(Events);

            // Get of Collection "Events" where "place" of all events is equal to "location"
            //Query query = collection.whereEqual("place", location);
            // Create a list of events
            //Document[] documents = query.find();

            Document[] documents = collection.find();

            for (Document doc : documents) {

                place = (String) doc.getField("place");
                //if (place.contains(location)) {

                firstDay = (String) doc.getField("firstDay");
                try {
                    mFirstDay = format.parse(firstDay);
                } catch (Exception e) {
                    mFirstDay = null;
                    e.printStackTrace();
                }

                lastDay = (String) doc.getField("lastDay");
                try {
                    mLastDate = format.parse(lastDay);
                } catch (Exception e) {
                    mLastDate = null;
                    e.printStackTrace();
                }

                if ((mDate != null) && (mFirstDay != null) && (mLastDate != null)) {

                    //(firstDay <= dia) && (dia <= lastDay)
                    if ((mDate.after(mFirstDay)) && (mDate.before(mLastDate))) {

                        //Añadimos este evento al ArrayList
                        id = doc.getId();

                        photo = (String) doc.getField("photo");
                        name = (String) doc.getField("name");
                        description = (String) doc.getField("descripton");

                        //place = (String) doc.getField("place");
                        //firstDay = (String) doc.getField("firstDay");
                        //lastDay = (String) doc.getField("lastDay");

                        capacity = (int) doc.getField("capacity");
                        assistants = (int) doc.getField("assistants");

                        sales = (String) doc.getField("sales");
                        webpage = (String) doc.getField("webpage");

                        // Esto es asi, porque en la DB del server, a veces este valor se guarda como "int" y otras como "string"
                        // -------------------------------------------------------------------------------
                        Object object = doc.getField("contact_number");
                        //Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:Contact_Number CLASS?: " + o.getClass());
                        if (object instanceof String) {
                            if ( (!TextUtils.isEmpty((String)object)) ) {
                                contact_number = Integer.parseInt( (String) object );
                            } else {
                                contact_number = 0;
                            }
                        } else if(object instanceof Integer) {
                            contact_number = (int) object;
                        } else {
                            contact_number = -1; //ERROR ...
                        }
                        // -------------------------------------------------------------------------------

                        creator = (String) doc.getField("creator");

                        event = new Event(id, photo, name, description, place, firstDay, lastDay, capacity,
                                assistants, sales, webpage, contact_number, creator);

                        list.add(event);
                    }
                }
                //}
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    //
    public void removeEvent(Event event) {
        mMeteor.remove(Events, event.getID());
        //mMeteor.remove(Events, event.getName());
    }

    //
    public void updateEvent(Event event) {
        // Get NAME of event that update
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("_id", event.getID());

        // Updating user into a collection "Users"
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("photo", event.getPhoto());
        values.put("name", event.getName());
        values.put("description", event.getDescription());

        values.put("place", event.getPlace());
        values.put("firstDay", event.getFirstDay());
        values.put("lastDay", event.getLastDay());

        values.put("capacity", event.getCapacity());
        values.put("assistants", event.getAssistants());

        values.put("sales", event.getSales());
        values.put("webpage", event.getWebpage());
        values.put("contact_number", event.getContact_number());

        values.put("creator", event.getCreator());

        mMeteor.update(Users, query, values);
    }
    // ********************************************************************************************
}
