package com.pucmm.edu.vaadin.Design;

import com.sendgrid.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.IOException;
import java.util.Map;

@SpringComponent
@UIScope
public class EmailScreen extends VerticalLayout {
    TextField from = new TextField("To:");
    TextField subject = new TextField("Subject:");
    TextArea body = new TextArea("Body:");

    public EmailScreen() {
        FormLayout formLayout = new FormLayout();

        H3 header = new H3("Send email");

        Button btnSend = new Button("Send");
        btnSend.getElement().setAttribute("theme", "primary");

        HorizontalLayout btnContainer = new HorizontalLayout(btnSend);

        btnContainer.setSpacing(true);

        formLayout.add(from, subject, body);
        setAlignItems(FlexComponent.Alignment.CENTER);

        add(header, formLayout, btnContainer);

        btnSend.addClickListener((event) -> {
            Email fromEmail = new Email("20160290@ce.pucmm.edu.do");
            String emailSubject = subject.getValue();
            Email toEmail = new Email(from.getValue());
            Content emailBody = new Content("text/plain", body.getValue());
            Mail email = new Mail(fromEmail, emailSubject, toEmail, emailBody);
            String apiKey = "SENDGRID_API_KEY";
            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();

            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(email.build());
                sg.api(request);
                from.setValue("");
                subject.setValue("");
                body.setValue("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
