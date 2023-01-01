import axios from "axios";
import { ErrorMessage, Field, Form, Formik } from "formik";
import React from "react";
import { useNavigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import * as Yup from "yup";
import * as api from "../API";

import logo from "../../assets/images/Signin.svg";
import Toast from "../Toast/Toast";
import "./Signin.css";

const LoginSchema = Yup.object().shape({
  email: Yup.string()
    .email("Invalid email address format")
    .required("Email is required"),
  password: Yup.string()
    .min(4, "Password must be 4 characters at minimum")
    .required("Password is required"),
});

export default function Signin() {
  const navigate = useNavigate();
  const [forgotPassword, setForgotPassword] = React.useState(false);

  const submitHandler = async (values) => {
    const data = { username: values.email, password: values.password };
    await axios
      .post(api.LOGIN, data)
      .then((res) => {
        sessionStorage.removeItem("authToken");
        sessionStorage.removeItem("userInfo");
        sessionStorage.setItem("authToken", res.data.data.jwt);
        sessionStorage.setItem("userInfo", JSON.stringify(res.data.data.user));
        navigate("/allTweets");
      })
      .catch(async (err) => {
        if (err.response.status === 403) {
          Toast({ message: err.response.data.message, type: "warning" });
        }
      });
  };

  const updateForgotPassword = () => {
    setForgotPassword(!forgotPassword);
  };

  const forgotPasswordHandler = (values) => {
    axios
      .post(api.FORGET_PASSWORD + `${values.email}/forgot`, {
        password: values.password,
      })
      .then((res) => {
        Toast({ message: res.data.message, type: "success" });
      })
      .catch(async (err) => {
        if (err.response.status === 403) {
          Toast({ message: err.response.data.message, type: "warning" });
        }
      });
    updateForgotPassword();
  };

  const goToSignup = () => {
    navigate('/signup')
  }

  return (
    <div className="container">
      <ToastContainer />
      <div className="row">
        <div className="col-lg-4 mt-5">
          <img src={logo} alt="" />
        </div>
        <div className="col-lg-8">
          <Formik
            initialValues={{ email: "", password: "" }}
            validationSchema={LoginSchema}
            onSubmit={(values) => {
              forgotPassword
                ? forgotPasswordHandler(values)
                : submitHandler(values);
            }}
          >
            {({ touched, errors }) => (
              <div>
                <div className="row mb-5">
                  <div className="col-lg-12 text-center">
                    <h1 className="mt-3">
                      {forgotPassword ? "Forgot Password" : "Signin"}
                    </h1>
                  </div>
                </div>
                <Form>
                  <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <Field
                      type="email"
                      name="email"
                      placeholder="Enter email"
                      autoComplete="off"
                      className={`mt-2 form-control
                            ${
                              touched.email && errors.email ? "is-invalid" : ""
                            }`}
                    />

                    <ErrorMessage
                      component="div"
                      name="email"
                      className="invalid-feedback"
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="password" className="mt-3">
                      {forgotPassword ? "Enter new password" : "Password"}
                    </label>
                    <Field
                      type="password"
                      name="password"
                      placeholder={
                        forgotPassword ? "Enter new password" : "Password"
                      }
                      className={`mt-2 form-control
                            ${
                              touched.password && errors.password
                                ? "is-invalid"
                                : ""
                            }`}
                    />
                    <ErrorMessage
                      component="div"
                      name="password"
                      className="invalid-feedback"
                    />
                  </div>

                  <button
                    type="submit"
                    className="btn btn-primary btn-block mt-4"
                  >
                    {forgotPassword ? "Change Password" : "Submit"}
                  </button>
                </Form>
              </div>
            )}
          </Formik>

          <div
            className="text-primary mt-4 text-center"
            style={{ cursor: "pointer" }}
            onClick = {goToSignup}
          >
            Create an Account
          </div>

          <div
            className="text-secondary mt-4 text-center"
            style={{ cursor: "pointer" }}
            onClick={updateForgotPassword}
          >
            {forgotPassword ? "Signin" : "Forgot Password"}
          </div>
        </div>
      </div>
    </div>
  );
}
