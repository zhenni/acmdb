package bookstore;

import java.sql.*;
import java.util.*;

public class Book {
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static int newBook(String isbn, String title, int year, int copy_num, String price, String format, String subject,
			String keywords, String publisher_id, HashSet<String> authors) throws SQLException{
		int res;
		String sql;
		
		sql = "INSERT INTO book(isbn, title, year_of_publication, copy_num, price, format, subject, keywords, publisher_id) VALUES (\'"
				+ isbn + "\', \'"
				+ title + "\', \'"
				+ year + "\', \'"
				+ copy_num + "\', \'"
				+ price + "\', \'"
				+ format + "\', \'"
				+ subject + "\', \'"
				+ keywords + "\', \'"
				+ publisher_id + "\')"
				;
		res = executeUpdate(sql);
		if(res == -1) return -1;
		
		for (Iterator<String> it = authors.iterator(); it.hasNext();){
			sql = "INSERT writes(isbn, author_id) VALUES (\'"+isbn+"\', \'"+it.next()+"\')";
			res = executeUpdate(sql);
			if (res == -1) return -1;
		}
		return res;
	}
	
	

	/**<strong>Book Browsing:</strong>
	 * <p>Users may search for books, by asking conjunctive queries on the authors, 
	 * and/or publisher, and/or title-words, and/or subject.
	 * Your system should allow the user to specify that the results are to be sorted
	 * <li>(a) by year,</li> 
	 * <li>(b) by the average numerical score of the feedbacks</li>
	 * <li>(c) by the average numerical score of the trusted user feedbacks.</li>
	 * @throws SQLException */
	public static int find(int u_id, String author, String publisher, String title, String subject, int order) throws SQLException {		
		if (author == null) author = "";
		if (publisher == null) publisher = "";
		if (title == null) title = "";
		if (subject == null) subject = "";
		
		String sql = "";
		
		//by year
		if (order == 1){
			sql += ("SELECT DISTINCT B.* " 
					+ "FROM book B, writes W "
					+ "WHERE "
					+ "B.isbn = W.isbn AND "
					+ "W.author_id like \'%" + author + "%\' AND " 
					+ "B.publisher_id like \'%" + publisher + "%\' AND "
					+ "B.title like \'%" + title + "%\' AND "
					+ "B.subject like \'%" + subject + "%\' ")
					;
			sql +=  ("ORDER BY B.year_of_publication");
		}
		//(b) by the average numerical score of the feedbacks
		else if (order == 2){
			sql += ("SELECT DISTINCT B.* " 
					+ "FROM book B, writes W, opinion O "
					+ "WHERE "
					+ "B.isbn = W.isbn AND "
					+ "W.author_id like \'%" + author + "%\' AND " 
					+ "B.publisher_id like \'%" + publisher + "%\' AND "
					+ "B.title like \'%" + title + "%\' AND "
					+ "B.subject like \'%" + subject + "%\' AND "
					+ "O.isbn = B.isbn "
					+ "GROUP BY B.isbn "
					+ "ORDER BY AVG(O.score) DESC"
					);
		}
		//(c) by the average numerical score of the trusted user feedbacks
		else if(order == 3){
			sql += ("SELECT B.* " 
					+ "FROM book B, writes W, opinion O, user_trust T "
					+ "WHERE "
					+ "B.isbn = W.isbn AND "
					+ "W.author_id like \'%" + author + "%\' AND " 
					+ "B.publisher_id like \'%" + publisher + "%\' AND "
					+ "B.title like \'%" + title + "%\' AND "
					+ "B.subject like \'%" + subject + "%\' AND "
					
					+ "O.isbn = B.isbn AND "
					+ "T.u_id1 = \'" + u_id + "\' AND "
					+ "T.u_id2 = O.u_id AND "
					+ "T.is_trust = \'1\' "
					+ "GROUP BY B.isbn "
					+ "ORDER BY AVG(O.score) DESC")
					;
		}else{
			System.out.println("Please choose the order from 1 to 3");
		}
		
		int res = 0;
		
		PrintResult.printQueryResult(sql);
		
		return res;
	}
	
	
	public static String findHTML(int u_id, String author, String publisher, String title, String subject, int order) throws SQLException {		
		if (author == null) author = "";
		if (publisher == null) publisher = "";
		if (title == null) title = "";
		if (subject == null) subject = "";
		
		String sql = "";
		
		//by year
		if (order == 1){
			sql += ("SELECT DISTINCT B.* " 
					+ "FROM book B, writes W "
					+ "WHERE "
					+ "B.isbn = W.isbn AND "
					+ "W.author_id like \'%" + author + "%\' AND " 
					+ "B.publisher_id like \'%" + publisher + "%\' AND "
					+ "B.title like \'%" + title + "%\' AND "
					+ "B.subject like \'%" + subject + "%\' ")
					;
			sql +=  ("ORDER BY B.year_of_publication");
		}
		//(b) by the average numerical score of the feedbacks
		else if (order == 2){
			sql += ("SELECT DISTINCT B.* " 
					+ "FROM book B, writes W, opinion O "
					+ "WHERE "
					+ "B.isbn = W.isbn AND "
					+ "W.author_id like \'%" + author + "%\' AND " 
					+ "B.publisher_id like \'%" + publisher + "%\' AND "
					+ "B.title like \'%" + title + "%\' AND "
					+ "B.subject like \'%" + subject + "%\' AND "
					+ "O.isbn = B.isbn "
					+ "GROUP BY B.isbn "
					+ "ORDER BY AVG(O.score) DESC"
					);
		}
		//(c) by the average numerical score of the trusted user feedbacks
		else if(order == 3){
			sql += ("SELECT B.* " 
					+ "FROM book B, writes W, opinion O, user_trust T "
					+ "WHERE "
					+ "B.isbn = W.isbn AND "
					+ "W.author_id like \'%" + author + "%\' AND " 
					+ "B.publisher_id like \'%" + publisher + "%\' AND "
					+ "B.title like \'%" + title + "%\' AND "
					+ "B.subject like \'%" + subject + "%\' AND "
					
					+ "O.isbn = B.isbn AND "
					+ "T.u_id1 = \'" + u_id + "\' AND "
					+ "T.u_id2 = O.u_id AND "
					+ "T.is_trust = \'1\' "
					+ "GROUP BY B.isbn "
					+ "ORDER BY AVG(O.score) DESC")
					;
		}else{
			System.out.println("Please choose the order from 1 to 3");
		}
		
		
		StringBuilder res = new StringBuilder("");
		PrintResult.getQueryResultHTML(sql, res);
		
		return res.toString();
	}
	
