import React, { useState } from "react";
import { Button, Container, Form, Image } from "react-bootstrap";
import "./login.scss";
import Logo from "../resources/Images/logo.png";
import axios from "axios";

const Login = (props) => {
  const [validated, setValidated] = useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();

    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    } else {
      setValidated(true);
      let email = form.email.value;
      let password = form.password.value;

      axios
        .post("http://localhost:8080/auth/login", {
          email: email,
          password: password,
        })
        .then((response) => {
          let token = response.data.token;
          sessionStorage.setItem("AuthToken", token);
          axios
            .put(
              "http://localhost:8080/trackers/record-waterIntake",
              {
                waterIntake: 200.1,
              },
              {
                headers: {
                  Authorization: "Bearer " + token,
                },
              }
            )
            .then((e) => {
              debugger;
            });
        })
        .catch((error) => {});
    }
  };

  return (
    <div className="fitapp-login">
      <Container className="login-form-container">
        <Image src={Logo} width={250} className="login-logo" />
        <Form validated={validated} onSubmit={handleSubmit}>
          <Form.Group className="login-form-group">
            <Form.Label>Email address</Form.Label>
            <Form.Control
              required
              type="email"
              name="email"
              placeholder="name@example.com"
            />
          </Form.Group>
          <Form.Group className="login-form-group">
            <Form.Label>Password</Form.Label>
            <Form.Control
              required
              type="password"
              placeholder="Password"
              name="password"
            />
          </Form.Group>
          <div className="login-btn-container">
            <Button type="submit">Sign In</Button>
            <Button
              variant="outline-primary"
              type="button"
              className="login-links"
            >
              Forgot password?
            </Button>

            <Button
              variant="outline-primary"
              type="button"
              className="login-links"
            >
              Create an account?
            </Button>

            <Button
              variant="outline-primary"
              type="button"
              className="login-links"
            >
              Guest Sign-in
            </Button>
          </div>
        </Form>
      </Container>
    </div>
  );
};

export default Login;
