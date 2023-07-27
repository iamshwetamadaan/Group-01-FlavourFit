import React, { useState } from "react";
import "./registration.scss";

import { Button, Container, Form, Image } from "react-bootstrap";
import Logo from "../resources/Images/logo.png";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import RegistrationDetails from "./RegistrationDetails";
import RegistrationCredentials from "./RegistrationCredentials";
import { useCallback } from "react";

const Registration = (props) => {
  const navigate = useNavigate();

  const [step, setStep] = useState(1);
  const [user, setUser] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    height: "",
    weight: "",
    targetWeight: "",
    preferences: "",
  });

  const handleContinue = (e) => {
    setStep(2);
  };

  const handleChange = (e) => {
    setUser((prevState) => {
      let newState = { ...prevState };
      newState = { ...newState, [e.target.name]: e.target.value };
      return newState;
    });
  };

  const createAccount = useCallback(
    (e) => {
      axios
        .post("http://localhost:8080/auth/register-user", {
          ...user,
        })
        .then((response) => {
          let token = response.data.token;
          sessionStorage.setItem("AuthToken", token);
          navigate("/home");
        })
        .catch((error) => {});
    },
    [user]
  );

  return (
    <div className="flavour-fit-registration">
      {step === 1 ? (
        <RegistrationDetails
          handleContinue={handleContinue}
          user={user}
          handleChange={handleChange}
        />
      ) : (
        <RegistrationCredentials
          user={user}
          handleChange={handleChange}
          createAccount={createAccount}
        />
      )}
    </div>
  );
};

export default Registration;
