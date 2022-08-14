package com.tweetapp.util;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.hash.Hashing;

public class TweetUtil {

	private TweetUtil() {
	}

	public static String hash(String key) {
		return Hashing.sha512().hashString(key, Charset.defaultCharset()).toString();
	}

	public static String formatDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat.format(date);
	}
}
