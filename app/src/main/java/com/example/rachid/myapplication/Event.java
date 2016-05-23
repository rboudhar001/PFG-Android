package com.example.rachid.myapplication;

import java.io.Serializable;

/**
 * Created by Rachid on 05/05/2016.
 */
@SuppressWarnings("serial")
public class Event implements Serializable {

    // ******************
    // Variables
    // ******************
    private String id; // ID del evento and Primary Key

    private String photo; //URL de la imagen
    private String name; // Titulo del evento and Primary Key
    private String description;

    private String place;
    private String firstDay; // Fecha del dia de inicio del evento
    private String lastDay; // Fecha del dia final del evento

    private int capacity;
    private int assistants;

    private String sales; // URL de la pagina para la venta de entradas
    private String webpage; // URL de la pagina
    private int contact_number; // Numero de telefono de la empresa

    private String creator; // Creador

    // ******************
    // Constructor
    // ******************
    public Event() {
    }

    public Event(String id, String photo, String name, String description, String place, String firstDay,
                 String lastDay, int capacity, int assistants, String sales, String webpage, int contact_number, String creator) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.place = place;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.capacity = capacity;
        this.assistants = assistants;
        this.sales = sales;
        this.webpage = webpage;
        this.contact_number = contact_number;
        this.creator = creator;
    }

    // ******************
    // Funciones
    // ******************

    // ID
    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }

    // Assistants
    public int getAssistants() {
        return assistants;
    }

    public void setAssistants(int assistants) {
        this.assistants = assistants;
    }

    // Capacity
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // Contact_number
    public int getContact_number() {
        return contact_number;
    }

    public void setContact_number(int contact_number) {
        this.contact_number = contact_number;
    }

    // Creator
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // FirstDay
    public String getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    // Image
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    // LastDay
    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Place
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    // Sales
    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    // Webpage
    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }
}
