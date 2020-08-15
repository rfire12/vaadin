package com.pucmm.edu.vaadin.Models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class MyEvent implements Serializable {
    @Id
    private long id;
    private Date date;
    private String title;
    private CalendarItemTheme color;
}
