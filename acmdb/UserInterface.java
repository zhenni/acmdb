package acmdb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class UserInterface {

	public static void displayMenu(int authority) {
		System.out.println("        Bookstore Management System     ");
		System.out.println("1. enter your own query:");
		System.out.println("2. enter your own update:");
		System.out.println("3. exit:");
		System.out.println("4. clear the tables (be careful)");
		System.out.println("5. show the funtionality menu");
		System.out.println("please enter your choice:");
	}
	
	public static void run(int authority) {
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
}
