package org.se2final.control;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.se2final.control.exceptions.DatabaseException;
import org.se2final.control.exceptions.NoSuchUserOrPassword;
import org.se2final.gui.ui.service.konstanten.Roles;
import org.se2final.gui.ui.service.konstanten.Views;
import org.se2final.model.objects.dto.User;
import org.se2final.service.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginCheck {

    public static void checkAuthentication(String email, String passwort) throws NoSuchUserOrPassword, DatabaseException, SQLException {
        User regUser = new User();

        String sql = "SELECT * FROM carpool.reg_user WHERE carpool.reg_user.reg_user_email = ? AND carpool.reg_user.reg_user_passwort = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        statement.setString(1, email);
        statement.setString(2, HashFunktionsKlasse.getHash(passwort.getBytes(), "MD5") );

        try(ResultSet set = statement.executeQuery()){
            if(set.next()){
                regUser.setId(set.getInt(1));
                regUser.setGender(set.getString(2));
                regUser.setName(set.getString(3));
                regUser.setSurname(set.getString(4));
                regUser.setEmail(set.getString(5));
                regUser.setPasswort(set.getString(6));
                regUser.setRolle(set.getString(7));
            }else{
                throw new NoSuchUserOrPassword();
            }
        } catch(SQLException ex){
            Logger.getLogger(LoginCheck.class.getName()).log(Level.SEVERE, null, ex);
        }

        VaadinSession.getCurrent().setAttribute(Roles.CURRENT, regUser);


        UI.getCurrent().getNavigator().navigateTo(Views.LANDING);

    }

    public static void logoutUser() {
        UI.getCurrent().getSession().close();
        UI.getCurrent().getPage().setLocation("#!Start");
        UI.getCurrent().getPage().reload();
    }
}
