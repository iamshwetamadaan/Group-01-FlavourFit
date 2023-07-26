import React from "react";
import "./trackers.scss";
import Calories from "./Calories";
import { Container } from "react-bootstrap";
import { useState } from "react";
import { useEffect } from "react";
import { axiosRequest } from "../HttpClients/axiosService";
import { formatDateToString } from "../Helper";
import WaterIntake from "./WaterIntake";
import Weights from "./Weights";
import { useCallback } from "react";

export const options = {
  plugins: {
    legend: {
      display: false,
    },
    title: {
      display: false,
    },
  },
  scales: {
    x: {
      grid: {
        display: false,
      },
    },
    y: {
      stacked: true,
      grid: {
        display: false,
      },
      grace: "40%",
    },
  },
};

export const getStartAndEndDate = (date) => {
  let endDate = formatDateToString(new Date(date));
  let startDate = new Date(date);
  startDate.setDate(startDate.getDate() - 6);
  startDate = formatDateToString(startDate);

  return {
    startDate,
    endDate,
  };
};

const Trackers = (props) => {
  const [calorieData, setCalorieData] = useState({
    calorieCount: 0.0,
    chartData: [],
  });

  const [waterData, setWaterData] = useState({
    waterIntake: 0.0,
    chartData: [],
  });

  const [weightData, setWeightData] = useState({
    currentWeight: 0.0,
    chartData: [],
  });

  const [calorieDates, setCalorieDates] = useState({
    startDate: getStartAndEndDate(new Date()).startDate,
    endDate: getStartAndEndDate(new Date()).endDate,
  });

  const [waterDates, setWaterDates] = useState({
    startDate: getStartAndEndDate(new Date()).startDate,
    endDate: getStartAndEndDate(new Date()).endDate,
  });

  const [weightDates, setWeightDates] = useState({
    startDate: getStartAndEndDate(new Date()).startDate,
    endDate: getStartAndEndDate(new Date()).endDate,
  });

  useEffect(() => {
    axiosRequest(
      {
        url: "/home/tracker-summary",
        method: "get",
      },
      (response) => {
        const trackerSummary = response?.data?.data ?? null;
        if (trackerSummary) {
          setCalorieData((prevState) => {
            let newState = { ...prevState };
            newState = {
              ...newState,
              calorieCount: trackerSummary.calorieCount,
            };
            return newState;
          });

          setWaterData((prevState) => {
            let newState = { ...prevState };
            newState = {
              ...newState,
              waterIntake: trackerSummary.waterIntake,
            };
            return newState;
          });

          setWeightData((prevState) => {
            let newState = { ...prevState };
            newState = {
              ...newState,
              currentWeight: trackerSummary.currentWeight,
            };
            return newState;
          });
        }
      },
      (error) => {}
    );
  }, []);

  useEffect(() => {
    axiosRequest(
      {
        url: "/trackers/calorie-history",
        method: "get",
        params: {
          startDate: calorieDates.startDate,
          endDate: calorieDates.endDate,
        },
      },
      (response) => {
        const calorieHistory = response?.data?.data ?? [];
        if (calorieHistory && calorieHistory.length > 0) {
          setCalorieData((prevState) => {
            let newState = { ...prevState };
            newState = {
              ...newState,
              chartData: [...calorieHistory].reverse(),
            };
            return newState;
          });
        }
      },
      (error) => {}
    );
  }, [calorieDates]);

  useEffect(() => {
    axiosRequest(
      {
        url: "/trackers/water-history",
        method: "get",
        params: {
          startDate: waterDates.startDate,
          endDate: waterDates.endDate,
        },
      },
      (response) => {
        const waterHistory = response?.data?.data ?? [];
        if (waterHistory && waterHistory.length > 0) {
          setWaterData((prevState) => {
            let newState = { ...prevState };
            newState = {
              ...newState,
              chartData: [...waterHistory].reverse(),
            };
            return newState;
          });
        }
      },
      (error) => {}
    );
  }, [waterDates]);

  useEffect(() => {
    axiosRequest(
      {
        url: "/trackers/weight-history",
        method: "get",
        params: {
          startDate: weightDates.startDate,
          endDate: weightDates.endDate,
        },
      },
      (response) => {
        const weightHistory = response?.data?.data ?? [];
        if (weightHistory && weightHistory.length > 0) {
          setWeightData((prevState) => {
            let newState = { ...prevState };
            newState = {
              ...newState,
              chartData: [...weightHistory].reverse(),
            };
            return newState;
          });
        }
      },
      (error) => {}
    );
  }, [weightDates]);

  const updateCalorieDate = useCallback(
    (increase = false) => {
      let updatedDates = { ...calorieDates };
      if (increase) {
        let date = new Date(updatedDates.endDate);
        date.setDate(date.getDate() + 7);
        updatedDates = getStartAndEndDate(date);
      } else {
        let date = new Date(updatedDates.startDate);
        date.setDate(date.getDate() + 1);
        updatedDates = getStartAndEndDate(date);
      }

      setCalorieDates((prevState) => {
        let newState = { ...prevState };
        newState.startDate = updatedDates.startDate;
        newState.endDate = updatedDates.endDate;
        return newState;
      });
    },
    [calorieDates]
  );

  const updateWaterDate = useCallback(
    (increase = false) => {
      let updatedDates = { ...waterDates };
      if (increase) {
        let date = new Date(updatedDates.endDate);
        date.setDate(date.getDate() + 7);
        updatedDates = getStartAndEndDate(date);
      } else {
        let date = new Date(updatedDates.startDate);
        date.setDate(date.getDate() + 1);
        updatedDates = getStartAndEndDate(date);
      }

      setWaterDates((prevState) => {
        let newState = { ...prevState };
        newState.startDate = updatedDates.startDate;
        newState.endDate = updatedDates.endDate;
        return newState;
      });
    },
    [waterDates]
  );

  const updateWeightDate = useCallback(
    (increase = false) => {
      let updatedDates = { ...weightDates };
      if (increase) {
        let date = new Date(updatedDates.endDate);
        date.setDate(date.getDate() + 7);
        updatedDates = getStartAndEndDate(date);
      } else {
        let date = new Date(updatedDates.startDate);
        date.setDate(date.getDate() + 1);
        updatedDates = getStartAndEndDate(date);
      }

      setWeightDates((prevState) => {
        let newState = { ...prevState };
        newState.startDate = updatedDates.startDate;
        newState.endDate = updatedDates.endDate;
        return newState;
      });
    },
    [weightDates]
  );

  return (
    <div className="flavour-fit-trackers">
      <Container className="calorie-water-container">
        <Calories
          data={calorieData}
          options={options}
          updateDate={updateCalorieDate}
        />
        <WaterIntake
          data={waterData}
          options={options}
          updateDate={updateWaterDate}
        />
      </Container>
      <Container className="weights-container">
        <Weights data={weightData} updateDate={updateWeightDate} />
      </Container>
    </div>
  );
};

export default Trackers;
