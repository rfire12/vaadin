package com.pucmm.edu.vaadin.Design;

import com.pucmm.edu.vaadin.Models.MyEvent;
import com.pucmm.edu.vaadin.Models.MyUser;
import com.pucmm.edu.vaadin.Services.EventsServices;
import com.pucmm.edu.vaadin.Services.UsersServices;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import org.vaadin.calendar.CalendarComponent;
import org.vaadin.calendar.CalendarItemTheme;
import org.vaadin.calendar.data.AbstractCalendarDataProvider;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Route("calendar")
@SpringComponent
@UIScope
public class MainVaadin extends VerticalLayout {
    public static CalendarComponent<MyEvent> calendar = new CalendarComponent<MyEvent>()
            .withItemDateGenerator(MyEvent::getDate)
            .withItemLabelGenerator(MyEvent::getTitle)
            .withItemThemeGenerator(MyEvent::getColor);

    public static EventsServices eventsServices;

    @Autowired
    public MainVaadin(@Autowired final EventScreen pantallaEvento,
                      @Autowired UsersServices usersServices,
                      @Autowired EventsServices eventsServices,
                      @Autowired final EmailScreen emailScreen,
                      @Autowired EditEventScreen editEventScreen) {
        MainVaadin.eventsServices = eventsServices;

        if (usersServices.listUsers().isEmpty()) {
            getUI().get().navigate("");
        } else if (!usersServices.listUsers().get(0).isLoggedIn()) {
            getUI().get().navigate("");
        } else {
            setAlignItems(FlexComponent.Alignment.CENTER);

            HorizontalLayout btnLayout = new HorizontalLayout();
            btnLayout.setSpacing(true);

            Button btnAdd = new Button("Add Event");
            Button btnSendEmail = new Button("Send Email");
            Button btnUser = new Button("User's Info");
            Button btnCrud = new Button("Managers");
            Button btnLogout = new Button("Log out");

            btnAdd.setIcon(new Icon(VaadinIcon.CALENDAR_CLOCK));
            btnAdd.getElement().setAttribute("theme", "primary");

            btnSendEmail.setIcon(new Icon(VaadinIcon.CALENDAR_ENVELOPE));
            btnSendEmail.getElement().setAttribute("theme", "primary");

            btnUser.setIcon(new Icon(VaadinIcon.CLIPBOARD_USER));

            btnCrud.setIcon(new Icon(VaadinIcon.FORM));
            btnCrud.getElement().setAttribute("theme", "success");

            btnLogout.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            btnLogout.getElement().setAttribute("theme", "error");

            configureScreenBtn(btnAdd, pantallaEvento);
            configureScreenBtn(btnSendEmail, emailScreen);

            btnLayout = new HorizontalLayout(btnAdd, btnSendEmail, btnUser, btnCrud, btnLogout);

            btnLogout.addClickListener((evento) -> {
                try {
                    MyUser user = usersServices.listUsers().get(0);
                    user.setLoggedIn(false);
                    usersServices.editUser(user);

                    getUI().get().navigate("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            btnUser.addClickListener((evento) -> getUI().get().navigate("usuario"));
            btnCrud.addClickListener((evento) -> getUI().get().navigate("gerentes"));

            eventsServices.createEvent(
                    1,
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    "Practica Vaadin",
                    CalendarItemTheme.Green
            );

            calendar.setDataProvider(new CustomDataProvider());
            calendar.addEventClickListener(evt -> {
                try {
                    editEventScreen.date.setValue(
                            evt.getDetail().getDate().toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDate()
                    );

                    editEventScreen.title.setValue(evt.getDetail().getTitle());

                    openScreen(editEventScreen);

                    eventsServices.createEvent(
                            evt.getDetail().getId(), evt.getDetail().getDate(),
                            evt.getDetail().getTitle(), evt.getDetail().getColor()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            H4 title = new H4("Calendar");
            H6 subtitle = new H6("Events");

            setAlignItems(FlexComponent.Alignment.CENTER);

            add(title, subtitle, btnLayout, calendar);
        }

        Button btnAdd = new Button("Add");
        btnAdd.setIcon(new Icon(VaadinIcon.PLUS));
        btnAdd.getElement().setAttribute("theme", "primary");
    }

    private void openScreen(com.vaadin.flow.component.orderedlayout.VerticalLayout form) {
        Dialog screen = new Dialog();
        screen.add(form);
        screen.open();
    }

    private void configureScreenBtn(Button btn, com.vaadin.flow.component.orderedlayout.VerticalLayout form) {
        btn.addClickListener((e) -> openScreen(form));
    }
}

@SpringComponent
@UIScope
class CustomDataProvider extends AbstractCalendarDataProvider<MyEvent> {
    @Override
    public Collection<MyEvent> getItems(Date fromDate, Date toDate) {
        return MainVaadin.eventsServices.listEvents();
    }
}
