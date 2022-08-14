package com.tweetapp.model;

import java.util.Date;

public class User {
	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String hashedPassword;
	private Long securityQuestionId;
	private String securityAnswer;
	private String createdBy;
	private Date createdDate;
	private Boolean deletedFlag;

	public User() {
	}

	public User(Long userId, String userName, String firstName, String lastName, String hashedPassword,
			Long securityQuestionId, String securityAnswer, String createdBy, Date createdDate, Boolean deletedFlag) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hashedPassword = hashedPassword;
		this.securityQuestionId = securityQuestionId;
		this.securityAnswer = securityAnswer;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.deletedFlag = deletedFlag;
	}

	public User(String userName, String firstName, String lastName, String hashedPassword, Long securityQuestionId,
			String securityAnswer, String createdBy) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hashedPassword = hashedPassword;
		this.securityQuestionId = securityQuestionId;
		this.securityAnswer = securityAnswer;
		this.createdBy = createdBy;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public Long getSecurityQuestionId() {
		return securityQuestionId;
	}

	public void setSecurityQuestionId(Long securityQuestionId) {
		this.securityQuestionId = securityQuestionId;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}

	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(Boolean deletedFlag) {
		this.deletedFlag = deletedFlag;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", firstName=" + firstName + ", lastName="
				+ lastName + ", hashedPassword=" + hashedPassword + ", securityQuestionId=" + securityQuestionId
				+ ", securityAnswer=" + securityAnswer + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", deletedFlag=" + deletedFlag + "]";
	}

}