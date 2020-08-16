package com.pucmm.edu.vaadin.Services;

import com.pucmm.edu.vaadin.Models.MyEvent;
import com.pucmm.edu.vaadin.Repositories.IEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.calendar.CalendarItemTheme;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class EventsServices {
    @Autowired
    private IEventsRepository eventsRepository;

    public List<MyEvent> listEvents() {
        return eventsRepository.findAll();
    }

    @Transactional
    public MyEvent createEvent(Date date, String title, CalendarItemTheme color) {
        return eventsRepository.save(new MyEvent(date, title, color));
    }

    @Transactional
    public MyEvent updateEvent(MyEvent event) {
        return eventsRepository.save(event);
    }
}
