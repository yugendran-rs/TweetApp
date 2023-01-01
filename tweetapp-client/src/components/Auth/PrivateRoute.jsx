import React from 'react';
import {Navigate,Outlet} from 'react-router-dom'

const useAuth = () => {
  const user = sessionStorage.getItem('authToken');
  if (user) {
    return { auth: true }
  }else{
    return {auth:false}
  }
}

const PrivateRoute = () => {
  const { auth } = useAuth();
  return auth ? <Outlet/> : <Navigate replace to="/"/>
    
   
}

export default PrivateRoute;
