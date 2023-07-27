import React, { useState } from "react";
import "./registration.scss";

import { Button, Container, Form, Image } from "react-bootstrap";
import Logo from "../resources/Images/logo.png";
import { useNavigate } from "react-router-dom";

const RegistrationDetails = ({ user, handleChange, handleContinue }) => {
  const [validated, setValidated] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();

    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    } else {
      setValidated(true);
      handleContinue();
    }
  };

  return (
    <div className="flavour-fit-registration">
      <Container className="register-form-container">
        <Image src={Logo} width={250} className="register-logo" />
        <Form validated={validated} onSubmit={handleSubmit}>
          <Form.Group className="register-form-group">
            <Form.Label>Height (in cm)</Form.Label>
            <Form.Control
              required
              type="number"
              name="height"
              placeholder="Enter height in cm"
              value={user.height}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="register-form-group">
            <Form.Label>Weight (in kg)</Form.Label>
            <Form.Control
              required
              type="number"
              name="weight"
              placeholder="Enter weight in kg"
              value={user.weight}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="register-form-group">
            <Form.Label>Target Weight (in kg)</Form.Label>
            <Form.Control
              type="number"
              name="targetWeight"
              placeholder="Enter target weight in kg (optional)"
              value={user.targetWeight}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="register-form-group">
            <Form.Label>Preferences</Form.Label>
            <Form.Control
              type="text"
              name="preferences"
              placeholder="Vegetarian, Chicken, Fish ..."
              value={user.preferences}
              onChange={handleChange}
            />
          </Form.Group>
          <div className="register-btn-container">
            <Button type="submit">Continue</Button>

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

export default RegistrationDetails;
