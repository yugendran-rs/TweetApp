package com.tweetapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {

	private static Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

	private static Connection con = null;

	public static void initConnection() {
		logger.info("Initializing Database Connection");
		con = connect();
	}

	private static Connection connect() {

		Properties properties = new Properties();
		Connection conn = null;

		InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e1) {
			logger.error("Unable to load property file");
			System.exit(0);
		}

		String url = properties.getProperty("jdbc.url");
		String userName = properties.getProperty("jdbc.username");
		String password = properties.getProperty("jdbc.password");

		try {
			conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			logger.error("Connection error, Please try after some time", e);
			System.exit(0);
		}
		return conn;
	}

	public static Connection getCon() {
		try {
			if (con.isClosed())
				initConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public static void closeCon() {
		logger.info("Closing DB Connection");
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
