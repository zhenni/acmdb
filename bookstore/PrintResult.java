package bookstore;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class PrintResult {
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	private static final int max_len = 80;
	private static final int max_len_per_col = 30;
	
	public static int printQueryResult(String sql) throws SQLException{
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		int len = max_len/numCols - 1;
		len = Math.min(max_len_per_col, len);
		int tot_len = (len+1) * numCols+1;
		String line = "";
		for(int i = 0; i < tot_len; ++i){
			line += '-';
		}
		System.out.println(line);
		String[] col = new String[numCols+1]; // 1 - numCols
		
		int row = 0;
		while (rs.next()) {
			
			if(row++ == 0){
				for (int i = 1; i <= numCols; ++i){
					col[i] = rsmd.getColumnName(i);
				}
				
				boolean flag1 = true;
				
				while(flag1){
					flag1 = false;
					System.out.print("|");
					for (int i = 1; i <= numCols; ++i){
						System.out.print(fillWithChar(col[i], len, ' '));
						if (len < col[i].length()) {
							col[i] = col[i].substring(len);
				        }
				        else {
				        	col[i] = "";
				        }
						System.out.print("|");
						
						if (col[i].length() > 0) flag1 = true;
					}
					System.out.println();
				}
				System.out.println(line);
			}
			
			boolean flag = true;
			for (int i = 1; i <= numCols; ++i){
				col[i] = rs.getString(i);
			}
			while (flag) {
				flag = false;
				System.out.print("|");
				for (int i = 1; i <= numCols; ++i){
					System.out.print(fillWithChar(col[i], len, ' '));
					System.out.print("|");
					if (len < col[i].length()) {
						col[i] = col[i].substring(len);
			        }
			        else {
			        	col[i] = "";
			        }
					
					if (col[i].length() > 0) flag = true;
				}
				System.out.println();
			}
			
		}
		if (row == 0) System.out.println("Empty set.");
		else System.out.println(line);
		System.out.println(" ");
		rs.close();
		return row;
	}
	
	public static int printQueryResult(String sql, int m) throws SQLException{
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		int len = max_len/numCols - 1;
		len = Math.min(max_len_per_col, len);
		int tot_len = (len+1) * numCols+1;
		String line = "";
		for(int i = 0; i < tot_len; ++i){
			line += '-';
		}
		System.out.println(line);
		String[] col = new String[numCols+1]; // 1 - numCols
		
		int row;
		for (row = 0; row < m && rs.next(); ++row) {
			
			if(row == 0){
				for (int i = 1; i <= numCols; ++i){
					col[i] = rsmd.getColumnName(i);
				}
				
				boolean flag1 = true;
				
				while(flag1){
					flag1 = false;
					System.out.print("|");
					for (int i = 1; i <= numCols; ++i){
						System.out.print(fillWithChar(col[i], len, ' '));
						if (len < col[i].length()) {
							col[i] = col[i].substring(len);
				        }
				        else {
				        	col[i] = "";
				        }
						System.out.print("|");
						
						if (col[i].length() > 0) flag1 = true;
					}
					System.out.println();
				}
				System.out.println(line);
			}
			
			boolean flag = true;
			for (int i = 1; i <= numCols; ++i){
				col[i] = rs.getString(i);
			}
			while (flag) {
				flag = false;
				System.out.print("|");
				for (int i = 1; i <= numCols; ++i){
					System.out.print(fillWithChar(col[i], len, ' '));
					System.out.print("|");
					if (len < col[i].length()) {
						col[i] = col[i].substring(len);
			        }
			        else {
			        	col[i] = "";
			        }
					
					if (col[i].length() > 0) flag = true;
				}
				System.out.println();
			}
			
		}
		if (row == 0) System.out.println("Empty set.");
		else System.out.println(line);
		System.out.println(" ");
		rs.close();
		return row;
	}
	
	/**get the result of the sql query result
	 * @param sql the query
	 * @param result type string; the result of the query
	 * @return the number of rows of the query result*/
	public static int getQueryResultString(String sql, String result) throws SQLException{
		
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		int len = max_len/numCols - 1;
		len = Math.min(max_len_per_col, len);
		int tot_len = (len+1) * numCols+1;
		String line = "";
		for(int i = 0; i < tot_len; ++i){
			line += '-';
		}
		result += line;
		result += "<BR>\n";
		
		String[] col = new String[numCols+1]; // 1 - numCols
		
		int row = 0;
		while (rs.next()) {
			if(row++ == 0){
				for (int i = 1; i <= numCols; ++i){
					col[i] = rsmd.getColumnName(i);
				}
				
				boolean flag1 = true;
				
				while(flag1){
					flag1 = false;
					result += ("|");
					for (int i = 1; i <= numCols; ++i){
						result += (fillWithChar(col[i], len, ' '));
						if (len < col[i].length()) {
							col[i] = col[i].substring(len);
				        }
				        else {
				        	col[i] = "";
				        }
						result += ("|");
						
						if (col[i].length() > 0) flag1 = true;
					}
					result += "<BR>\n";
				}
				result += line;
				result += "<BR>\n";
			}
			
			boolean flag = true;
			for (int i = 1; i <= numCols; ++i){
				col[i] = rs.getString(i);
			}
			while (flag) {
				flag = false;
				result += ("|");
				for (int i = 1; i <= numCols; ++i){
					result += (fillWithChar(col[i], len, ' '));
					result += ("|");
					if (len < col[i].length()) {
						col[i] = col[i].substring(len);
			        }
			        else {
			        	col[i] = "";
			        }
					
					if (col[i].length() > 0) flag = true;
				}
				result += "<BR>\n";
			}
			
		}
		if (row == 0) result += ("Empty set.");
		else result += (line + "<BR>\n");
		
		result += "<BR>\n";
		rs.close();
		return row;
	}
	
	
	//TODO not modified
	
	/**get the result of the sql query result
	 * @param sql the query
	 * @param result type string; the result of the query
	 * @param m the first m'th rows of the query
	 * @return the number of rows of the query result*/
	public static int getQueryResultString(String sql, int m) throws SQLException{
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		int len = max_len/numCols - 1;
		len = Math.min(max_len_per_col, len);
		int tot_len = (len+1) * numCols+1;
		String line = "";
		for(int i = 0; i < tot_len; ++i){
			line += '-';
		}
		System.out.println(line);
		String[] col = new String[numCols+1]; // 1 - numCols
		
		int row;
		for (row = 0; row < m && rs.next(); ++row) {
			
			if(row == 0){
				for (int i = 1; i <= numCols; ++i){
					col[i] = rsmd.getColumnName(i);
				}
				
				boolean flag1 = true;
				
				while(flag1){
					flag1 = false;
					System.out.print("|");
					for (int i = 1; i <= numCols; ++i){
						System.out.print(fillWithChar(col[i], len, ' '));
						if (len < col[i].length()) {
							col[i] = col[i].substring(len);
				        }
				        else {
				        	col[i] = "";
				        }
						System.out.print("|");
						
						if (col[i].length() > 0) flag1 = true;
					}
					System.out.println();
				}
				System.out.println(line);
			}
			
			boolean flag = true;
			for (int i = 1; i <= numCols; ++i){
				col[i] = rs.getString(i);
			}
			while (flag) {
				flag = false;
				System.out.print("|");
				for (int i = 1; i <= numCols; ++i){
					System.out.print(fillWithChar(col[i], len, ' '));
					System.out.print("|");
					if (len < col[i].length()) {
						col[i] = col[i].substring(len);
			        }
			        else {
			        	col[i] = "";
			        }
					
					if (col[i].length() > 0) flag = true;
				}
				System.out.println();
			}
			
		}
		if (row == 0) System.out.println("Empty set.");
		else System.out.println(line);
		System.out.println(" ");
		rs.close();
		return row;
	}
	
	public static String fillWithChar(String str, int length, char c) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = c;
        }
        int len = Math.min(length, str.length());
        for (int i = 0; i < len; i++) {
            chars[i] = str.charAt(i);
        }
        
        return new String(chars);
    }
}
