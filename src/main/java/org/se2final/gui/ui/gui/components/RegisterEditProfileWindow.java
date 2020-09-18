package org.se2final.gui.ui.gui.components;


import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import org.se2final.control.RegCheck;
import org.se2final.control.exceptions.DatabaseException;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dao.UserDAO;
import org.se2final.model.objects.dto.User;


import java.sql.SQLException;


public class RegisterEditProfileWindow extends Window {
    private boolean isRegistered;
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);

    public RegisterEditProfileWindow(){
        FormLayout content = new FormLayout();

        NativeSelect<String> anredeField = new NativeSelect<>("Anrede:");
        anredeField.setItems("-", "Herr", "Frau");

        NativeSelect<String> roleField = new NativeSelect<>("Ich bin:");
        roleField.setItems(Roles.KUNDE, Roles.MITARBEITER);

        TextField nachnameField = new TextField("Nachname:");
        TextField vornameField = new TextField("Vorname:");

        TextField emailField = new TextField("E-Mail Adresse:");

        PasswordField passwordField = new PasswordField("Passwort:");
        PasswordField passwordConfirmField = new PasswordField("Passwort:");

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

        //Click Listener
        cancelButton.addClickListener(event -> this.close());

        registerButton.addClickListener(event -> {
            try {
                isRegistered = RegCheck.isNotRegistered(emailField.getValue());
            } catch (DatabaseException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }catch (NullPointerException n){
                n.printStackTrace();
            }
            System.out.println(user);
            try {
                if(user == null) {
                    if (anredeField.getValue().isEmpty() || roleField.getValue().isEmpty() || nachnameField.getValue().isEmpty() ||
                            vornameField.getValue().isEmpty() || emailField.getValue().isEmpty() || passwordField.getValue().isEmpty() ) {
                        Notification.show("Fehler:", "Bitte füllen Sie alle Felder aus!", Notification.Type.ERROR_MESSAGE);
                    } else if (!passwordField.getValue().equals(passwordConfirmField.getValue())) {
                        Notification.show("Fehler:", "Passwörter stimmen nicht überein!", Notification.Type.ERROR_MESSAGE);
                        passwordField.setValue("");
                        passwordConfirmField.setValue("");
                    } else if (passwordField.getValue().length() < 10) {
                        Notification.show("Fehler:", "Passwortlänge muss 10 Zeichen betragen!", Notification.Type.ERROR_MESSAGE);
                        passwordField.setValue("");
                        passwordConfirmField.setValue("");
                    } else if (passwordField.getValue().length() != 0 && passwordField.getValue().length() < 10) {
                        Notification.show("Fehler:", "Passwortlänge muss 10 Zeichen betragen!", Notification.Type.ERROR_MESSAGE);
                        passwordField.setValue("");
                        passwordConfirmField.setValue("");
                    } else if (roleField.getValue().matches(Roles.MITARBEITER) && !emailField.getValue().contains("@carlook.de")) {

                        Notification.show("Fehler:", "Als Angestellter muss ihre E-Mail Adresse mit \"@carlook.de\" enden!", Notification.Type.ERROR_MESSAGE);

                    } else if (RegCheck.isEmail(emailField.getValue()) == false) {
                        Notification.show("Fehler:", "E-Mail Adresse hat kein gültiges Format!", Notification.Type.ERROR_MESSAGE);
                        emailField.setValue("");
                    } else if (isRegistered == false) {
                        Notification.show("Fehler:", "E-Mail Adresse ist bereits registriert!", Notification.Type.ERROR_MESSAGE);
                    } else {
                        User newUser = new User();
                        newUser.setGender(RegCheck.getGender(anredeField.getValue()));
                        newUser.setName(vornameField.getValue());
                        newUser.setSurname(nachnameField.getValue());
                        newUser.setEmail(emailField.getValue());
                        newUser.setPasswort(passwordField.getValue());
                        newUser.setRolle(roleField.getValue());

                        UserDAO.getInstance().registerUser(newUser);
                        Notification.show("Das Konto wurde erfolgreich erstellt!", "", Notification.Type.HUMANIZED_MESSAGE);
                        user = null;
                        this.close();
                        UI.getCurrent().getNavigator().navigateTo(Views.START);
                        }
                }else{
                        int countChanges = 0;

                        user.setPasswort("");

                        if(!user.getGender().equals(RegCheck.getGender(anredeField.getValue()))){
                            ++countChanges;
                            user.setGender(RegCheck.getGender(anredeField.getValue()));

                        }else if(!user.getName().equals(vornameField.getValue())){
                            ++countChanges;
                            user.setName(vornameField.getValue());
                        }else if(!user.getSurname().equals(nachnameField.getValue())){
                            ++countChanges;
                            user.setSurname(nachnameField.getValue());
                        }else if(passwordField.getValue().length()!=0 && passwordConfirmField.getValue().equals(passwordField.getValue()) && passwordField.getValue().length() > 9){
                            ++countChanges;
                            user.setPasswort(passwordField.getValue());
                        }
                        else if(passwordField.getValue().length()!=0 && !passwordConfirmField.getValue().equals(passwordField.getValue())){
                            Notification.show("Fehler:", "Passwörter stimmen nicht überein!", Notification.Type.ERROR_MESSAGE);
                            passwordField.setValue("");
                            passwordConfirmField.setValue("");
                        }else if(passwordField.getValue().length()!=0 && passwordField.getValue().length() < 9){
                            Notification.show("Fehler:", "Passwortlänge muss 10 Zeichen betragen!", Notification.Type.ERROR_MESSAGE);
                            passwordField.setValue("");
                            passwordConfirmField.setValue("");
                        }

                        if(countChanges>0) {
                            UserDAO.getInstance().updateUser(user);
                            Notification.show(countChanges + " Änderungen wurden erfolgreich gespeichert!", "", Notification.Type.HUMANIZED_MESSAGE);
                            UI.getCurrent().getNavigator().navigateTo(Views.LANDING);
                            this.close();
                        }else {
                            Notification.show("Es wurden keine Änderungen vorgenommen!", "", Notification.Type.HUMANIZED_MESSAGE);
                            this.close();
                        }
                }
            }catch (NullPointerException nullPointerException){
                Notification.show("Fehler:", "Bitte füllen Sie alle Felder aus!", Notification.Type.ERROR_MESSAGE);
            }
        });

        //Anpassen ob Profil bearbeitet oder erstellt wird
        if(user!=null){
            this.setCaption("Profil bearbeiten:");
            registerButton.setCaption("Speichern");
            roleField.setEnabled(false);
            emailField.setEnabled(false);

            anredeField.setValue(RegCheck.getGenderFullname(user.getGender()));
            vornameField.setValue(user.getName());
            nachnameField.setValue(user.getSurname());
            emailField.setValue(user.getEmail());
            roleField.setValue(user.getRolle());

            Button deleteButton = new Button("Profil löschen");
            buttonPane.addComponents(registerButton, deleteButton, cancelButton);
            buttonPane.setSizeUndefined();

            deleteButton.addClickListener(event -> {
                ConfirmDelete deleteWindow = new ConfirmDelete();
                UI.getCurrent().addWindow(deleteWindow);
            });


        }else{
            this.setCaption("Registrieren:");
        }

    }
}
