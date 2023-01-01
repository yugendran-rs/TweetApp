import React from "react";
import Avatar from "react-avatar";
import "./Profile.css";
import * as constant from '../../utilities/Constants';

function Profile() {
  const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));

  return (
    <div className="profile">
      <Avatar
        round={true}
        name={constant.capitalize(userInfo.firstName, userInfo.lastName)}
        size="60"
      />
      <div>
        <h3 className="display-6">{constant.capitalize(userInfo.firstName, userInfo.lastName)}</h3>
      </div>
    </div>
  );
}

export default Profile;
