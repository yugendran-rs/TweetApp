package com.tweetapp.dto;

public class ForgotPasswordDto {

	private String password;

	public ForgotPasswordDto() {
		super();
	}

	public ForgotPasswordDto(String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ForgotPasswordDto [password=" + password + "]";
	}

}
