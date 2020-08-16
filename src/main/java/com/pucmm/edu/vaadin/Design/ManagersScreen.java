package com.pucmm.edu.vaadin.Design;

import com.pucmm.edu.vaadin.Models.MyUser;
import com.pucmm.edu.vaadin.Services.UsersServices;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route("managers")
@SpringComponent
@UIScope
public class ManagersScreen extends VerticalLayout {
    boolean editing = false;
    Integer selectedId;
    DataProvider<MyUser, Void> dataProvider;

    public ManagersScreen(@Autowired UsersServices usersServices) {
        TextField txtName = new TextField("Name:");
        TextField txtEmail = new TextField("Email:");
        PasswordField txtPassword = new PasswordField("Password:");

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return usersServices.listUsersWithPagination(offset, limit).stream();
                },
                query -> Math.toIntExact(usersServices.totalUsers() - 1)
        );

        Binder<MyUser> binder = new Binder<>();
        Grid<MyUser> tabla = new Grid<>();

        Button btnAdd = new Button("Save");
        btnAdd.getElement().setAttribute("theme", "primary");

        Button btnCancel = new Button("Cancel");
        btnCancel.getElement().setAttribute("theme", "error");

        ManagerActionsScreen managerActionsScreen = new ManagerActionsScreen();

        if (usersServices.listUsers().isEmpty())
            getUI().get().navigate("");
        else if (!usersServices.listUsers().get(0).isLoggedIn())
            getUI().get().navigate("");
        else {
            btnAdd.addClickListener((event) -> {
                try {
                    usersServices.createUser(
                            txtName.getValue(),
                            txtEmail.getValue(),
                            txtPassword.getValue()
                    );

                } catch (Exception exp) {
                    exp.printStackTrace();
                }

                txtName.setValue("");
                txtEmail.setValue("");
                txtPassword.setValue("");

                dataProvider.refreshAll();
            });

            btnCancel.addClickListener((evento) -> {
                txtName.setValue("");
                txtEmail.setValue("");
                txtPassword.setValue("");
            });


            H4 title = new H4("Calendar");
            H6 subtitle = new H6("Events");

            HorizontalLayout btnContainer = new HorizontalLayout();

            Button btnCalendar = new Button("Calendar");

            Button btnLogout = new Button("Logout");
            btnLogout.getElement().setAttribute("theme", "error");

            btnContainer.add(btnCalendar, btnLogout);

            btnLogout.addClickListener((event) -> {
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

            HorizontalLayout btnHorizontalContainer = new HorizontalLayout(btnAdd, btnCancel);
            btnHorizontalContainer.setSpacing(true);

            txtName.setTitle("Name: ");
            txtEmail.setTitle("Email: ");
            txtPassword.setTitle("Password: ");

            tabla.setDataProvider(dataProvider);
            tabla.addColumn(MyUser::getName).setHeader("Name");
            tabla.addColumn(MyUser::getEmail).setHeader("Email");

            tabla.addSelectionListener(event -> {
                if (event.getFirstSelectedItem().isPresent()) {
                    openScreen(managerActionsScreen);
                    managerActionsScreen.btnDelete.addClickListener((evento) -> {
                        MyUser user = event.getFirstSelectedItem().get();
                        usersServices.removeUser((int) user.getId());
                        binder.readBean(user);

                        dataProvider.refreshAll();
                    });

                    managerActionsScreen.btnEdit.addClickListener((evento) -> {
                        MyUser user = event.getFirstSelectedItem().get();

                        txtName.setValue(user.getName());
                        txtEmail.setValue(user.getEmail());
                        txtPassword.setValue(user.getPassword());
                        editing = true;
                        selectedId = user.getId();

                        try {
                            binder.writeBean(user);
                        } catch (ValidationException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });

            setAlignItems(Alignment.CENTER);
            FormLayout form = new FormLayout(txtName, txtEmail, txtPassword);

            add(title, subtitle, btnContainer, form, btnHorizontalContainer, tabla);

            txtName.setValue("");
            txtEmail.setValue("");
            txtPassword.setValue("");
        }
    }

    private void openScreen(VerticalLayout form) {
        Dialog screen = new Dialog();
        screen.add(form);
        screen.open();
    }
}

