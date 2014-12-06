package acmdb;

import java.sql.*;

public class User {
	
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	/**regislate a new user*/
	public static int newUser(String login_name, String password, String name, String address, String phone_num) throws SQLException{
		String sql = "INSERT INTO user(login_name, password, name, address, phone_num) VALUES (\'"
				+ login_name + "\', \'"
				+ password + "\', \'"
				+ name + "\', \'"
				+ address + "\', \'"
				+ phone_num + "\')"
				;
		int res = stmt.executeUpdate(sql);
		return res;
	}
}
