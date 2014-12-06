package acmdb;

import java.io.*;
import java.sql.*;

public class BookStore {
	
	public static Statement stmt;

	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static void initialize(Statement stmt) throws SQLException {
		setConfiguration(stmt);
	}
	
	private static final int ADMIN = 1;
	private static final int USER = 2;
	
	public static int authority = 0;
	
	public static boolean isManager(int authority) {
		if (authority == ADMIN) return true;
		return false;
	}
	
	public static boolean login(String username, String password) {
		/*
		 * TODO
		 * how to divide root and user
		 */
		String sql = null;
		ResultSet res = null;
		try {
			sql = "SELECT COUNT(*) FROM user U "
				+ "WHERE (U.login_name = '" + username + "' AND U.password = '" + password + "')";
			res = stmt.executeQuery(sql);
			if (res.getInt(1) == 1) {
				authority = USER;
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
				System.out.println("You have an error in your SQL syntax");
				e.printStackTrace();
			} else {
				System.out.println("No such login name or password incorrect");
			}
			return false;
		}
	}
	
	public static void logout() {
		authority = 0;
	}
}
