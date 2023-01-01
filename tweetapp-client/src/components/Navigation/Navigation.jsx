import React from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Navigation.css";

const Navigation = React.memo(() => {
  const data = sessionStorage.getItem("authToken");
  const navigate = useNavigate();

  const logout = () => {
    window.location.reload();
    sessionStorage.removeItem("authToken");
    sessionStorage.removeItem("userInfo");
    navigate("/");
  };

  return (
    <div className="f_nav_container">
      <nav className="navbar navbar-expand-lg">
        <div className="navbar-brand">
          <span className="my-auto font-weight-bold">Tweet App</span>
        </div>
        {data && (
          <div className="collapse navbar-collapse">
            <ul className="navbar-nav ml-auto">
              <li className="nav-item active">
                <Link to="/allTweets">
                  <p>All Tweets</p>
                </Link>
              </li>
              <li className="nav-item">
                <Link to="/myTweets">
                  <p>My Tweets</p>
                </Link>
              </li>
              <li className="nav-item">
                <Link to="/allusers">
                  <p>All Users</p>
                </Link>
              </li>
              <li className="nav-item" onClick={logout}>
                <p>Logout</p>
              </li>
            </ul>
          </div>
        )}
      </nav>
    </div>
  );
});

export default Navigation;
