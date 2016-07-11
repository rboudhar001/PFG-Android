package com.example.rachid.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    //
    public boolean isConnected(){
        return mMeteor.isConnected();
    }

    //
    public boolean isLoggedIn(){
        return mMeteor.isLoggedIn();
    }

    //
    public void Disconnect(){
        mMeteor.disconnect();
    }

    //
    public void onConnect(boolean signedInAutomatically) {

        Log.i(TAG, "ENTRO A MyMeteor:onConnect: Connected");
        Log.i(TAG, "ENTRO A MyMeteor:onConnect: Is logged in: " + mMeteor.isLoggedIn());
        Log.i(TAG, "ENTRO A MyMeteor:onConnect: User ID: " + mMeteor.getUserId());

        if ( signedInAutomatically ) {
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

    //
    public void onDisconnect() {
        System.out.println("Disconnected");
        mMeteor.removeCallback(this);
    }

    //
    public void onDataAdded(String collectionName, String documentID, String newValuesJson) {
        // parse the JSON and manage the data yourself (not recommended)
        // or
        // enable a database (see section "Using databases to manage data") (recommended)
    }

    //
    public void onDataChanged(String collectionName, String documentID, String updatedValuesJson, String removedValuesJson) {
        // parse the JSON and manage the data yourself (not recommended)
        // or
        // enable a database (see section "Using databases to manage data") (recommended)
    }

    //
    public void onDataRemoved(String collectionName, String documentID) {
        // parse the JSON and manage the data yourself (not recommended)
        // or
        // enable a database (see section "Using databases to manage data") (recommended)
    }

    //
    public void onException(Exception e) {
    }
    // ********************************************************************************************

    // **********
    // USUARIOS : Llamada a funciones de la API de Meteor
    // **********
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
    public void loginUserWithService(String email, String id, ResultListener listener) {

        final Map<String, Object> userData = new HashMap<String, Object>();
        if (email != null) {
            userData.put("email", email);
        }
        else {
            throw new IllegalArgumentException("You must provide either a email");
        }

        final Map<String, Object> authData = new HashMap<String, Object>();
        authData.put("user", userData);
        authData.put("password", id);

        mMeteor.call("login", new Object[] { authData }, listener);

        /*
        final Map<String, Object> userData = new HashMap<String, Object>();
        userData.put("requestPermissions", new ArrayList<String>() );
        userData.put("requestOfflineToken", false);
        userData.put("loginUrlParameters", new HashMap<String, Object>());
        if (email != null) {
            userData.put("loginHint", email);
        }
        else {
            throw new IllegalArgumentException("You must provide either a email");
        }
        userData.put("loginStyle", "");
        userData.put("redirectUrl", "");

        final Map<String, Object> authData = new HashMap<String, Object>();
        authData.put("options", userData);

        mMeteor.call("loginWithGoogle", new Object[]{ authData }, listener);
        */
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

        mMeteor.call("update", new Object[]{data});
    }

    //
    public void setPassword(final String userID, final String newPassword) {

        if ( (userID == null) || (newPassword == null) ) {
            throw new IllegalArgumentException("You must provide either a userID and Password");
        }

        final Map<String, Object> data = new HashMap<String, Object>();
        data.put("userID", userID);
        data.put("newPassword", newPassword);
        //data.put("logout", false);

        mMeteor.call("setPassword", new Object[]{ data });
    }

    //
    public void changePassword(final String oldPassword, final String newPassword, ResultListener listener) {
        if ( (oldPassword == null) || (newPassword == null) ) {
            throw new IllegalArgumentException("You must provide either a oldPassword and newPassword");
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

        // Get of Collection "Users" where Age of all users is equal to 30
        Document document = collection.getDocument(id);
        Log.i(TAG, "ENTRO A MyMeteor:getUserWithId:Document: " + document);

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

        ArrayList<String> festivals_created = (ArrayList<String>) document.getField("festivals_created");
        ArrayList<String> festivals_assisted = (ArrayList<String>) document.getField("festivals_assisted");

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

        ArrayList<String> festivals_created = (ArrayList<String>) document.getField("festivals_created");
        ArrayList<String> festivals_assisted = (ArrayList<String>) document.getField("festivals_assisted");

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

        ArrayList<String> festivals_created = (ArrayList<String>) document.getField("festivals_created");
        ArrayList<String> festivals_assisted = (ArrayList<String>) document.getField("festivals_assisted");

        String location = MyState.getUser().getLocation();
        String language = MyState.getUser().getLanguage();
        // ----------------------------------------------------------------------------------------

        return new User(id, email, user_name, password, name, surname, gender, birthday, place,
                music_style, image, google_id, facebook_id, festivals_created, festivals_assisted, location, language);
    }

    //
    public void updateUser(User user, final ResultListener listener) {

        // QUERY: ID of user that update
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("_id", user.getID());

        // Use the command "$set" for each value
        Map<String, Object> set = new HashMap<String, Object>();

        // Values of User
        Map<String, Object> values = new HashMap<String, Object>();

        // ACTUALIZACION DE LOS CAMPOS
        // ----------------------------------------------------------------------------------------
        // ID
        //values.put("_id", user.getID());

        // EMAIL
        // ---------------------------------------------------
        LinkedHashMap<String, java.io.Serializable> email_complete = new LinkedHashMap<>();
        email_complete.put("address", user.getEmail());
        email_complete.put("verified", false);

        ArrayList<LinkedHashMap<String, java.io.Serializable>> emails = new ArrayList<>();
        emails.add(email_complete);

        values.put("emails", emails);
        // ---------------------------------------------------

        // USER_NAME
        values.put("username", user.getUsername());

        // PASSWORD
        //value = new HashMap<String, Object>();
        //value.put("password", user.getPassword());
        //params.put("$set", value);

        // NAME
        values.put("name", user.getName());

        // SURNAME
        values.put("surname", user.getSurname());

        // GENDER
        values.put("gender", user.getGender());

        // BIRTHDAY
        values.put("birthday", user.getBirthday());

        // PLACE
        values.put("place", user.getPlace());

        // MUSIC_STYLE
        values.put("music_style", user.getMusicStyle());

        // IMAGE
        values.put("image", user.getImage());

        // GOOGLE_ID
        values.put("google_id", user.getGoogle_id());

        // FACEBOOK_ID
        values.put("facebook_id", user.getFacebook_id());

        // FESTIVALS_CREATED
        values.put("festivals_created", user.getFestivalsCreated());

        // FESTIVALS_ASSISTED
        values.put("festivals_assisted", user.getfestivalsAssisted());
        // ----------------------------------------------------------------------------------------

        set.put("$set", values);
        Map<String, Object> emptyMap = new HashMap<String, Object>();

        mMeteor.update(Users, query, set, emptyMap, listener);
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
        values.put("contact_number", "" + event.getContact_number());

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
        String description = (String) document.getField("description");

        String place = (String) document.getField("place");
        String firstDay = (String) document.getField("firstDay");
        String lastDay = (String) document.getField("lastDay");

        int capacity = (int) document.getField("capacity");
        int assistants = (int) document.getField("assistants");

        String sales = (String) document.getField("sales");
        String webpage = (String) document.getField("webpage");

        // Esto es asi, porque en la DB del server, a veces este valor se guarda como "int" y otras como "string"
        // -------------------------------------------------------------------------------
        Object object = document.getField("contact_number");
        int contact_number;
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

        Date mFirstDay, mLastDay;
        Date mCurrentDate = getCurrentDate();
        Log.i(TAG, "ENTRO A MyMeteor:getAllEvents: CURRENT_DATE: " + mCurrentDate);

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

                // Buscamos eventos por localizacion
                place = (String) doc.getField("place");
                if (place.contains( location )) {

                    firstDay = (String) doc.getField("firstDay");
                    lastDay = (String) doc.getField("lastDay");

                    //mFirstDay = convertDate(firstDay);
                    mLastDay = convertDate(lastDay);

                    //Log.i(TAG, "ENTRO A MyMeteor:getAllEvents: FIRST_DAY: " + mFirstDay);
                    Log.i(TAG, "ENTRO A MyMeteor:getAllEvents: LAST_DAY: " + mLastDay);

                    // Buscamos eventos que no hayan pasado
                    if (mCurrentDate.compareTo(mLastDay) <= 0) {
                    //if ( !mCurrentDate.after(mLastDay) ) {

                        id = doc.getId();

                        photo = (String) doc.getField("photo");
                        name = (String) doc.getField("name");

                        Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:NAME: " + name);
                        Log.i(TAG, "ENTRO A MyMeteor:getAllEvents:IMAGE: " + photo);

                        description = (String) doc.getField("description");

                        //place = (String) doc.getField("place");
                        //firstDay = (String) doc.getField("firstDay");
                        //lastDay = (String) doc.getField("lastDay");

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
                            if ((!TextUtils.isEmpty((String) object))) {
                                contact_number = Integer.parseInt((String) object);
                            } else {
                                contact_number = 0;
                            }
                        } else if (object instanceof Integer) {
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

        Date mFirstDay, mLastDay;
        Date mCurrentDate = getCurrentDate();
        Date mSearchDate = convertDate(date);
        Log.i(TAG, "ENTRO A MyMeteor:getAllEventsWithDate: CURRENT_DATE: " + mCurrentDate);
        Log.i(TAG, "ENTRO A MyMeteor:getAllEventsWithDate: SEARCH_DATE: " + mSearchDate);

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

                // Buscamos eventos por localizacion
                place = (String) doc.getField("place");
                if (place.contains(location)) {

                    firstDay = (String) doc.getField("firstDay");
                    lastDay = (String) doc.getField("lastDay");

                    mFirstDay = convertDate(firstDay);
                    mLastDay = convertDate(lastDay);

                    Log.i(TAG, "ENTRO A MyMeteor:getAllEventsWithDate: FIRST_DAY: " + mFirstDay);
                    Log.i(TAG, "ENTRO A MyMeteor:getAllEventsWithDate: LAST_DAY: " + mLastDay);

                    // Buscamos eventos que no hayan pasado
                    if (mCurrentDate.compareTo(mLastDay) <= 0) {
                    //if ( !mCurrentDate.after( mLastDay ) ) {

                        //Buscamos eventos para la fecha concreta
                        //(firstDay <= dia) && (dia <= lastDay)
                        if ( (0 <= mSearchDate.compareTo(mFirstDay)) && (mSearchDate.compareTo(mLastDay) <= 0) ) {
                        //if ((mSearchDate.after(mFirstDay)) && (mSearchDate.before(mLastDay))) {

                            //Añadimos este evento al ArrayList
                            id = doc.getId();

                            photo = (String) doc.getField("photo");
                            name = (String) doc.getField("name");
                            description = (String) doc.getField("description");

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
                }
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
    public void updateEvent(Event event, final ResultListener listener) {

        // Get NAME of event that update
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("_id", event.getID());

        // Use the command "$set" for each value
        Map<String, Object> set = new HashMap<String, Object>();

        // Values of Event
        Map<String, Object> values = new HashMap<String, Object>();

        // ACTUALIZACION DE LOS CAMPOS
        // ---------------------------------------------------------------------------------------
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
        values.put("contact_number", "" + event.getContact_number());

        values.put("creator", event.getCreator());
        // ---------------------------------------------------------------------------------------

        set.put("$set", values);
        Map<String, Object> emptyMap = new HashMap<String, Object>();

        mMeteor.update(Events, query, set, emptyMap, listener);
    }
    // ********************************************************************************************

    //
    public Date convertDate(String dateString) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate;

        try {

            convertedDate = dateFormat.parse( dateString );

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            convertedDate = new Date();
        }

        return convertedDate;
    }

    public Date getCurrentDate() {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        month = month + 1;  // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
        int day = c.get(Calendar.DAY_OF_MONTH);

        String dateString = "" + day + "/" + month + "/" + year;
        return convertDate(dateString);
    }
}
