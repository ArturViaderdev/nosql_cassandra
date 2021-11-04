/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import enums.EventType;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Event's model
 *
 * @author User
 */
public class Event {

    public EventType type;
    public Date dateCreated;
    public Employee employee;

    public Event(EventType type, Date dateCreated, Employee employee) {
        this.type = type;
        this.dateCreated = dateCreated;
        this.employee = employee;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getFormatDateCreated() {
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(dateCreated);
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Event: " + type + "\n\t"
                + "Date: " + getFormatDateCreated() + "\n\t"
                + "Employee: " + employee.getUsername();

    }

}
