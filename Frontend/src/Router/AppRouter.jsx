import * as React from "react";

import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "../App";
import Login from "../Login/Login";

const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />}>
        <Route path="home" element={<div>Home</div>} />
      </Route>
      <Route path="/login" element={<Login />} />
    </Routes>
  </BrowserRouter>
);

export default AppRouter;
