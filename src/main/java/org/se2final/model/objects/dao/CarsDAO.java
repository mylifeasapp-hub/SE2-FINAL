package org.se2final.model.objects.dao;

import com.vaadin.server.VaadinService;
import org.junit.jupiter.engine.discovery.predicates.IsTestClassWithTests;
import org.se2final.control.HashFunktionsKlasse;
import org.se2final.model.objects.dto.Cars;
import org.se2final.model.objects.dto.Reservation;
import org.se2final.model.objects.dto.User;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CarsDAO extends AbstractDAO {
    private static CarsDAO dao = null;
    private  CarsDAO(){

    }
    public static CarsDAO getInstance(){
        if(dao == null){
            dao = new CarsDAO();
        }
        return dao;
    }

    public void enterCar(Cars newCar){
        String sql = "insert into carpool.cars (car_year, car_description, car_brand, car_picture, car_reg_user_id, car_modell, car_ps, car_maxspeed) values (?,?,?,?,?,?,?,?)";
        PreparedStatement statement = this.getPreparedStatement(sql);

        //Angaben in cars schreiben
        try{
            statement.setString(1, newCar.getCarYear());
            statement.setString(2, newCar.getCarDescription());
            statement.setString(3, newCar.getCarBrand());
            statement.setString(4, newCar.getCarPicture());
            statement.setInt(5, newCar.getOwnerID());
            statement.setString(6, newCar.getCarModel());
            statement.setInt(7, newCar.getCarPS());
            statement.setInt(8, newCar.getCarMaxSpeed());

            statement.executeUpdate();

        } catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Cars> carsByUser(User ownerUser) throws SQLException {
        List<Cars> carsList = new ArrayList<>();
        String sql = "Select * from carpool.cars where car_reg_user_id=?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        statement.setInt(1, ownerUser.getId());
        //Ausgabe der Autos
        if(statement==null) return Collections.emptyList();

        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return Collections.emptyList();

            while(rs.next()){
                Cars carTemp = new Cars();

                carTemp.setCarID(rs.getInt(1));
                carTemp.setCarYear(rs.getString(2));
                carTemp.setCarDescription(rs.getString(3));
                carTemp.setCarBrand(rs.getString(4));
                carTemp.setCarPicture(rs.getString(5));
                carTemp.setOwnerID(rs.getInt(6));
                carTemp.setCarModel(rs.getString(7));
                carTemp.setCarPS((rs.getInt(8)));
                carTemp.setCarMaxSpeed(rs.getInt(9));

                carsList.add(carTemp);
            }


        } catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return carsList;
    }

    public List<Cars> carsComplete() throws SQLException {
        List<Cars> carsList = new ArrayList<>();
        String sql = "Select * from carpool.cars";
        PreparedStatement statement = this.getPreparedStatement(sql);

        //Ausgabe der Autos
        if(statement==null) return Collections.emptyList();

        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return Collections.emptyList();

            while(rs.next()){
                Cars carTemp = new Cars();

                carTemp.setCarID(rs.getInt(1));
                carTemp.setOwnerID(rs.getInt(6));
                carTemp.setCarYear(rs.getString(2));
                carTemp.setCarDescription(rs.getString(3));
                carTemp.setCarBrand(rs.getString(4));
                carTemp.setCarPicture(rs.getString(5));

                carsList.add(carTemp);
            }


        } catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return carsList;
    }

    //Für StellenangebotSuchenView
    public List<Cars> searchCars(String search []) throws SQLException {
        PreparedStatement statement = searchCarsStatement(search);
        List<Cars> carsList = new ArrayList<>();
        return searchCarsList(carsList, statement);
    }
    public PreparedStatement searchCarsStatement(String search []) throws SQLException {
        String basic = "SELECT * FROM carpool.cars";
        PreparedStatement statement;
        String input;

        if(search[0].equals("")){
            statement = this.getPreparedStatement(basic);
        }
        else{
            String source = " and concat_ws(upper(car_brand), upper(car_description), upper(car_year), car_ps, car_maxspeed, upper(car_modell)) like '";
            String sql = "select * from carpool.cars where concat_ws(upper(car_brand), upper(car_description), upper(car_year), car_ps, car_maxspeed, upper(car_modell)) like '%"+search[0].toUpperCase()+"%'";
            for(int i = 1; i<search.length; i++){
                input = source + "%"+search[i].toUpperCase()+"%' ";
                sql = sql + input;
            }
            statement = this.getPreparedStatement(sql+";");
        }


        return statement;
    }

    private List<Cars> searchCarsList(List<Cars> carsList, PreparedStatement statement) {
        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return Collections.emptyList();
            while(rs.next()) carsList.add(getCar(rs));
        }catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return carsList;
    }

    private Cars getCar(ResultSet rs) throws SQLException {
        Cars carTemp = new Cars();

        carTemp.setCarID(rs.getInt(1));
        carTemp.setOwnerID(rs.getInt(6));
        carTemp.setCarYear(rs.getString(2));
        carTemp.setCarDescription(rs.getString(3));
        carTemp.setCarBrand(rs.getString(4));
        carTemp.setCarPicture(rs.getString(5));
        carTemp.setCarModel(rs.getString(7));
        carTemp.setCarPS(rs.getInt(8));
        carTemp.setCarMaxSpeed(rs.getInt(9));

        return carTemp;
    }

    public Cars getCarByID(int currentCar) throws SQLException {
        String sql = "Select * from carpool.cars where car_id=?";
        PreparedStatement statement = this.getPreparedStatement(sql);
        statement.setInt(1, currentCar);
        //Ausgabe der Autos
        Cars carTemp = new Cars();
        try(ResultSet rs = statement.executeQuery()){
            if(rs == null) return null;

            while(rs.next()){
                carTemp.setCarID(rs.getInt(1));
                carTemp.setCarYear(rs.getString(2));
                carTemp.setCarDescription(rs.getString(3));
                carTemp.setCarBrand(rs.getString(4));
                carTemp.setCarPicture(rs.getString(5));
                carTemp.setOwnerID(rs.getInt(6));
                carTemp.setCarModel(rs.getString(7));
                carTemp.setCarPS((rs.getInt(8)));
                carTemp.setCarMaxSpeed(rs.getInt(9));
            }


        } catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return carTemp;
    }

    public void updateCar(Cars currentCar){
        String sql =    "update carpool.cars set " +
                "car_year= '"+currentCar.getCarYear()+"', " +
                "car_description = '" + currentCar.getCarDescription()+"', " +
                "car_brand = '" + currentCar.getCarBrand()+"', " +
                "car_picture = '" + currentCar.getCarPicture()+"', " +
                "car_modell = '" + currentCar.getCarModel()+"', " +
                "car_ps = '" + currentCar.getCarPS()+"', " +
                "car_maxspeed = '" + currentCar.getCarMaxSpeed()+"' " +
                "where car_id = "+currentCar.getCarID()+";";

        PreparedStatement statement = this.getPreparedStatement(sql);

        //Angaben in cars schreiben
        try{
            statement.execute();

        } catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteCar(int carID){
        String sql =    "delete from carpool.reservation where car_id = "+carID+";" +
                        "delete from carpool.cars where car_id = "+carID+";";

        PreparedStatement statement = this.getPreparedStatement(sql);
        Cars tempCar = null;
        //Bild löschen
        try {
            tempCar = CarsDAO.getInstance().getCarByID(carID);
        } catch (SQLException ex) {
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!tempCar.getCarPicture().isEmpty()){
            File image = new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() +"/WEB-INF/cars/"+tempCar.getCarPicture()+".jpg");
            image.delete();
        }
        //Auto löschen
        try{
            statement.execute();

        } catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
