package com.pucmm.edu.vaadin.Design;

import com.pucmm.edu.vaadin.Models.MyUser;
import com.pucmm.edu.vaadin.Services.UsersServices;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;

@Route("")
@SpringComponent
@UIScope
public class LoginScreen extends VerticalLayout {
    public LoginScreen(@Autowired UsersServices userService) {

        TextField txtEmail = new TextField("Email");
        PasswordField txtPassword = new PasswordField("Password");
        TextField txtName = new TextField("Name");

        Button btnAction = userService.listUsers().isEmpty() ? new Button("Sign Up") : new Button("Login");
        btnAction.getElement().setAttribute("theme", "primary");
        HorizontalLayout horizontalLayout;

        if (userService.listUsers().isEmpty()) {
            horizontalLayout = new HorizontalLayout(txtName, txtEmail, txtPassword);
        } else {
            horizontalLayout = new HorizontalLayout(txtEmail, txtPassword);
        }

        btnAction.addClickListener((evento) -> {
            if (userService.listUsers().isEmpty()) {
                try {
                    userService.createUser(txtName.getValue(), txtEmail.getValue(), txtPassword.getValue());
                    getUI().get().getPage().reload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (userService.validate(txtEmail.getValue(), txtPassword.getValue())) {
                    try {
                        MyUser user = userService.listUsers().get(0);
                        user.setLoggedIn(true);
                        userService.editUser(user);
                        getUI().get().navigate("calendar");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    getUI().get().getPage().reload();
                }
            }
        });

        H4 title = new H4("Calendar");
        H6 subtitle = userService.listUsers().isEmpty()
                ? new H6("Sign Up to view this page")
                : new H6("Please, login to view this page");

        setAlignItems(Alignment.CENTER);
        add(title, subtitle, horizontalLayout, btnAction);
    }
}

