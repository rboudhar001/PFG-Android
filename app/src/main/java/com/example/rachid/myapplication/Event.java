package com.example.rachid.myapplication;

/**
 * Created by Rachid on 05/05/2016.
 */
public class Event {

    // ******************
    // Variables
    // ******************
    private String image; //URL de la imagen
    private String name; // Titulo del evento and Primary Key
    private String description;

    private String place;
    private String firstDay; // Fecha del dia de inicio del evento
    private String lastDay; // Fecha del dia final del evento

    private String capacity;
    private String assistants;

    private String sales; // URL de la pagina para la venta de entradas
    private String webpage; // URL de la pagina
    private String contact_number; // Numero de telefono de la empresa

    private String creator; // Creador
    private String created_on; // Fecha del dia en el que se creo este evento

    // ******************
    // Constructor
    // ******************
    public Event() {
        this.image = null;
        this.name = null;
        this.description = null;
        this.place = null;
        this.firstDay = null;
        this.lastDay = null;
        this.capacity = null;
        this.assistants = null;
        this.sales = null;
        this.webpage = null;
        this.contact_number = null;
        this.creator = null;
        this.created_on = null;
    }

    public Event(String image, String name, String description, String place, String firstDay,
                 String lastDay, String capacity, String assistants, String sales, String webpage,
                 String contact_number, String creator, String created_on) {
        this.image = image;
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
        this.created_on = created_on;
    }

    // ******************
    // Funciones
    // ******************

    //
    public String getAssistants() {
        return assistants;
    }

    public void setAssistants(String assistants) {
        this.assistants = assistants;
    }

    //
    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    //
    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    //
    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    //
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    //
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //
    public String getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(String firstDay) {
        this.firstDay = firstDay;
    }

    //
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //
    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    //
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    //
    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    //
    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }
}
