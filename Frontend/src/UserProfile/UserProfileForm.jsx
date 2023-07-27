import react,{Component} from 'react'
import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import Row from 'react-bootstrap/Row';
class UserProfileForm extends Component{

    state = {
        initialData:null,
        data:null,
        validated:false,
        disabled:false
    }

    componentDidMount(){
        if(this.props.currentData!=null){
            this.setState({
                data:this.props.currentData,
                initialData : this.props.currentData
            })
        }
    }

    componentDidUpdate(prevProps){
       if(prevProps!=this.props){
        this.setState({
            data:this.props.currentData
        })
       }
    }

    handleReset = () =>{
        this.setState({
            data:this.props.initialData
        })
    }


    handleSubmit = (event) => {
        const form = event.currentTarget;
        event.preventDefault();
        if (form.checkValidity() === false) {
          event.stopPropagation();
        }
        else{
            this.setState({
                validated:true,
                disabled:true
            })
           this.props.updateUser(this.state.data)
        }
       
    }

    handleChange = (columnName, value)=>{
        var data = this.state.data;
        data[columnName] = value;
         this.setState({
            data:data
         })
    }

    render(){
        return(
        
            <Form noValidate validated={this.state.validated} onSubmit={this.handleSubmit}>
            <Row className="mb-3">
            <Form.Group as={Col} md="4" controlId="validationCustom01">
                    <Form.Label>First name</Form.Label>
                    <Form.Control
                    required
                    type="text"
                    disabled={this.state.disabled}
                    placeholder="First name"
                    onChange={(e)=>{this.handleChange("firstName",e.target.value)}}
                    defaultValue={this.state.data!=null?this.state.data.firstName:''}
                    />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
            </Form.Group>
            <Form.Group as={Col} md="4" controlId="validationCustom02">
                <Form.Label>Last name</Form.Label>
                <Form.Control
                required
                type="text"
                disabled={this.state.disabled}
                placeholder="Last name"
                onChange={(e)=>{this.handleChange("lastName",e.target.value)}}
                defaultValue={this.state.data!=null?this.state.data.lastName:''}
                />
                <Form.Control.Feedback>Looks good!</Form.Control.Feedback>
            </Form.Group>
            <Form.Group as={Col} md="4" controlId="validationCustomUsername">
                <Form.Label>Username</Form.Label>
                <InputGroup hasValidation>
                <InputGroup.Text id="inputGroupPrepend">@</InputGroup.Text>
                <Form.Control
                    type="text"
                    placeholder="Username"
                    disabled={this.state.disabled}
                    aria-describedby="inputGroupPrepend"
                    onChange={(e)=>{this.handleChange("username",e.target.value)}}
                    defaultValue={this.state.data!=null?this.state.data.username:''}
                    required
                />
                <Form.Control.Feedback type="invalid">
                    Please enter username.
                </Form.Control.Feedback>
                </InputGroup>
            </Form.Group>
            </Row>
            <Row className="mb-4">
            <Form.Group as={Col} md="6" controlId="validationCustom03">
                <Form.Label>Street Address</Form.Label>
                <Form.Control 
                    type="text" 
                    placeholder="Street Address" 
                    disabled={this.state.disabled}
                    onChange={(e)=>{this.handleChange("streetAddress",e.target.value)}}
                    defaultValue={this.state.data!=null?this.state.data.streetAddress:''}
                    required />
                <Form.Control.Feedback type="invalid">
                Please provide a valid city.
                </Form.Control.Feedback>
            </Form.Group>
            <Form.Group as={Col} md="3" controlId="validationCustom04">
                <Form.Label>State</Form.Label>
                <Form.Control 
                    type="text" 
                    disabled={this.state.disabled}
                    onChange={(e)=>{this.handleChange("state",e.target.value)}}
                    defaultValue={this.state.data!=null?this.state.data.state:''}
                    placeholder="State" required />
                <Form.Control.Feedback type="invalid">
                Please provide a valid state.
                </Form.Control.Feedback>
            </Form.Group>
            <Form.Group as={Col} md="3" controlId="validationCustom05">
                <Form.Label>Zip</Form.Label>
                <Form.Control type="text"
                onChange={(e)=>{this.handleChange("zipCode",e.target.value)}}
                disabled={this.state.disabled}
                    defaultValue={this.state.data!=null?this.state.data.zipCode:''} 
                    placeholder="Zip" required />
                <Form.Control.Feedback type="invalid">
                Please provide a valid zip.
                </Form.Control.Feedback>
            </Form.Group>
            </Row>
            <Row className="mb-3">
            <Form.Group as={Col} md="3" controlId="validationCustom03">
                <Form.Label>Current weight</Form.Label>
                <Form.Control 
                    type="text" 
                    placeholder="Current Weight" 
                    disabled={this.state.disabled}
                    onChange={(e)=>{this.handleChange("currentWeight",e.target.value)}}
                    defaultValue={this.state.data!=null?this.state.data.currentWeight:''}
                    required />
                <Form.Control.Feedback type="invalid">
                Please provide a valid weight.
                </Form.Control.Feedback>
            </Form.Group>
            <Form.Group as={Col} md="3" controlId="validationCustom04">
                <Form.Label>Target Weight</Form.Label>
                <Form.Control 
                    type="text" 
                    disabled={this.state.disabled}
                    onChange={(e)=>{this.handleChange("targetWeight",e.target.value)}}
                    defaultValue={this.state.data!=null?this.state.data.targetWeight:''}
                    placeholder="Target weight" required />
                <Form.Control.Feedback type="invalid">
                Please provide a target weight.
                </Form.Control.Feedback>
            </Form.Group>
            </Row>
            <Button type="submit" className="mb-3">Update</Button>
        </Form>
  );
        
    }
}

export default UserProfileForm