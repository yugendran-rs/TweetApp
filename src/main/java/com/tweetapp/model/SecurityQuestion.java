package com.tweetapp.model;

import java.util.Date;

public class SecurityQuestion {
	private Long securityQuestionId;
	private String question;
	private String createdBy;
	private Date createdDate;
	private Boolean deletedFlag;

	public SecurityQuestion() {
	}

	public SecurityQuestion(Long securityQuestionId, String question, String createdBy, Date createdDate,
			Boolean deletedFlag) {
		super();
		this.securityQuestionId = securityQuestionId;
		this.question = question;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.deletedFlag = deletedFlag;
	}

	public Long getSecurityQuestionId() {
		return securityQuestionId;
	}

	public void setSecurityQuestionId(Long securityQuestionId) {
		this.securityQuestionId = securityQuestionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Boolean getDeletedFlag() {
		return deletedFlag;
	}

	public void setDeletedFlag(Boolean deletedFlag) {
		this.deletedFlag = deletedFlag;
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

	@Override
	public String toString() {
		return "SecurityQuestion [securityQuestionId=" + securityQuestionId + ", question=" + question + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", deletedFlag=" + deletedFlag + "]";
	}

}