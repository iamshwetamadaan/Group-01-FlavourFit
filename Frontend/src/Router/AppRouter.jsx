import * as React from "react";
import "../common.scss";

import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "../App";
import Login from "../Login/Login";
import Home from "../Home/Home";
import Registration from "../Registration/Registration";
import GuestLogin from "../GuestLogin/GuestLogin";
import UserProfile from "../UserProfile/userProfile";

const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />}>
        <Route path="home" element={<Home />} />
      
      </Route>
      <Route path="/login" element={<Login />} />
      <Route path="/guest-login" element={<GuestLogin />} />
      <Route path="/register" element={<Registration />} />
      <Route path="/edit-profile" element={<UserProfile/>}/>
     
    </Routes>
  </BrowserRouter>
);

export default AppRouter;
