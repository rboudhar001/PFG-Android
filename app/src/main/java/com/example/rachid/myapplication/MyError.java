package com.example.rachid.myapplication;

/**
 * Created by Rachid on 26/05/2016.
 */
public abstract class MyError {

    // *****************
    // *** VARIABLES ***
    // *****************
    private static boolean subscribeResponse;
    private static boolean signupResponse;
    private static boolean loginResponse;

    // *****************
    // *** FUNCIONES ***
    // *****************

    // SUBSCRIBE
    public static void setSubscribeResponse(boolean subscribe) {
        subscribeResponse = subscribe;
    }

    public static boolean getSubscribeResponse() {
        return subscribeResponse;
    }

    // SIGN UP
    public static void setSignupResponse(boolean signup) {
        signupResponse = signup;
    }

    public static boolean getSignupResponse() {
        return signupResponse;
    }

    // LOGIN
    public static void setLoginResponse(boolean login) {
        loginResponse = login;
    }

    public static boolean getLoginResponse() {
        return loginResponse;
    }
}
