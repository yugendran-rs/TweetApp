import axios from "axios";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import * as api from "../API";
import Profile from "../Profile/Profile";
import Toast from "../Toast/Toast";
import Tweet from "../Tweet/Tweet";

function AllTweet() {
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
      .get(api.ALL_TWEETS, {
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
            <div className="all-tweet">
              {data && data.length > 0 ? (
                data.map((tweet, key) => (
                  <Tweet key={key} tweet={tweet} isMyTweet={false} />
                ))
              ) : (
                <p>No Tweets Found</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default AllTweet;
