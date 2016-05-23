package com.example.rachid.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
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
    private String Users = "Accounts";
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
        mMeteor = new Meteor(activity, "ws://example.meteor.com/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);
    }

    public MyMeteor(Fragment f){

        fragment = f;

        // enable logging of internal events for the library
        Meteor.setLoggingEnabled(true);

        // create a new instance
        mMeteor = new Meteor(fragment.getContext(), "ws://example.meteor.com/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);
    }

    // *****************
    // *** FUNCIONES ***
    // *****************
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
        else {
            Log.i(TAG, "ENTRO A MyMeteor:onConnect: NOT Successfully logged in automatically");

            // sign in to the server
            mMeteor.loginWithUsername("USER", "PASSWORD", new ResultListener() {

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
                    mMeteor.registerAndLogin("USER", "EMAIL", "PASSWORD", new ResultListener() {

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

    // AÑADIDO: USERS
    // ********************************************************************************************
    //
    public String addUser(User user) {

        // Inserting new user into a collection "Users"
        // ----------------------------------------------------------------------------------------
        Map<String, Object> values = new HashMap<String, Object>();
        //values.put("_id", "my-id");
        values.put("email", user.getEmail());
        values.put("user_name", user.getUsername());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("surname", user.getSurname());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("place", user.getPlace());
        values.put("music_style", user.getMusicStyle());
        values.put("image", user.getImage());

        mMeteor.insert(Users, values);
        // ----------------------------------------------------------------------------------------

        // Get ID user of the new user
        // ----------------------------------------------------------------------------------------
        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Users);

        // Get of Collection "Users" where Age of all users is equal to 30
        Query query = collection.whereEqual("email", user.getEmail());
        Document document = query.findOne();

        return document.getId();
        // ----------------------------------------------------------------------------------------
    }

    //
    public void updateUser(User user) {

        // Get ID of user that update
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("_id", user.getID());

        // Updating user into a collection "Users"
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("email", user.getEmail());
        values.put("user_name", user.getUsername());
        values.put("password", user.getPassword());
        values.put("name", user.getName());
        values.put("surname", user.getSurname());
        values.put("gender", user.getGender());
        values.put("birthday", user.getBirthday());
        values.put("place", user.getPlace());
        values.put("music_style", user.getMusicStyle());
        values.put("image", user.getImage());

        mMeteor.update(Users, query, values);
    }

    //
    public void changePassword(String _id, String oldPassword, String newPassword) {

        // Actualizamos la Pass
        // ---------------------------------------------------------------------------------------
        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Users);

        // Get of Collection "Users" where Age of all users is equal to 30
        Query query = collection.whereEqual("_id", _id);
        Document document = query.findOne();

        String password = (String) document.getField("password");
        if ( (oldPassword != null) & (oldPassword.equals(password)) ) { // La verificacion a sido superada

            // TODO: Actualizamos la password de este usuario.
            // Get ID of user that update
            Map<String, Object> query_2 = new HashMap<String, Object>();
            query_2.put("_id", _id);

            // Updating user into a collection "Users"
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("password", newPassword);

            mMeteor.update(Users, query_2, values);
        }
        // ---------------------------------------------------------------------------------------
    }

    //
    public User getUser(String _id) {

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Users);

        // Get Document ID is "wjQvNQ6sGjzLMDyiJ"
        //Document document = collection.getDocument("001");

        // Get of Collection "Users" where Age of all users is equal to 30
        //Query query = collection.whereEqual("age", 30);

        // Get of Collection "Users" where ID of user is "001", your name.
        //Object field = document.getField("name");

        // Get of Collection "Users" where Age of all users is equal to 30
        Query query = collection.whereEqual("_id", _id);
        Document document = query.findOne();

        String id = document.getId();
        String email = (String) document.getField("email");
        String user_name = (String) document.getField("user_name");
        String password = (String) document.getField("password");
        String name = (String) document.getField("name");
        String surname = (String) document.getField("surname");
        String gender = (String) document.getField("gender");
        String birthday= (String) document.getField("birthday");
        String place = (String) document.getField("place");
        String music_style= (String) document.getField("music_style");
        String image = (String) document.getField("image");
        String location = MyState.getUser().getLocation();

        User user = new User(id, email, user_name, password, name, surname, gender, birthday, place, music_style, image, location);

        return user;
    }

    //
    public User getUser(String _email, String _password) {

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Users"
        Collection collection = database.getCollection(Users);

        // Get of Collection "Users" where Age of all users is equal to 30
        Query query = collection.whereEqual("email", _email);
        Document document = query.findOne();

        String password = (String) document.getField("password");
        if (_password != null) {
            if (_password.equals(password)) {

                String id = document.getId();
                String email = (String) document.getField("email");
                String user_name = (String) document.getField("user_name");
                //String password = (String) document.getField("password");
                String name = (String) document.getField("name");
                String surname = (String) document.getField("surname");
                String gender = (String) document.getField("gender");
                String birthday= (String) document.getField("birthday");
                String place = (String) document.getField("place");
                String music_style= (String) document.getField("music_style");
                String image = (String) document.getField("image");
                String location = MyState.getUser().getLocation();

                User user = new User(id, email, user_name, password, name, surname, gender, birthday, place, music_style, image, location);
                return user;
            }
        }

        return null;
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

    //
    public Event getEvent(String _id) {

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Events"
        Collection collection = database.getCollection(Events);

        // Get of Collection "Events" where "place" of all events is equal to "location"
        Query query = collection.whereEqual("_id", _id);

        Document document = query.findOne();

        String id = document.getId();

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

        Event event = new Event(id, photo, name, description, place, firstDay, lastDay, capacity,
                assistants, sales, webpage, contact_number, creator);

        return event;
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
                    contact_number = (int) doc.getField("contact_number");

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

        Event event = null;
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
                            contact_number = (int) doc.getField("contact_number");

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
    public ArrayList<Event> getRegisteredEvents(String userID) {

        ArrayList<Event> list = new ArrayList<>();

        try {

            //TODO: Terminar esto ...
            // ...

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    //
    public ArrayList<Event> getPublishedEvents(String userID) {

        ArrayList<Event> list = new ArrayList<>();

        //TODO: Terminar esto ...
        // ...

        return list;
    }
    // ********************************************************************************************
}