	/**<strong>Useful feedbacks:</strong>
	 * <p> For a given book, 
	 * a user could ask for the top n most `useful' feedbacks.
	 * The value of n is user-specified (say, 5, or 10).
	 * The `usefulness' of a feedback is its average `usefulness' score.
	 * */
	public static int displayUsefulFeedback(String isbn, int n) {
		int res = -1;
		
		String sql = "SELECT O.*, AVG(F.score) "
				+ "FROM opinion O, feedback F "
				+ "WHERE O.isbn = F.isbn AND O.u_id = F.u_id2 AND O.isbn = \'" + isbn + "\' "
				+ "GROUP BY O.u_id, O.isbn "
				+ "ORDER BY AVG(F.score) DESC";
		
		try {
			PrintResult.printQueryResult(sql, n);
			res = 1;
		} catch (Exception e) {
			System.err.println(e);
			res = -1;
		}
		
		return res;
	}
	
	/**<strong>Useful feedbacks:</strong>
	 * <p> For a given book, 
	 * a user could ask for the top n most `useful' feedbacks.
	 * The value of n is user-specified (say, 5, or 10).
	 * The `usefulness' of a feedback is its average `usefulness' score.
	 * */
	public static String displayUsefulFeedbackHTML(String isbn, int n) {
		StringBuilder res = new StringBuilder();
		
		String sql = "SELECT O.*, AVG(F.score) "
				+ "FROM opinion O, feedback F "
				+ "WHERE O.isbn = F.isbn AND O.u_id = F.u_id2 AND O.isbn = \'" + isbn + "\' "
				+ "GROUP BY O.u_id, O.isbn "
				+ "ORDER BY AVG(F.score) DESC";
		
		try {
			res.append(PrintResult.getQueryResultHTML(sql, n, res));
		} catch (Exception e) {
			System.err.println(e);
			res.append("<p>something wrong.</p>");
		}
		
		return res.toString();
	}
	
