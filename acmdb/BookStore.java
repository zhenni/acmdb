package acmdb;

import java.sql.*;

public class BookStore {
	
	public static Statement stmt;

	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static void initialize(Statement stmt) throws SQLException {
		setConfiguration(stmt);
	}
	
	private static final int ADMIN = 1;
	private static final int USER = 2;
	
	public static int authority = 0;
	
	public static boolean isManager(int authority) {
		if (authority == ADMIN) return true;
		return false;
	}
	
	public static boolean login(String loginName, String password) throws SQLException {
		String sql = null;
		ResultSet res = null;
		try {
			sql = "SELECT * FROM user U "
				+ "WHERE (U.login_name = '" + loginName + "' AND U.password = '" + password + "')";
			res = stmt.executeQuery(sql);
			if (res.next()) {
				if (loginName.equals("admin")) authority = ADMIN; else authority = USER;
				
				User.u_id = res.getInt(1);
				User.name = res.getString(2);
				User.login_name = res.getString(3);
				User.password = res.getString(4);
				User.address = res.getString(5);
				User.phone_num = res.getString(6);
				
				res.close();
				return true;
			} else {
				res.close();
				return false;
			}
		} catch (Exception e) {
			if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
				System.out.println("You have an error in your SQL syntax");
				e.printStackTrace();
			} else {
				System.out.println("No such login name or password incorrect");
			}
			res.close();
			return false;
		}
	}
	
	public static void logout() {
		authority = 0;
		
		User.u_id = -1;
		User.name = null;
		User.login_name = null;
		User.password = null;
		User.address = null;
		User.phone_num = null;
	}

	public static void displayStatistics(int m, java.sql.Timestamp time1, java.sql.Timestamp time2) throws SQLException {
		// the list of the m most popular books(in terms of copies sold in this semester)
		System.out.println("The list of the m most popular books:");
		
		String sql = "SELECT isbn, SUM(copy_num) AS S "
					+"FROM orders O "
					+"WHERE O.time >= \'" + time1 +"\' AND O.time <= \'" + time2 + "\' "
					+"GROUP BY isbn "
					+"ORDER BY S DESC";
		
		printQueryResult(sql, m);
		
		// the list of m most popular authors
		System.out.println("The list of m most popular authors:");
		
		sql = "SELECT W.author_id, SUM(O.copy_num) AS S "
			+ "FROM orders O, writes W "
			+ "WHERE O.time >= \'" + time1 + "\' AND O.time <= \'" + time2 + "\' AND "
			+ "		O.isbn = W.isbn "
			+ "GROUP BY W.author_id "
			+ "ORDER BY S DESC";
		
		printQueryResult(sql, m);
		
		// the list of m most popular publishers
		System.out.println("The list of m most popular publishers:");
		
		sql = "SELECT B.publisher_id, SUM(O.copy_num) AS S "
			+ "FROM orders O, book B "
			+ "WHERE O.time >= \'" + time1 + "\' AND O.time <= \'" + time2 + "\' AND "
			+ "		O.isbn = B.isbn "
			+ "GROUP BY B.publisher_id "
			+ "ORDER BY S DESC";
		
		printQueryResult(sql, m);
	}

	public static void displayAwardedUsers(int m) throws SQLException {
		// the top m most 'trusted' users(the trust score of a user is the count of 
		// users 'trusting' him/her, minus the count of users 'not-trusting' him/her)
		System.out.println("The top " + m + " most 'trusted' users:");
		
		String sql = "SELECT login_name, S1.num - S2.num "
					+"FROM user U, "
					+"(SELECT COUNT(*) AS num "
					+" FROM user_trust UT "
					+" WHERE UT.u_id2 = U.u_id AND UT.is_trust = 1) AS S1, "
					+"(SELECT COUNT(*) AS num "
					+" FROM user_trust UT "
					+" WHERE UT.u_id2 = U.u_id AND UT.is_trust = 0) AS S2 "
					+"GROUP BY U.login_name "
					+"ORDER BY S1.num - S2.num";
		
		printQueryResult(sql, m);
		
		// the top m most 'useful' users(the userfulness score of a user is the average
		// 'usefulness' of all of his/her feedbacks combined)
		System.out.println("The top " + m + " most 'useful' users:");
		
		sql = "SELECT login_name, AVG(F.score) "
			+ "FROM opinion O, feedback F "
			+ "WHERE O.isbn = F.isbn AND O.u_id = F.u_id2 "
			+ "GROUP BY login_name "
			+ "ORDER BY AVG(F.score) DESC";
		
		printQueryResult(sql, m);
	}
	
	public static void printQueryResult(String sql, int m) throws SQLException {
		System.err.println("DEBUG CHECK : "+ sql);
		
		ResultSet res = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = res.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		for (int i = 1; i <= numCols; ++i)
			System.out.print(rsmd.getColumnName(i) + "  ");
		System.out.println();
		for (int row = 1; res.next() && row <= m; ++row) {
			for (int i = 1; i <= numCols; ++i)
				System.out.print(res.getString(i) + "  ");
			System.out.println("");
		}
		System.out.println(" ");
		
		res.close();
	}
}
