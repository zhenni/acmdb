package acmdb;

import java.io.*;
import java.sql.*;

public class Driver {
	
	public static final int CHOICE = 5;
	public static final int FUNCTIONALITY = 13;
	
	public static final String[] tableNames= { "user","user_trust", "author", "publisher", "book", "writes", "orders", "opinion", "feedback"};
	
	public static void createTables(Connector con) {
		Tables.setConfiguration(con.stmt);
		
		String sql = "CREATE TABLE IF NOT EXISTS user ( "
					+"u_id CHAR(30) PRIMARY KEY,"
					+"name CHAR(30), "
					+"login_name CHAR(30) UNIQUE, "
					+"password CHAR(30), "
					+"address CHAR(100), "
					+"phone_num CHAR(20) "
					+");";
		Tables.create(tableNames[0], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS user_trust ( "
			+ "u_id1 CHAR(30), "
			+ "u_id2 CHAR(30), "
			+ "is_trust TINYINT(1), "
			+ "PRIMARY KEY (u_id1, u_id2), "
			+ "FOREIGN KEY (u_id1) REFERENCES user(u_id), "
			+ "FOREIGN KEY (u_id2) REFERENCES user(u_id)"
			+ ");";
		Tables.create(tableNames[1], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS author ( "
			+ "author_id CHAR(30) PRIMARY KEY, "
			+ "name CHAR(30) "
			+ ");";
		Tables.create(tableNames[2], sql);

		sql = "CREATE TABLE IF NOT EXISTS publisher ( "
			+ "publisher_id CHAR(30) PRIMARY KEY, "
			+ "name CHAR(30) "
			+ ");";
		Tables.create(tableNames[3], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS book ( "
			+ "isbn CHAR(30) PRIMARY KEY, "
			+ "title CHAR(30), "
			+ "year_of_publication INTEGER, "
			+ "copy_num INTEGER, "
			+ "price REAL, "
			+ "format CHAR(30), "
			+ "subject CHAR(100), "
			+ "keywords CHAR(100), "
			+ "publisher_id CHAR(30), "
			+ "FOREIGN KEY (publisher_id) REFERENCES publisher(publisher_id)"
			+ "); ";
		Tables.create(tableNames[4], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS writes ( "
			+ "isbn CHAR(30), "
			+ "author_id CHAR(30), "
			+ "PRIMARY KEY (isbn, author_id), "
			+ "FOREIGN KEY (isbn) REFERENCES book(isbn),"
			+ "FOREIGN KEY (author_id) REFERENCES author(author_id) "
			+ ");";
		Tables.create(tableNames[5], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS orders ( "
			+ "time TIMESTAMP, "
			+ "copy_num INTEGER, "
			+ "u_id CHAR(30), "
			+ "isbn CHAR(30), "
			+ "PRIMARY KEY (time, u_id, isbn), "
			+ "FOREIGN KEY (u_id) REFERENCES user (u_id), "
			+ "FOREIGN KEY (isbn) REFERENCES book (isbn)"
			+ ");";
		Tables.create(tableNames[6], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS opinion ( "
			+ "op_id CHAR(30) PRIMARY KEY, "
			+ "date DATE, "
			+ "short_text VARCHAR(200), "
			+ "score INTEGER, "
			+ "u_id CHAR(30), "
			+ "isbn CHAR(30), "
			+ "FOREIGN KEY (u_id) REFERENCES user(u_id), "
			+ "FOREIGN KEY (isbn) REFERENCES book(isbn)"
			+ ");";
		Tables.create(tableNames[7], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS feedback ( "
			+ "score INTEGER, "
			+ "u_id CHAR(30), "
			+ "op_id CHAR(30), "
			+ "PRIMARY KEY (u_id, op_id), "
			+ "FOREIGN KEY (u_id) REFERENCES user(u_id), "
			+ "FOREIGN KEY (op_id) REFERENCES opinion (op_id) "
			+ ");";
		Tables.create(tableNames[8], sql);
	}
	
	public static void clearTables(){
		int len = tableNames.length;
		for (int i = len-1; i >= 0; --i){
			Tables.clear(tableNames[i]);
		}
	}
	
	public static void displayMenu() {
		System.out.println("        Bookstore Management System     ");
		System.out.println("1. enter your own query:");
		System.out.println("2. enter your own update:");
		System.out.println("3. exit:");
		System.out.println("4. clear the tables (be careful)");
		System.out.println("5. show the funtionality menu");
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
			Functionality.setConfiguration(con.stmt);
			Functionality.init();
			
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
				else if (c == 4){
					clearTables();
				}
				else if (c == 5){
					Functionality.printFuncMenu();
					String funcChoice;
					while ((funcChoice = in.readLine()) == null && funcChoice.length() == 0);
					int func;
					try {
						func = Integer.parseInt(funcChoice);
					} catch (Exception e) {
						continue;
					}
					if (func > FUNCTIONALITY) 
						System.out.println("out of the range");
					else handleFunctionality(func);
				}
				else {//c == 3
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

	
	private static void handleFunctionality(int op) {
		try {
			
			Functionality.handleFunc(op);
			
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

	
}
