package acmdb;

import java.io.*;
import java.sql.*;

public class Functionality {
	
	public static Statement stmt;

	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}

	public static final int REG = 1;
	public static void handleFunc(int op) {
		try {
			switch (op) {
			case REG:
				String sql = "";
				ResultSet res;
			
				res = stmt.executeQuery(sql);
				break;
			default:
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
