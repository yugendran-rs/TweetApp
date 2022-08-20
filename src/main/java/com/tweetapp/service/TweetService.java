package com.tweetapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.tweetapp.constants.ServiceConstants;
import com.tweetapp.dto.Reply;
import com.tweetapp.dto.Response;
import com.tweetapp.model.Tweets;
import com.tweetapp.model.User;
import com.tweetapp.repository.TweetRepository;
import com.tweetapp.repository.UserRepository;

@Service
public class TweetService {

	@Autowired
	private TweetRepository tweetRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private KafkaProducerService kafkaProducerService;

	private Logger logger = LogManager.getLogger(TweetService.class);

	public ResponseEntity<Response> getAllTweets() {
		logger.info("Inside getAllTweets() ...");
		Response response;
		try {
			List<Tweets> list = tweetRepository.findAll(Sort.by(Sort.Direction.DESC, "creation_time"));
			if (!ObjectUtils.isEmpty(list)) {
				response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, null, list);
			} else {
				response = new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR, "No Tweets Found",
						null);
			}
		} catch (Exception e) {
			logger.error("Error - {}", e);
			response = new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR, "No Tweets Found", null);
		}
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	public Response getTweetsByUsername(String email) {
		logger.info("Inside getTweetsByUsername() ...");
		Response response;
		try {
			Optional<List<Tweets>> optional = tweetRepository.findByEmailOrderByCreationTimeDesc(email);
			if (optional.isPresent() && optional.get().size() > 0) {
				response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "Tweets Found",
						optional.get());
			} else {
				response = new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR, "No Tweets Found",
						null);
			}
		} catch (Exception e) {
			logger.error("Error - {}", e);
			response = new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR, "No Tweets Found", null);
		}
		return response;
	}

	public Response postNewTweet(String email, Tweets tweet) {
		logger.info("Inside postNewTweet() ...");
		Response response;
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isPresent()) {
			User user = optional.get();
			tweet.setUsername(user.getUsername());
			tweet.setFirstName(user.getFirstName());
			tweet.setLastName(user.getLastName());
		} else {
			throw new UsernameNotFoundException("User Not Found !");
		}
		tweet.setEmail(email);
		tweet.setCreationTime(LocalDateTime.now());
		tweet.setLikeCount(0);
		tweet.setLikedByList(new ArrayList<>());
		tweet.setReplies(new ArrayList<>());
		try {
			Tweets savedTweet = tweetRepository.save(tweet);
			response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "Tweet Saved", savedTweet);
		} catch (Exception e) {
			logger.error("Error - {}", e);
			return new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR, "Unable to save tweet");
		}
		return response;
	}

	public Response updateTweet(String email, String id, Tweets tweet) {
		logger.info("Inside updateTweet() ...");
		Response response;
		Optional<Tweets> optional = tweetRepository.findByIdAndEmail(id, email);
		try {
			if (optional.isPresent()) {
				Tweets tweets = optional.get();
				tweets.setTweetDescription(tweet.getTweetDescription());
				tweets.setCreationTime(LocalDateTime.now());
				tweet = tweetRepository.save(tweets);
				response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "Tweet Updated", tweet);
			} else {
				response = new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST, "Tweet Updation Failed");
			}
		} catch (Exception e) {
			logger.error("Error - {}", e);
			response = new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST, "Tweet Updation Failed");
		}
		return response;
	}

	public Response deleteTweet(String email, String id) {
		Response response;
//		kafkaProducerService.sendMessage(id);
		tweetRepository.deleteById(id);
		response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "Tweet Deleted");
		return response;
	}

	public Response likeTweet(String email, String id) {
		logger.info("Inside likeTweet() ...");
		Response response;
		Optional<Tweets> optional = tweetRepository.findById(id);
		if (optional.isPresent()) {
			Tweets tweet = optional.get();
			List<String> likedByList = tweet.getLikedByList();
			if (!checkEmailInList(email, likedByList)) {
				tweet.setLikeCount(tweet.getLikeCount() + 1);
				likedByList.add(email);
				tweet.setLikedByList(likedByList);
				tweet = tweetRepository.save(tweet);
				response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "Tweet Liked", tweet);
			} else {
				response = new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR,
						"User Already liked the tweet");
			}

		} else {
			response = new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR,
					"No tweet with this id exists");
		}
		return response;
	}

	private boolean checkEmailInList(String email, List<String> likedByList) {
		Optional<String> findAny = likedByList.stream().filter(rec -> rec.equals(email)).findAny();
		return findAny.isPresent();
	}

	public Response replyToTweet(String email, String id, Reply reply) {
		logger.info("Inside replyToTweet() ...");
		Response response;
		reply.setCreationTime(LocalDateTime.now());
		reply.setEmail(email);
		try {
			Optional<Tweets> optional = tweetRepository.findById(id);
			Optional<User> optional2 = userRepository.findByEmail(email);
			if (optional.isPresent() && optional2.isPresent()) {
				Tweets tweet = optional.get();
				User user = optional2.get();
				reply.setUsername(user.getUsername());
				reply.setFirstName(user.getFirstName());
				reply.setLastName(user.getLastName());
				List<Reply> replies = tweet.getReplies();
				replies.add(reply);
				tweet.setReplies(replies);
				Tweets savedTweet = tweetRepository.save(tweet);
				response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "Reply Added", savedTweet);
			} else {
				response = new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST, "Unable to reply");
			}
		} catch (Exception e) {
			logger.error("Error - {}", e);
			response = new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST, "Unable to reply");
		}
		return response;
	}

}
