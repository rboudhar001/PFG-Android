package com.example.rachid.myapplication;

/**
 * Created by Rachid on 06/04/2016.
 */
public class State {

    private static boolean state;
    private static User user = new User();

    public State(){
        /*
        if (this.user == null) {
            this.user = new User();
        }
        */
    }

    // STATE
    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState(){
        return this.state;
    }

    // USER
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}
