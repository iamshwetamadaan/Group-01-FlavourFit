import { Outlet, useNavigate } from "react-router-dom";
import logo from "./logo.svg";
import { Container, Nav, Navbar, Image } from "react-bootstrap";
import { useEffect, useState } from "react";
import Logo from "./resources/Images/logo.png";
import userIcon from "./resources/Images/circle-user-solid.svg";
import { axiosRequest } from "./HttpClients/axiosService";

function App() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);
  useEffect(() => {
    axiosRequest(
      { url: "/users/get-current-user" },
      (response) => {
        if (response?.data?.data?.userDetails) {
          const userDetails = { ...response.data.data.userDetails };
          setUser(userDetails);

          if (userDetails.type === "guest") {
            navigate("/recipes");
          }
        }
      },
      (error) => {}
    );
  }, []);

  return (
    <div className="App">
      <Navbar bg="light" data-bs-theme="light">
        <Container>
          <Navbar.Brand
            href={user?.type === "registered" ? "/home" : "/recipes"}
          >
            <Image src={Logo} width={150} className="guest-login-logo" />
          </Navbar.Brand>
          <Nav className="me-auto">
            {user?.type === "registered" ? (
              <Nav.Link href="/home">Home</Nav.Link>
            ) : null}
            {user?.type === "registered" ? (
              <Nav.Link href="/trackers">Trackers</Nav.Link>
            ) : null}
            <Nav.Link href="/recipes">Recipes</Nav.Link>
            <Nav.Link href="/feeds">Feed</Nav.Link>
          </Nav>
          {user?.type === "registered" ? (
            <Nav>
              <Nav.Link href="/edit-profile">
                <Image src={userIcon} width={30} />
              </Nav.Link>
            </Nav>
          ) : null}
        </Container>
      </Navbar>
      <Outlet />
    </div>
  );
}

export default App;
