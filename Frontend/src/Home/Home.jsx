import React from "react";
import "./home.scss";
import { useEffect } from "react";
import { axiosRequest } from "../HttpClients/axiosService";

const Home = (props) => {
  useEffect(() => {
    axiosRequest({
      url: "/trackers/calorie-history",
      method: "get",
      params: {
        startDate: "2023-07-10",
        endDate: "2023-07-20",
      },
      success: (response) => {},
      failure: (error) => {},
    });
  }, []);

  return (
    <div className="flavour-fit-home">
      <h1>Homepage</h1>
    </div>
  );
};

export default Home;
