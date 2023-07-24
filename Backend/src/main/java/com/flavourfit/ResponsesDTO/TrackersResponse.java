package com.flavourfit.ResponsesDTO;

import com.flavourfit.Trackers.Calories.CalorieHistoryDto;
import com.flavourfit.Trackers.Water.WaterHistoryDto;
import com.flavourfit.Trackers.Weights.WeightHistoryDto;

public class TrackersResponse {

        private boolean success;
        private String message;
        private WaterHistoryDto waterIntake;
        private CalorieHistoryDto calories;
        private WeightHistoryDto weight;
        public TrackersResponse(boolean success, String message, WaterHistoryDto waterIntake, CalorieHistoryDto calories, WeightHistoryDto weight) {
            this.success = success;
            this.message = message;
            this.waterIntake = waterIntake;
            this.calories = calories;
            this.weight = weight;
        }



}
