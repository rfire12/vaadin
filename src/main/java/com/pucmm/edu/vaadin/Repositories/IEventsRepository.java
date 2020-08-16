package com.pucmm.edu.vaadin.Repositories;

import com.pucmm.edu.vaadin.Models.MyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IEventsRepository extends JpaRepository<MyEvent, Long> {
    List<MyEvent> findAllByDate(Date date);

    @Query("select event from MyEvent event where event.date between ?1 and ?2")
    List<MyEvent> findByDatesBetween(Date startDate, Date endDate);
}
