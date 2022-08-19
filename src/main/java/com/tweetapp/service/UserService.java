package com.tweetapp.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.constants.ServiceConstants;
import com.tweetapp.dto.AuthRequest;
import com.tweetapp.dto.AuthResponse;
import com.tweetapp.dto.ForgotPasswordDto;
import com.tweetapp.dto.Response;
import com.tweetapp.exception.EmailExistsException;
import com.tweetapp.exception.UsernameExistsException;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenService jwtTokenService;

	private Logger logger = LogManager.getLogger(UserService.class);

	public AuthResponse login(AuthRequest authRequest) {
		logger.info("Inside login() ...");
		AuthResponse authResponse;
		Optional<User> optional = userRepository.findByEmailAndPassword(authRequest.getUsername(),
				authRequest.getPassword());
		if (optional.isPresent()) {
			String jwtToken = jwtTokenService.generateAcccessToken(authRequest.getUsername());
			User user = optional.get();
			user.setPassword("********");
			authResponse = new AuthResponse(jwtToken, user);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unable to login, Check your credentials !");
		}
		return authResponse;
	}

	public User registerUser(User user) throws Exception {
		logger.info("Inside registerUser() ...");
		User savedUser;
		Optional<User> findByEmail = userRepository.findByEmail(user.getEmail());
		Optional<User> findByUsername = userRepository.findByUsername(user.getUsername());
		if (findByEmail.isPresent() && findByUsername.isPresent()) {
			throw new Exception("The Entered Email and Username is already taken !");
		} else if (findByUsername.isPresent()) {
			throw new UsernameExistsException("The Entered Username is already taken !");
		} else if (findByEmail.isPresent()) {
			throw new EmailExistsException("The Entered Email is already taken");
		} else {
			try {
				savedUser = userRepository.save(user);
				savedUser.setPassword("********");
			} catch (Exception e) {
				logger.error("Error - {}", e);
				savedUser = null;
			}
		}
		return savedUser;
	}

	public Response forgotPassword(String email, ForgotPasswordDto forgotPasswordDto) {
		logger.info("Inside forgotPassword() ...");
		Response response;
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setPassword(forgotPasswordDto.getPassword());
			userRepository.save(user);
			logger.info("User password updated !");
			response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK,
					"Password changed successfully");
		} else {
			response = new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST,
					"Unable to change password, please check the username");
		}
		return response;
	}

	public Response getAllUsers() {
		logger.info("Inside getAllUsers() ...");
		Response response;
		List<User> findAll = userRepository.findAll();
		try {
			if (!ObjectUtils.isEmpty(findAll) && findAll.size() > 0) {
				findAll.stream().forEach(rec -> {
					rec.setPassword("********");
				});
				response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "Users Found", findAll);
			} else {
				response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "No users exist !", null);
			}
		} catch (Exception e) {
			response = new Response(ServiceConstants.FAILED, ServiceConstants.INTERNAL_ERROR, "No users Found !", null);
		}
		return response;
	}

	public Response getUserByUsername(String email) {
		logger.info("Inside getUserByUsername() ...");
		Response response;
		Optional<User> optional = userRepository.findByEmail(email);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setPassword("********");
			response = new Response(ServiceConstants.SUCCESS, ServiceConstants.HTTP_OK, "User Found", user);
		} else {
			response = new Response(ServiceConstants.FAILED, ServiceConstants.BAD_REQUEST, "User Not Found", null);
		}
		return response;
	}

}
