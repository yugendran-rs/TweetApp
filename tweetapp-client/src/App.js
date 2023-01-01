import { Route, Routes } from "react-router-dom";
import "./App.css";
import AllTweet from "./components/AllTweet/AllTweet";
import Allusers from "./components/Allusers/Allusers";
import MyTweet from "./components/MyTweet/MyTweet";
import Navigation from "./components/Navigation/Navigation";
import Profile from "./components/Profile/Profile";
import Signin from "./components/Signin/Signin";
import Signup from "./components/Signup/Signup";
import React, { useEffect } from "react";
import PrivateRoute from "./components/Auth/PrivateRoute";
import PageNotFound from "./components/PagenotFound/PageNotFound";

function App() {
  const token = sessionStorage.getItem("authToken");
  const [data, setData] = React.useState("");

  useEffect(() => {
    setData(token);
  }, []);

  return (
    <div className="App">
      {/* <Wrapper /> */}
      <Navigation />
      {/* <div className="container mt-5"> */}
        {/* <div className="row"> */}
          {/* {data && data.length > 0 && (
            <>
              <div className="col-md-4">
                <Profile />
              </div>
              <div className="col-1"></div>
            </>
          )} */}
          {/* <div
            className={
              data && data.length > 0 && data ? "col-md-7" : "col-md-12"
            }
          > */}
            <Routes>
              <Route path="/" element={<Signin />} />
              <Route path="/signup" element={<Signup />} />
              <Route element={<PrivateRoute />}>
                <Route path="/allTweets" element={<AllTweet />} />
                <Route path="/myTweets" element={<MyTweet />} />
                <Route path="/allusers" element={<Allusers />} />
              </Route>
              <Route path="*" element={<PageNotFound />} />
            </Routes>
          </div>
        // </div>
      // </div>
    // </div>
  );
}

export default App;
