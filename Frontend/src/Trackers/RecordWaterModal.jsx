import React, { useState } from "react";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Modal from "react-bootstrap/Modal";
import { axiosRequest } from "../HttpClients/axiosService";

const RecordWaterModal = ({ show, handleClose, handleSuccess }) => {
  const [validated, setValidated] = useState(false);

  const handleSubmit = (event) => {
    event.preventDefault();

    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      event.stopPropagation();
    } else {
      setValidated(true);
      let water = form.water.value;

      axiosRequest(
        {
          url: "/trackers/record-waterIntake",
          method: "PUT",
          data: {
            waterIntake: Number(water).toFixed(2),
          },
        },
        (response) => {
          handleSuccess();
          setValidated(false);
          handleClose();
        },
        (error) => {}
      );
    }
  };
  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Record Water intake</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form validated={validated} onSubmit={handleSubmit}>
          <Form.Group className="record-form-group">
            <Form.Label>Water Intake</Form.Label>
            <Form.Control
              required
              type="number"
              name="water"
              placeholder="Enter water intake in mL"
            />
          </Form.Group>
          <div className="record-btn-container">
            <Button type="submit">Record</Button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default RecordWaterModal;
