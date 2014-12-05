package acmdb;

import java.sql.*;

public class User {
	
	private static Statement stmt;
	
	public User(Statement _stmt) throws SQLException {
		stmt = _stmt;
	}
	
	public void newUser(String login_name, String password, String name, String address, String phone_num) throws SQLException{
		String sql = "INSERT INTO user(u_id, login_name, password, name, address, phone_num) VALUES (\'"
				+ "NULL" + "\', \'"
				+ login_name + "\', \'"
				+ password + "\', \'"
				+ name + "\', \'"
				+ address + "\', \'"
				+ phone_num + "\')"
				;
		stmt.executeUpdate(sql);
	}
}
