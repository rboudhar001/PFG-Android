package com.example.rachid.myapplication;

import java.util.Date;

/**
 * Created by Rachid on 06/04/2016.
 */
public class User {

    private String id;
    private String email;
    private String password;
    private String name;
    private String gender;
    private String birthday;
    private String url_image_profile;
    private String location;

    public User() {
        id = "";
        email = "";
        password = "";
        name = "";
        gender = "";
        birthday = "";
        url_image_profile = "";
        location = "";
    }

    public User(String id, String email, String password, String name, String gender, String birthday, String url_image_profile, String location) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.url_image_profile = url_image_profile;
        this.location = location;
    }

    // ID
    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    // Email
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    // Password
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    // Name
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Gender
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    // Birthday
    public void setBirthday (String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    // URL Image Profile
    public void setUrlImageProfile(String url_image_profile) {
        this.url_image_profile = url_image_profile;
    }

    public String getUrlImageProfile() {
        return url_image_profile;
    }

    // Location
    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
