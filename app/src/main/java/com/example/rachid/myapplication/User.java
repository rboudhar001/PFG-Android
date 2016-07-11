package com.example.rachid.myapplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rachid on 06/04/2016.
 */
@SuppressWarnings("serial")
public class User implements Serializable {

    // **************
    // Variables
    // **************
    private String id;
    private String email;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String gender;
    private String birthday;
    private String place;
    private String music_style;
    private String image;
    private String google_id;
    private String facebook_id;

    private ArrayList<String> festivalsCreated;
    private ArrayList<String> festivalsAssisted;

    private String location;
    private String language;

    // **************
    // Constructores
    // **************
    public User() {
    }

    public User(String location, String language) {
        this.location = location;
        this.language = language;
    }

    public User(String id, String email, String username, String password, String name,
                String surname, String gender, String birthday, String place, String music_style,
                String image, String google_id, String facebook_id, ArrayList<String> festivalsCreated,
                ArrayList<String> festivalsAssisted, String location, String language) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = null; //password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birthday = birthday;
        this.place = place;
        this.music_style = music_style;
        this.image = image;

        this.festivalsCreated = festivalsCreated;
        this.festivalsAssisted = festivalsAssisted;

        this.location = location;

        this.language = language;
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

    // Username
    public void setUsername(String user_name) {
        this.username = user_name;
    }

    public String getUsername() {
        return username;
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
    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
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

    // Google_id
    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    // Facebook_id
    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    // Festivales Publicados
    public void setFestivalsCreated(ArrayList<String> festivalsCreated) {
        this.festivalsCreated = festivalsCreated;
    }

    public ArrayList<String> getFestivalsCreated() {
        return festivalsCreated;
    }

    // Festivales Apuntados
    public void setfestivalsAssisted(ArrayList<String> festivalsAssisted) {
        this.festivalsAssisted = festivalsAssisted;
    }

    public ArrayList<String> getfestivalsAssisted() {
        return festivalsAssisted;
    }

    // Location
    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    // LANGUAGE
    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage(){
        return language;
    }
}
