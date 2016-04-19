package com.example.rachid.myapplication;

/**
 * Created by Rachid on 18/04/2016.
 */
public abstract class MyNetwork {

    private static User user;

    //
    public static String signupUser(User mUser) {

        if (mUser != null) {

            // TODO: Accede al servidor y guarda en la DB este nuevo usuario
            // ---------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------

            return "";
        }

        return null;
    }

    //
    public static User loginUser(String mEmail, String mPassword) {

        user = null;

        // TODO: Accede al servidor y solicita que se le de el usuario con el email y password dados como parametros.
        // ---------------------------------------------------------------------------------------
        // ---------------------------------------------------------------------------------------

        return user;
    }

    //
    public static boolean updateUser(User mUser) {

        if (mUser != null) {

            // TODO: Accede al servidor y actualiza en la DB el usuario pasado como parametro
            // ---------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------

            return true;
        }

        return false;
    }
}
