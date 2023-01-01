package com.tweetapp.config;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.DecryptionFailureException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InternalServiceErrorException;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

	@Value("${mongo.db}")
	private String mongoDbName;

	@Value("${secret.name}")
	private String secretName;

	@Value("${secret.region}")
	private String secretRegion;

	public String getSecret() {

		AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withRegion(secretRegion).build();

		String secret = null, decodedBinarySecret;
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
		GetSecretValueResult getSecretValueResult = null;

		try {
			getSecretValueResult = client.getSecretValue(getSecretValueRequest);
		} catch (DecryptionFailureException e) {
			throw e;
		} catch (InternalServiceErrorException e) {
			throw e;
		} catch (InvalidParameterException e) {
			throw e;
		} catch (InvalidRequestException e) {
			throw e;
		} catch (ResourceNotFoundException e) {
			throw e;
		}

		if (getSecretValueResult.getSecretString() != null) {
			secret = getSecretValueResult.getSecretString();
		} else {
			decodedBinarySecret = new String(
					Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
		}
		return secret;
	}

	@Bean
	public MongoClient mongoClient() {
		ConnectionString connectionString = new ConnectionString(getSecret().trim());
		MongoClientSettings clientSettings = MongoClientSettings.builder().applyConnectionString(connectionString)
				.build();

		return MongoClients.create(clientSettings);
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoClient(), mongoDbName);
	}

}
