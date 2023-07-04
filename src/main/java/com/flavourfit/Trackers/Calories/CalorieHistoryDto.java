package com.flavourfit.Trackers.Calories;

public class CalorieHistoryDto {
    private int calorieHistoryId;
    private double calorieCount;
    private String updateDate;
    private int userId;

    public CalorieHistoryDto(int calorieHistoryId, double calorieCount, String updateDate, int userId) {
        this.calorieHistoryId = calorieHistoryId;
        this.calorieCount = calorieCount;
        this.updateDate = updateDate;
        this.userId = userId;
    }

    public CalorieHistoryDto(double calorieCount, String updateDate, int userId) {
        this.calorieCount = calorieCount;
        this.updateDate = updateDate;
        this.userId = userId;
    }

    public int getCalorieHistoryId() {
        return calorieHistoryId;
    }

    public void setCalorieHistoryId(int calorieHistoryId) {
        this.calorieHistoryId = calorieHistoryId;
    }

    public double getCalorieCount() {
        return calorieCount;
    }

    public void setCalorieCount(double calorieCount) {
        this.calorieCount = calorieCount;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
