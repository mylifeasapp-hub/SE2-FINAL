package org.se2final.model.objects.dto;

public class User {

    private int id;
    private String gender;
    private String name;
    private String surname;
    private String email;
    private String passwort;
    private String rolle;

    public User(){

    }

    public User(int id, String gender, String name, String surname, String email, String passwort, String rolle){
        this.id = id;
        this.gender = gender;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.passwort = passwort;
        this.rolle = rolle;
    }

    public int getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswort() {
        return passwort;
    }

    public String getRolle() {
        return rolle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }


}
