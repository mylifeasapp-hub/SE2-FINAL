package org.se2final.model.objects.dto;

public class Cars {
    private int carID;
    private int ownerID;
    private int carPS;
    private int carMaxSpeed;
    private String carYear;
    private String carDescription;
    private String carBrand;
    private String carPicture;
    private String carModel;


    public Cars(){}

    public Cars(int carID, int ownerID, int carPS, int carMaxSpeed, String carYear, String carDescription, String carBrand, String carPicture, String carModel) {
        this.carID = carID;
        this.ownerID = ownerID;
        this.carPS = carPS;
        this.carMaxSpeed = carMaxSpeed;
        this.carYear = carYear;
        this.carDescription = carDescription;
        this.carBrand = carBrand;
        this.carPicture = carPicture;
        this.carModel = carModel;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public void setCarDescription(String carDescription) {
        this.carDescription = carDescription;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public void setCarPicture(String carPicture) {
        this.carPicture = carPicture;
    }



    public int getCarID() {
        return carID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getCarYear() {
        return carYear;
    }

    public String getCarDescription() {
        return carDescription;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarPicture() {
        return carPicture;
    }

    public int getCarPS() {
        return carPS;
    }

    public void setCarPS(int carPS) {
        this.carPS = carPS;
    }

    public int getCarMaxSpeed() {
        return carMaxSpeed;
    }

    public void setCarMaxSpeed(int carMaxSpeed) {
        this.carMaxSpeed = carMaxSpeed;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }






}
