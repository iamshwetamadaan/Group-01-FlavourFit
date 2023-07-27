import React from "react";
import "./trackers.scss";
import Calories from "./Calories";
import { Container, Modal } from "react-bootstrap";
import { useState } from "react";
import { useEffect } from "react";
import { axiosRequest } from "../HttpClients/axiosService";
import { formatDateToString } from "../Helper";
import WaterIntake from "./WaterIntake";
import Weights from "./Weights";
import { useCallback } from "react";
import RecordCalorieModal from "./RecordCalorieModal";
import RecordWaterModal from "./RecordWaterModal";

export const options = {
  responsive: true,
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

export const getStartAndEndDate = (date, period = 6) => {
  let endDate = formatDateToString(new Date(date));
  let startDate = new Date(date);
  startDate.setDate(startDate.getDate() - period);
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
    startDate: getStartAndEndDate(new Date(), 90).startDate,
    endDate: getStartAndEndDate(new Date(), 90).endDate,
  });

  const [showCalModal, setShowCalModal] = useState(false);
  const handleShowCalModal = () => {
    setShowCalModal(true);
  };
  const handleCloseCalModal = () => {
    setShowCalModal(false);
  };

  const [showWaterModal, setShowWaterModal] = useState(false);
  const handleShowWaterModal = () => {
    setShowWaterModal(true);
  };
  const handleCloseWaterModal = () => {
    setShowWaterModal(false);
  };

  const fetchTrackerSummary = () => {
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
  };

  useEffect(() => {
    fetchTrackerSummary();
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
        date.setDate(date.getDate() + 8);
        updatedDates = getStartAndEndDate(date);
      } else {
        let date = new Date(updatedDates.startDate);
        date.setDate(date.getDate());
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
        date.setDate(date.getDate() + 8);
        updatedDates = getStartAndEndDate(date);
      } else {
        let date = new Date(updatedDates.startDate);
        date.setDate(date.getDate());
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
      debugger;
      if (increase) {
        let date = new Date(updatedDates.endDate);
        date.setDate(date.getDate() + 92);
        updatedDates = getStartAndEndDate(date, 90);
      } else {
        let date = new Date(updatedDates.startDate);
        date.setDate(date.getDate());
        updatedDates = getStartAndEndDate(date, 90);
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

  const handleCalorieRecordSuccess = () => {
    fetchTrackerSummary();
    setCalorieDates({
      startDate: getStartAndEndDate(new Date()).startDate,
      endDate: getStartAndEndDate(new Date()).endDate,
    });
  };

  const handleWaterRecordSuccess = () => {
    fetchTrackerSummary();
    setWaterDates({
      startDate: getStartAndEndDate(new Date()).startDate,
      endDate: getStartAndEndDate(new Date()).endDate,
    });
  };

  return (
    <div className="flavour-fit-trackers">
      <RecordCalorieModal
        show={showCalModal}
        handleClose={handleCloseCalModal}
        handleSuccess={handleCalorieRecordSuccess}
      />
      <RecordWaterModal
        show={showWaterModal}
        handleClose={handleCloseWaterModal}
        handleSuccess={handleWaterRecordSuccess}
      />
      <Container className="calorie-water-container">
        <Calories
          data={calorieData}
          options={options}
          updateDate={updateCalorieDate}
          addCalories={handleShowCalModal}
        />
        <WaterIntake
          data={waterData}
          options={options}
          updateDate={updateWaterDate}
          addWater={handleShowWaterModal}
        />
      </Container>
      <Container className="weights-container">
        <Weights data={weightData} updateDate={updateWeightDate} />
      </Container>
    </div>
  );
};

export default Trackers;
