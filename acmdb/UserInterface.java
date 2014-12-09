package acmdb;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.HashSet;
import java.util.Locale;

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
	public static final int FUNCS_ADMIN = 12;
	public static final int FUNCS_USER = 12;
	
	public static void run() {
		try {
			while (true) {
				while (!displayLoginOrRegistration());
				
				int authority = BookStore.authority;
				
				String choice;
				int c = 0;
				String sql = null;
				
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				
				while (true) {
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
						for (int i = 1; i <= numCols; ++i)
							System.out.print(rsmd.getColumnName(i) + "  ");
						System.out.println();
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
						while ((sql = in.readLine()) == null)
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
						while ((funcChoice = in.readLine()) == null);
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
						break;
					}
					else {//c == 6
						System.out.println("Welcome to the next visit :D");
						stmt.close();
						return;
					}
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
	
	private static boolean displayLoginOrRegistration() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String choice = null;
		
		while (true) {
			System.out.println("Would you like to login or register?(l/r)");
			while ((choice = in.readLine()) == null);
			if (!choice.equals("l") && !choice.equals("r")) continue;
			break;
		}
		
		if (choice.equals("l")) {
			try {
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
		} else {	
			String login_name, password, password2, name, address, phone_num;
			
			System.out.println("Please enter a login_name:");
			while ((login_name = in.readLine()) == null);
			
			while (true) {
				System.out.println("Please enter a password:");
				while ((password = in.readLine()) == null);
				System.out.println("Please enter the password again:");
				while ((password2 = in.readLine()) == null);
				
				if (password.equals(password2)) break; else
					System.out.println("Password not match, please enter again.");
			}
			
			System.out.println("Please enter a name:");
		 	while ((name = in.readLine()) == null);
		 	
		 	System.out.println("Please enter a address:");
		 	while ((address = in.readLine()) == null);
		 	
		 	System.out.println("Please enter a phone_num:");
		 	while ((phone_num = in.readLine()) == null);
		 	
		 	try {
		 		int res = User.newUser(login_name, password, name, address, phone_num);
		 		if (res != -1) {
		 			System.out.println("Success to registration.");
		 		} else {
		 			System.out.println("Registration failed.");
		 		}
		 	} catch (Exception e) {
		 		System.err.println(e.getMessage());
		 		System.out.println("Registration failed.");
		 	}
		 	return false;
		}
	}
	
	public static final int ORDERING = 1;
	public static final int NEWBOOK = 2;
	public static final int ADDCOPIES = 3;
	public static final int FEEDBACK = 4;
	public static final int USEFULNESS_RATING = 5;
	public static final int TRUST = 6;
	public static final int BROWSING = 7;
	public static final int USEFUL_FEEDBACK = 8;
	public static final int SUGGESTION = 9;
	public static final int DEGREE = 10;
	public static final int STATISTICS = 11;
	public static final int AWARDS = 12;
	
	public static void displayFuncMenu(int authority) {
		System.out.println("1.  Ordering");
		System.out.println("2.  New book");
		System.out.println("3.  Arrival of more copies");
		System.out.println("4.  Feedback recordings");
		System.out.println("5.  Usefulness ratings");
		System.out.println("6.  Trust recordings");
		System.out.println("7.  Book browsing");
		System.out.println("8.  Useful feedbacks");
		System.out.println("9.  Buying suggestions");
		System.out.println("10. \'Two degrees of separation\'");
		System.out.println("11. Statistics");
		System.out.println("12. User awards");
		
		if (BookStore.isManager(authority)) {
			// TODO
		}
		System.out.println("Please enter your choice: (1~12)");
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
			case ORDERING:
				String isbn, num, time = null;
				int n;
				java.sql.Timestamp stamp;
				
				System.out.println("Please enter the isbn of the book you want to order:");
				while ((isbn = in.readLine()) == null);
				
				while (true) {
					System.out.println("Please enter the number of copies you want to order for this book:");
					while ((num = in.readLine()) == null);
					try {
						n = Integer.parseInt(num);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				while (true) {
					System.out.println("Please enter the date:(yyyy-MM-dd hh:mm:ss)");
					while ((time = in.readLine()) == null);
					try {
						System.err.println(time);
						DateFormat dateFormat;
						dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
						dateFormat.setLenient(false);
						java.util.Date timeDate = dateFormat.parse(time);
						stamp = new java.sql.Timestamp(timeDate.getTime());
						System.err.println("time :" + time);
						System.err.println("timedate : " + timeDate);
						System.err.println("timestamp : " + stamp);
						
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				if (Order.order(User.u_id, isbn, n, stamp) != -1)
					System.out.println("Ordering successed.");
				else System.out.println("Operation failed.");
				
				break;
			case NEWBOOK:
				if (!BookStore.isManager(BookStore.authority)) {
					System.out.println("Insufficient user permissions.");
					break;
				}
				
				String title, year, copy_num, price, format, subject, keywords;
				String publisher_name, st;
				HashSet<String> author_names = new HashSet<String>();
				int y, copy;
				
				System.out.println("Please enter the isbn of the new book:");
				while ((isbn = in.readLine()) == null);
				
				System.out.println("Please enter the title of the new book:");
				while ((title = in.readLine()) == null);
				
				while (true) {
					System.out.println("Please enter the year of the publication of the new book:");
					while ((year = in.readLine()) == null);
					try {
						y = Integer.parseInt(year);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				while (true) {
					System.out.println("Please enter the number of copies arrived of the new book:");
					while ((copy_num = in.readLine()) == null);
					try {
						copy = Integer.parseInt(copy_num);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				while (true) {
					System.out.println("Please enter the price of the new book:");
					while ((price = in.readLine()) == null);
					try {
						Double.parseDouble(price);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				System.out.println("Please enter the format of the new book:");
				while ((format = in.readLine()) == null);
				
				System.out.println("Please enter the subject of the new book:");
				while ((subject = in.readLine()) == null);
				
				System.out.println("Please enter the keywords of the new book:");
				while ((keywords = in.readLine()) == null);
				
				System.out.println("Please enter the name of the publisher of the new book:");
				while ((publisher_name = in.readLine()) == null);
				
				while (true) {
					System.out.println("Please enter the number of the authors of the new book:");
					while ((st = in.readLine()) == null);
					try {
						n = Integer.parseInt(st);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				for (int i = 1; i <= n; ++i) {
					System.out.println("Please enter the name of the author " + i);
					String tmp;
					while ((tmp = in.readLine()) == null);
					author_names.add(tmp);
					
					/*
					if (Author.newAuthor(tmp) == -1)
						System.out.println("Author added failed.");
					*/
				}
				
				if (Book.newBook(isbn, title, y, copy, price, format, subject, keywords, publisher_name, author_names) == -1)
					System.out.println("New book added failed.");
				else {
					System.out.println("New book added success");
				}
				break;
			case ADDCOPIES:
				if (!BookStore.isManager(BookStore.authority)) {
					System.out.println("Insufficient user permissions.");
					break;
				}
				
				System.out.println("Please enter the isbn of the arrived book:");
				while ((isbn = in.readLine()) == null);
				
				while (true) {
					System.out.println("Please enter the number of added copies:");
					while ((num = in.readLine()) == null);
					try {
						n = Integer.parseInt(num);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				Book.addCopies(isbn, n);
				
				break;
			case FEEDBACK:
				int score;
				String comment = null;
				
				
				System.out.println("Please enter the isbn of the book you want to give feedback:");
				while ((isbn = in.readLine()) == null);
				
				if (!Order.haveOrder(User.u_id, isbn)){
					System.out.println("You have not order this book.");
					break;
				}
				
				if (Book.haveGivenFeedback(isbn, User.u_id)) {
					System.out.println("You have already given a feedback to this book.");
					break;
				}
				
				while (true) {
					System.out.println("Given your numerical score(0 = terrible, 10 = masterpiece):");
					while ((st = in.readLine()) == null);
					try {
						score = Integer.parseInt(st);
					} catch (Exception e) {
						continue;
					}
					
					if (score < 0 || score > 10) {
						System.out.println("Score out of range.");
						continue;
					}
					break;
				}
				
				while (true) {
					System.out.println("Would you want to give some additional short comments? (y/n)");
					while ((st = in.readLine()) == null);
					if (!st.equals("y") && !st.equals("n")) continue;
					break;
				}
				
				if (st.equals("y")) {
					System.out.println("Please enter your short text:");
					while ((comment = in.readLine()) == null);
				}
				
				while (true) {
					System.out.println("Please enter the date:(yyyy-MM-dd hh:mm:ss)");
					while ((time = in.readLine()) == null);
					try {
						DateFormat dateFormat;
						dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
						dateFormat.setLenient(false);
						java.util.Date timeDate = dateFormat.parse(time);
						stamp = new java.sql.Timestamp(timeDate.getTime());
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				if (Book.giveFeedback(isbn, User.u_id, score, comment, stamp) != -1) {
					System.out.println("Given comments successed.");
				} else System.out.println("Operation failed.");
				
				break;
			case USEFULNESS_RATING:
				String name;
				int u2_id;
				
				System.out.println("Please enter the isbn of the book of the feedback you want to assess:");
				while ((isbn = in.readLine()) == null);
				
				if (Book.showFeedbacks(User.u_id, isbn) == -1)
					System.out.println("No feedbacks for this book yet.");
				
				System.out.println("Please enter the login name of the user of the feedback you want to assess:");
				while ((name = in.readLine()) == null);
				
				u2_id = User.getUserId(name);
				
				if(!Book.haveGivenFeedback(isbn, u2_id)){
					System.out.println("the user "+ name + " has not give a feed back for the book "+ isbn);
					break;
				}
				
				
				while (true) {
					System.out.println("Please give your numerical score(0 = 'userless', 1 = 'useful', 2 = 'very useful'):");
					while ((st = in.readLine()) == null);
					try {
						score = Integer.parseInt(st);
					} catch (Exception e) {
						continue;
					}
					
					if (score < 0 || score > 2) continue;
					break;
				}
				
				if (Book.usefulnessRating(User.u_id, isbn, u2_id, score) != -1)
					System.out.println("Feedback assessing successed.");
				else System.out.println("Operation failed.");
				
				break;
			case TRUST:
				int trust;
				
				System.out.println("Please enter the login name of the user that you want to trust or not trust:");
				while ((name = in.readLine()) == null);
				
				u2_id = User.getUserId(name);
				
				while (true) {
					System.out.println("Do you want to trust or untrust this user? (t/u)");
					while ((st = in.readLine()) == null);
					
					if (st.equals("t")) {
						trust = 1;
					} else
					if (st.equals("u")) {
						trust = 0;
					} else
						continue;
					break;
				}
				
				if (User.setTrustOrNot(User.u_id, u2_id, trust) != -1)
					System.out.println("Changes done.");
				else System.out.println("Operation failed.");
				
				break;
			case BROWSING:
				String need_author = null, need_publisher = null, need_title = null, need_subject = null;
				publisher_name = null; title = null; subject = null;
				String author_name = null, order = null;
				int c;
				
				while (true) {
					System.out.println("Would you want to search depends on authors? (y/n)");
					while ((need_author = in.readLine()) == null);
					if (need_author.equals("y") || need_author.equals("n")) break;
				}
				if (need_author.equals("y")) {
					System.out.println("Please enter the author's name:");
					while ((author_name = in.readLine()) == null);
				}
				
				while (true) {
					System.out.println("Would you want to search depends on publishers? (y/n)");
					while ((need_publisher = in.readLine()) == null);
					if (need_publisher.equals("y") || need_publisher.equals("n")) break;
				}
				if (need_publisher.equals("y")) {
					System.out.println("Please enter the publisher's name:");
					while ((publisher_name = in.readLine()) == null);
				}
				
				while (true) {
					System.out.println("Would you want to search depends on title? (y/n)");
					while ((need_title = in.readLine()) == null);
					if (need_title.equals("y") || need_title.equals("n")) break;
				}
				if (need_title.equals("y")) {
					System.out.println("Please enter the title words:");
					while ((title = in.readLine()) == null);
				}
				
				if (need_author.equals("n") && need_publisher.equals("n") && need_title.equals("n")) {
					System.out.println("Please enter the subject:");
					while ((subject = in.readLine()) == null);
				} else {
					while (true) {
						System.out.println("Would you want to search depends on subject? (y/n)");
						while ((need_subject = in.readLine()) == null);
						if (need_subject.equals("y") || need_subject.equalsIgnoreCase("n")) break;
					}
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
				
				if (Book.find(User.u_id, author_name, publisher_name, title, subject, c) == -1)
					System.out.println("Operation failed.");
				break;
				
			case USEFUL_FEEDBACK:
				System.out.println("Enter the isbn of the book you want to ask:");
				while ((isbn = in.readLine()) == null);
				
				while (true) {
					System.out.println("Enter the number of top 'useful' feedbacks you expect to displayed:");
					while ((st = in.readLine()) == null);
					try {
						c = Integer.parseInt(st);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				if (Book.displayUsefulFeedback(isbn, c) == -1)
					System.out.println("Operation failed.");
				
				break;
			case SUGGESTION:
				Book.giveSuggestBooks(User.u_id);
				break;
			case DEGREE:
				String author1, author2;
				
				System.out.println("Please enter the name of one of the author:");
				while ((author1 = in.readLine()) == null);
				
				System.out.println("Please enter the name of the other author:");
				while ((author2 = in.readLine()) == null);
				
				try {
					Book.giveSeparationDegree(author1, author2);
				} catch (Exception e) {
					System.out.println("Operation failed.");
				}
				break;
			case STATISTICS:
				int m;
				String time1 = null, time2 = null;
				java.sql.Timestamp stamp1, stamp2;
				
				while (true) {
					System.out.println("Please enter the number of top items you want to see:");
					while ((st = in.readLine()) == null);
					try {
						m = Integer.parseInt(st);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				while (true) {
					System.out.println("Please enter the start date of the statistics:(yyyy-MM-dd hh:mm:ss)");
					while ((time1 = in.readLine()) == null);
					try {
						DateFormat dateFormat;
						dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
						dateFormat.setLenient(false);
						java.util.Date timeDate = dateFormat.parse(time1);
						stamp1 = new java.sql.Timestamp(timeDate.getTime());
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				while (true) {
					System.out.println("Please enter the end date of the statistics:(yyyy-MM-dd hh:mm:ss)");
					while ((time2 = in.readLine()) == null);
					try {
						DateFormat dateFormat;
						dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
						dateFormat.setLenient(false);
						java.util.Date timeDate = dateFormat.parse(time2);
						stamp2 = new java.sql.Timestamp(timeDate.getTime());
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				BookStore.displayStatistics(m, stamp1, stamp2);
				break;
			case AWARDS:
				while (true) {
					System.out.println("Please enter the number of top items you want to see:");
					while ((st = in.readLine()) == null);
					try {
						m = Integer.parseInt(st);
					} catch (Exception e) {
						continue;
					}
					break;
				}
				
				BookStore.displayAwardedUsers(m);
				
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
