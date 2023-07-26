import React,{Component} from 'react'
import { axiosRequest } from "../HttpClients/axiosService";
import Card from 'react-bootstrap/Card';
import Exercise from "./Exercise.webp"
import "./home.scss"

class FitnessRoutines extends Component{

    state = {
        quoteOfTheDay : "",
        data:null
    }

    componentDidMount(){
        axiosRequest({
            url: "/home/exercises",
            method: "get",
          },
          (response)=>{
                this.setState({
                    data:response.data.data.routines,
                    quoteOfTheDay:response.data.data.quoteOfTheDay
                })
          },
          (error)=>{

          }
          );
    }

    render(){
        return(
            <div>
                {/* Quote of the day */}
                <div className="justify-content-div row border mt-4 bg-white rounded">
                    <div className="col-7">
                        <h5 className='pt-3' style={{textAlign:"center"}}>Quote of the day</h5>
                        <h3 style={{color:"#0e1361", textAlign:"center"}}>{this.state.quoteOfTheDay}</h3>
                    </div>
                    <div className="col-5 image-class"></div> 
                </div>

                {/* Fitness Routines */}

                <div className='row mt-3'>

                    <div className='col-8 bg-light border rounded'>
                        <div className='py-3 px-3'>
                            <h5 className='mb-3 blue-text-color'>Recomended Workouts</h5>
                            {this.state.data!=null?this.state.data.map((item)=>(
                                <div className='row border rounded mb-4 box-shadow'>
                                    
                                    <div className='col-3 px-0'>
                                        <img src={Exercise} width="100%" height="100%"/>
                                    </div>
                                    <div className='col-9 px-0'>
                                    <Card className="border-0">
                                        <Card.Body>
                                            <Card.Title className="blue-text-color">{item.routineName}</Card.Title>
                                            <Card.Text>
                                                {item.routineDescription}
                                            </Card.Text>
                                            <small className='text-muted'>Tip - </small>
                                           <small className='text-muted'>{item.tips}</small>
                                        </Card.Body>
                                    </Card>
                                    </div>
                                </div>
                            )):''}
                        </div>
                    </div>

                    <div className='col-4'>
                        {/* Recipe go in here */}
                    </div>

                </div>
             
            </div>
        )
    }
}

export default FitnessRoutines