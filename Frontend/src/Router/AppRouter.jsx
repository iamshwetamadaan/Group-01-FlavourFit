import * as React from "react";
import "../common.scss";

import { BrowserRouter, Route, Routes, Navigate } from "react-router-dom";
import App from "../App";
import Login from "../Login/Login";
import Home from "../Home/Home";
import Registration from "../Registration/Registration";
import GuestLogin from "../GuestLogin/GuestLogin";
import Trackers from "../Trackers/Trackers";
import UserProfile from "../UserProfile/userProfile";
import Recipes from "../Recipes/Recipes";
import RecipeDetails from "../Recipes/RecipeDetails";
import RecordRecipe from "../Recipes/RecordRecipe";
import Feeds from "../Feeds/Feeds";

const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />}>
        <Route path="home" element={<Home />} />
        <Route path="" element={<Navigate to="/home" />} />
        <Route path="/feed" element={<Feeds/>} />
        <Route path="trackers" element={<Trackers />} />
        <Route path="feeds" element={<Feeds />} />
        <Route path="/edit-profile" element={<UserProfile />} />
        <Route path="/recipes" element={<Recipes />} />
        <Route path={`/recipes/:id`} element={<RecipeDetails />} />
        <Route path={`/recipes/record/:id`} element={<RecordRecipe />} />
      </Route>
      <Route path="/login" element={<Login />} />
      <Route path="/guest-login" element={<GuestLogin />} />
      <Route path="/register" element={<Registration />} />
    </Routes>
  </BrowserRouter>
);

export default AppRouter;
