package com.pucmm.edu.vaadin.Design;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class ManagerActionsScreen extends VerticalLayout {

    Button btnEdit = new Button("Edit");
    Button btnDelete = new Button("Delete");

    public ManagerActionsScreen() {
        btnEdit.getElement().setAttribute("theme", "success");
        btnDelete.getElement().setAttribute("theme", "error");

        HorizontalLayout btnContainer = new HorizontalLayout(btnEdit, btnDelete);
        btnContainer.setSpacing(true);
        add(btnContainer);
    }
}
