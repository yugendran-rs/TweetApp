package com.tweetapp.dto;

import java.time.LocalDateTime;

public class Reply {
	private String reply;
	private String tag;
	private String email;
	private String username;
	private String firstName;
	private String lastName;
	private LocalDateTime creationTime;

	public Reply() {
		super();
	}

	public Reply(String reply, String tag, String email, String username, LocalDateTime creationTime) {
		super();
		this.reply = reply;
		this.tag = tag;
		this.email = email;
		this.username = username;
		this.creationTime = creationTime;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Reply [reply=" + reply + ", tag=" + tag + ", email=" + email + ", username=" + username + ", firstName="
				+ firstName + ", lastName=" + lastName + ", creationTime=" + creationTime + "]";
	}

}
