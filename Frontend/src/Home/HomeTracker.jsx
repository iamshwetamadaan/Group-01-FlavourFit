import React, { useEffect, useState } from "react";
import { axiosRequest } from "../HttpClients/axiosService";

const HomeTracker = (props) => {
  const [trackerSummary, setTrackerSummary] = useState(null);
  useEffect(() => {
    axiosRequest(
      {
        url: "/home/tracker-summary",
        method: "get",
      },
      (response) => {
        const trackerSummary = response?.data?.data ?? null;
        if (trackerSummary) {
          setTrackerSummary({ ...trackerSummary });
        }
      },
      (error) => {}
    );
  }, []);

  if (!trackerSummary) {
    return null;
  }

  return (
    <div className="ff-home-tracker">
      <div className="home-streak">
        Fitness Streak: {trackerSummary.fitnessStreak ?? 0} days
      </div>
      <div className="home-trackers">
        <div className="home-tracker-container ci-green">
          <div className="home-tracker-label ff-green">Calories</div>
          <div className="home-tracker-value ff-green">
            {trackerSummary.calorieCount ?? 0} kCal
          </div>
        </div>
        <div className="home-tracker-container ci-blue">
          <div className="home-tracker-label ff-blue">Water intake</div>
          <div className="home-tracker-value ff-blue">
            {trackerSummary.waterIntake ?? 0} mL
          </div>
        </div>
        <div className="home-tracker-container ci-orange">
          <div className="home-tracker-label ff-orange">Current weight</div>
          <div className="home-tracker-value ff-orange">
            {trackerSummary.currentWeight ?? 0} kg
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomeTracker;
