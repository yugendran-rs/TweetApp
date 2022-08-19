package com.tweetapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.dto.Reply;
import com.tweetapp.dto.Response;
import com.tweetapp.model.Tweets;
import com.tweetapp.service.TweetService;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins = "*")
public class TweetController {

	@Autowired
	private TweetService tweetService;

	@GetMapping("/all")
	public ResponseEntity<?> getAllTweets() {
		ResponseEntity<Response> allTweets = tweetService.getAllTweets();
		return allTweets;
	}

	@GetMapping("/{username}")
	public ResponseEntity<?> getAllTweetsOfUser(@PathVariable("username") String username) {
		Response response = tweetService.getTweetsByUsername(username);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/{username}/add")
	public ResponseEntity<Response> postNewTweet(@PathVariable("username") String username, @RequestBody Tweets tweet) {
		Response response = tweetService.postNewTweet(username, tweet);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/{username}/update/{id}")
	public ResponseEntity<Response> updateTweet(@PathVariable("username") String username,
			@PathVariable("id") String id, @RequestBody Tweets tweet) {
		Response response = tweetService.updateTweet(username, id, tweet);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@DeleteMapping("/{username}/delete/{id}")
	public ResponseEntity<Response> deleteTweet(@PathVariable("username") String username,
			@PathVariable("id") String id) {
		Response response = tweetService.deleteTweet(username, id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PutMapping("/{username}/like/{id}")
	public ResponseEntity<Response> likeTweet(@PathVariable("username") String username,
			@PathVariable("id") String id) {
		Response response = tweetService.likeTweet(username, id);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/{username}/reply/{id}")
	public ResponseEntity<Response> replyToTweet(@PathVariable("username") String username,
			@PathVariable("id") String id, @RequestBody Reply reply) {
		Response response = tweetService.replyToTweet(username, id, reply);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
