package acmdb;

import java.sql.*;

public class User {
	
	public User(){
		++u_id;
	}
	
	private static int u_id = 0;
	
	public void newUser(String login_name, String password, String name, String address, String phone_num, Statement stmt) throws SQLException{
		String sql = "INSERT INTO user(u_id, login_name, password, name, address, phone_num) VALUES (\'"
				+ (new Integer(u_id).toString()) + "\', \'"
				+ login_name + "\', \'"
				+ password + "\', \'"
				+ name + "\', \'"
				+ address + "\', \'"
				+ phone_num + "\')"
				;
		stmt.executeUpdate(sql);
	}
}
