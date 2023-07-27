import React, { Component } from "react";
import UserProfileForm from "./UserProfileForm";
import { axiosRequest } from "../HttpClients/axiosService";
import "./UserProfile.css";
import { Container } from "react-bootstrap";
import { Link } from "react-router-dom";

class UserProfile extends Component {
  state = {
    data: null,
    initialData: null,
    showSuccess: false,
    showError: false,
  };

  componentDidMount() {
    axiosRequest(
      {
        url: "/users/get-current-user",
        method: "get",
      },
      (response) => {
        this.setState({
          data: response.data.data["user details"],
          initialData: response.data.data["user details"],
        });
      },
      (error) => {}
    );
  }

  updateUser = (data) => {
    axiosRequest(
      {
        url: "/users/update-user",
        method: "put",
        data: data,
      },
      (response) => {
        if (response.status == 200) {
          this.setState({
            showSuccess: true,
          });
        }
      },
      (error) => {
        this.setState({
          showError: true,
        });
      }
    );
  };

  render() {
    return (
      <div className="container-userProfile">
        <Container className="form-container">
          {this.state.showSuccess ? (
            <p style={{ color: "green" }}>
              Update was successfull.{" "}
              <span>
                <Link to="/home">
                  <u>Click here </u>
                </Link>
              </span>{" "}
              to go to Home Page
            </p>
          ) : (
            ""
          )}
          {this.state.showError ? (
            <p style={{ color: "red" }}>
              Unable to do the update.{" "}
              <span>
                <Link to="/home">
                  <u>Click here </u>
                </Link>
              </span>{" "}
              to go to Home Page
            </p>
          ) : (
            ""
          )}
          <h3 className="mb-4">User Profile</h3>
          <UserProfileForm
            initialData={this.state.initialData}
            currentData={this.state.data}
            updateUser={this.updateUser}
          ></UserProfileForm>
        </Container>
      </div>
    );
  }
}

export default UserProfile;
