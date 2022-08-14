package com.tweetapp;

import com.tweetapp.service.TweetAppService;

public class App {
	public static void main(String[] args) throws Exception {
		TweetAppService service = new TweetAppService();
		service.init();
	}
}