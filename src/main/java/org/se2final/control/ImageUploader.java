package org.se2final.control;

import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import org.se2final.control.exceptions.DatabaseException;
import org.se2final.gui.ui.views.MyCars;
import org.se2final.model.objects.dao.AbstractDAO;
import org.se2final.model.objects.dao.CarsDAO;
import org.se2final.model.objects.dto.Cars;
import org.se2final.service.db.JDBCConnection;


import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageUploader extends AbstractDAO {
    String sql;

    public int getPicture(int carID) throws DatabaseException, SQLException {
        sql = "SELECT * FROM carpool.cars WHERE car_id = ?";
        PreparedStatement statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        statement.setInt(1, carID);
        try(ResultSet set = statement.executeQuery()){
            int tmp = 0;
            if(set.next()) {
                tmp = set.getInt(8);
            }
            return tmp;
        }catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public void setPicture(int id, int counter) throws DatabaseException {
        sql = "UPDATE coll_at_hbrs.user SET bild_id = ? WHERE benutzerid = ?";

        try(PreparedStatement statement = this.getPreparedStatement(sql)){
            statement.setInt(1, counter);
            statement.setInt(2, id);
            statement.executeUpdate();
        }catch(SQLException ex){
            Logger.getLogger(CarsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static FileResource carPicture(String fileName){
        ImageUploader data = new ImageUploader();
        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath() +"/";
        FileResource resourcePicture = null;
        if(fileName.isEmpty()) {
            resourcePicture = new FileResource(new File(basepath + "WEB-INF/images/stock.png"));
        } else {
            resourcePicture = new FileResource(new File(basepath + "WEB-INF/cars/" + fileName + ".jpg"));
        }
        return resourcePicture;
    }
}