import axios from "axios";
import { ErrorMessage, Field, Form, Formik } from "formik";
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import * as Yup from "yup";
import * as api from "../API";
import Profile from "../Profile/Profile";
import Toast from "../Toast/Toast";
import Tweet from "../Tweet/Tweet";

const TweetSchema = Yup.object().shape({
  tweetDescription: Yup.string()
    .min(2, "Atleast 2 character required")
    .required("Tweet can't be empty"),
});

function MyTweet() {
  const navigate = useNavigate();
  const [data, setData] = useState([]);
  const [refresh, setRefresh] = useState([]);
  const token = sessionStorage.getItem("authToken");
  const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));

  useEffect(() => {
    if (token === null) {
      setTimeout(() => {
        Toast({ message: "Session Expired", type: "warning" });
      }, 2000);
      navigate("/");
    }
    axios
      .get(api.MY_TWEETS + userInfo.email, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setData(res.data.data);
      })
      .catch((err) => {
        if (err.response.status === 403) {
          sessionStorage.removeItem("authToken");
          sessionStorage.removeItem("userInfo");
          navigate("/");
        }
      });
  }, [refresh]);

  const postHandler = ({ tweetDescription }) => {
    const payload = {
      tweetDescription,
      email: userInfo.email,
      username: userInfo.username,
      firstName: userInfo.firstName,
      lastName: userInfo.lastName,
    };

    axios
      .post(api.POST_TWEET + `${userInfo.email}/add`, payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        Toast({ message: res.data.message, type: "success" });
        const newTweet = res.data.data;
        if (data) setData([...data, newTweet]);
        else setData([newTweet]);
        // setRefresh(res.data.data);
      })
      .catch(async (err) => {
        if (err.response.status == 403) {
          Toast({ message: err.response.data.message, type: "warning" });
        }
      });
  };

  return (
    <>
      <div className="container mt-5">
        <div className="row">
          <div className="col-md-4">
            <Profile />
          </div>
          <div className="col-md-1"></div>
          <div className="col-md-7">
            <div className="post-tweet">
              <Formik
                initialValues={{
                  tweetDescription: "",
                }}
                validationSchema={TweetSchema}
                onSubmit={(tweetDescription) => {
                  postHandler(tweetDescription);
                }}
              >
                {({ touched, errors }) => (
                  <Form>
                    <div className="row g-2 mb-5">
                      <div className="col-auto">
                        <Field
                          type="textarea"
                          name="tweetDescription"
                          placeholder="What's on your mind?"
                          autoComplete="off"
                          style={{ width: "450px", height: "70px" }}
                          className={`mt-2 form-control
                            ${
                              touched.tweetDescription &&
                              errors.tweetDescription
                                ? "is-invalid"
                                : ""
                            }`}
                        />
                        <ErrorMessage
                          component="div"
                          name="tweetDescription"
                          className="invalid-feedback"
                        />
                      </div>
                      <div className="col-auto mt-4">
                        <button type="submit" className="btn btn-primary">
                          Post Tweet
                        </button>
                      </div>
                    </div>
                  </Form>
                )}
              </Formik>
            </div>
            <div className="all-tweet">
              {data && data.length > 0 ? (
                data.map((tweet, index) => (
                  <Tweet key={index} tweet={tweet} isMyTweet={true} />
                ))
              ) : (
                <p className="text-center">No Tweets Found</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default MyTweet;
