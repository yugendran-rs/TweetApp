package com.tweetapp.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.constants.ServiceConstants;
import com.tweetapp.dto.Response;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger logger = LogManager.getLogger(this.getClass());

	@ExceptionHandler(EmailExistsException.class)
	public ResponseEntity<Response> handleEmailAlreadyExistsException(EmailExistsException exception) {
		return new ResponseEntity<Response>(
				new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST, exception.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UsernameExistsException.class)
	public ResponseEntity<Response> handleUsernameAlreadyExistsException(UsernameExistsException exception) {
		return new ResponseEntity<Response>(
				new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST, exception.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Response> handleResponseStatusException(ResponseStatusException exception) {
		return new ResponseEntity<Response>(new Response(ServiceConstants.FAILED, ServiceConstants.FORBIDDEN,
				"Unable to login, Please Check Credentials!"), HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleException(Exception exception) {
		logger.error("Error - {}", exception.getMessage());
		return new ResponseEntity<Response>(
				new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR, exception.getLocalizedMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
