import React, { useState, useEffect } from "react";
import Avatar from "react-avatar";
import "../Tweet/Tweet.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import * as api from "../API";
import Toast from "../Toast/Toast";
import * as constant from "../../utilities/Constants";
import Profile from "../Profile/Profile";

const Allusers = () => {
  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const token = sessionStorage.getItem("authToken");

  useEffect(() => {
    if (token === null) {
      setTimeout(() => {
        Toast({ message: "Session Expired", type: "warning" });
      }, 2000);
      navigate("/");
    }
    axios
      .get(api.ALL_USERS, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setData(res.data.data);
      })
      .catch((err) => {
        if (err.response.status === 403) {
          sessionStorage.removeItem('authToken');
          sessionStorage.removeItem('userInfo');
          navigate("/");
        }
      });
  }, []);

  return (
    <>
      <div className="container mt-5">
        <div className="row">
            <div className="col-md-4">
              <Profile />
            </div>
            <div className="col-md-1"></div>
            <div className="col-md-7">
              {data &&
                data.length > 0 &&
                data.map((user, key) => (
                  <div className="row" key={key}>
                    <div className="tweet mb-5">
                      <div className="col-4">
                        <Avatar
                          round={true}
                          name={constant.capitalize(
                            user.firstName,
                            user.lastName
                          )}
                          size="90"
                        />
                      </div>
                      <div className="col-8">
                        <h3 className="display-5">
                          {constant.capitalize(user.firstName, user.lastName)}
                        </h3>
                        <div className="text-muted">@{user.username}</div>
                        <div className="display-6">{user.contactNumber}</div>
                      </div>
                    </div>
                  </div>
                ))}
            </div>
        </div>
      </div>
    </>
  );
};

export default Allusers;
