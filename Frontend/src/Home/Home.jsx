import React, { useState } from "react";
import "./home.scss";
import { useEffect } from "react";
import { axiosRequest } from "../HttpClients/axiosService";
import QuoteOfTheDay from "./FitnessRoutines";
import FitnessRoutines from "./FitnessRoutines";
import HomeRecipes from "./HomeRecipes";
import HomeTracker from "./HomeTracker";

const Home = (props) => {
  return (
    <div className="flavour-fit-home">
      <div className="container">
        <FitnessRoutines />
      </div>
    </div>
  );
};

export default Home;
