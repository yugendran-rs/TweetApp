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
import com.tweetapp.model.SecurityQuestion;

public class SecurityQuestionDaoImpl implements Crud<SecurityQuestion> {

	private static SecurityQuestionDaoImpl securityQuestionDaoImpl;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private SecurityQuestionDaoImpl() {
	}

	public static SecurityQuestionDaoImpl getInstance() {
		if (securityQuestionDaoImpl == null)
			securityQuestionDaoImpl = new SecurityQuestionDaoImpl();
		return securityQuestionDaoImpl;
	}

	@Override
	public Optional<SecurityQuestion> findById(Long id) {
		SecurityQuestion securityQuestion = null;
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con
					.prepareStatement("select * from app.security_question sc where sc.security_question_id = ?");
			prepStmt.setLong(1, id);

			resultSet = prepStmt.executeQuery();

			if (!resultSet.next())
				return Optional.empty();

			securityQuestion = setSecurityQuestion(resultSet);

		} catch (SQLException e) {
			logger.error(ServiceConstants.ERROR_OCCURED);
		}
		return Optional.of(securityQuestion);
	}

	@Override
	public SecurityQuestion save(SecurityQuestion obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(SecurityQuestion obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SecurityQuestion> findAll() {
		List<SecurityQuestion> securityQuestion = new ArrayList<>();
		ResultSet resultSet = null;
		Connection con = DatabaseConfig.getCon();
		try {
			PreparedStatement prepStmt = con
					.prepareStatement("select * from app.security_question sc where sc.deleted_flag is false");

			resultSet = prepStmt.executeQuery();

			if (!resultSet.next())
				return securityQuestion;

			securityQuestion.add(setSecurityQuestion(resultSet));
			while (resultSet.next())
				securityQuestion.add(setSecurityQuestion(resultSet));

		} catch (SQLException e) {
			logger.error(ServiceConstants.ERROR_OCCURED);
		}
		return securityQuestion;
	}

	private SecurityQuestion setSecurityQuestion(ResultSet rs) throws SQLException {
		SecurityQuestion securityQn = new SecurityQuestion(rs.getLong(1), rs.getString(2), rs.getString(3),
				rs.getDate(4), rs.getBoolean(5));
		return securityQn;
	}

}
