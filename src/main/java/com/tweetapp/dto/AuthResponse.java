package com.tweetapp.dto;

import com.tweetapp.model.User;

public class AuthResponse {

	private final String jwt;
	private final User user;

	public AuthResponse(String jwt, User user) {
		super();
		this.jwt = jwt;
		this.user = user;
	}

	public String getJwt() {
		return jwt;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "AuthResponse [jwt=" + jwt + ", user=" + user + "]";
	}

}
