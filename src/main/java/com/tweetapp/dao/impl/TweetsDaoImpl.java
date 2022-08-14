package com.tweetapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tweetapp.common.ServiceConstants;
import com.tweetapp.config.DatabaseConfig;
import com.tweetapp.dao.Crud;
import com.tweetapp.model.TweetUser;
import com.tweetapp.model.Tweets;

public class TweetsDaoImpl implements Crud<Tweets> {

	private static TweetsDaoImpl tweetsDaoImpl;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private TweetsDaoImpl() {
	}

	public static TweetsDaoImpl getInstance() {
		if (tweetsDaoImpl == null)
			tweetsDaoImpl = new TweetsDaoImpl();
		return tweetsDaoImpl;
	}

	@Override
	public Optional<Tweets> findById(Long id) {
		Tweets tweets = null;
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con
					.prepareStatement("select * from app.tweets t where t.tweets_id = ? where t.deleted_flag is false");
			prepStmt.setLong(1, id);

			resultSet = prepStmt.executeQuery();

			if (!resultSet.next())
				return Optional.empty();

			tweets = setTweets(resultSet);
		} catch (SQLException e) {
			logger.error("Tweet not found");
		}

		return Optional.of(tweets);
	}

	@Override
	public Tweets save(Tweets tweet) {
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con
					.prepareStatement("insert into app.tweets (user_id, tweet_desc) values (?, ?)");
			prepStmt.setLong(1, tweet.getUserId());
			prepStmt.setString(2, tweet.getTweetDesc());

			int result = prepStmt.executeUpdate();

			if (result == 0)
				return null;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		}
		return tweet;
	}

	@Override
	public int update(Tweets obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Tweets> findAll() {
		List<Tweets> tweets = new ArrayList<>();
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con
					.prepareStatement("select * from app.tweets t where t.deleted_flag is false");

			resultSet = prepStmt.executeQuery();

			if (resultSet == null)
				return tweets;

			while (resultSet.next())
				tweets.add(setTweets(resultSet));

		} catch (SQLException e) {
			logger.error(ServiceConstants.ERROR_OCCURED, e);
		}
		return tweets;
	}

	public List<Tweets> findTweetsByUserId(Long userId) {
		List<Tweets> tweets = new ArrayList<>();
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con
					.prepareStatement("select * from app.tweets t where t.deleted_flag is false and t.user_id = ?");
			prepStmt.setLong(1, userId);

			resultSet = prepStmt.executeQuery();

			if (resultSet == null)
				return tweets;

			while (resultSet.next())
				tweets.add(setTweets(resultSet));

		} catch (SQLException e) {
			logger.error(ServiceConstants.ERROR_OCCURED, e);
		}
		return tweets;
	}

	public List<TweetUser> findTweetUser() {
		List<TweetUser> tweetUser = new ArrayList<>();
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con.prepareStatement(
					"select t.tweets_id, initcap(concat(u.first_name, ' ', u.last_name)) as full_name, t.tweet_desc, t.created_date from app.tweets t join app.\"user\" u on t.user_id = u.user_id and u.deleted_flag is false where t.deleted_flag is false order by full_name asc, t.created_date desc");

			resultSet = prepStmt.executeQuery();

			while (resultSet.next())
				tweetUser.add(setTweetUser(resultSet));

		} catch (SQLException e) {
			logger.error(ServiceConstants.ERROR_OCCURED, e);
		}
		return tweetUser;
	}

	private Tweets setTweets(ResultSet rs) throws SQLException {
		Tweets tweets = new Tweets(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getDate(4), rs.getBoolean(5));
		return tweets;
	}

	private TweetUser setTweetUser(ResultSet rs) throws SQLException {
		TweetUser tweetUser = new TweetUser(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getDate(4));
		return tweetUser;
	}

}
