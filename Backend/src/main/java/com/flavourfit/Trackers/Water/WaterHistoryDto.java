package com.flavourfit.Trackers.Water;

public class WaterHistoryDto {

    private int waterHistoryId;
    private double WaterIntake;
    private String updateDate;
    private int userId;

    public WaterHistoryDto(int waterHistoryId, double waterIntake, String updateDate, int userId) {
        this.waterHistoryId = waterHistoryId;
        this.WaterIntake = waterIntake;
        this.updateDate = updateDate;
        this.userId = userId;
    }

    public WaterHistoryDto(double waterIntake, String updateDate, int userId) {
        this.WaterIntake = waterIntake;
        this.updateDate = updateDate;
        this.userId = userId;
    }

    public int getWaterHistoryId() {
        return waterHistoryId;
    }

    public void setWaterHistoryId(int waterHistoryId) {
        this.waterHistoryId = waterHistoryId;
    }

    public double getWaterIntake() {
        return WaterIntake;
    }

    public void setWaterIntake(double WaterIntake) {
        this.WaterIntake = WaterIntake;
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
