package acmdb;

import java.sql.*;

public class Tables {
	
	public static Statement stmt;

	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	/*
	 * If the specified table is not exists, create a new table
	 */
	public static void create(String name, String sql) {
		Order order = new Order();
		if (order.exists(stmt, name)) return;
		
		try {
			stmt.executeQuery(sql);
		} catch (Exception e) {
			if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
				System.out.println("You have an error in your SQL syntax");
			} else {
				e.printStackTrace();
				System.err.println("Cannot connect to database server");
			}
		}
	}
}
