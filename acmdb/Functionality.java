package acmdb;

import java.io.*;
import java.sql.*;

public class Functionality {
	
	public static Statement stmt;

	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}

	public static void printFuncMenu() {
		
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
		System.out.println("Please enter your choice: (1~13)");

	}
	
	public static final int REG = 1;
	public static void handleFunc(int op) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			switch (op) {
			case REG:
				
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
    		 	User user=new User();
    		 	user.newUser(login_name, password, name, address, phone_num, stmt);
				
				break;
			default:
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
