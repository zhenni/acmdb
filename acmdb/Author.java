package acmdb;

import java.sql.SQLException;
import java.sql.Statement;

public class Author {
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static int newAuthor(String author) throws SQLException {
		String sql = "INSERT INTO author(author_id, name) VALUES (\'"+author+"\', \'"+author+"\')";
		int res = executeUpdate(sql);
		return res;
	}
	
	public static int executeUpdate(String sql) throws SQLException {
		System.err.println("DEBUG CHECK : " + sql);
		return stmt.executeUpdate(sql);
	}
}
