package com.flavourfit.Trackers.Weights;

public class WeightGraphDto {

        private String date;
        private double weight;

        public WeightGraphDto(String date, double weight) {
            this.date = date;
            this.weight = weight;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }





