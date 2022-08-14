package com.tweetapp.util;

import java.util.List;
import java.util.Optional;

import com.tweetapp.common.ServiceConstants;
import com.tweetapp.dao.impl.UserDaoImpl;
import com.tweetapp.exception.UserExistsException;
import com.tweetapp.exception.ValidationException;
import com.tweetapp.model.SecurityQuestion;

public class Validations {

	public static void validateUserName(String userName) throws ValidationException, UserExistsException {
		if (isEmpty(userName))
			throw new ValidationException("Username should be be empty");

		if (userName.length() < 6)
			throw new ValidationException("Username should be greater than 6 characters");

		if (userName.length() > 30)
			throw new ValidationException("Username should be less than 30 characters");

		boolean isExist = UserDaoImpl.getInstance().isUserExist(userName);
		if (isExist)
			throw new UserExistsException(ServiceConstants.USER_EXIST);
	}

	public static void validateFirstName(String firstName) throws ValidationException {
		if (isEmpty(firstName))
			throw new ValidationException("This field can't be empty");
	}

	public static void validatePassword(String password, String confirmPassword) throws ValidationException {
		if (confirmPassword == null) {
			if (isEmpty(password))
				throw new ValidationException("Password can't be empty");

			if (password.length() < 6)
				throw new ValidationException("Please enter atlease 6 digit password");
		}

		if (confirmPassword != null && password != null) {
			if (!TweetUtil.hash(password).equals(TweetUtil.hash(confirmPassword)))
				throw new ValidationException("Password & Confirm password must be same!");
		}
	}

	public static void validateSecurityQuestion(String securityQuestionId, List<SecurityQuestion> questions)
			throws ValidationException {
		if (isEmpty(securityQuestionId))
			throw new ValidationException("Please select question number");

		if (!securityQuestionId.matches("[+-]?\\d*(\\.\\d+)?"))
			throw new ValidationException("Please select valid option");

		Long selectedQuestion = Long.valueOf(securityQuestionId);
		Optional<SecurityQuestion> findAny = questions.stream()
				.filter(q -> q.getSecurityQuestionId().equals(selectedQuestion)).findAny();

		if (findAny.isEmpty())
			throw new ValidationException("Please select valid question");
	}

	public static void validateSecurityAnswer(String securityAnswer) throws ValidationException {
		if (isEmpty(securityAnswer))
			throw new ValidationException("Answer can't be empty");
	}

	public static void validateTweet(String tweetDesc) throws ValidationException {
		if (isEmpty(tweetDesc))
			throw new ValidationException("Tweet can't be empty");
		if (tweetDesc.length() < 5)
			throw new ValidationException("Please enter atleast 5 characters");
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty())
			return true;
		return false;
	}

}
