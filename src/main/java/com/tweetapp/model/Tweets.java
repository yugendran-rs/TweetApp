package com.tweetapp.model;

import java.util.Date;

public class Tweets {
	private Long tweetsId;
	private Long userId;
	private String tweetDesc;
	private Date createdDate;
	private Boolean deletedFlag;

	public Tweets() {
	}

	public Tweets(Long tweetsId, Long userId, String tweetDesc, Date createdDate, Boolean deletedFlag) {
		super();
		this.tweetsId = tweetsId;
		this.userId = userId;
		this.tweetDesc = tweetDesc;
		this.createdDate = createdDate;
		this.deletedFlag = deletedFlag;
	}

	public Tweets(Long userId, String tweetDesc) {
		super();
		this.userId = userId;
		this.tweetDesc = tweetDesc;
	}

	public Long getTweetsId() {
		return tweetsId;
	}

	public void setTweetsId(Long tweetsId) {
		this.tweetsId = tweetsId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTweetDesc() {
		return tweetDesc;
	}

	public void setTweetDesc(String tweetDesc) {
		this.tweetDesc = tweetDesc;
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
		return "Tweets [tweetsId=" + tweetsId + ", userId=" + userId + ", tweetDesc=" + tweetDesc + ", createdDate="
				+ createdDate + ", deletedFlag=" + deletedFlag + "]";
	}

}