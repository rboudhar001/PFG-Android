package com.example.rachid.myapplication;

/**
 * Created by Rachid on 06/04/2016.
 */
public class State {

    private static boolean loged;
    private static boolean existsLocation;
    private static User user = new User();

    public State(){
        /*
        if (this.user == null) {
            this.user = new User();
        }
        */
    }

    // LOGED
    public void setLoged(boolean loged) {
        this.loged = loged;
    }

    public boolean getLoged(){
        return this.loged;
    }

    // LOCATION
    public void setExistsLocation(boolean location) {
        this.existsLocation = location;
    }

    public boolean getExistsLocation(){
        return this.existsLocation;
    }

    // USER
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}
