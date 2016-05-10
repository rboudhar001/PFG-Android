package com.example.rachid.myapplication;

/**
 * Created by Rachid on 05/05/2016.
 */
public class Event {

    private String image; //URL de la imagen
    private String name; // Titulo del evento
    private String description;

    private String place;
    private String firstDate; // Fecha del dia de inicio del evento
    private String lastDate; // Fecha del dia final del evento

    private String capacity;
    private String assistants;

    private String sales; // URL de la pagina para la venta de entradas
    private String webpage; // URL de la pagina
    private String contact_number; // Numero de telefono de la empresa

    private String creator; // Creador
    private String created_on; // Fecha del dia en el que se creo este evento

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
    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    //
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //
    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
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
