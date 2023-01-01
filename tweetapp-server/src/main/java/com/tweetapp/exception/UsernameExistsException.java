package com.tweetapp.exception;

public class UsernameExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsernameExistsException(String str) {
		super(str);
	}
}
