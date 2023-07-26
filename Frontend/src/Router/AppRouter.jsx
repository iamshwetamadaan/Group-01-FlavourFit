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

const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />}>
        <Route path="home" element={<Home />} />
        <Route path="" element={<Navigate to="/home" />} />
        <Route path="trackers" element={<Trackers />} />
          <Route path="/edit-profile" element={<UserProfile/>}/>
      </Route>
      <Route path="/login" element={<Login />} />
      <Route path="/guest-login" element={<GuestLogin />} />
      <Route path="/register" element={<Registration />} />

    </Routes>
  </BrowserRouter>
);

export default AppRouter;
