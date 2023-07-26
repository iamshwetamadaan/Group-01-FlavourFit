package com.flavourfit.Homepage.DTO;

public class FitnessStreakDTO {
    private int streak;
    private double avgCalorie;
    private double avgWaterIntake;

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public double getAvgCalorie() {
        return avgCalorie;
    }

    public void setAvgCalorie(double avgCalorie) {
        this.avgCalorie = avgCalorie;
    }

    public double getAvgWaterIntake() {
        return avgWaterIntake;
    }

    public void setAvgWaterIntake(double avgWaterIntake) {
        this.avgWaterIntake = avgWaterIntake;
    }
}
