package org.se2final.model.objects.dao;

import org.se2final.control.HashFunktionsKlasse;
import org.se2final.control.RegCheck;
import org.se2final.model.objects.dto.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO extends AbstractDAO{
    private static UserDAO dao = null;
    private  UserDAO(){

    }
    public static UserDAO getInstance(){
        if(dao == null){
            dao = new UserDAO();
        }
        return dao;
    }

    public void registerUser(User newUser){
        String sql = "insert into carpool.reg_user (reg_user_gender, reg_user_name, reg_user_surname, reg_user_email, reg_user_passwort, reg_user_rolle) values (?,?,?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        //Angaben in reg_user schreiben
        try{
            statement.setString(1, newUser.getGender());
            statement.setString(2, newUser.getName());
            statement.setString(3, newUser.getSurname());
            statement.setString(4, newUser.getEmail());
            statement.setString(5, HashFunktionsKlasse.getHash(newUser.getPasswort().getBytes(), "MD5"));
            statement.setString(6, newUser.getRolle());

            statement.executeUpdate();

        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}