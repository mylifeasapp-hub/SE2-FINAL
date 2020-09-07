package org.se2final.model.objects.dto;

import java.sql.Time;
import java.sql.Date;

public class Reservation {
    private int userID;
    private int carID;
    private int workID;
    private Date resDate;
    private Time resTime;
    private String resStatus;

    public Reservation(){}

    public Reservation(int userID, int carID, Date resDate, Time resTime, String resStatus) {
        this.userID = userID;
        this.carID = carID;
        this.resDate = resDate;
        this.resTime = resTime;
        this.resStatus = resStatus;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public Date getResDate() {
        return resDate;
    }

    public void setResDate(Date resDate) {
        this.resDate = resDate;
    }

    public Time getResTime() {
        return resTime;
    }

    public void setResTime(Time resTime) {
        this.resTime = resTime;
    }

    public String getResStatus() {
        return resStatus;
    }

    public void setResStatus(String resStatus) {
        this.resStatus = resStatus;
    }

    public int getWorkID() {
        return workID;
    }

    public void setWorkID(int workID) {
        this.workID = workID;
    }
}
