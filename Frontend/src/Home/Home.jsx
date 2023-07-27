import React from "react";
import "./home.scss";
import { useEffect } from "react";
import { axiosRequest } from "../HttpClients/axiosService";
import QuoteOfTheDay from "./FitnessRoutines";
import FitnessRoutines from "./FitnessRoutines";

const Home = (props) => {
  // useEffect(() => {
  //   axiosRequest({
  //     url: "/trackers/calorie-history",
  //     method: "get",
  //     params: {
  //       startDate: "2023-07-10",
  //       endDate: "2023-07-20",
  //     },
  //     success: (response) => {},
  //     failure: (error) => {},
  //   });
  // }, []);

  return (
    <div className="flavour-fit-home">
      <div className="container">
        <FitnessRoutines></FitnessRoutines>
      </div>
      
    </div>
  );
};

export default Home;
