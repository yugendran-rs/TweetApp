package com.tweetapp.exception;

public class EmailExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailExistsException(String str) {
		super(str);
	}
}
