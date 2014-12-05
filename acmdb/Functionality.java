package acmdb;

import java.io.*;
import java.sql.*;

public class Functionality {
	
	public static Statement stmt;

	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static void handleFunc(int op) {
		switch (op) {
		case Driver.REG:
			String sql = "";
			ResultSet res;
			try {
				res = stmt.executeQuery(sql);
			} catch (Exception e) {
				
			}
		default:
		}
	}
}
