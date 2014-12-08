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
			case ORDERING:
				break;
			case NEWBOOK:
				break;
			case ADDCOPIES:
				break;
			case FEEDBACK:
				break;
			case USEFULNESS_RATING:
				break;
			case TRUST:
				break;
			case BROWSING:
				String need_author = null, need_publisher = null, need_title = null, need_subject = null;
				String author_name = null, publisher_name = null, title = null, subject = null, order = null;
				int c;
				
				System.out.println("Would you want to search depends on authors? (y/n)");
				while ((need_author = in.readLine()) == null || !(need_author.equals("y") || need_author.equals("n")));
				if (need_author.equals("y")) {
					System.out.println("Please enter the author's name:");
					while ((author_name = in.readLine()) == null);
				}
				
				System.out.println("Would you want to search depends on publishers? (y/n)");
				while ((need_publisher = in.readLine()) == null || !(need_publisher.equals("y") || need_publisher.equals("n")));
				if (need_publisher.equals("y")) {
					System.out.println("Please enter the publisher's name:");
					while ((publisher_name = in.readLine()) == null);
				}
				
				System.out.println("Would you want to search depends on title? (y/n)");
				while ((need_title = in.readLine()) == null || !(need_title.equals("y") || need_title.equals("n")));
				if (need_title.equals("y")) {
					System.out.println("Please enter the title words:");
					while ((title = in.readLine()) == null);
				}
				
				if (need_author.equals("n") && need_publisher.equals("n") && need_title.equals("n")) {
					System.out.println("Please enter the subject:");
					while ((subject = in.readLine()) == null);
				} else {
					System.out.println("Would you want to search depends on subject? (y/n)");
					while ((need_subject = in.readLine()) == null || !(need_subject.equals("y") || need_subject.equalsIgnoreCase("n")));
					if (need_subject.equals("y")) {
						System.out.println("Please enter the subject:");
						while ((subject = in.readLine()) == null);
					}
				}
				
				while (true) {
					System.out.println("Which way you want to order the results:");
					System.out.println("1. by year");
					System.out.println("2. by the average numerical score of the feedbacks");
					System.out.println("3. by the average numerical score of the trusted user feedbacks");
					System.out.println("Enter your choice: (1/2/3)");
					while ((order = in.readLine()) == null);
					try {
						c = Integer.parseInt(order);
					} catch (Exception e) {
						continue;
					}
					if (c < 1 || c > 3) continue;
					break;
				}
				
				Book.find(need_author, author_name, need_publisher, publisher_name, need_title, title, need_subject, subject, c);
				break;
			case USEFUL_FEEDBACK:
				break;
			case SUGGESTION:
				break;
			case DEGREE:
				break;
			case STATISTICS:
				break;
			case AWARDS:
				break;
			default:
				System.out.println("Functionality not support");
			}
			
			
			
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("ERROR: " + e.getMessage());
		}
	}
}
