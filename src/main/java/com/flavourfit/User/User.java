package com.flavourfit.User;

/**
 * Data object for User table
 **/
public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private double currentWeight;
    private double height;


    private double targetWeight;

    public int getUserId() {

        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public double getHeight() {
        return height;
    }


    public double getTargetWeight(){return targetWeight;}

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public void setHeight(double height) {
        this.height = height;
    }
    public void setTargetWeight(double targetWeight){this.targetWeight = targetWeight;}
}
