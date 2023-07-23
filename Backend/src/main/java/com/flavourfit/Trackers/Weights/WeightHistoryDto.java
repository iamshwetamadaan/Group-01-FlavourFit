package com.flavourfit.Trackers.Weights;

public class WeightHistoryDto {


        private int weightHistoryId;
        private double Weight;
        private String updateDate;
        private int userId;

        public WeightHistoryDto(int weightHistoryId, double Weight, String updateDate, int userId) {
            this.weightHistoryId = weightHistoryId;
            this.Weight = Weight;
            this.updateDate = updateDate;
            this.userId = userId;
        }

        public WeightHistoryDto(double Weight, String updateDate, int userId) {
            this.Weight = Weight;
            this.updateDate = updateDate;
            this.userId = userId;
        }

        public int getWeightHistoryId() {
            return weightHistoryId;
        }

        public void setWeightHistoryId(int waterHistoryId) {
            this.weightHistoryId = weightHistoryId;
        }

        public double getWeight() {
            return Weight;
        }

        public void setWeight(double Weight) {
            this.Weight = Weight;
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



