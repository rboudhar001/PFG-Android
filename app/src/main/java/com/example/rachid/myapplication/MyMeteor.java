package com.example.rachid.myapplication;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.ddp.Meteor;
import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.db.Collection;
import im.delight.android.ddp.db.Database;
import im.delight.android.ddp.db.Document;
import im.delight.android.ddp.db.Query;

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

    private static String TAG = "MyMeteor";
    private Activity activity;
    private Fragment fragment;
    private Meteor mMeteor;

    // Añadido: Collection Names
    // -------------------------------------------------------------------------------------------
    private String Users = "Accounts";
    private String Events = "festivals";
    // -------------------------------------------------------------------------------------------

    public MyMeteor(Activity a){

        activity = a;

        // create a new instance
        mMeteor = new Meteor(activity, "ws://example.meteor.com/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);
    }

    public MyMeteor(Fragment f){

        fragment = f;

        // create a new instance
        mMeteor = new Meteor(fragment.getContext(), "ws://example.meteor.com/websocket");

        // register the callback that will handle events and receive messages
        mMeteor.addCallback(this);
    }

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

            /*
            // sign up for a new account
            mMeteor.registerAndLogin("john-doe", "john.doe@example.com", "password1", new ResultListener() {

                @Override
                public void onSuccess(String result) {
                    System.out.println("Successfully registered: "+result);
                }

                @Override
                public void onError(String error, String reason, String details) {
                    System.out.println("Could not register: "+error+" / "+reason+" / "+details);
                }

            });
            */

            // sign in to the server
            mMeteor.loginWithUsername("USER_NAME", "PASSWORD", new ResultListener() {

                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: Successfully logged in: " + result);

                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: Is logged in: " + mMeteor.isLoggedIn());
                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: User ID: " + mMeteor.getUserId());
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.i(TAG, "ENTRO A MyMeteor:onConnect:loginWithUsername: Could not log in: " + error + " / " + reason + " / " + details);
                }

            });
        }

        /*
        // subscribe to data from the server
        String subscriptionId = mMeteor.subscribe("publicMessages");

        // unsubscribe from data again (usually done later or not at all)
        mMeteor.unsubscribe(subscriptionId);

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

        String id = document.getId();

        return id;
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
        values.put("image", event.getImage());
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
        values.put("created_on", event.getCreated_on());

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

        String id = document.getId();

        return id;
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
        values.put("image", event.getImage());
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
        values.put("created_on", event.getCreated_on());

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

        String image = (String) document.getField("image");
        String name = (String) document.getField("name");
        String description = (String) document.getField("descripton");

        String place = (String) document.getField("place");
        String firstDay = (String) document.getField("firstDay");
        String lastDay = (String) document.getField("lastDay");

        String capacity = (String) document.getField("capacity");
        String assistants = (String) document.getField("assistants");

        String sales = (String) document.getField("sales");
        String webpage = (String) document.getField("webpage");
        String contact_number = (String) document.getField("contact_number");

        String creator = (String) document.getField("creator");
        String created_on = (String) document.getField("created_on");

        Event event = new Event(id, image, name, description, place, firstDay, lastDay, capacity,
                assistants, sales, webpage, contact_number, creator, created_on);

        return event;
    }

    //
    public ArrayList<Event> getAllEvents(String location) {

        Event event;
        ArrayList<Event> list = new ArrayList<>();

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Events"
        Collection collection = database.getCollection(Events);

        // Get of Collection "Events" where "place" of all events is equal to "location"
        Query query = collection.whereEqual("place", location);

        // Create a list of events
        Document[] documents = query.find();
        for (Document doc : documents) {
            String id = doc.getId();

            String image = (String) doc.getField("image");
            String name = (String) doc.getField("name");
            String description = (String) doc.getField("descripton");

            String place = (String) doc.getField("place");
            String firstDay = (String) doc.getField("firstDay");
            String lastDay = (String) doc.getField("lastDay");

            String capacity = (String) doc.getField("capacity");
            String assistants = (String) doc.getField("assistants");

            String sales = (String) doc.getField("sales");
            String webpage = (String) doc.getField("webpage");
            String contact_number = (String) doc.getField("contact_number");

            String creator = (String) doc.getField("creator");
            String created_on = (String) doc.getField("created_on");

            event = new Event(id, image, name, description, place, firstDay, lastDay, capacity,
                    assistants, sales, webpage, contact_number, creator, created_on);

            list.add(event);
        }

        return list;
    }

    //
    public ArrayList<Event> getAllEvents(String location, String date) {

        Event event;
        ArrayList<Event> list = new ArrayList<>();

        // Get Database
        Database database = mMeteor.getDatabase();

        // Get Collection name is "Events"
        Collection collection = database.getCollection(Events);

        // Get of Collection "Events" where "place" of all events is equal to "location"
        Query query = collection.whereEqual("place", location);

        String[] pieces;
        Calendar mDate = null;
        Calendar mFirstDate = null;
        Calendar mLastDate = null;
        int Day, Month, Year, fDay, fMonth, fYear, lDay, lMonth, lYear;

        pieces = date.split("/");
        Day = Integer.parseInt(pieces[0]);
        Month = Integer.parseInt(pieces[1]);
        Year = Integer.parseInt(pieces[2]);
        mDate.set(Year, Month, Day);

        // Create a list of events
        Document[] documents = query.find();
        for (Document doc : documents) {

            String firstDay = (String) doc.getField("firstDay");
            pieces = firstDay.split("/");
            fDay = Integer.parseInt(pieces[0]);
            fMonth = Integer.parseInt(pieces[1]);
            fYear = Integer.parseInt(pieces[2]);
            mFirstDate.set(fYear, fMonth, fDay);

            String lastDay = (String) doc.getField("lastDay");
            pieces = lastDay.split("/");
            lDay = Integer.parseInt(pieces[0]);
            lMonth = Integer.parseInt(pieces[1]);
            lYear = Integer.parseInt(pieces[2]);
            mLastDate.set(lYear, lMonth, lDay);

            //(firstDay <= dia) && (dia <= lastDay)
            if ( (mDate.after(mFirstDate)) && (mDate.before(mLastDate)) ) {

                //Añadimos este evento al ArrayList
                String id = doc.getId();

                String image = (String) doc.getField("image");
                String name = (String) doc.getField("name");
                String description = (String) doc.getField("descripton");

                String place = (String) doc.getField("place");
                //String firstDay = (String) doc.getField("firstDay");
                //String lastDay = (String) doc.getField("lastDay");

                String capacity = (String) doc.getField("capacity");
                String assistants = (String) doc.getField("assistants");

                String sales = (String) doc.getField("sales");
                String webpage = (String) doc.getField("webpage");
                String contact_number = (String) doc.getField("contact_number");

                String creator = (String) doc.getField("creator");
                String created_on = (String) doc.getField("created_on");

                event = new Event(id, image, name, description, place, firstDay, lastDay, capacity,
                        assistants, sales, webpage, contact_number, creator, created_on);

                list.add(event);
            }
        }

        return list;
    }

    //
    public ArrayList<Event> getRegisteredEvents(String userID) {

        ArrayList<Event> list = new ArrayList<>();

        //TODO: Terminar esto ...
        // ...

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
