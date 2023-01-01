import React from 'react'
import { useNavigate } from 'react-router-dom'

import errorSvg from '../../assets/images/404.svg';

export default function PageNotFound() {
    const navigate = useNavigate();

    const backHandler =()=>{
        navigate('/')
    }
  return (
      <div className="container text-center">
          <img src = {errorSvg} height="400" alt="Page not found"/>
          <div className="text-center" onClick={backHandler}> 
              <h3 className="text-primary mt-3" style={ {cursor:"pointer"}}>Back to Signin page</h3>
          </div>
          
      </div>
  )
}
