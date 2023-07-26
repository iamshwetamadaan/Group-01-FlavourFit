import { Outlet } from "react-router-dom";
import logo from "./logo.svg";
import { Container, Nav, Navbar } from "react-bootstrap";
import { useEffect } from "react";

function App() {
  return (
    <div className="App" style={{backgroundColor:"#d8e4ed"}}>
      <Navbar style={{backgroundColor:"#0e1361"}} data-bs-theme="dark">
        <Container>
          <Navbar.Brand href="#home">FlavourFit</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="/home">Home</Nav.Link>
            <Nav.Link href="/features">Recipe</Nav.Link>
            <Nav.Link href="/pricing">Trackers</Nav.Link>
            <Nav.Link href="/pricing">Feed</Nav.Link>
            <Nav.Link href="/pricing">Permium</Nav.Link>
          </Nav>
        </Container>
      </Navbar>
      <Outlet />
    </div>
  );
}

export default App;
