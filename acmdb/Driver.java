package acmdb;

import java.io.*;
import java.sql.*;

public class Driver {
	
	public static final int CHOICE = 3;
	
	public static void createTables(Connector con) {
		Tables.setConfiguration(con.stmt);
		
		String sql = "CREATE TABLE user ( "
					+"u_id CHAR(30) PRIMARY KEY,"
					+"name CHAR(30), "
					+"login_name CHAR(30) UNIQUE, "
					+"password CHAR(30), "
					+"address CHAR(100), "
					+"phone_num CHAR(20) "
					+");";
		Tables.create("user", sql);
		sql = "CREATE TABLE user_trust ( "
			+ "user1 CHAR(30), "
			+ "user2 CHAR(30), "
			+ "is_trust TINYINT(1), "
			+ "PRIMARY KEY (user1, user2), "
			+ "FOREIGN KEY user1 REFERENCES user, "
			+ "FOREIGN KEY user2 REFERENCES user "
			+ ");";
		Tables.create("user_trust", sql);
		sql = "CREATE TABLE book ( "
			+ "isbn CHAR(30) PRIMARY KEY, "
			+ "title CHAR(30), "
			+ "year_of_publication INTEGER, "
			+ "copy_num INTEGER, "
			+ "price REAL, "
			+ "format CHAR(30), "
			+ "subject CHAR(100), "
			+ "keywords CHAR(100), "
			+ "publisher CHAR(30), "
			+ "FOREIGN KEY publisher REFERENCES publisher "
			+ "CHECK( "
			+ "EXIST (SELECT (*) "
			+ "FROM write W "
			+ "WHERE W.isbn = isbn) "
			+ ") "
			+ "); ";
		Tables.create("book", sql);
		sql = "CREATE TABLE author ( "
			+ "author_id CHAR(30) PRIMARY KEY, "
			+ "name CHAR(30) "
			+ ");";
		Tables.create("author", sql);
		sql = "CREATE TABLE publisher ( "
			+ "publisher_id CHAR(30) PRIMARY KEY, "
			+ "name CHAR(30) "
			+ ");";
		Tables.create("publisher", sql);
		sql = "CREATE TABLE write ( "
			+ "isbn CHAR(30), "
			+ "author_id CHAR(30), "
			+ "PRIMARY KEY (isbn, author_id) "
			+ "FOREIGN KEY isbn REFERENCES book, "
			+ "FOREIGN KEY author_id REFERENCES author "
			+ ");";
		Tables.create("write", sql);
		sql = "CREATE TABLE order ( "
			+ "date DATE, "
			+ "time CHAR(30), "
			+ "copy_num INTEGER, "
			+ "login_name CHAR(30), "
			+ "isbn CHAR(30), "
			+ "PRIMARY KEY (date, time, login_name, isbn), "
			+ "FOREIGN KEY login_name REFERENCES user, "
			+ "FOREIGN KEY isbn REFERENCES book "
			+ ");";
		Tables.create("order", sql);
		sql = "CREATE TABLE opinion ( "
			+ "id CHAR(20) PRIMARY KEY, "
			+ "date DATE, "
			+ "short_text VARCHAR(200), "
			+ "score INTEGER, "
			+ "login_name CHAR(30), "
			+ "isbn CHAR(30), "
			+ "FOREIGN KEY login_name REFERENCES user, "
			+ "FOREIGN KEY isbn REFERENCES book "
			+ ");";
		Tables.create("opinion", sql);
		sql = "CREATE TABLE feedback ( "
			+ "score INTEGER, "
			+ "login_name CHAR(30), "
			+ "op_id CHAR(20), "
			+ "PRIMARY KEY (login_name, op_id), "
			+ "FOREIGN KEY login_name REFERENCES user, "
			+ "FOREIGN KEY op_id REFERENCES opinion "
			+ ");";
		Tables.create("feedback", sql);
	}
	
	public static void displayMenu() {
		System.out.println("        Bookstore Management System     ");
		System.out.println("1. enter your own query:");
		System.out.println("2. enter your own update:");
		System.out.println("3. exit:");
		System.out.println("pleasse enter your choice:");
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to our Bookstore");
		Connector con = null;
		String choice;
		int c = 0;
		String sql = null;
		try {
			con = new Connector();
			System.out.println("Database connection established");

			createTables(con);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			while (true) {
				displayMenu();
				while ((choice = in.readLine()) == null);
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {
					continue;
				}
				
				if (c < 1 || c > CHOICE) continue;
				
				if (c == 1) {
					System.out.println("please enter your query below:");
					while ((sql = in.readLine()) == null)
						System.out.println(sql);
					ResultSet rs = con.stmt.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData();
					int numCols = rsmd.getColumnCount();
					while (rs.next()) {
						for (int i = 1; i <= numCols; ++i)
							System.out.print(rs.getString(i) + "  ");
						System.out.println("");
					}
					System.out.println(" ");
					rs.close();
				}
				else if (c==2){
					System.out.println("please enter your operation below:");
					while ((sql = in.readLine()) == null && sql.length() == 0)
						System.out.println(sql);
					int res =con.stmt.executeUpdate(sql);
					if (res != -1) {
						System.out.println("Update suscess"); 
					}
				}
				else {
					System.out.println("Welcome to the next visit :D");
					con.stmt.close();
					break;
				}
			}
		} catch (Exception e) {
			if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
				System.out.println("You have an error in your SQL syntax");
			} else {
				e.printStackTrace();
				System.err.println("Cannot connect to database server");
			}
		} finally {
			if (con != null) {
				try {
					con.closeConnection();
					System.out.println("Database connection terminated");
				} catch (Exception e) {
					/* No action for close errors */
				}
			}
		}
	}
}
