package com.tweetapp.service;

import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

	/*
	 * @Value("${topic.name}") private String topicName;
	 * 
	 * @Autowired private TweetRepository tweetRepository;
	 * 
	 * private Logger logger = LogManager.getLogger(KafkaConsumerService.class);
	 * 
	 * @KafkaListener(topics = "tweet-topic", groupId = "tweet-group") public void
	 * consumeDeletionMessage(String id) {
	 * logger.info("TweetID read from kafka topic by consumer = {}", id); if (id !=
	 * null) { logger.info("Inside deleteTweet() ... "); try {
	 * tweetRepository.deleteById(id); logger.info("Tweet Deleted Successfully !");
	 * } catch (Exception e) { logger.error("Error : {}", e); } } }
	 */
}
