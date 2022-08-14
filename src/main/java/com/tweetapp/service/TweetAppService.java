package com.tweetapp.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.tweetapp.config.DatabaseConfig;
import com.tweetapp.dao.impl.SecurityQuestionDaoImpl;
import com.tweetapp.dao.impl.TweetsDaoImpl;
import com.tweetapp.dao.impl.UserDaoImpl;
import com.tweetapp.exception.LoginException;
import com.tweetapp.exception.UserExistsException;
import com.tweetapp.exception.ValidationException;
import com.tweetapp.model.SecurityQuestion;
import com.tweetapp.model.TweetUser;
import com.tweetapp.model.Tweets;
import com.tweetapp.model.User;
import com.tweetapp.util.TweetUtil;
import com.tweetapp.util.Validations;

public class TweetAppService {

	private UserDaoImpl userDao;

	private SecurityQuestionDaoImpl securityDao;

	private TweetsDaoImpl tweetsDao;

	private Scanner sc;

	private User loginUser;

	public TweetAppService() {
		DatabaseConfig.initConnection();
		sc = new Scanner(System.in);
		this.userDao = UserDaoImpl.getInstance();
		this.securityDao = SecurityQuestionDaoImpl.getInstance();
		this.tweetsDao = TweetsDaoImpl.getInstance();
	}

	public void init() {

		System.out.println("************* WELCOME TO TWEET APP *************");
		while (true) {
			System.out.println("\n****** MAIN MENU ******" + "\n***********************" + "\n1. Register"
					+ "\n2. Login" + "\n3. Forgot Password" + "\n4. Exit" + "\n");
			System.out.print("Please select your option: ");

			String option = sc.nextLine();

			switch (option) {
			case "1":
				System.out.println("----------Registration----------");
				registerUser();
				break;
			case "2":
				System.out.println("----------Login----------");
				login();
				break;
			case "3":
				System.out.println("----------Forgot Password----------");
				forgotPassword();
				break;
			case "4":
				System.out.println("Thanks for using TweetApp");
				DatabaseConfig.closeCon();
				sc.close();
				System.exit(0);
				break;
			default:
				System.out.println("Wrong choice.. Please select correct option");
				break;
			}
		}

	}

	private void registerUser() {
		while (true) {
			try {
				String userName = getInput("Please Enter Username", "Min 6, Max 30");
				Validations.validateUserName(userName);
				String firstName = getInput("Please Enter Firstname: ", "");
				Validations.validateFirstName(firstName);
				String lastName = getInput("Please Enter Lastname: ", "");
				Validations.validateFirstName(lastName);
				String password = getInput("Please Enter Password", "Atleast 6 characters");
				Validations.validatePassword(password, null);
				String confirmPassword = getInput("Confirm Password", "Atleast 6 characters");
				Validations.validatePassword(confirmPassword, null);
				Validations.validatePassword(password, confirmPassword);

				System.out.println("**** Security Question ****");
				List<SecurityQuestion> questions = securityDao.findAll();
				questions.forEach(qn -> System.out.println(qn.getSecurityQuestionId() + ". " + qn.getQuestion()));
				String securityQuestionId = getInput("Please Select Security Question: ", "");
				Validations.validateSecurityQuestion(securityQuestionId, questions);
				String securityAnswer = getInput("Please Enter Security Answer: ", "");
				Validations.validateSecurityAnswer(securityAnswer);

				User user = new User(userName, firstName, lastName, password, Long.valueOf(securityQuestionId),
						securityAnswer, "system");

				user = userDao.save(user);

			} catch (ValidationException | UserExistsException e) {
				System.err.println(e.getMessage());
				System.out.println(System.lineSeparator());
				continue;
			}
			System.out.println("User Created Successfully!!");
			break;
		}
	}

	private void login() {
		try {
			String userName = getInput("Please enter your Username: ", "");
			String password = getInput("Please enter your Password: ", "");
			validateUser(userName, password);
		} catch (LoginException e) {
			System.err.println(e.getMessage());
			return;
		}

		String welcomeMsg = new StringBuilder("Welcome ").append(loginUser.getFirstName()).append(" ")
				.append(loginUser.getLastName()).toString().toUpperCase();

		System.out.println(welcomeMsg);

		while (true) {
			System.out.println("\n****** MENU ******" + "\n******************");
			System.out.println("\n1. Post A Tweet" + "\n2. View My Tweets" + "\n3. View All Tweets"
					+ "\n4. View All Users" + "\n5. Reset Password" + "\n6. Logout");
			System.out.print("Please select your option: ");

			String option = sc.nextLine();

			try {
				switch (option) {
				case "1":
					System.out.println("----------Post A Tweet----------");
					postTweet();
					break;
				case "2":
					System.out.println("-------- My Tweets --------");
					viewMyTweets();
					break;
				case "3":
					System.out.println("-------- All Tweets --------");
					viewAllTweets();
					break;
				case "4":
					System.out.println("-------- View Users --------");
					viewAllUsers();
					break;
				case "5":
					System.out.println("----------Reset Password----------");
					resetPassword();
					break;
				case "6":
					doLogout();
					return;
				default:
					System.out.println("Wrong choice.. Please select correct option");
					break;
				}
			} catch (ValidationException e) {
				System.err.println(e.getMessage());
			}

		}

	}

