import React, { Component } from "react";
import { axiosRequest } from "../HttpClients/axiosService";
import ListGroup from "react-bootstrap/ListGroup";
import "./feeds.css";
import Button from "react-bootstrap/Button";
import Feed from "./Feed";
import Card from "react-bootstrap/Card";
import Form from "react-bootstrap/Form";
import Modal from "react-bootstrap/Modal";

class Feeds extends Component {
  state = {
    feeds: null,
    activeTab: "all",
    showModal: false,
  };

  componentDidMount() {
    axiosRequest(
      {
        url: "/users/get-current-user",
        method: "get",
      },
      (response2) => {
        sessionStorage.setItem(
          "userId",
          response2.data.data["user details"].userId
        );
        sessionStorage.setItem(
          "username",
          response2.data.data["user details"].username
        );
      },
      (error) => {}
    );
    axiosRequest(
      {
        url: "/feeds/get-all-feeds",
        method: "get",
        params: {
          offset: 0,
        },
      },
      (response) => {
        var feeds = response.data.data;
        feeds.forEach((feed) => {
          axiosRequest(
            {
              url: "/users/get-current-user",
              method: "get",
            },
            (response2) => {
              var username = response2.data.data["user details"].username;
              feed["userName"] = username;
            },
            (error) => {}
          );
        });
        this.setState({
          feeds: feeds,
        });
      },
      (error) => {}
    );
    }

    componentDidMount(){
        axiosRequest({
            url: "/users/get-current-user",
            method: "get",
          },
          (response2) => {
            sessionStorage.setItem("userId",response2.data.data["user details"].userId)
            sessionStorage.setItem("username",response2.data.data["user details"].username)
          },
        (error) => {},);
        axiosRequest({
                url: "/feeds/get-all-feeds",
                method: "get",
                params: {
                    offset:0
                },
              },
              (response) => {
               
                var feeds = response.data.data;
                feeds.forEach(feed=>{
                    axiosRequest({
                            url: "/users/get-current-user",
                            method: "get",
                          },
                          (response2) => {
                            var username = response2.data.data["userDetails"].username
                            feed["userName"]=username;
                          },
                        (error) => {},);
                })
                this.setState({
                    feeds: feeds
                })
              },
              (error) => {},);
    }

  editAndCreatepost = (feedId, data) => {
        var result;
        axiosRequest({
            url: "/users/get-current-user",
            method: "get",
          },
          (response) => {
        result = response.data.data["userDetails"];
        var dataToSend = {
          comments: [],
          feedContent: data,
          feedId: feedId,
          likeCount: 0,
          userId: result.userId,
          username: result.username,
        };
      
        axiosRequest(
          {
            url: "/feeds/record-post",
            method: "put",
            data: dataToSend,
          },
          (response) => {

           
            result = response.data.data["userDetails"]
            var dataToSend = {
                comments : [],
                feedContent : data,
                feedId:feedId,
                likeCount:0,
                userId:result.userId,
                username:result.username
            }
            console.log(dataToSend)
            axiosRequest({
                url: "/feeds/record-post",
                method: "put",
                data:dataToSend
              },
              (response) => {
                var feeds = this.state.feeds;
                var currentFeed = response.data.data;
                currentFeed["username"] = result.username
                feeds.push(currentFeed);
                this.setState({
                    feeds:feeds
                })
              },
            (error) => {},);

            var feeds = this.state.feeds;
            var currentFeed = response.data.data;
            currentFeed["username"] = result.username;
            feeds.push(currentFeed);
            this.setState({
              feeds: feeds,
            });

          },
        (error) => {},);
    })
    }

    recordPost = (count) =>{
        this.handleClose();
        var data = document.getElementById("input").value
        this.editAndCreatepost(count,data)  
    }

    handleClose = () =>{
        this.setState({
            show:false
        })
       
      
    }

    handleShow = () =>{
        this.setState({
            show:true
        })
    }

    render(){
        return(
            <div className='container'>
                {/* Feeds tabs */}
                <div className='mt-3' style={{width:'25%',margin:'auto'}}>
                    <h3 className='text-white'>Welcome to Feeds</h3>
                </div>



                {/* New Feed */}
                <div className="button-style">
                    <Button className="ms-5" variant="light" size="sm" onClick={this.handleShow}>
                        + Create Post
                    </Button>
                </div>
              

                <>
                    <Modal show={this.state.show} onHide={()=>{this.handleClose(0)}}>
                        <Modal.Header closeButton>
                        <Modal.Title>New Post</Modal.Title>
                        </Modal.Header>
                        <Modal.Body>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlTextarea1">
                            <Form.Label>Post Description</Form.Label>
                            <Form.Control as="textarea" rows={3} id="input"/>
                            {/* <Form.Control type="text" placeholder="Normal text"  id="input"/> */}
                        </Form.Group>
                        </Modal.Body>
                        <Modal.Footer>
                        <Button variant="primary" onClick={()=>{this.recordPost(0)}}>
                           Add Post
                        </Button>
                        </Modal.Footer>
                    </Modal>
                    </>



                {/* List existing Post */}
                <div className='feeds-div'>
                    {this.state.feeds!=null?this.state.feeds.map(item=>(
                        
                        <Feed feed={item} editAndCreatepost={this.editAndCreatepost}></Feed>
                    )):''}
                
                </div>
            </div>
        )
    }
}

export default Feeds