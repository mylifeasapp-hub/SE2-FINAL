package org.se2final.components;

import com.vaadin.ui.*;


public class RegisterWindow extends Window {
    public RegisterWindow(){
        super("Registrieren:");
        FormLayout content = new FormLayout();

        NativeSelect<String> anredeField = new NativeSelect<>("Anrede:");
        anredeField.setItems("-", "Herr", "Frau");

        NativeSelect<String> roleField = new NativeSelect<>("Ich bin:");
        roleField.setItems("Kunde", "Angestellter");

        TextField nachnameField = new TextField("Nachname:");
        nachnameField.setValue("Nachname");
        TextField vornameField = new TextField("Vorname:");
        vornameField.setValue("Vorname");

        TextField emailField = new TextField("E-Mail Adresse:");
        emailField.setValue("E-Mail");

        PasswordField passwordField = new PasswordField("Passwort:");
        passwordField.setValue("Passwort");
        PasswordField passwordConfirmField = new PasswordField("Passwort:");
        passwordConfirmField.setValue("Passwort");

        HorizontalLayout buttonPane = new HorizontalLayout();
        Button registerButton = new Button("Anmelden");
        Button cancelButton = new Button("Abbrechen");
        buttonPane.addComponents(registerButton, cancelButton);
        buttonPane.setSizeUndefined();

        content.addComponents(roleField, anredeField, nachnameField, vornameField, emailField, passwordField, passwordConfirmField, buttonPane);

        content.setSizeUndefined(); // Shrink to fit
        content.setMargin(true);
        setContent(content);
        center();
        setClosable(false);
        setResizable(false);
        setModal(true);
    }
}
