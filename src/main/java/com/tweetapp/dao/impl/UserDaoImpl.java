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
import com.tweetapp.exception.UserExistsException;
import com.tweetapp.model.User;
import com.tweetapp.util.TweetUtil;

public class UserDaoImpl implements Crud<User> {

	private static UserDaoImpl userDaoImpl;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserDaoImpl() {
	}

	public static UserDaoImpl getInstance() {
		if (userDaoImpl == null)
			userDaoImpl = new UserDaoImpl();
		return userDaoImpl;
	}

	@Override
	public Optional<User> findById(Long id) {
		User user = null;
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con.prepareStatement("select * from app.user u where u.user_id = ?");
			prepStmt.setLong(1, id);

			resultSet = prepStmt.executeQuery();

			if (!resultSet.next())
				return Optional.empty();

			user = setUser(resultSet);
		} catch (SQLException e) {
			logger.error("User not found");
		}

		return Optional.of(user);
	}

	@Override
	public User save(User user) throws UserExistsException {
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con.prepareStatement("insert into app.user ("
					+ "user_name, first_name, last_name, hashed_password, security_question_id, security_answer, created_by)"
					+ "values (?, ?, ?, ?, ?, ?, 'system')");
			prepStmt.setString(1, user.getUserName());
			prepStmt.setString(2, user.getFirstName());
			prepStmt.setString(3, user.getLastName());
			prepStmt.setString(4, TweetUtil.hash(user.getHashedPassword()));
			prepStmt.setLong(5, user.getSecurityQuestionId());
			prepStmt.setString(6, TweetUtil.hash(user.getSecurityAnswer()));

			int result = prepStmt.executeUpdate();

			if (result == 0)
				return null;
		} catch (SQLException e) {
			throw new UserExistsException(ServiceConstants.USER_EXIST);
		}
		return user;
	}

	@Override
	public int update(User obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<User> findAll() {
		List<User> users = new ArrayList<>();
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con.prepareStatement("select * from app.user u where u.deleted_flag is false");

			resultSet = prepStmt.executeQuery();

			if (resultSet == null)
				return users;

			while (resultSet.next())
				users.add(setUser(resultSet));

		} catch (SQLException e) {
			logger.error(ServiceConstants.ERROR_OCCURED, e);
		}
		return users;
	}

	public boolean isUserExist(String userName) {
		boolean result = false;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement stmt = con.prepareStatement("select * from app.\"user\" u where u.user_name = ?");
			stmt.setString(1, userName);

			ResultSet resultSet = stmt.executeQuery();

			result = resultSet.next();

		} catch (SQLException e) {
			logger.error(ServiceConstants.ERROR_OCCURED, e);
		}

		return result;
	}

	public Optional<User> findByUsername(String userName) {
		User user = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement stmt = con.prepareStatement("select * from app.\"user\" u where u.user_name = ?");
			stmt.setString(1, userName);

			ResultSet resultSet = stmt.executeQuery();

			if (!resultSet.next())
				return Optional.empty();
			user = setUser(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("User not found");
		}

		return Optional.of(user);
	}

	public int updatePasswordById(Long userId, String password) {
		int result = 0;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement stmt = con.prepareStatement("update app.user set hashed_password = ? where user_id = ?");
			stmt.setString(1, password);
			stmt.setLong(2, userId);

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	private User setUser(ResultSet rs) throws SQLException {
		User user = new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
				rs.getLong(6), rs.getString(7), rs.getString(8), rs.getDate(9), rs.getBoolean(10));
		return user;
	}

}
