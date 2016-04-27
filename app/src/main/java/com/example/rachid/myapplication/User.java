package com.example.rachid.myapplication;

import java.util.Date;

/**
 * Created by Rachid on 06/04/2016.
 */
public class User {

    // **************
    // Variables
    // **************
    private String id;
    private String email;
    private String user_name;
    private String password;
    private String name;
    private String surname;
    private String gender;
    private String birthday;
    private String place;
    private String music_style;
    private String url_image_profile;
    private String location;

    // **************
    // Constructores
    // **************
    public User() {
    }

    public User(String location) {
        this.location = location;
    }

    public User(String id, String email, String user_name, String password, String name, String surname, String gender, String birthday, String place, String music_style, String url_image_profile, String location) {
        this.id = id;
        this.email = email;
        this.user_name = user_name;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthday = birthday;
        this.place = place;
        this.music_style = music_style;
        this.url_image_profile = url_image_profile;
        this.location = location;
    }

    // **************
    // Funciones
    // **************

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

    // User_Name
    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getUserName() {
        return user_name;
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

    // Surname
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
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

    // Place
    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    // Music_Style
    public void setMusicStyle(String music_style) {
        this.music_style = music_style;
    }

    public String getMusicStyle() {
        return music_style;
    }

    // Location
    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
