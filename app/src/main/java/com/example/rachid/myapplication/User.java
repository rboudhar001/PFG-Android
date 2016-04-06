package com.example.rachid.myapplication;

/**
 * Created by Rachid on 06/04/2016.
 */
public class User {

    private String id;
    private String email;
    private String name;
    private String gender;
    private String birthday;
    private String url_image_profile;

    public User() {
    }

    public User(String id, String email, String name, String gender, String birthday, String url_image_profile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.url_image_profile = url_image_profile;
    }

    // ID
    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    // Name
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Email
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    // Gender
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    // Birthday
    public void setBirthday(String birthday) {
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
}
