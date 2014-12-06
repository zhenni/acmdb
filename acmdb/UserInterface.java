package acmdb;

import java.io.*;
import java.sql.*;

public class UserInterface {
	
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}

	public static void displayMenu(int authority) {
		System.out.println("        Bookstore Management System     ");
		System.out.println("1. enter your own query:");
		System.out.println("2. enter your own update:");
		System.out.println("3. clear the tables (be careful)");
		System.out.println("4. show the funtionality menu");
		System.out.println("5. log out");
		System.out.println("6. exit:");
		System.out.println("please enter your choice:");
	}
	
	public static final int CHOICES = 6;
	
	//TODO need to be modified
	public static final int FUNCS_ADMIN = 13;
	public static final int FUNCS_USER = -1;
	
	public static void run() {
		try {
			while (true) {
				BookStore.login();
				
				int authority = BookStore.authority;
				
				String choice;
				int c = 0;
				String sql = null;
				
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				
				displayMenu(authority);
				while ((choice = in.readLine()) == null);
				try {
					c = Integer.parseInt(choice);
				} catch (Exception e) {
					continue;
				}
				
				if (c < 1 || c > CHOICES) continue;
				
				if (c == 1) {
					System.out.println("please enter your query below:");
					while ((sql = in.readLine()) == null)
						System.out.println(sql);
					ResultSet rs = stmt.executeQuery(sql);
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
					int res = stmt.executeUpdate(sql);
					if (res != -1) {
						System.out.println("Update suscess"); 
					}
				}
				else if (c == 3){
					Driver.clearTables();
				}
				else if (c == 4){
					BookStore.printFuncMenu(authority);
					String funcChoice;
					while ((funcChoice = in.readLine()) == null && funcChoice.length() == 0);
					int func;
					try {
						func = Integer.parseInt(funcChoice);
					} catch (Exception e) {
						continue;
					}
					int num = FUNCS_USER;
					if (BookStore.isManager(authority)) num = FUNCS_ADMIN;
					if (func > num) 
						System.out.println("out of the range");
					else handleFunctionality(func);
				}
				else if (c == 5) {
					BookStore.logout();
					continue;
				}
				else {//c == 6
					System.out.println("Welcome to the next visit :D");
					stmt.close();
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
		}
	}
	
	private static void handleFunctionality(int op) {
		try {
			BookStore.handleFunc(op);
			
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
