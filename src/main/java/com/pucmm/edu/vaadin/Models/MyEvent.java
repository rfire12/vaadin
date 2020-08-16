package com.pucmm.edu.vaadin.Models;

import org.vaadin.calendar.CalendarItemTheme;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class MyEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date date;
    private String title;
    private CalendarItemTheme color;

    public MyEvent(){

    }

    public MyEvent(Date date, String title, CalendarItemTheme color) {
        this.date = date;
        this.title = title;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CalendarItemTheme getColor() {
        return color;
    }

    public void setColor(CalendarItemTheme color) {
        this.color = color;
    }
}
