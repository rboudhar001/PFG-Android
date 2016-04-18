package com.example.rachid.myapplication;

/**
 * Created by Rachid on 18/04/2016.
 */
public abstract class MyNetwork {

    private static User user;

    public static User loginUser(String mEmail, String mPassword) {

        user = null;

        // TODO: Accede al servidor y solicita que se le de el usuario con el email y password dados como parametros.
        // ---------------------------------------------------------------------------------------
        // ---------------------------------------------------------------------------------------

        return user;
    }

    public static boolean signupUser(User mUser) {

        if (mUser != null) {

            // TODO: Accede al servidor y guarda en la DB este nuevo usuario
            // ---------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------

            return true;
        }

        return false;
    }
}
