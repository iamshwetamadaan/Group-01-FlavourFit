import React from "react";
import { useEffect } from "react";
import { useState } from "react";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import chevLeft from "../resources/Images/chevron-left.svg";
import chevRight from "../resources/Images/chevron-right.svg";

export const options = {
  responsive: true, // Instruct chart js to respond nicely.
  maintainAspectRatio: false, // Add to prevent default behaviour of full-width/height
  plugins: {
    legend: {
      display: false,
      labels: {
        font: {
          size: 9,
        },
      },
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
      ticks: {
        font: {
          size: 14,
        },
      },
    },
    y: {
      min: 0,
      ticks: {
        stepSize: 20,
        font: {
          size: 14,
        },
      },
      grid: {
        display: false,
      },
    },
  },
};

const Weights = ({ data, updateDate }) => {
  ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
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
          if (row?.weight !== 0) {
            labels.push(row?.date ?? "");
            chData.push(row.weight);
          }
        });
        newState = {
          ...newState,
          datasets: [
            {
              label: "Calories",
              data: [...chData],
              backgroundColor: "#d66d20",
              borderColor: "#d66d20",
            },
          ],
        };
        newState.labels = [...labels];
      }

      return newState;
    });
  }, [data]);

  return (
    <div className="tracker-container" style={{ maxHeight: "600px" }}>
      <div className="tracker-title">Weight history</div>
      <div className="tracker-current">
        <div className="tracker-current-value ff-orange">
          {data?.currentWeight ?? 0.0}
        </div>
        <div className="tracker-current-unit ff-orange">kg</div>
      </div>
      <Line
        id="weight-chart"
        options={options}
        data={chartData}
        style={{ width: "100%", height: "400px" }}
      />
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
