import React from "react";
import { useEffect } from "react";
import { useState } from "react";
import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import chevLeft from "../resources/Images/chevron-left.svg";
import chevRight from "../resources/Images/chevron-right.svg";
import plusCircle from "../resources/Images/circle-plus-solid.svg";

const Calories = ({ data, options, updateDate, addCalories }) => {
  ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
  );

  const [chartData, setChartData] = useState({
    labels: [],
    datasets: [],
  });

  useEffect(() => {
    setChartData((prevState) => {
      let newState = { ...prevState };
      let labels = [];
      let chData = [];
      if (data?.chartData?.length > 0) {
        data.chartData.forEach((row, index) => {
          labels.push(row?.date ?? "");
          chData.push(row?.calorie ?? 0);
        });
        newState = {
          ...newState,
          datasets: [
            {
              label: "Calories",
              data: [...chData],
              backgroundColor: "#2ca588",
              barThickness: 20,
              borderRadius: 8,
            },
          ],
        };
        newState.labels = [...labels];
      }

      return newState;
    });
  }, [data]);

  return (
    <div className="tracker-container">
      <div className="tracker-title">
        <div>Calories</div>
        <img
          src={plusCircle}
          width={30}
          alt="+"
          title="Record calories"
          style={{ cursor: "pointer" }}
          onClick={addCalories}
        />
      </div>
      <div className="tracker-current">
        <div className="tracker-current-value ff-green">
          {data?.calorieCount ?? 0.0}
        </div>
        <div className="tracker-current-unit ff-green">kCal</div>
        <div className="tracker-current-bar">
          <div
            className="tracker-current-bar-inner ff-bg-green"
            style={{ width: `${((data?.calorieCount ?? 0) / 2000) * 100}%` }}
          ></div>
        </div>
      </div>
      <Bar options={options} data={chartData} />

      {data?.chartData?.length > 0 ? (
        <div className="tracker-paginate">
          <img
            src={chevLeft}
            width={10}
            style={{ cursor: "pointer" }}
            alt="<"
            onClick={(e) => {
              updateDate(false);
            }}
          />
          <span>
            {data.chartData[0].date} -{" "}
            {data.chartData[data.chartData.length - 1].date}
          </span>
          <img
            src={chevRight}
            width={10}
            style={{ cursor: "pointer" }}
            alt=">"
            onClick={(e) => {
              updateDate(true);
            }}
          />
        </div>
      ) : null}
    </div>
  );
};

export default Calories;
