import React, { useState } from "react";
import "./guestLogin.scss";

import { Button, Container, Form, Image } from "react-bootstrap";
import Logo from "../resources/Images/logo.png";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useCallback } from "react";

const GuestLogin = (props) => {
  const [validated, setValidated] = useState(false);
  const navigate = useNavigate();
  const [showPasswordField, setShowPasswordField] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = useCallback(
    (event) => {
      setError("");
      event.preventDefault();
      const form = event.currentTarget;
      if (form.checkValidity() === false) {
        event.stopPropagation();
      } else {
        setValidated(true);
        let email = form.email.value;

        if (showPasswordField) {
          let password = form.password.value;

          axios
            .post("http://localhost:8080/auth/guest-login", {
              email: email,
              password: password,
            })
            .then((response) => {
              let token = response.data.token;
              sessionStorage.setItem("AuthToken", token);
              navigate("/home");
            })
            .catch((error) => {});
        } else {
          axios
            .post("http://localhost:8080/auth/request-otp", { email: email })
            .then((response) => {
              setShowPasswordField(true);
            })
            .catch((error) => {
              setError(error?.response?.data?.message ?? "");
            });
        }
      }
    },
    [showPasswordField, navigate]
  );

  return (
    <div className="fitapp-guest-login">
      <Container className="guest-login-form-container">
        <Image src={Logo} width={250} className="guest-login-logo" />
        <Form validated={validated} onSubmit={handleSubmit}>
          <Form.Group className="guest-login-form-group">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              required
              type="email"
              name="email"
              placeholder="name@example.com"
            />
          </Form.Group>
          {showPasswordField ? (
            <Form.Group className="guest-login-form-group">
              <Form.Label>Password</Form.Label>
              <Form.Control
                required
                type="password"
                placeholder="Password"
                name="password"
              />
            </Form.Group>
          ) : null}

          {error ? <span className="flavour-fit-error">{error}</span> : null}

          <div className="guest-login-btn-container">
            {!showPasswordField ? (
              <Button type="submit">Request OTP</Button>
            ) : (
              <Button type="submit">Sign In</Button>
            )}

            <Button
              variant="outline-primary"
              type="button"
              className="guest-login-links"
              onClick={() => navigate("/register")}
            >
              Create an account?
            </Button>

            <Button
              variant="outline-primary"
              type="button"
              className="guest-login-links"
              onClick={() => navigate("/login")}
            >
              Sign-in
            </Button>
          </div>
        </Form>
      </Container>
    </div>
  );
};

export default GuestLogin;
