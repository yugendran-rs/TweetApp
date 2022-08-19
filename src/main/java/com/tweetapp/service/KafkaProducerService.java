package com.tweetapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

	@Value("${topic.name}")
	private String topicName;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private Logger logger = LogManager.getLogger(KafkaProducerService.class);

	public void sendMessage(String id) {
		logger.info("Writing TweetID to be deleted on kafka topic, TweetID - {}", id);
		kafkaTemplate.send(topicName, id);
	}
}
