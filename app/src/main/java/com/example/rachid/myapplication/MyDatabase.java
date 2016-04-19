package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Rachid on 14/04/2016.
 */
public abstract class MyDatabase {

    // Variables
    private static String TAG;
    private static Activity activity;

    //
    public static void insertLocation(String T, Activity A, String city) {

        TAG = T;
        activity = A;

        Log.i(TAG, "ENTRO A M:insertLocation:0");

        String email = "";
        Boolean existsAccount = false;

        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la cuenta
                if (c.getString(1) != null) {
                    Log.i(TAG, "ENTRO A M:insertLocation:1");

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

                Log.i(TAG, "ENTRO A M:insertLocation:2");

                //Actualizamos la localizacion
                ContentValues valores = new ContentValues();
                valores.put("location", city);

                String[] args = new String[]{email};

                db.update("Users", valores, "email=?", args);
                db.close();
            }
        } else { // No existe la cuenta
            if (db != null) {

                Log.i(TAG, "ENTRO A M:insertLocation:3");

                //Insertamos la nueva cuenta con la localización
                ContentValues valores = new ContentValues();
                valores.put("id", (String)null);
                valores.put("email", (String)null);
                valores.put("password", (String)null);
                valores.put("name", (String)null);
                valores.put("gender", (String)null);
                valores.put("birthday", (String)null);
                valores.put("image", (String)null);
                valores.put("location", city);

                db.insert("Users", null, valores);
                db.close();
            }
        }
    }

    //
    public static void deleteLocation(String T, Activity A) {

        TAG = T;
        activity = A;

        Log.i(TAG, "ENTRO A M:deleteUser:0");

        Boolean existsAccount = false;
        String email = "";

        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la cuenta
                if (c.getString(1) != null) {
                    Log.i(TAG, "ENTRO A M:insertLocation:1");

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

                Log.i(TAG, "ENTRO A M:deleteUser:2");

                //Eliminamos la localizacion
                ContentValues valores = new ContentValues();
                valores.put("location", (String)null);

                String[] args = new String[]{email};

                db.update("Users", valores, "email=?", args);
                db.close();
            }
        } else { // No existe la cuenta
            if (db != null) {

                Log.i(TAG, "ENTRO A M:deleteUser:3");

                db.delete("Users", null, null);
                db.close();
            }
        }
    }

    //
    public static void insertUser(String T, Activity A, User user) {

        TAG = T;
        activity = A;

        Log.i(TAG, "ENTRO A M:insertUser:0");

        Boolean existsLocation = false;

        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la localizacion del usuario

                if (c.getString(7) != null) {

                    Log.i(TAG, "ENTRO A M:insertUser:1");

                    existsLocation = true;
                }
            }
            c.close();
            db.close();
        }

        db = mDB_Activity.getWritableDatabase();
        if (existsLocation) { //Existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A M:insertUser:2");

                //Actualizamos la cuenta
                ContentValues valores = new ContentValues();
                valores.put("id", user.getID());
                valores.put("email", user.getEmail());
                valores.put("password", user.getPassword());
                valores.put("name", user.getName());
                valores.put("gender", user.getGender());
                valores.put("birthday", user.getBirthday());
                valores.put("image", user.getUrlImageProfile());

                String[] args = new String[]{user.getLocation()};
                db.update("Users", valores, "location=?", args);

                db.close();
            }
        }
        else { // No existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A M:insertUser:3");

                //Insertamos la nueva cuenta
                ContentValues valores = new ContentValues();
                valores.put("id", user.getID());
                valores.put("email", user.getEmail());
                valores.put("password", user.getPassword());
                valores.put("name", user.getName());
                valores.put("gender", user.getGender());
                valores.put("birthday", user.getBirthday());
                valores.put("image", user.getUrlImageProfile());
                valores.put("location", (String) null);

                db.insert("Users", null, valores);
                db.close();
            }
        }
    }

    //
    public static void updateUser(String T, Activity A, User user) {

        TAG = T;
        activity = A;

        Log.i(TAG, "ENTRO A M:updateUser:0");

        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getWritableDatabase();
        if (db != null) {

            Log.i(TAG, "ENTRO A M:updateUser:1");

            //Actualizamos la cuenta
            ContentValues valores = new ContentValues();
            valores.put("id", user.getID());
            valores.put("email", user.getEmail());
            valores.put("password", user.getPassword());
            valores.put("name", user.getName());
            valores.put("gender", user.getGender());
            valores.put("birthday", user.getBirthday());
            valores.put("image", user.getUrlImageProfile());
            valores.put("location", user.getLocation());

            String[] args = new String[]{user.getID()};
            db.update("Users", valores, "id=?", args);

            db.close();
        }
    }

    //
    public static void deleteUser(String T, Activity A, User user) {

        TAG = T;
        activity = A;

        Log.i(TAG, "ENTRO A M:deleteUser:0");

        Boolean existsLocation = false;

        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(activity.getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la localizacion del usuario

                if (c.getString(7) != null) {

                    Log.i(TAG, "ENTRO A M:deleteUser:1");

                    existsLocation = true;
                }
            }
            c.close();
            db.close();
        }

        db = mDB_Activity.getWritableDatabase();
        if (existsLocation) { //Existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A M:deleteUser:2");

                //Actualizamos la cuenta
                ContentValues valores = new ContentValues();
                valores.put("id", (String)null);
                valores.put("email", (String)null);
                valores.put("password", (String)null);
                valores.put("name", (String)null);
                valores.put("gender", (String)null);
                valores.put("birthday", (String)null);
                valores.put("image", (String)null);
                valores.put("location", user.getLocation());

                String[] args = new String[]{user.getEmail()};

                db.update("Users", valores, "email=?", args);
                db.close();
            }
        } else { // No existe la localizacion
            if (db != null) {

                Log.i(TAG, "ENTRO A M:deleteUser:3");

                db.delete("Users", null, null);
                db.close();
            }
        }
    }
}