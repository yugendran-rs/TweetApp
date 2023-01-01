import axios from "axios";
import { ErrorMessage, Field, Form as FForm, Formik } from "formik";
import moment from "moment";
import React, { useEffect, useState } from "react";
import Avatar from "react-avatar";
import Modal from "react-bootstrap/Modal";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import * as Yup from "yup";
import * as constant from "../../utilities/Constants";
import * as api from "../API";
import Toast from "../Toast/Toast";
import "./Tweet.css";

const CommentSchema = Yup.object().shape({
  reply: Yup.string()
    .min(2, "Atleast 2 character required")
    .required("Comment can't be empty"),
});

const ReplySchema = Yup.object().shape({
  desc: Yup.string()
    .min(2, "Atleast 2 character required")
    .required("Tweet can't be empty"),
});

function Tweet({ tweet: tweeet, isMyTweet }) {
  const [expand, setExpand] = useState(false);
  const [expandReply, setExpandReply] = useState(false);
  const [isLiked, setIsLiked] = useState(false);
  const [tweet, setTweet] = useState(tweeet);
  const [showDelete, setShowDelete] = useState(isMyTweet);
  const [show, setShow] = useState(false);
  const [des, setDes] = useState(tweet && tweet.tweetDescription);

  const token = sessionStorage.getItem("authToken");
  const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));

  useEffect(() => {
    setLiked();
  }, [isLiked, tweet]);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  function likeHandler() {
    if (isLiked) {
      Toast({ message: "You already liked this Tweet !", type: "warning" });
    } else {
      axios
        .put(
          api.LIKE_TWEET + `${userInfo.email}/like/${tweet.id}`,
          { tweetDescription: des },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((res) => {
          Toast({ message: res.data.message, type: "success" });
          setTweet(res.data.data);
        })
        .catch(async (err) => {
          if (err.response.status === 403) {
            Toast({ message: err.response.data.message, type: "warning" });
          }
        });
    }
  }

  const setLiked = () =>
    setIsLiked(tweet && tweet.likedByList.includes(userInfo.email));

  const replyHandler = (reply) => {
    axios
      .post(api.REPLY_TWEET + `${userInfo.email}/reply/${tweet.id}`, reply, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        Toast({ message: res.data.message, type: "success" });
        setTweet(res.data.data);
      })
      .catch(async (err) => {
        if (err.response.status === 403) {
          Toast({ message: err.response.data.message, type: "warning" });
        }
      });
  };

  const deleteTweet = () => {

    axios
      .delete(api.DELETE_TWEET + `${userInfo.email}/delete/${tweet.id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        Toast({ message: res.data.message, type: "warning" });
        setTweet(null);
      })
      .catch(async (err) => {
        if (err.response.status === 403) {
          Toast({ message: err.response.data.message, type: "error" });
        }
      });
  };


  const editHandler = (desc) => {
    axios
      .put(
        api.UPDATE_TWEET + `${userInfo.email}/update/${tweet.id}`,
        {
          tweetDescription: desc.desc,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((res) => {
        Toast({ message: res.data.message, type: "success" });
        setTweet(res.data.data);
      })
      .catch(async (err) => {
          Toast({ message: err.response.data.message, type: "warning" });
      });
    setShow(false);
  };

  return (
    <div>
      {tweet ? (
        <div className="tweet mb-5">
          <div className="avatar">
            <Avatar
              round={true}
              name={constant.capitalize(tweet.firstName, tweet.lastName)}
              size="60"
            />
          </div>
          <div className="content">
            <div className="name">
              <h6>{constant.capitalize(tweet.firstName, tweet.lastName)}</h6>
              <span className="space"></span>
              <p>{moment(tweet.creationTime).fromNow()}</p>
              {showDelete && (
                <>
                  <i
                    className="bx bx-edit-alt"
                    onClick={handleShow}
                    style={{ marginLeft: "15px" }}
                  ></i>
                  <Modal centered show={show} onHide={handleClose}>
                    <Modal.Header>
                      <Modal.Title>Do you want to edit the Tweet !</Modal.Title>
                    </Modal.Header>
                    <Formik
                      initialValues={{
                        desc: des,
                      }}
                      validationSchema={ReplySchema}
                      onSubmit={(desc) => {
                        editHandler(desc);
                      }}
                    >
                      {({ touched, errors }) => (
                        <FForm>
                          <Modal.Body>
                            <Field
                              type="textarea"
                              name="desc"
                              placeholder="What's on your mind?"
                              autoComplete="off"
                              style={{ width: "450px", height: "70px" }}
                              className={`mt-2 form-control
                            ${touched.desc && errors.desc ? "is-invalid" : ""}`}
                            />
                            <ErrorMessage
                              component="div"
                              name="desc"
                              className="invalid-feedback"
                            />
                          </Modal.Body>
                          <Modal.Footer>
                            <button
                              type="button"
                              className="btn btn-secondary"
                              onClick={handleClose}
                            >
                              Close
                            </button>
                            <button type="submit" className="btn btn-primary">
                              Update Tweet
                            </button>
                          </Modal.Footer>
                        </FForm>
                      )}
                    </Formik>
                  </Modal>
                </>
              )}
            </div>
            <div className="message">
              <p>{tweet.tweetDescription}</p>
            </div>
            <div className="reactions">
              <div>
                <ToastContainer />
                <i
                  className={isLiked ? "bx bxs-heart" : "bx bx-heart"}
                  onClick={likeHandler}
                  style={isLiked ? { color: "#fe1313" } : { color: "#b3b3b3" }}
                ></i>
                &nbsp;<span>{tweet.likeCount}</span>
              </div>

              <div>
                <i
                  className="bx bx-comment"
                  onClick={() => {
                    setExpandReply(false);
                    setExpand(!expand);
                  }}
                ></i>{" "}
              </div>
              {tweet.replies && tweet.replies.length > 0 && (
                <div>
                  <i
                    className="bx bx-reply"
                    onClick={() => {
                      setExpandReply(!expandReply);
                      setExpand(false);
                    }}
                  ></i>
                </div>
              )}
              {showDelete && (
                <div>
                  <i className="bx bx-trash-alt" onClick={deleteTweet}></i>
                </div>
              )}
            </div>
            {expand && (
              <div className="expand">
                <Formik
                  initialValues={{
                    reply: "",
                  }}
                  validationSchema={CommentSchema}
                  onSubmit={(reply) => {
                    replyHandler(reply);
                  }}
                >
                  {({ touched, errors }) => (
                    <FForm>
                      <div className="form-group">
                        <Field
                          type="textarea"
                          name="reply"
                          placeholder="Enter Comment"
                          autoComplete="off"
                          className={`mt-2 form-control
                            ${
                              touched.reply && errors.reply ? "is-invalid" : ""
                            }`}
                        />

                        <ErrorMessage
                          component="div"
                          name="reply"
                          className="invalid-feedback"
                        />
                      </div>

                      <button type="submit" className="btn btn-primary btn-sm">
                        Submit
                      </button>
                    </FForm>
                  )}
                </Formik>
              </div>
            )}
            {expandReply &&
              tweet.replies.length > 0 &&
              tweet.replies.map((reply, key) => (
                <div className="expand-reply" key={key}>
                  <div className="name">
                    <h6>
                      {constant.capitalize(reply.firstName, reply.lastName)}
                    </h6>
                    <span className="space"></span>
                    <p>{moment(reply.creationTime).fromNow()}</p>
                  </div>
                  <div className="reply">{reply.reply}</div>
                </div>
              ))}
          </div>
        </div>
      ) : (
        <ToastContainer />
      )}
    </div>
  );
}

export default Tweet;
