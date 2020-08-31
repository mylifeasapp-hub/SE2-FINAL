package org.se2final.components;

import com.vaadin.ui.*;
import org.se2final.service.konstanten.Views;


public class LoginWindow extends Window {
    public LoginWindow(){
        super("Anmelden:");
        FormLayout content = new FormLayout();

        TextField emailField = new TextField("E-Mail Adresse:");
        emailField.setValue("E-Mail Adresse hier eingeben...");

        PasswordField passwordField = new PasswordField("Passwort:");
        passwordField.setValue("Passwort");

        HorizontalLayout buttonPane = new HorizontalLayout();
        Button loginButton = new Button("Anmelden");
        Button cancelButton = new Button("Abbrechen");
        buttonPane.addComponents(loginButton, cancelButton);
        buttonPane.setSizeUndefined();

        content.addComponents(emailField, passwordField, buttonPane);

        content.setSizeUndefined(); // Shrink to fit
        content.setMargin(true);
        setContent(content);
        center();
        setClosable(false);
        setResizable(false);
        setModal(true);

        cancelButton.addClickListener(event -> this.close());
        loginButton.addClickListener(event -> {
            UI.getCurrent().getNavigator().navigateTo(Views.LANDING);
            this.close();
        });
    }
}
