package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Rachid on 14/04/2016.
 */
public abstract class MyDatabase {

    public static void inicializate(String TAG, Activity activity) {

        //Abrimos la base de datos
        Database mDB_Activity = new Database(activity, null);
        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) {

                Log.i(TAG, "ENTRO A MyDatabase:inicializate:EMAIL: " + c.getString(1) + ", LOCATION: " + c.getString(15));

                if (c.getString(1) != null) {
                    Log.i(TAG, "ENTRO A MyDatabase:inicializate: OBTENER_EMAIL");
                    MyState.setLoged(true); // Usuario logeado
                }
                if (c.getString(15) != null) {
                    Log.i(TAG, "ENTRO A MyDatabase:inicializate: OBTENER_LOCALIZACION");
                    MyState.setExistsLocation(true); // Usuario con localizacion
                }
                if ((c.getString(1) /*email*/ != null) || (c.getString(15) /*location*/ != null)) {

                    // Creamos el ArrayList de eventos creados por este usuarios
                    // ---------------------------------------------------------------------------
                    ArrayList festivals_created;
                    if (c.getString(13) != null) {
                        festivals_created = new ArrayList();
                        try {
                            JSONObject json = new JSONObject(c.getString(13)); /*festivals_created*/
                            JSONArray jArray = json.optJSONArray("festivals_created");

                            if (jArray != null) {
                                for (int i = 0; i < jArray.length(); i++) {
                                    festivals_created.add(jArray.optString(i));
                                }
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        festivals_created = null;
                    }
                    // ---------------------------------------------------------------------------

                    // Creamos el ArrayList de eventos registrados por este usuarios
                    // ---------------------------------------------------------------------------
                    ArrayList festivals_assisted;
                    if (c.getString(14) != null) {
                        festivals_assisted = new ArrayList();
                        try {
                            JSONObject json = new JSONObject(c.getString(14)); /*festivals_assisted*/
                            JSONArray jArray = json.optJSONArray("festivals_assisted");

                            if (jArray != null) {
                                for (int i = 0; i < jArray.length(); i++) {
                                    festivals_assisted.add(jArray.optString(i));
                                }
                            }
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        festivals_assisted = null;
                    }
                    // ---------------------------------------------------------------------------

                    MyState.setUser(new User(c.getString(0) /*id*/, c.getString(1) /*email*/, c.getString(2) /*user_name*/,
                            c.getString(3) /*password*/, c.getString(4) /*name*/, c.getString(5) /*surname*/, c.getString(6) /*gender*/,
                            c.getString(7) /*birthday*/, c.getString(8) /*place*/, c.getString(9) /*music_style*/,
                            c.getString(10) /*image*/,  c.getString(11) /*google_id*/, c.getString(12) /*facebook_id*/,
                            festivals_created /*google_id*/, festivals_assisted /*facebook_id*/, c.getString(15) /*location*/));
                }
            }
            c.close();
            db.close();
        }
    }

    //
    public static void insertLocation(String TAG, Activity activity, String city) {

        Log.i(TAG, "ENTRO A MyDatabase:insertLocation:0");

        String email = "";
        Boolean existsAccount = false;

        //Abrimos la base de datos
        Database mDB_Activity = new Database(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la cuenta
                if (c.getString(1) != null) {
                    Log.i(TAG, "ENTRO A MyDatabase:insertLocation:1");

                    existsAccount = true;
                    email = c.getString(1);
                }
            }
            c.close();
            db.close();
        }

        db = mDB_Activity.getWritableDatabase();
        if (existsAccount) { //Existe la cuenta
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:insertLocation:2");

                //Actualizamos la localizacion
                ContentValues valores = new ContentValues();
                valores.put("location", city);

                String[] args = new String[]{email};

                db.update("Users", valores, "email=?", args);
                db.close();
            }
        } else { // No existe la cuenta
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:insertLocation:3");

                //Insertamos la nueva cuenta con la localización
                ContentValues valores = new ContentValues();
                valores.put("id", (String)null);
                valores.put("email", (String)null);
                valores.put("user_name", (String)null);
                valores.put("password", (String)null);
                valores.put("name", (String)null);
                valores.put("surname", (String)null);
                valores.put("gender", (String)null);
                valores.put("birthday", (String)null);
                valores.put("place", (String)null);
                valores.put("music_style", (String)null);
                valores.put("image", (String)null);
                valores.put("google_id", (String)null);
                valores.put("facebook_id", (String)null);
                valores.put("festivals_created", (String)null);
                valores.put("festivals_assisted", (String)null);
                valores.put("location", city);

                db.insert("Users", null, valores);
                db.close();
            }
        }
    }

    //
    public static void deleteLocation(String TAG, Activity activity) {

        Log.i(TAG, "ENTRO A MyDatabase:deleteUser:0");

        Boolean existsAccount = false;
        String email = "";

        //Abrimos la base de datos
        Database mDB_Activity = new Database(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la cuenta
                if (c.getString(1) != null) {
                    Log.i(TAG, "ENTRO A MyDatabase:insertLocation:1");

                    existsAccount = true;
                    email = c.getString(1);
                }
            }
            c.close();
            db.close();
        }

        db = mDB_Activity.getWritableDatabase();
        if (existsAccount) { //Existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:deleteUser:2");

                //Eliminamos la localizacion
                ContentValues valores = new ContentValues();
                valores.put("location", (String)null);

                String[] args = new String[]{email};

                db.update("Users", valores, "email=?", args);
                db.close();
            }
        } else { // No existe la cuenta
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:deleteUser:3");

                db.delete("Users", null, null);
                db.close();
            }
        }
    }

    //
    public static void insertUser(String TAG, Activity activity, User user) {

        Log.i(TAG, "ENTRO A MyDatabase:insertUser:0");

        Boolean existsLocation = false;
        String location = "";

        //Abrimos la base de datos
        Database mDB_Activity = new Database(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la localizacion del usuario

                if (c.getString(15) != null) {

                    Log.i(TAG, "ENTRO A MyDatabase:insertUser:1");
                    existsLocation = true;
                    location = c.getString(15);
                }
            }
            c.close();
            db.close();
        }

        db = mDB_Activity.getWritableDatabase();
        if (existsLocation) { //Existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:insertUser:2");

                //Actualizamos la cuenta
                ContentValues valores = new ContentValues();
                valores.put("id", user.getID());
                valores.put("email", user.getEmail());
                valores.put("user_name", user.getUsername());
                //valores.put("password", user.getPassword());
                valores.put("name", user.getName());
                valores.put("surname", user.getSurname());
                valores.put("gender", user.getGender());
                valores.put("birthday", user.getBirthday());
                valores.put("place", user.getPlace());
                valores.put("music_style", user.getMusicStyle());
                valores.put("image", user.getImage());
                valores.put("google_id", user.getGoogle_id());
                valores.put("facebook_id", user.getFacebook_id());

                String festivals_created = null;
                try {
                    JSONObject json = new JSONObject();
                    json.put("festivals_created", new JSONArray(user.getFestivalsCreated()));
                    festivals_created = json.toString();
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                valores.put("festivals_created", festivals_created);

                String festivals_assisted = null;
                try {
                    JSONObject json = new JSONObject();
                    json.put("festivals_assisted", new JSONArray(user.getfestivalsAssisted()));
                    festivals_assisted = json.toString();
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                valores.put("festivals_assisted", festivals_assisted);

                String[] args = new String[]{location};
                db.update("Users", valores, "location=?", args);

                Log.i(TAG, "ENTRO A MyDatabase:insertUser:location: " + location);

                db.close();
            }
        }
        else { // No existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:insertUser:3");

                //Insertamos la nueva cuenta
                ContentValues valores = new ContentValues();
                valores.put("id", user.getID());
                valores.put("email", user.getEmail());
                valores.put("user_name", user.getUsername());
                //valores.put("password", user.getPassword());
                valores.put("name", user.getName());
                valores.put("surname", user.getSurname());
                valores.put("gender", user.getGender());
                valores.put("birthday", user.getBirthday());
                valores.put("place", user.getPlace());
                valores.put("music_style", user.getMusicStyle());
                valores.put("image", user.getImage());
                valores.put("google_id", user.getGoogle_id());
                valores.put("facebook_id", user.getFacebook_id());

                String festivals_created = null;
                try {
                    JSONObject json = new JSONObject();
                    json.put("festivals_created", new JSONArray(user.getFestivalsCreated()));
                    festivals_created = json.toString();
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                valores.put("festivals_created", festivals_created);

                String festivals_assisted = null;
                try {
                    JSONObject json = new JSONObject();
                    json.put("festivals_assisted", new JSONArray(user.getfestivalsAssisted()));
                    festivals_assisted = json.toString();
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
                valores.put("festivals_assisted", festivals_assisted);

                valores.put("location", (String) null);

                db.insert("Users", null, valores);
                db.close();
            }
        }
    }

    //
    public static void updateUser(String TAG, Activity activity, User user) {

        Log.i(TAG, "ENTRO A MyDatabase:updateUser:0");

        //Abrimos la base de datos
        Database mDB_Activity = new Database(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getWritableDatabase();
        if (db != null) {

            Log.i(TAG, "ENTRO A MyDatabase:updateUser:1");

            //Actualizamos la cuenta
            ContentValues valores = new ContentValues();
            valores.put("id", user.getID());
            valores.put("email", user.getEmail());
            valores.put("user_name", user.getUsername());
            //valores.put("password", user.getPassword());
            valores.put("name", user.getName());
            valores.put("surname", user.getSurname());
            valores.put("gender", user.getGender());
            valores.put("birthday", user.getBirthday());
            valores.put("place", user.getPlace());
            valores.put("music_style", user.getMusicStyle());
            valores.put("image", user.getImage());
            valores.put("google_id", user.getGoogle_id());
            valores.put("facebook_id", user.getFacebook_id());

            String festivals_created = null;
            try {
                JSONObject json = new JSONObject();
                json.put("festivals_created", new JSONArray(user.getFestivalsCreated()));
                festivals_created = json.toString();
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
            valores.put("festivals_created", festivals_created);

            String festivals_assisted = null;
            try {
                JSONObject json = new JSONObject();
                json.put("festivals_assisted", new JSONArray(user.getfestivalsAssisted()));
                festivals_assisted = json.toString();
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
            valores.put("festivals_assisted", festivals_assisted);

            valores.put("location", user.getLocation());

            String[] args = new String[]{user.getID()};
            db.update("Users", valores, "id=?", args);

            db.close();
        }
    }

    //
    public static void deleteUser(String TAG, Activity activity, User user) {

        Log.i(TAG, "ENTRO A MyDatabase:deleteUser:0");

        Boolean existsLocation = false;
        String location = "";

        //Abrimos la base de datos
        Database mDB_Activity = new Database(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la localizacion del usuario

                if (c.getString(15) != null) {

                    Log.i(TAG, "ENTRO A MyDatabase:deleteUser:1");
                    existsLocation = true;
                    location = c.getString(15);
                }
            }
            c.close();
            db.close();
        }

        db = mDB_Activity.getWritableDatabase();
        if (existsLocation) { //Existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:deleteUser:2");

                //Actualizamos la cuenta
                ContentValues valores = new ContentValues();
                valores.put("id", (String)null);
                valores.put("email", (String)null);
                valores.put("user_name", (String)null);
                valores.put("password", (String)null);
                valores.put("name", (String)null);
                valores.put("surname", (String)null);
                valores.put("gender", (String)null);
                valores.put("birthday", (String)null);
                valores.put("place", (String)null);
                valores.put("music_style", (String)null);
                valores.put("image", (String)null);
                valores.put("google_id", (String)null);
                valores.put("facebook_id", (String)null);
                valores.put("festivals_created", (String)null);
                valores.put("festivals_assisted", (String)null);

                String[] args = new String[]{location};

                db.update("Users", valores, "location=?", args);
                db.close();
            }
        } else { // No existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A MyDatabase:deleteUser:3");

                db.delete("Users", null, null);
                db.close();
            }
        }
    }

    //
    public static void registerUserEvent(String TAG, Activity activity, User user, String nameEvent) {

        Log.i(TAG, "ENTRO A MyDatabase:registerUserEvent: 0");

        //Abrimos la base de datos
        Database mDB_Activity = new Database(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getWritableDatabase();
        if (db != null) {

            Log.i(TAG, "ENTRO A MyDatabase:registerUserEvent: 1");

            // Actualizamos la cuenta
            ContentValues valores = new ContentValues();
            ArrayList festivales_asistidos = user.getfestivalsAssisted();
            festivales_asistidos.remove(nameEvent);

            String festivals_assisted = null;
            try {
                JSONObject json = new JSONObject();
                json.put("festivals_assisted", new JSONArray( festivales_asistidos ));
                festivals_assisted = json.toString();
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
            valores.put("festivals_assisted", festivals_assisted);

            // Where ...
            String[] args = new String[]{user.getID()};
            db.update("Users", valores, "id=?", args);

            db.close();
        }
    }

    //
    public static void unregisterUserEvent(String TAG, Activity activity, User user, String nameEvent) {



    }
}