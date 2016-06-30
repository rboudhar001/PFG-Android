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
    private static boolean changePasswordResponse;
    private static boolean usernameResponse;

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

    // CHANGE_PASSWORD
    public static void setChangePasswordResponse(boolean changePassword) {
        changePasswordResponse = changePassword;
    }

    public static boolean getChangePasswordResponse() {
        return changePasswordResponse;
    }

    // SET USER NAME
    public static void setUsernameResponse(boolean username) {
        usernameResponse = username;
    }

    public static boolean getUsernameResponse() {
        return usernameResponse;
    }
}