	/**<strong>Arrival of more copies:</strong>
	 * <p>The store manager increases the appropriate counts.</p>*/
	public static int addCopies(String isbn, int copy_num) throws SQLException{
		String sql = "UPDATE book SET copy_num = copy_num +\'" + copy_num + "\'";
		int res = executeUpdate(sql);
		return res;
	}
	
	/**<strong>Feedback recordings: </strong>
	 * <p>Users can record their feedback for a book. 
	 * We should record the date, the numerical score (0= terrible, 10= masterpiece), and an optional short text.
	 * No changes are allowed; only one feedback per user per book is allowed.</p>*/
	public static int giveFeedback(String isbn, int u_id, int score, String short_text, java.sql.Timestamp time){
		
		String sql = "INSERT INTO opinion(isbn, u_id, score, short_text, time) VALUES (\'" 
			+ isbn + "\', \'"
			+ u_id + "\', \'"
			+ score + "\', \'"
			+ short_text + "\', \'"
			+ time + "\')";
		
		int res;
		try {
			res = executeUpdate(sql);
			return res;

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return -1;
		}
	}

	/**<p>The user can only give one book one feedback once</p>*/
	public static boolean haveGivenFeedback(String isbn, int u_id) throws Exception{
		String sql = "SELECT COUNT(*) FROM opinion WHERE isbn = \'"+isbn+"\' AND u_id = \'"+ u_id + "\'";
		int num = Integer.parseInt(getQueryWithOneResult(sql));
		if(num == 0) return false;
		return true;
	}

	public static int showFeedbacks(int u_id, String isbn) throws Exception{
		String sql = "SELECT U.login_name, O.score, O.short_text "
				+ "FROM opinion O, user U "
				+ "WHERE O.isbn = \'" + isbn + "\' AND "
						+ "O.u_id = U.u_id AND "
						+ "O.u_id <> \'"+u_id + "\'";
		int row = PrintResult.printQueryResult(sql);
		if (row == 0) return -1;
		return 0;
	}
	
	/**<strong>Usefulness ratings:</strong> 
	 * <p>Users can assess a feedback record, giving it a numerical score 0, 1, or 2
	 * ('useless', 'useful', 'very useful' respectively).
	 * A user should not be allowed to provide a usefulness-rating for his/her own feedbacks.</p>*/
	public static int usefulnessRating(int u_id, String isbn, int u_id2, int score) throws SQLException{
		if (u_id == u_id2){
			System.out.println("You can not provide a usefulness-rating for your own feedbacks.");
			return -1;
		}
		String sql = "INSERT INTO feedback(u_id, isbn, u_id2, score) VALUES (\'"
			+ u_id + "\', \'"
			+ isbn + "\', \'"
			+ u_id2 + "\', \'"
			+ score + "\')";
		return executeUpdate(sql);
	}

