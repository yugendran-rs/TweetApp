import axios from "axios";
import { ErrorMessage, Field, Form, Formik } from "formik";
import React from "react";
import { useNavigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import * as Yup from "yup";
import * as api from "../API";
import Toast from "../Toast/Toast";
import "./Signup.css"

const SignupSchema = Yup.object().shape({
  firstName: Yup.string()
    .min(3, "Firstname must be 3 characters at minimum")
    .required("Firstname is required"),
  lastName: Yup.string()
    .min(1, "Lastname must be 1 characters at minimum")
    .required("Lastname is required"),
  username: Yup.string()
    .min(4, "Username must be 4 characters at minimum")
    .required("Username is required"),
  contactNumber: Yup.string()
    .matches(
      /^((\\+[1-9]{1,4}[ \\-]*)|(\\([0-9]{2,3}\\)[ \\-]*)|([0-9]{2,4})[ \\-]*)*?[0-9]{3,4}?[ \\-]*[0-9]{3,4}?$/,
      "Mobile Number is not valid"
    )
    .min(10, "Mobile Number must be 10 digit")
    .max(10, "Mobile Number must be 10 digit")
    .required("Mobile Number is required"),
  email: Yup.string()
    .email("Invalid email address format")
    .required("Email is required"),
  password: Yup.string()
    .min(4, "Password must be 4 characters at minimum")
    .required("Password is required"),
});

export default function Signup() {
  const navigate = useNavigate();

  const submitHandler = async (values) => {
    await axios
      .post(api.REGISTER, values)
      .then((res) => {
        setTimeout(() => {
          Toast({ message: res.data.message, type: "success" });
        }, 2000);
        navigate("/");
      })
      .catch(async (err) => {
        Toast({ message: err.response.data.message, type: "error" });
      });
  };
  return (
    <div className="container">
      <ToastContainer />
      <div className="row">
        <div className="col-lg-12">
          <Formik
            initialValues={{
              firstName: "",
              lastName: "",
              username: "",
              contactNumber: "",
              email: "",
              password: "",
            }}
            validationSchema={SignupSchema}
            onSubmit={(values) => {
              submitHandler(values);
            }}
          >
            {({ touched, errors}) => (
              <div>
                <div className="row mb-5">
                  <div className="col-lg-12 text-center">
                    <h1 className="">Sigup</h1>
                  </div>
                </div>
                <Form>
                  <div className="form-group">
                    <label htmlFor="firstName">Firstname</label>
                    <Field
                      type="text"
                      name="firstName"
                      placeholder="Enter Firstname"
                      autoComplete="off"
                      className={`mt-2 form-control
                            ${
                              touched.firstName && errors.firstName
                                ? "is-invalid"
                                : ""
                            }`}
                    />

                    <ErrorMessage
                      component="div"
                      name="firstName"
                      className="invalid-feedback"
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="lastName">Lastname</label>
                    <Field
                      type="text"
                      name="lastName"
                      placeholder="Enter Lastname"
                      autoComplete="off"
                      className={`mt-2 form-control
                            ${
                              touched.lastName && errors.lastName
                                ? "is-invalid"
                                : ""
                            }`}
                    />

                    <ErrorMessage
                      component="div"
                      name="lastName"
                      className="invalid-feedback"
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <Field
                      type="text"
                      name="username"
                      placeholder="Enter UserName"
                      autoComplete="off"
                      className={`mt-2 form-control
                            ${
                              touched.username && errors.username
                                ? "is-invalid"
                                : ""
                            }`}
                    />

                    <ErrorMessage
                      component="div"
                      name="username"
                      className="invalid-feedback"
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="contactNumber">Mobile Number</label>
                    <Field
                      type="text"
                      name="contactNumber"
                      placeholder="Enter Mobile Number"
                      autoComplete="off"
                      className={`mt-2 form-control
                            ${
                              touched.contactNumber && errors.contactNumber
                                ? "is-invalid"
                                : ""
                            }`}
                    />

                    <ErrorMessage
                      component="div"
                      name="contactNumber"
                      className="invalid-feedback"
                    />
                  </div>

                  <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <Field
                      type="email"
                      name="email"
                      placeholder="Enter Email"
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
                      Password
                    </label>
                    <Field
                      type="password"
                      name="password"
                      placeholder="Enter password"
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
                    className="btn btn-primary btn-block mt-4 mb-4"
                  >
                    Submit
                  </button>
                </Form>
              </div>
            )}
          </Formik>
        </div>
      </div>
    </div>
  );
}
