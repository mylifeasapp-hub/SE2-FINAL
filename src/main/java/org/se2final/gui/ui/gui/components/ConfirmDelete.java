package org.se2final.gui.ui.gui.components;


import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.se2final.control.HashFunktionsKlasse;
import org.se2final.control.LoginCheck;
import org.se2final.control.exceptions.DatabaseException;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.model.objects.dao.UserDAO;
import org.se2final.model.objects.dto.User;
import org.se2final.service.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmDelete extends Window {
    User user = (User) VaadinSession.getCurrent().getAttribute(Roles.CURRENT);
    public ConfirmDelete(){
        this.setCaption("Account löschen:");

        Label deleteHint = new Label();
        deleteHint.setContentMode(ContentMode.HTML);

        if(user.getRolle().equals(Roles.MITARBEITER)){
            deleteHint.setValue("Sind Sie sicher, dass Sie ihren Account löschen wollen?</br>Folgende Inhalte werden mit Ihrem Account gelöscht:</br><ul><li>Reservierungen</li><li>Autos</li><li>Profil</li></ul>" +
                    "Die Löschung ist unwiederruflich!");
        }else {
            deleteHint.setValue("Sind Sie sicher, dass Sie ihren Account löschen wollen?</br>Folgende Inhalte werden mit Ihrem Account gelöscht:</br><ul><li>Reservierungen</li><li>Profil</li></ul>" +
                    "Die Löschung ist unwiederruflich!");
        }

        PasswordField confirmPassword = new PasswordField("Passwort zur bestätigung eingeben:");

        Button deleteButton = new Button("Löschen");
        Button exitButton = new Button("Abbrechen");

        HorizontalLayout buttonPane = new HorizontalLayout();
        buttonPane.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        buttonPane.addComponents(deleteButton, exitButton);

        FormLayout content = new FormLayout();
        content.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        content.addComponents(deleteHint, confirmPassword, buttonPane);

        content.setSizeUndefined(); // Shrink to fit
        content.setMargin(true);
        setContent(content);
        center();
        setClosable(false);
        setResizable(false);
        setModal(true);

        exitButton.addClickListener(event -> this.close());
        deleteButton.addClickListener(event -> {
            Boolean checkPassword = null;
            String sql = "SELECT * FROM carpool.reg_user WHERE carpool.reg_user.reg_user_email = '"+user.getEmail()+"' AND carpool.reg_user.reg_user_passwort = '"+HashFunktionsKlasse.getHash(confirmPassword.getValue().getBytes(), "MD5")+"'";
            PreparedStatement statement = null;
            try {
                statement = JDBCConnection.getInstance().getPreparedStatement(sql);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }

            try(ResultSet set = statement.executeQuery()){
                if(set.next()){
                    checkPassword = true;
                }else{
                    checkPassword = false;
                }
            } catch(SQLException ex){
                Logger.getLogger(LoginCheck.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(checkPassword == true && user.getRolle().equals(Roles.KUNDE)){
                UserDAO.getInstance().deleteCustomer(user.getId());
                Notification deleteError = Notification.show("Profil wurde erfolgreich gelöscht!", "", Notification.Type.HUMANIZED_MESSAGE);
                deleteError.setStyleName("error");
                LoginCheck.logoutUser();
            }
            else if(checkPassword == true && user.getRolle().equals(Roles.MITARBEITER)){
                UserDAO.getInstance().deleteWorker(user.getId());
                Notification deleteError2 = Notification.show("Profil wurde erfolgreich gelöscht!", "", Notification.Type.HUMANIZED_MESSAGE);
                deleteError2.setStyleName("error");
                LoginCheck.logoutUser();
            }
            else{
                Notification.show("Fehler:","Passwort falsch, versuchen Sie es erneut!", Notification.Type.ERROR_MESSAGE);
            }
        });

    }
}
