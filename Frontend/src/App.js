import { Outlet } from "react-router-dom";
import logo from "./logo.svg";
import { Container, Nav, Navbar, Image } from "react-bootstrap";
import { useEffect } from "react";
import Logo from "./resources/Images/logo.png";

function App() {
  return (
    <div className="App">
      <Navbar bg="light" data-bs-theme="light">
        <Container>
          <Navbar.Brand href="/home">
            <Image src={Logo} width={150} className="guest-login-logo" />
          </Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="/home">Home</Nav.Link>
            <Nav.Link href="/trackers">Trackers</Nav.Link>
            <Nav.Link href="/recipes">Recipes</Nav.Link>
            <Nav.Link href="/feed">Feed</Nav.Link>
          </Nav>
        </Container>
      </Navbar>
      <Outlet />
    </div>
  );
}

export default App;
