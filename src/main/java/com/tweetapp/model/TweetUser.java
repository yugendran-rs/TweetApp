package com.tweetapp.model;

import java.util.Date;

public class TweetUser {
	private Long tweetId;
	private String fullName;
	private String tweetDesc;
	private Date createdDate;

	public TweetUser() {
	}

	public TweetUser(Long tweetId, String fullName, String tweetDesc, Date createdDate) {
		super();
		this.tweetId = tweetId;
		this.fullName = fullName;
		this.tweetDesc = tweetDesc;
		this.createdDate = createdDate;
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	@Override
	public String toString() {
		return "TweetUser [tweetId=" + tweetId + ", fullName=" + fullName + ", tweetDesc=" + tweetDesc
				+ ", createdDate=" + createdDate + "]";
	}

}