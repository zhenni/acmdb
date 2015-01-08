package bookstore;

import java.sql.*;

public class User {
	
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	// the information of the entered user
	// modified in BookStore.java
	public int u_id = -1;
	public String name = null;
	public String login_name = null;
	public String password = null;
	public String address = null;
	public String phone_num = null;
	
	public int authority = 0;
	
	/**regislate a new user*/
	public static int newUser(String login_name, String password, String name, String address, String phone_num) {
		String sql = "INSERT INTO user(login_name, password, name, address, phone_num) VALUES (\'"
				+ login_name + "\', \'"
				+ password + "\', \'"
				+ name + "\', \'"
				+ address + "\', \'"
				+ phone_num + "\')"
				;
		try {
			int res = executeUpdate(sql);
			return res;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static boolean exists(String login_name) throws Exception{
		String sql = "SELECT COUNT(*) "
				+ "FROM user "
				+ "WHERE login_name = \'" + login_name + "\'";
		int cnt = Integer.parseInt(getQueryWithOneResult(sql));
		if (cnt > 0) return true;
		return false;
	}

	public static int getUserId(String login_name) {
		String sql = "SELECT u_id FROM user WHERE user.login_name = \'"+login_name+"\'";
		try {
			int res = Integer.parseInt(getQueryWithOneResult(sql));
			if (res == 0) return -1;
			return res;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static int setTrustOrNot(int u_id1, int u_id2, int trust) throws SQLException{
		String sql = "REPLACE INTO user_trust (u_id1, u_id2, is_trust) VALUES (\'"
			+ u_id1 + "\', \'"
			+ u_id2 + "\', \'"
			+ trust + "\')";
		return executeUpdate(sql);
	}
	
	public static String getQueryWithOneResult(String sql) throws SQLException {
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		if (numCols > 1){
			System.err.println("not only one result col");
		}
		rs.next();
		
		String res = rs.getString(1);
		
		if(rs.next()){
			System.err.println("not only one result");
		}
		rs.close();
		return res;
	}
	
	public static int executeUpdate(String sql) throws SQLException{
		System.err.println("DEBUG CHECK : " + sql);
		return stmt.executeUpdate(sql);
	}

	
}
