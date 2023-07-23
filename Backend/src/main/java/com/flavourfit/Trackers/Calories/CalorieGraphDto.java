package com.flavourfit.Trackers.Calories;

public class CalorieGraphDto {
    private String date;
    private double calorie;

    public CalorieGraphDto(String date, double calorie) {
        this.date = date;
        this.calorie = calorie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }
}
