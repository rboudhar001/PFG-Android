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
    private static boolean updateUserResponse;
    private static boolean updateEventResponse;

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

    // UPDATE USER
    public static void setUpdateUserResponse(boolean update) {
        updateUserResponse = update;
    }

    public static boolean getUpdateUserResponse() {
        return updateUserResponse;
    }

    // UPDATE EVENT
    public static void setUpdateEventResponse(boolean update) {
        updateEventResponse = update;
    }

    public static boolean getUpdateEventResponse() {
        return updateEventResponse;
    }
}
