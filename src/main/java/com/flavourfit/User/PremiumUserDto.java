package com.flavourfit.User;



/**
 * Data Transfer Object(DTO) for the users table
 */
public class

PremiumUserDto {
    private int userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int age;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private double currentWeight;
    private double targetWeight;
    private String type;

    private int membership_ID;

    private String Start_date;

    private String Expiry_date;

    private int Is_active;

    private String password;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(double targetWeight) {
        this.targetWeight = targetWeight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMembership_ID() {
        return membership_ID;
    }

    public String getStart_date() {
        return Start_date;
    }

    public String getExpiry_date() {
        return Expiry_date;
    }

    public int getIs_active() {
        return Is_active;
    }

    public void setMembership_ID(int membership_ID) {
        this.membership_ID = membership_ID;
    }

    public void setStart_date(String start_date) {
        Start_date = start_date;
    }

    public void setExpiry_date(String expiry_date) {
        Expiry_date = expiry_date;
    }

    public void setIs_active(int is_active) {
        Is_active = is_active;
    }

    /*@Override
    public String toString() {
        return "UserDto{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", currentWeight=" + currentWeight +
                ", targetWeight=" + targetWeight +
                ", type='" + type + '\'' +
                ", password='" + password + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return "PremiumUserDto{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", currentWeight=" + currentWeight +
                ", targetWeight=" + targetWeight +
                ", type='" + type + '\'' +
                ", password='" + password + '\'' +
                ", Membership_ID='" + membership_ID + '\'' +
                ", Start_date='" + Start_date + '\'' +
                ", Expiry_Date='" + Expiry_date + '\'' +
                ", Is_Active='" + Is_active + '\''+
                '}';
    }
}
