package com.flavourfit.User;



/**
 * Data Transfer Object(DTO) for the users table
 */
public class

PremiumUserDto {
    private int userId;
    private int membership_ID;

    private String Start_date;

    private String Expiry_date;

    private int Is_active;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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


    @Override
    public String toString() {
        return "PremiumUserDto{" +
                "userId=" + userId +
                "Membership_ID='" + membership_ID + '\'' +
                ", Start_date='" + Start_date + '\'' +
                ", Expiry_Date='" + Expiry_date + '\'' +
                ", Is_Active='" + Is_active + '\''+
                '}';
    }
}
