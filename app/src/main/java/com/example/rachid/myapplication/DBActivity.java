package com.example.rachid.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rachid on 26/03/2016.
 */
public class DBActivity extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Socialfest.db";
    public static final int DATABASE_VERSION = 1;

    //Sentencia SQL para crear la tabla de Usuarios
    //String sqlCreate = "CREATE TABLE Users (codigo INTEGER, nombre TEXT)";
    String sqlCreate = "CREATE TABLE Users (id TEXT, email TEXT PRIMART KEY, password TEXT, name TEXT, gender TEXT, birthday TEXT, image TEXT, location TEXT)";
    //DATETIME = YYYY-MM-DD

    public DBActivity(Context contexto, CursorFactory factory) {
        super(contexto, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente
        //      la opción de eliminar la tabla anterior y crearla de nuevo
        //      vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la
        //      tabla antigua a la nueva, por lo que este método debería
        //      ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Users");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
