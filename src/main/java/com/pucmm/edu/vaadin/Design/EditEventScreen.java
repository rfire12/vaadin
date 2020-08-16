package com.pucmm.edu.vaadin.Design;

import com.pucmm.edu.vaadin.Models.MyEvent;
import com.pucmm.edu.vaadin.Services.EventsServices;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.calendar.CalendarItemTheme;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringComponent
@UIScope
public class EditEventScreen extends VerticalLayout {
    DatePicker date = new DatePicker();
    TextField title = new TextField("Title");
    MyEvent myEvent;

    public EditEventScreen(@Autowired EventsServices calendarEventService) {
        FormLayout formLayout = new FormLayout();

        H3 header = new H3("Edit Event");

        date.setLabel("Start Date");
        date.setPlaceholder("Select a Date");
        date.setValue(LocalDate.now());

        Button btnEdit = new Button("Edit");
        btnEdit.getElement().setAttribute("theme", "success");

        HorizontalLayout btnContainer = new HorizontalLayout(btnEdit);
        btnContainer.setSpacing(true);

        formLayout.add(title, date);
        setAlignItems(Alignment.CENTER);

        add(header, formLayout, btnContainer);

        btnEdit.addClickListener((event) -> {
            MyEvent myEvent = new MyEvent(
                    Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    title.getValue(),
                    CalendarItemTheme.Blue
            );

            myEvent.setDate(Date.from(date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            myEvent.setTitle(title.getValue());
            myEvent.setColor(CalendarItemTheme.Blue);

            try {
                calendarEventService.updateEvent(myEvent);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            MainVaadin.calendar.refresh();
        });

    }
}
