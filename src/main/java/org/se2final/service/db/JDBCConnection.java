package org.se2final.service.db;



import org.se2final.control.exceptions.DatabaseException;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCConnection {

    private static JDBCConnection connection = null;
    private String url = "jdbc:postgresql://dumbo.inf.h-brs.de/dprost2s";
    private Connection conn;

    public static JDBCConnection getInstance() throws DatabaseException {
        if(connection==null){
            connection = new JDBCConnection();
        }
        return connection;
    }

    private JDBCConnection() throws DatabaseException {
        this.initConnection();
    }

    public void initConnection() throws DatabaseException {
        try{
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch(SQLException ex){
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.openConnection();
    }

    public void openConnection() throws DatabaseException {

        try {
            Properties props = new Properties();
            props.setProperty("user", ZugangDB.LOGIN);
            props.setProperty("password", ZugangDB.LOGIN);

            this.conn = DriverManager.getConnection(url, props);

        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            throw new DatabaseException("Fehler bei Datenbankzugriff! Sichere Vebindung vorhanden?");
        }
    }

    public Statement getStatement() throws DatabaseException{

        try {
            if(this.conn.isClosed()){
                this.openConnection();
            }
            return this.conn.createStatement();

        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public PreparedStatement getPreparedStatement(String sql) throws DatabaseException{
        try {
            if(this.conn.isClosed()){
                this.openConnection();
            }
            return this.conn.prepareStatement(sql);

        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean closeConnection(){
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;  }
}
