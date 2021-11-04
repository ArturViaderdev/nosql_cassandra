/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import enums.Urgency;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Incidence's model
 * @author Jonathan Cacay
 */
public class Incidence {

    private String id;
    private Employee from, destination;
    private String message;
    private Urgency urgency;
    private Date dateCreated;

    public Incidence(Employee from, Employee destination, String message, Urgency urgency, Date dateCreated) {
        this.from = from;
        this.destination = destination;
        this.message = message;
        this.urgency = urgency;
        this.dateCreated = dateCreated;
    }

    public Incidence(String id, Employee from, Employee destination, String message, Urgency urgency, Date dateCreated) {
        this.id = id;
        this.from = from;
        this.destination = destination;
        this.message = message;
        this.urgency = urgency;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Employee getFrom() {
        return from;
    }

    public void setFrom(Employee from) {
        this.from = from;
    }

    public Employee getDestination() {
        return destination;
    }

    public void setDestination(Employee destination) {
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFormatDateCreated() {
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(dateCreated);
    }

    @Override
    public String toString() {
        return "Incidence:\n\t"
                + "From: " + from.getUsername() + "\n\t"
                + "Destination: " + destination.getUsername() + "\n\t"
                + "Urgency: " + urgency.toString() + "\n\t"
                + "Date: " + getFormatDateCreated() + "\n\t"
                + "Message: " + message + "\n"
                + "----------------------------------------------------";

    }

}
