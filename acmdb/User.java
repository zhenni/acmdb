package acmdb;

import java.sql.*;

public class User {
	
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	// the information of the entered user
	// modified in BookStore.java
	public static int u_id = -1;
	public static String name = null;
	public static String login_name = null;
	public static String password = null;
	public static String address = null;
	public static String phone_num = null;
	
	/**regislate a new user*/
	public static int newUser(String login_name, String password, String name, String address, String phone_num) throws SQLException{
		//login_name = (login_name ? null "NULL": );
		String sql = "INSERT INTO user(login_name, password, name, address, phone_num) VALUES ("
				+ login_name
				+ password
				+ name
				+ address
				+ phone_num + ")"
				;
		int res = stmt.executeUpdate(sql);
		return res;
	}
}
