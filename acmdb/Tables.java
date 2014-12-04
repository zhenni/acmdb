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
		try {
			int res = stmt.executeUpdate(sql);
			if(res != -1)
				System.out.println("Tables: success to create table " + name);
		} catch (Exception e) {
			if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
				System.out.println("You have an error in your SQL syntax");
				e.printStackTrace();
			} 
			else {
				e.printStackTrace();
				System.err.println("Cannot connect to database server");
			}
		}
	}
	
	public static void clear(String name){
		String sql = "DROP TABLE IF EXISTS " + name + ";";
		try {
			int res = stmt.executeUpdate(sql);
			if(res != -1)
				System.out.println("Tables: success to drop table " + name);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("You have an error in your SQL syntax while drop the table");
		}
	}
}
