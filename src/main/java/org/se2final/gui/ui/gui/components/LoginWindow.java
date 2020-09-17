package org.se2final.gui.ui.gui.components;

import com.vaadin.ui.*;
import org.se2final.control.LoginCheck;
import org.se2final.control.RegCheck;
import org.se2final.control.exceptions.DatabaseException;
import org.se2final.control.exceptions.NoSuchUserOrPassword;
import org.se2final.gui.ui.views.Main;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginWindow extends Window {
    public LoginWindow(){
        super("Anmelden:");
        FormLayout content = new FormLayout();

        TextField emailField = new TextField("E-Mail Adresse:");

        PasswordField passwordField = new PasswordField("Passwort:");


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

        loginButton.addClickListener(e -> {

            String loginEmail = emailField.getValue();
            String password = passwordField.getValue();

            try{
                LoginCheck.checkAuthentication(loginEmail,password);
            } catch (NoSuchUserOrPassword noSuchUserOrPassword){
                boolean registered = false;

                try {
                    registered = RegCheck.isNotRegistered(emailField.getValue());
                } catch (DatabaseException databaseException) {
                    databaseException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                if(registered == true){
                    Notification wrongMail = Notification.show("Fehler:", "Zur eingegebenen E-Mail Adresse existiert kein Konto!", Notification.Type.ERROR_MESSAGE);
                    wrongMail.setStyleName("error");
                }else {
                    Notification wrongPassword = Notification.show("Fehler:", "Das eingegebene Passwort ist falsch!", Notification.Type.ERROR_MESSAGE);
                    wrongPassword.setStyleName("error");
                }

                emailField.setValue("");
                passwordField.setValue("");
            } catch (DatabaseException ex){
                Notification.show("DB-Fehler", ex.getReason(), Notification.Type.ERROR_MESSAGE);
                emailField.setValue("");
                passwordField.setValue("");
            } catch (SQLException throwables) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, throwables);
            }
            this.close();
        });
    }
}
