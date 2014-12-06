package acmdb;

import java.io.*;
import java.sql.*;

public class UserInterface {
	
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static void initialize(Statement _stmt) {
		setConfiguration(_stmt);
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
				while (!displayLogin());
				
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
					displayFuncMenu(authority);
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
	
	private static boolean displayLogin() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			String loginName = "";
			String password = "";
			
			System.out.println("Please enter your login name:");
			while ((loginName = in.readLine()) == null);
			
			System.out.println("Please enter your password:");
			while ((password = in.readLine()) == null);
			
			if (BookStore.login(loginName, password)) {
				System.out.println("Login successfully");
				return true;
			} else {
				System.out.println("Login failed");
				return false;
			}
		} catch (Exception e) {
			System.out.println("Some errors when login");
			return false;
		}
	}
	
	public static final int REGISTRATION = 1;
	public static final int ORDERING = 2;
	public static final int NEWBOOK = 3;
	public static final int ADDCOPIES = 4;
	public static final int FEEDBACK = 5;
	public static final int USEFULNESS_RATING = 6;
	public static final int TRUST = 7;
	public static final int BROWSING = 8;
	public static final int USEFUL_FEEDBACK = 9;
	public static final int SUGGESTION = 10;
	public static final int DEGREE = 11;
	public static final int STATISTICS = 12;
	public static final int AWARDS = 13;
	
	public static void displayFuncMenu(int authority) {	
		System.out.println("1.  Registration");
		System.out.println("2.  Ordering");
		System.out.println("3.  New book");
		System.out.println("4.  Arrival of more copies");
		System.out.println("5.  Feedback recordings");
		System.out.println("6.  Usefulness ratings");
		System.out.println("7.  Trust recordings");
		System.out.println("8.  Book browsing");
		System.out.println("9.  Useful feedbacks");
		System.out.println("10. Buying suggestions");
		System.out.println("11. \'Two degrees of separation\'");
		System.out.println("12. Statistics");
		System.out.println("13. User awards");
		
		if (BookStore.isManager(authority)) {
			// TODO
		}
		System.out.println("Please enter your choice: (1~13)");
	}
	
	private static void handleFunctionality(int op) {
		try {
			handleFunc(op);
			
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
	
	public static void handleFunc(int op) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			switch (op) {
			case REGISTRATION:
				String login_name, password, name, address, phone_num;
				System.out.println("please enter a login_name:");
				while ((login_name = in.readLine()) == null && login_name.length() == 0);
				System.out.println("please enter a password:");
				while ((password = in.readLine()) == null && password.length() == 0);
				System.out.println("please enter a name:");
    		 	while ((name = in.readLine()) == null && name.length() == 0);
    		 	System.out.println("please enter a address:");
    		 	while ((address = in.readLine()) == null && address.length() == 0);
    		 	System.out.println("please enter a phone_num:");
    		 	while ((phone_num = in.readLine()) == null && phone_num.length() == 0);
    		 	
    		 	int res = User.newUser(login_name, password, name, address, phone_num);
				if (res != -1) System.out.println("success to registration");
    		 	
    		 	break;
			default:
				
			}
			
			
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("ERROR: " + e.getMessage());
		}
	}
}
