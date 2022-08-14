package com.tweetapp.exception;

public class UserExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserExistsException(String msg) {
		super(msg);
	}
}
