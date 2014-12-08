package acmdb;

import java.sql.*;
//import javax.servlet.http.*;

public class Order{
	
	public static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static int order(int u_id, String isbn, int copy_num, java.sql.Timestamp time) throws Exception{
		String sql;
		int res;


		sql = "SELECT COUNT(*) FROM book WHERE isbn = \'" + isbn + "\'";
	
		int cnt = Integer.parseInt(getQueryWithOneResult(sql));
		if (cnt == 0){
			System.out.println("There is not a book with the isbn " + isbn);
			return -1;
		}
		
		sql = "SELECT copy_num FROM book WHERE isbn = \'" + isbn + "\'";
	
		int book_copies = Integer.parseInt(getQueryWithOneResult(sql));
		if(book_copies < copy_num) {
			System.out.println("There are not enough copies of the book with " + isbn + ". Only " + book_copies + " copies exist, but you order " + copy_num);
			return -1;
		}

		
		sql = "INSERT INTO orders(time, copy_num, u_id, isbn) VALUES (\'"+time+"\', \'"+copy_num+"\', \'"+u_id+"\', \'"+isbn+"\')";
		res = executeUpdate(sql);
		if (res == -1) {
			return -1;
		}
		
		//update the copy_num in book table
		sql = "UPDATE book SET copy_num = copy_num - \'"+ copy_num + "\' WHERE isbn = \'"+isbn+"\'";
		res = executeUpdate(sql);
		if (res == -1){
			//FIXME drop the record from the orders
		}
		return res;
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