	private void forgotPassword() {
		String userName = getInput("Please enter your Username: ", "");
		try {
			Validations.validateFirstName(userName);
			Optional<User> optionalUser = userDao.findByUsername(userName);

			if (optionalUser.isPresent()) {
				User user = optionalUser.get();
				SecurityQuestion securityQuestion = securityDao.findById(user.getSecurityQuestionId()).get();

				System.out.println(securityQuestion.getQuestion());
				String answer = getInput("Please enter your answer: ", "");

				if (user.getSecurityAnswer().equals(TweetUtil.hash(answer))) {
					String password = getInput("Please enter new password", "Atleast 6 characters");
					Validations.validatePassword(password, null);
					String confirmPassword = getInput("Confirm Password", "Atleast 6 characters");
					Validations.validatePassword(confirmPassword, null);
					Validations.validatePassword(password, confirmPassword);
					int updateResult = userDao.updatePasswordById(user.getUserId(), TweetUtil.hash(confirmPassword));
					if (updateResult == 1)
						System.out.println("Your password has been changed successfully!");
					else
						System.out.println("Unable to update your password");
				} else {
					System.err.println("Incorrect answer");
				}

			} else {
				System.err.println("User doesn't exist with user name ".concat(userName));
			}
		} catch (ValidationException e) {
			System.err.println(e.getMessage());
		}
	}

	/*----------------Login Methods----------------*/
	private void postTweet() throws ValidationException {
		String tweetDesc = getInput("Please type something", "atleast 5 characters");
		Validations.validateTweet(tweetDesc);
		Tweets tweet = new Tweets(this.loginUser.getUserId(), tweetDesc);
		tweet = tweetsDao.save(tweet);
		System.out.println("Tweet has been posted!");
	}

	private void viewMyTweets() {
		List<Tweets> tweetsList = tweetsDao.findTweetsByUserId(this.loginUser.getUserId());

		if (tweetsList.isEmpty()) {
			System.out.println("No Tweets Available!");
		} else {
			tweetsList.forEach(tweet -> {
				System.out.println(TweetUtil.formatDate(tweet.getCreatedDate()) + "\t-\t" + tweet.getTweetDesc());
			});
		}
	}

	private void viewAllTweets() {
		Map<String, List<TweetUser>> tweetUser = tweetsDao.findTweetUser().stream()
				.collect(Collectors.groupingBy(TweetUser::getFullName));
		if (tweetUser.isEmpty())
			System.out.println("No Tweets Available!");
		else {
			tweetUser.forEach((user, tweets) -> {
				System.out.println(user + ":");
				tweets.forEach(tweet -> {
					System.out.println(
							"\t" + TweetUtil.formatDate(tweet.getCreatedDate()) + " - " + tweet.getTweetDesc());
				});
			});
		}
	}

	private void viewAllUsers() {
		List<User> users = userDao.findAll();

		if (users.isEmpty())
			System.out.println("No Users Available!");
		else {
			users.forEach(user -> {
				System.out.println(user.getFirstName() + " " + user.getLastName());
			});
		}
	}

	private void resetPassword() {
		try {
			String password = getInput("Please enter old password: ", "");
			Validations.validatePassword(password, null);

			if (!TweetUtil.hash(password).equals(this.loginUser.getHashedPassword())) {
				throw new ValidationException("Incorrect Password !");
			}

			String newPassword = getInput("Please Enter New Password", "Atleast 6 characters");
			Validations.validatePassword(newPassword, null);
			String confirmPassword = getInput("Confirm Password", "Atleast 6 characters");
			Validations.validatePassword(confirmPassword, null);
			Validations.validatePassword(newPassword, confirmPassword);

			int updatePassword = userDao.updatePasswordById(this.loginUser.getUserId(), TweetUtil.hash(newPassword));

			if (updatePassword != 1)
				throw new ValidationException("Can't update password.  Please try after sometime.");
			else
				this.loginUser.setHashedPassword(TweetUtil.hash(newPassword));

			System.out.println("Your password has been updated successfully!");
		} catch (ValidationException e) {
			System.err.println(e.getMessage());
		}
	}

	private void doLogout() {
		setLoginUser(null);
		System.out.println("Logged out from TweetApp");
	}

	private void validateUser(String userName, String password) throws LoginException {
		Optional<User> optionalUser = userDao.findByUsername(userName);

		if (optionalUser.isEmpty())
			throw new LoginException("User doesn't exist with user name ".concat(userName));

		if (!optionalUser.get().getHashedPassword().equals(TweetUtil.hash(password)))
			throw new LoginException("Incorrect password!");

		setLoginUser(optionalUser.get());
	}

	private void setLoginUser(User user) {
		this.loginUser = user;
	}

	private String getInput(String str, String validationMsg) {
		if (!validationMsg.isEmpty())
			str = new StringBuilder(str).append(" (").append(validationMsg).append("): ").toString();
		System.out.print(str);
		String input = sc.nextLine();
		return input;
	}

}
