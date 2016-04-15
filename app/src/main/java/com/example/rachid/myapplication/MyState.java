package com.example.rachid.myapplication;

/**
 * Created by Rachid on 06/04/2016.
 */
public abstract class MyState {

    private static boolean loged;
    private static boolean existsLocation;
    private static User user = new User();

    // LOGED
    public static void setLoged(boolean l) {
        loged = l;
    }

    public static boolean getLoged(){
        return loged;
    }

    // LOCATION
    public static void setExistsLocation(boolean e) {
        existsLocation = e;
    }

    public static boolean getExistsLocation(){
        return existsLocation;
    }

    // USER
    public static void setUser(User u) {
        user = u;
    }

    public static User getUser(){
        return user;
    }
}