	/**<strong>Two degrees of separation': </strong>
	 * <p> Given two author names, determine their `degree of separation', 
	 * defined as follows: Two authors `A' and `B' are 1-degree away 
	 * if they have co-authored at least one book together; 
	 * they are 2-degrees away if there exists an author `C' who is 1-degree away from each of `A' and `B', 
	 * AND `A' and `B' are not 1-degree away at the same time.</p>
	 * @throws SQLException */
	/*
	public static void giveSeparationDegree(String author1, String author2) throws Exception {
		String sql;
		
		sql = "SELECT COUNT(*) FROM writes WHERE author_id = \'" + author1 + "\'";
		if (Integer.parseInt(getQueryWithOneResult(sql)) < 1){
			System.out.println("The author1 you typed does not exist.");
			return;
		}
		
		sql = "SELECT COUNT(*) FROM writes WHERE author_id = \'" + author2 + "\'";
		if (Integer.parseInt(getQueryWithOneResult(sql)) < 1){
			System.out.println("The author2 you typed does not exist.");
			return;
		}
		
		if (author1.equals(author2)){
			System.out.println("The 2 names of author are the same.");
			return;
		}
		
		sql = "CREATE OR REPLACE VIEW degree AS "
				+ "SELECT W1.author_id AS a1, W2.author_id AS a2, W1.isbn AS sisbn "
				+ "FROM writes W1, writes W2 "
				+ "WHERE "
				+ "W1.isbn = W2.isbn AND "
				+ "W1.author_id <>  W2.author_id";
		if (executeUpdate(sql) == -1){
			System.err.println("failed to create view");
			return;
		}
		
		sql = "SELECT COUNT(*) FROM degree "
				+ "WHERE "
				+ "a1 = \'" + author1 + "\' AND "
				+ "a2 = \'" + author2 + "\'";
		int cnt = Integer.parseInt(getQueryWithOneResult(sql));
		if(cnt > 0){
			System.out.println("Authors " + author1 + " and " + author2 + " are 1-degree away");
			return;
		}
	
		sql =  "SELECT COUNT(*) "
			+ "FROM degree D1, degree D2 "
			+ "WHERE D1.a2 = D2.a1 AND "
			+ "D1.a1 = \'" + author1 + "\' AND "
			+ "D2.a2 = \'" + author2 + "\'";
		
		cnt = Integer.parseInt(getQueryWithOneResult(sql));
		if(cnt > 0){
			System.out.println("Authors " + author1 + " and " + author2 + " are 2-degree away");
			return;
		}
		
		System.out.println("Authors " + author1 + " and " + author2 + " are neither 1-degree nor 2-degree away");
	}
	*/

	/**<strong>Two degrees of separation': </strong>
	 * <p> Given two author names, determine their `degree of separation', 
	 * defined as follows: Two authors `A' and `B' are 1-degree away 
	 * if they have co-authored at least one book together; 
	 * they are 2-degrees away if there exists an author `C' who is 1-degree away from each of `A' and `B', 
	 * AND `A' and `B' are not 1-degree away at the same time.</p>
	 * @throws SQLException */
	public static String giveSeparationDegree(String author1, String author2) throws Exception {
		String sql;
		String res = "";
		
		sql = "SELECT COUNT(*) FROM writes WHERE author_id = \'" + author1 + "\'";
		if (Integer.parseInt(getQueryWithOneResult(sql)) < 1){
			res += "The name of the fisrt author you typed does not exist.";
			return res;
		}
		
		sql = "SELECT COUNT(*) FROM writes WHERE author_id = \'" + author2 + "\'";
		if (Integer.parseInt(getQueryWithOneResult(sql)) < 1){
			res += "The name of the second author you typed does not exist.";
			return res;
		}
		
		if (author1.equals(author2)){
			res += "The two names of author are the same.";
			return res;
		}
		
		sql = "CREATE OR REPLACE VIEW degree AS "
				+ "SELECT W1.author_id AS a1, W2.author_id AS a2, W1.isbn AS sisbn "
				+ "FROM writes W1, writes W2 "
				+ "WHERE "
				+ "W1.isbn = W2.isbn AND "
				+ "W1.author_id <>  W2.author_id";
		if (executeUpdate(sql) == -1){
			System.err.println("failed to create view");
			return res;
		}
		
		sql = "SELECT COUNT(*) FROM degree "
				+ "WHERE "
				+ "a1 = \'" + author1 + "\' AND "
				+ "a2 = \'" + author2 + "\'";
		int cnt = Integer.parseInt(getQueryWithOneResult(sql));
		if(cnt > 0){
			res += "Authors " + author1 + " and " + author2 + " are 1-degree away.";
			return res;
		}
	
		sql =  "SELECT COUNT(*) "
			+ "FROM degree D1, degree D2 "
			+ "WHERE D1.a2 = D2.a1 AND "
			+ "D1.a1 = \'" + author1 + "\' AND "
			+ "D2.a2 = \'" + author2 + "\'";
		
		cnt = Integer.parseInt(getQueryWithOneResult(sql));
		if(cnt > 0){
			res += "Authors " + author1 + " and " + author2 + " are 2-degree away.";
			return res ;
		}
		
		res += "Authors " + author1 + " and " + author2 + " are neither 1-degree nor 2-degree away.";
		return res;
	}
	/**<strong>Buying suggestions:</strong> 
	 * Like most e-commerce websites, when a user orders a copy of book `A', 
	 * your system should give a list of other suggested books.
	 * Book `B' is suggested, if there exist a user `X' that bought both `A' and `B'.
	 * The suggested books should be sorted on decreasing sales count
	 * (i.e., most popular first); count only sales to users like `X'.
	 * @throws SQLException */
	public static void giveSuggestBooks(int u_id) throws SQLException {
		String sql = "SELECT B2.* "
				+ "FROM book B1, book B2, orders O "
				+ "WHERE (EXISTS (SELECT * FROM orders O2 WHERE O2.u_id = \'" + u_id + "\' AND O2.isbn = B1.isbn)) AND "
				+ "		(EXISTS (SELECT * FROM orders O3 WHERE O3.u_id = O.u_id AND O3.isbn = B1.isbn)) AND "
				+ "		O.isbn = B2.isbn "
				+ "GROUP BY B2.isbn "
				+ "ORDER BY SUM(O.copy_num) DESC";
		PrintResult.printQueryResult(sql);
	}

