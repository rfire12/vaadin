package com.pucmm.edu.vaadin.Design;

import com.pucmm.edu.vaadin.Models.MyUser;
import com.pucmm.edu.vaadin.Services.UsersServices;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route("user")
@SpringComponent
@UIScope
public class UserScreen extends VerticalLayout {
    public UserScreen(@Autowired UsersServices usersServices) {
        MyUser user;

        HorizontalLayout horizontalLayout;
        HorizontalLayout btnHorizotalLayout;
        FormLayout formEditInfo;
        VerticalLayout verticalLayout = new VerticalLayout();

        if (usersServices.listUsers().isEmpty())
            getUI().get().navigate("");
        else if (!usersServices.listUsers().get(0).isLoggedIn())
            getUI().get().navigate("");
        else {
            user = usersServices.listUsers().get(0);

            horizontalLayout = new HorizontalLayout();
            btnHorizotalLayout = new HorizontalLayout();

            setAlignItems(Alignment.CENTER);

            horizontalLayout.setMargin(true);
            horizontalLayout.setSpacing(true);
            horizontalLayout.setSizeFull();
            horizontalLayout.setAlignItems(Alignment.CENTER);

            H4 title = new H4("Calendar");
            H6 subtitle = new H6("Events");

            Button btnCalendar = new Button("Calendar");
            btnCalendar.setIcon(new Icon(VaadinIcon.ARROW_CIRCLE_LEFT_O));

            Button btnLogout = new Button("Logout");
            btnLogout.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            btnLogout.getElement().setAttribute("theme", "error");

            btnLogout.addClickListener((evento) -> {
                try {
                    MyUser auxUser = usersServices.listUsers().get(0);
                    auxUser.setLoggedIn(false);
                    usersServices.editUser(auxUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUI().get().navigate("");
            });

            btnCalendar.addClickListener((event) -> getUI().get().navigate("calendar"));

            FormLayout formLayoutUser = new FormLayout();

            H3 usersTitle = new H3("User's Info");
            H6 usersName = new H6("Name: " + user.getName());
            H6 usersEmail = new H6("Email: " + user.getEmail());

            formLayoutUser.add(usersTitle, usersName, usersEmail);

            VerticalLayout verticalLayoutEdit = new VerticalLayout();
            formEditInfo = new FormLayout();

            H3 editTitle = new H3("Edit User's Info");

            TextField txtNewEmail = new TextField("Email");
            TextField txtNewName = new TextField("Name");

            Button btnSave = new Button("Save Changes");
            btnSave.setIcon(new Icon(VaadinIcon.DATABASE));

            formEditInfo.add(editTitle, txtNewName, txtNewEmail);
            verticalLayoutEdit.add(editTitle, formEditInfo, btnSave);

            btnSave.addClickListener((evento) -> {
                try {
                    if (!txtNewEmail.getValue().equals(""))
                        user.setEmail(txtNewEmail.getValue());

                    if (!txtNewName.getValue().equals(""))
                        user.setName(txtNewName.getValue());

                    usersServices.editUser(user);
                    getUI().get().getPage().reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            btnHorizotalLayout.add(btnCalendar, btnLogout);
            verticalLayout.add(formLayoutUser, new HorizontalLayout(verticalLayoutEdit));
            verticalLayout.setAlignItems(Alignment.CENTER);

            add(title, subtitle, btnHorizotalLayout, verticalLayout);
        }
    }
}

