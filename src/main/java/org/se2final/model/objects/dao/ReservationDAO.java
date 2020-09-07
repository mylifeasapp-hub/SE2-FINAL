package org.se2final.model.objects.dao;

import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.Reservation;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationDAO extends AbstractDAO{
    private static ReservationDAO dao = null;
    private  ReservationDAO(){

    }
    public static ReservationDAO getInstance(){
        if(dao == null){
            dao = new ReservationDAO();
        }
        return dao;
    }

    public void sendReservation(Reservation newReservation){
        String sql = "insert into carpool.reservation (user_id, res_date, res_time, res_status, car_id) values (?,?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        //Angaben in cars schreiben
        try{
            statement.setInt(1, newReservation.getUserID());
            statement.setDate(2, newReservation.getResDate());
            statement.setTime(3, newReservation.getResTime());
            statement.setString(4, newReservation.getResStatus());
            statement.setInt(5, newReservation.getCarID());


            statement.executeUpdate();

        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Reservation> getReservationsForWorker(int currentUser) throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String sql = "SELECT r.car_id, r.res_date, r.res_status, r.res_time, r.user_id FROM\n" +
                "carpool.reg_user ru,\n" +
                "carpool.cars c,\n" +
                "carpool.reservation r\n" +
                "WHERE \n" +
                "ru.reg_user_id = ? and ru.reg_user_id = c.car_reg_user_id and c.car_id = r.car_id;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        statement.setInt(1, currentUser);

        //Ausgabe der Autos
        if(statement==null) return Collections.emptyList();

        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return Collections.emptyList();

            while(rs.next()){
                Reservation resTemp = new Reservation();

                resTemp.setCarID(rs.getInt(1));
                resTemp.setResDate(rs.getDate(2));
                resTemp.setResStatus(rs.getString(3));
                resTemp.setResTime(rs.getTime(4));
                resTemp.setUserID(rs.getInt(5));

                reservationList.add(resTemp);
            }


        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservationList;
    }

    public List<Reservation> getReservationsForCustomer(int currentUser) throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String sql = "SELECT r.car_id, r.res_date, r.res_status, r.res_time, r.user_id FROM\n" +
                "carpool.reg_user ru,\n" +
                "carpool.cars c,\n" +
                "carpool.reservation r\n" +
                "WHERE \n" +
                "ru.reg_user_id = ? and ru.reg_user_id = r.user_id and r.car_id = c.car_id;";
        PreparedStatement statement = this.getPreparedStatement(sql);
        statement.setInt(1, currentUser);

        //Ausgabe der Autos
        if(statement==null) return Collections.emptyList();

        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return Collections.emptyList();

            while(rs.next()){
                Reservation resTemp = new Reservation();

                resTemp.setCarID(rs.getInt(1));
                resTemp.setResDate(rs.getDate(2));
                resTemp.setResStatus(rs.getString(3));
                resTemp.setResTime(rs.getTime(4));
                resTemp.setUserID(rs.getInt(5));

                reservationList.add(resTemp);
            }


        } catch(SQLException ex){
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reservationList;
    }

}
