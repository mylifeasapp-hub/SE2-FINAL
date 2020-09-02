package org.se2final.model.objects.dao;

import org.se2final.control.exceptions.DatabaseException;
import org.se2final.service.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractDAO {

    protected Statement getStatement(){
        Statement statement = null;

        try{
            statement = JDBCConnection.getInstance().getStatement();
        } catch(DatabaseException ex){
            Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statement;
    }

    protected PreparedStatement getPreparedStatement(String sql){
        PreparedStatement statement = null;
        try{
            statement = JDBCConnection.getInstance().getPreparedStatement(sql);
        } catch(DatabaseException ex){
            Logger.getLogger(AbstractDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statement;

    }
}
