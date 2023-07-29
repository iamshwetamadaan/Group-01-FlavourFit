import React, { Component } from "react";
import Card from "react-bootstrap/Card";
import EDIT from "./edit.png";
import DELETE from "./delete.png";
import { axiosRequest } from "../HttpClients/axiosService";
import InputGroup from "react-bootstrap/InputGroup";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";

class Comments extends Component {
  state = {
    data: null,
    showInput: false,
    inputvalue: null,
  };

  componentDidMount() {
    this.setState({
      data: this.props.comment,
    });
  }

  handleEditClick = () => {
    this.setState({
      showInput: !this.state.showInput,
    });
  };

  recordInput = (e) => {
    var value = e.target.value;
    this.setState({
      inputvalue: value,
    });
  };

  handleDeleteClick = (commentId) => {
    axiosRequest(
      {
        url: "/feeds/comment-feed",
        method: "DELETE",
        params: {
          feedID: this.props.feedId,
          commentId: commentId,
        },
      },
      (response) => {
        console.log(response);
      },
      (error) => {}
    );
  };

  handleAddComment = () => {
    var commentInformation = {
      commentId: this.state.data.commentId,
      commentContent: this.state.inputvalue,
      feedId: this.props.feedId,
      userId: parseInt(sessionStorage.getItem("userId")),
      username: sessionStorage.getItem("username"),
    };
    axiosRequest(
      {
        url: "/feeds/record-comment",
        method: "put",
        data: commentInformation,
      },
      (response) => {
        var comments = response?.data?.data ?? [];
        this.props.callback(comments);
        this.setState({
          showInput: false,
        });
      },
      (error) => {}
    );
  };

  render() {
    return (
      <Card>
        {this.state.showInput ? (
          <>
            <InputGroup size="sm" className="">
              <Form.Control
                placeholder="Enter your comment"
                id="comment"
                onChange={this.recordInput}
                defaultValue={
                  this.state.data != null ? this.state.data.commentContent : ""
                }
              />
              <Button
                variant="primary"
                id="button-addon2"
                onClick={this.handleAddComment}
              >
                Send
              </Button>
            </InputGroup>
          </>
        ) : (
          <Card.Body className="py-1">
            <p className="mb-0 font-90">
              <strong>
                @{this.state.data != null ? this.state.data.username : ""}
              </strong>
            </p>
            <p className="mb-0 font-90">
              {this.state.data != null ? this.state.data.commentContent : ""}
              {this.state.data != null &&
              parseInt(sessionStorage.getItem("userId")) ===
                this.state.data.userId ? (
                <>
                  <span className="float-end cursor">
                    <img
                      src={EDIT}
                      width="7%"
                      onClick={this.handleEditClick}
                      style={{ marginLeft: "95%" }}
                    />
                  </span>

                  <span className="float-end cursor">
                    <img
                      src={DELETE}
                      width="4%"
                      onClick={() => {
                        this.handleDeleteClick(this.state.data.commentId);
                      }}
                      style={{ marginLeft: "90%", marginTop: "-9%" }}
                    />
                  </span>
                </>
              ) : (
                ""
              )}
            </p>
          </Card.Body>
        )}
      </Card>
    );
  }
}

export default Comments;
