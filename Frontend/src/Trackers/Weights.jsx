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
      grace: "0",
    },
  },
};

const Weights = ({ data, updateDate }) => {
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
          chData.push(row?.weight ?? 0);
        });
        newState = {
          ...newState,
          datasets: [
            {
              label: "Calories",
              data: [...chData],
              backgroundColor: "#d66d20",
              barThickness: 20,
              borderRadius: 8,
            },
          ],
        };
        newState.labels = [...labels];
      }

      return newState;
    });

    console.log(data);
  }, [data]);

  return (
    <div className="tracker-container">
      <div className="tracker-title">Weight history</div>
      <div className="tracker-current">
        <div className="tracker-current-value ff-orange">
          {data?.currentWeight ?? 0.0}
        </div>
        <div className="tracker-current-unit ff-orange">kg</div>
      </div>
      <Bar id="weight-chart" options={options} data={chartData} />
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

export default Weights;
