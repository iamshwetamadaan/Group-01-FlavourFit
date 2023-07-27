import React, { Component } from "react";
import Card from "react-bootstrap/Card";
import LIKE from "./like.png";
import EDIT from "./edit.png";
import Button from "react-bootstrap/Button";
import Comments from "./Comments";
import { axiosRequest } from "../HttpClients/axiosService";
import Form from "react-bootstrap/Form";
import InputGroup from "react-bootstrap/InputGroup";

class Feed extends Component {
  state = {
    feed: null,
    showComments: false,
    showPost: false,
  };

  componentDidMount() {
    this.setState({
      feed: this.props.feed,
    });
  }

  getUserName = () => {};

  handleEditClick = () => {
    this.setState({
      showPost: !this.state.showPost,
    });
  };

  handlePostUpdateClick = () => {
    this.setState({
      showPost: false,
    });
    var data = document.getElementById("input").value;
    this.props.editAndCreatepost(this.state.feed.feedId, data);
  };

  callback = (data) => {
    debugger;
    var feed = this.state.feed;
    feed = { ...feed, comments: [...data] };
    this.setState({
      feed: feed,
    });
  };

  handleLikes = () => {
    debugger;
    axiosRequest(
      {
        url: "/feeds/like-feeds",
        method: "PATCH",
        params: {
          feedID: this.state.feed.feedId,
        },
      },
      (response) => {
        this.setState({
          feed: {
            ...this.state.feed,
            likeCount: this.state.feed.likeCount + 1,
          },
        });
      },
      (error) => {}
    );
  };

  handleComments = () => {
    this.setState({
      showComments: !this.state.showComments,
    });
  };

  handleAddComment = () => {
    var value = document.getElementById("comment").value;
    document.getElementById("comment").value = "";
    var feed = this.state.feed;
    var commentInformation = {
      commentContent: value,
      feedId: this.state.feed.feedId,
      userId: sessionStorage.getItem("userId"),
      username: sessionStorage.getItem("username"),
    };
    axiosRequest(
      {
        url: "/feeds/record-comment",
        method: "put",
        data: commentInformation,
      },
      (response) => {
        var comments = response.data.data;
        feed["comments"] = comments;
        this.setState({
          feed: feed.reverse(),
        });
      },
      (error) => {}
    );
  };

  componentDidUpdate(prevProps) {
    // //console.log(prevProps)
    // if(prevProps.feed!=this.props.feed)
    //     this.setState({
    //         feed:prevProps.feed
    //     })
  }

  render() {
    // //console.log(this.state.feed)
    return (
      <div className="w-100 mb-3">
        <Card border="primary" className="bg-light">
          <Card.Header className=" border-white blue-text-color" as="p">
            <b>@{this.state.feed != null ? this.state.feed.userName : ""}</b>
            {this.state.feed != null &&
            sessionStorage.getItem("userId") == this.state.feed.userId ? (
              <span className="float-end cursor">
                <img
                  src={EDIT}
                  width="7%"
                  onClick={this.handleEditClick}
                  style={{ marginLeft: "90%" }}
                />
              </span>
            ) : (
              ""
            )}
          </Card.Header>
          <Card.Body className="py-2">
            {!this.state.showPost ? (
              <Card.Text className="">
                {this.state.feed != null ? this.state.feed.feedContent : ""}
              </Card.Text>
            ) : (
              ""
            )}

            {this.state.showPost ? (
              <>
                <Form.Group
                  className="mb-3"
                  controlId="exampleForm.ControlTextarea2"
                >
                  {/* <Form.Label>Post Updation</Form.Label> */}
                  <Form.Control
                    as="textarea"
                    defaultValue={
                      this.state.feed != null ? this.state.feed.feedContent : ""
                    }
                    rows={3}
                    id="input"
                  />
                </Form.Group>
                <Button
                  className="ms-5"
                  variant="primary"
                  size="sm"
                  onClick={this.handlePostUpdateClick}
                >
                  Update Post
                </Button>
              </>
            ) : (
              ""
            )}
          </Card.Body>
          <Card.Footer className="py-1">
            <div className="row">
              <div className="col-6 text-center border-end">
                <div className="row font-90">
                  <div className="col-6">
                    <p
                      className="text-primary cursor mb-0"
                      onClick={this.handleLikes}
                    >
                      <span className="ml-2">
                        <img src={LIKE} width="12%" />
                      </span>
                      Like
                    </p>
                  </div>
                  <div className="col-6">
                    <p className="mb-0">
                      <strong>
                        <i>
                          {this.state.feed != null
                            ? this.state.feed.likeCount
                            : ""}
                        </i>
                      </strong>
                    </p>
                  </div>
                </div>
              </div>
              <div
                className="col-6 text-center text-primary cursor"
                onClick={this.handleComments}
              >
                (
                <span>
                  {this.state.feed != null
                    ? this.state.feed.comments.length
                    : ""}
                </span>
                ) Comment
              </div>
            </div>
          </Card.Footer>
          {this.state.showComments ? (
            <>
              <Card>
                <Card.Body className="py-1">
                  <InputGroup size="sm" className="">
                    <Form.Control
                      placeholder="Enter your comment"
                      id="comment"
                    />
                    <Button
                      variant="primary"
                      id="button-addon2"
                      onClick={this.handleAddComment}
                    >
                      Send
                    </Button>
                  </InputGroup>
                </Card.Body>
              </Card>

              {this.state.feed != null && this.state.feed.comments.length > 0
                ? this.state.feed.comments.map((item, index) => (
                    <React.Fragment key={"comment" + index}>
                      <Comments
                        comment={item}
                        comments={this.state.feed.comments}
                        feedId={this.state.feed.feedId}
                        callback={this.callback}
                      ></Comments>
                    </React.Fragment>
                  ))
                : ""}
            </>
          ) : (
            ""
          )}
        </Card>
      </div>
    );
  }
}

export default Feed;
