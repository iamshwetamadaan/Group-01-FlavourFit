package com.flavourfit.Trackers.Water;

public class WaterGraphDto {

        private String date;
        private double waterIntake;

        public WaterGraphDto(String date, double waterIntake) {
            this.date = date;
            this.waterIntake = waterIntake;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getWaterIntake() {
            return waterIntake;
        }

        public void setWaterIntake(double waterIntake) {
            this.waterIntake = waterIntake;
        }
    }



