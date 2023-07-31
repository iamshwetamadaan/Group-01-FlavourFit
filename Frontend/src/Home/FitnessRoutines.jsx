import React, { Component } from "react";
import { axiosRequest } from "../HttpClients/axiosService";
import Card from "react-bootstrap/Card";
import Exercise from "./Exercise.webp";
import "./home.scss";
import { Container } from "react-bootstrap";
import HomeRecipes from "./HomeRecipes";
import HomeTracker from "./HomeTracker";

class FitnessRoutines extends Component {
  state = {
    quoteOfTheDay: "",
    data: null,
    recipes: [],
  };

  componentDidMount() {
    axiosRequest(
      {
        url: "/home/exercises",
        method: "get",
      },
      (response) => {
        this.setState({
          data: response.data.data.routines,
          quoteOfTheDay: response.data.data.quoteOfTheDay,
        });
      },
      (error) => {}
    );

    axiosRequest(
      {
        url: "/recipes/saved-recipes",
        method: "get",
        params: {
          count: 100,
        },
      },
      (response) => {
        if (response?.data?.data) {
          this.setState({ recipes: [...response.data.data] });
        }
      },
      (error) => {}
    );
  }

  render() {
    return (
      <div className="home-top">
        <div className="justify-content-div row border mt-4 bg-white rounded">
          <div className="col-7">
            <h5 className="pt-3" style={{ textAlign: "center" }}>
              Quote of the day
            </h5>
            <h3 style={{ color: "#0e1361", textAlign: "center" }}>
              {this.state.quoteOfTheDay}
            </h3>
          </div>
          <div className="col-5 image-class"></div>
        </div>

        <HomeTracker />

        <div className="home-recipes">
          <HomeRecipes recipes={this.state.recipes} />
        </div>
        <div className="fitness-recipes">
          <div className="ff-exercises bg-light border rounded">
            <div className="py-3 px-3">
              <h5 className="mb-3 blue-text-color">Recomended Workouts</h5>
              {this.state.data != null
                ? this.state.data.map((item) => (
                    <div className="row border rounded mb-4 box-shadow">
                      <div className="col-3 px-0">
                        <img src={Exercise} width="100%" height="100%" />
                      </div>
                      <div className="col-9 px-0">
                        <Card className="border-0">
                          <Card.Body>
                            <Card.Title className="blue-text-color">
                              {item.routineName}
                            </Card.Title>
                            <Card.Text>{item.routineDescription}</Card.Text>
                            <small className="text-muted">Tip - </small>
                            <small className="text-muted">{item.tips}</small>
                          </Card.Body>
                        </Card>
                      </div>
                    </div>
                  ))
                : ""}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default FitnessRoutines;
