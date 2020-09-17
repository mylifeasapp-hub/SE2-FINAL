package org.se2final.model.objects.dao;

import com.vaadin.server.VaadinService;
import org.se2final.control.HashFunktionsKlasse;
import org.se2final.control.RegCheck;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.User;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
    public User getUserByID(int currentUserID) throws SQLException {
        String sql = "Select * from carpool.reg_user where reg_user_id=?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        statement.setInt(1, currentUserID);
        //Ausgabe der Autos
        User currentUser = new User();
        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return null;

            while(rs.next()){
                currentUser.setId(rs.getInt(1));
                currentUser.setGender(rs.getString(2));
                currentUser.setName(rs.getString(3));
                currentUser.setSurname(rs.getString(4));
                currentUser.setEmail(rs.getString(5));
                currentUser.setPasswort(rs.getString(6));
                currentUser.setRolle(rs.getString(7));

            }


        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return currentUser;
    }
    public void updateUser(User currentUser){
        String sql = "update carpool.reg_user set " +
                "reg_user_gender = '" + currentUser.getGender() + "', "+
                "reg_user_name = '" + currentUser.getName() + "', "+
                "reg_user_surname = '" + currentUser.getSurname() + "' "+
                "where reg_user_id="+currentUser.getId();

        if(!currentUser.getPasswort().isEmpty()){
            sql = sql + "; update carpool.reg_user set reg_user_passwort = '"+HashFunktionsKlasse.getHash(currentUser.getPasswort().getBytes(), "MD5")+"' where reg_user_id ="+currentUser.getId()+";";
        }
        PreparedStatement statement = this.getPreparedStatement(sql);
        //Angaben in reg_user schreiben
        try{
            statement.execute();
        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteWorker(int userID){
        String sql =    "delete from carpool.reservation r where exists ( select 1 from carpool.cars c where r.car_id = c.car_id and exists (select 1 from carpool.reg_user u where u.reg_user_id = c.car_reg_user_id and u.reg_user_id = "+userID+")); "+
                        "delete from carpool.cars c where exists (select 1 from carpool.reg_user u where u.reg_user_id = c.car_reg_user_id and u.reg_user_id = "+userID+"); " +
                        "delete from carpool.reg_user u where u.reg_user_id = "+userID+";";

        List<Cars> ownedCars = null;
        try {
            ownedCars = CarsDAO.getInstance().carsByUser(UserDAO.getInstance().getUserByID(userID));
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Cars cars:ownedCars) {
            if(!cars.getCarPicture().isEmpty()){
                File image = new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() +"/WEB-INF/cars/"+cars.getCarPicture()+".jpg");
                image.delete();
            }
        }

        PreparedStatement statement = this.getPreparedStatement(sql);
        //User löschen
        try{
            statement.execute();
        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteCustomer(int userID){
        String sql =    "delete from carpool.reservation r where exists (select 1 from carpool.reg_user u where u.reg_user_id = r.user_id and u.reg_user_id = "+userID+");"+
                        "delete from carpool.reg_user u where u.reg_user_id = "+userID+";";

        PreparedStatement statement = this.getPreparedStatement(sql);
        //User löschen
        try{
            statement.execute();
        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public User getUserID(User user){
        String sql = "Select reg_user_id from carpool.reg_user where reg_user_email='"+user.getEmail()+"'";
        PreparedStatement statement = this.getPreparedStatement(sql);
        int userID = 0;
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                userID = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        user.setId(userID);
        return user;
    }

}
