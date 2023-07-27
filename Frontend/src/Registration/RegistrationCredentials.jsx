import React, { useState } from "react";
import "./registration.scss";

import { Button, Container, Form, Image } from "react-bootstrap";
import Logo from "../resources/Images/logo.png";
import { useNavigate } from "react-router-dom";

const RegistrationCredentials = ({ user, handleChange, createAccount }) => {
  const [validated, setValidated] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();

    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    } else {
      setValidated(true);
      createAccount();
    }
  };

  return (
    <div className="flavour-fit-registration">
      <Container className="register-form-container">
        <Image src={Logo} width={250} className="register-logo" />
        <Form validated={validated} onSubmit={handleSubmit}>
          <Form.Group className="register-form-group">
            <Form.Label>First name</Form.Label>
            <Form.Control
              type="text"
              name="firstName"
              placeholder="First name"
              value={user.firstName}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="register-form-group">
            <Form.Label>Last name</Form.Label>
            <Form.Control
              type="text"
              name="lastName"
              placeholder="Last name"
              value={user.lastName}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="register-form-group">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              name="email"
              placeholder="Enter Email"
              value={user.email}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="register-form-group">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              name="password"
              placeholder="Enter password"
              value={user.password}
              onChange={handleChange}
            />
          </Form.Group>
          <div className="register-btn-container">
            <Button type="submit">Create Account</Button>

            <Button
              variant="outline-primary"
              type="button"
              className="register-links"
              onClick={() => navigate("/login")}
            >
              Already have an account? Sign in.
            </Button>
          </div>
        </Form>
      </Container>
    </div>
  );
};

export default RegistrationCredentials;
