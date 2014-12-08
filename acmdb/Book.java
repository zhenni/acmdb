package acmdb;

import java.sql.*;

public class Book {
	private static Statement stmt;
	
	public Book(Statement _stmt) throws SQLException {
		stmt = _stmt;
	}
	
	public int newBook(String isbn, String title, String year, String copy_num, String price, String format, String subject, String keywords, String publisher) throws SQLException{
		//TODO select publisher
		String sql = "INSERT INTO user() VALUES (\'"
				+ isbn + "\', \'"
				+ year + "\')"
				;
		int res = stmt.executeUpdate(sql);
		return res;
	}
	
	public static void find(String need_author, String author, String need_publisher, String publisher, String need_title, String title, String need_subject, String subject, int order) {
		// TODO
	}
}