	/**<strong>Buying suggestions:</strong> 
	 * Like most e-commerce websites, when a user orders a copy of book `A', 
	 * your system should give a list of other suggested books.
	 * Book `B' is suggested, if there exist a user `X' that bought both `A' and `B'.
	 * The suggested books should be sorted on decreasing sales count
	 * (i.e., most popular first); count only sales to users like `X'.
	 * @throws SQLException */
	public static String giveSuggestBooksHTML(int u_id) throws SQLException {
		String sql = "SELECT B2.* "
				+ "FROM book B1, book B2, orders O "
				+ "WHERE (EXISTS (SELECT * FROM orders O2 WHERE O2.u_id = \'" + u_id + "\' AND O2.isbn = B1.isbn)) AND "
				+ "		(EXISTS (SELECT * FROM orders O3 WHERE O3.u_id = O.u_id AND O3.isbn = B1.isbn)) AND "
				+ "		O.isbn = B2.isbn "
				+ "GROUP BY B2.isbn "
				+ "ORDER BY SUM(O.copy_num) DESC";
		StringBuilder res = new StringBuilder();
		PrintResult.getQueryResultHTML(sql, res);
		return res.toString();
	}
	
	/*
	public static int printQueryResult(String sql) throws SQLException{
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
				
		int row = 0;
		while (rs.next()) {
			if(row++ == 0){
				for (int i = 1; i <= numCols; ++i)
					System.out.print(rsmd.getColumnName(i) + "  ");
				System.out.println();
			
			}
			for (int i = 1; i <= numCols; ++i)
				System.out.print(rs.getString(i) + "  ");
			System.out.println("");
		}
		if (row == 0) System.out.println("Empty set.");
		System.out.println(" ");
		rs.close();
		return row;
	}
	
	public static int printQueryResult(String sql, int m) throws SQLException{
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
				
		int row;
		for (row = 1; row <= m && rs.next(); ++row) {
			if(row == 1){
				for (int i = 1; i <= numCols; ++i)
					System.out.print(rsmd.getColumnName(i) + "  ");
				System.out.println();
			}
			
			for (int i = 1; i <= numCols; ++i)
				System.out.print(rs.getString(i) + "  ");
			System.out.println("");
		}
		if (row == 1) System.out.println("Empty set.");
		System.out.println(" ");
		rs.close();
		return row;
	}
	*/

	public static String getQueryWithOneResult(String sql) throws SQLException {
		System.err.println("DEBUG CHECK : "+ sql);
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCols = rsmd.getColumnCount();
		
		if (numCols > 1){
			System.err.println("not only one result col");
		}
		rs.next();
		
		String res = rs.getString(1);
		
		if(rs.next()){
			System.err.println("not only one result");
		}
		rs.close();
		return res;
	}
	
	public static int executeUpdate(String sql) throws SQLException{
		System.err.println("DEBUG CHECK : " + sql);
		return stmt.executeUpdate(sql);
	}

}
