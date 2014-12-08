package acmdb;

import java.sql.*;
import java.util.*;

public class Book {
	private static Statement stmt;
	
	public static void setConfiguration(Statement _stmt) {
		stmt = _stmt;
	}
	
	public static int newBook(String isbn, String title, int year, int copy_num, String price, String format, String subject,
			String keywords, String publisher_id, int n, ArrayList<String> authors) throws SQLException{
		int res;
		String sql;
		
		sql = "INSERT INTO book(isbn, title, year, copy_num, price, format, subject, keywords, publisher_id) VALUES (\'"
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
		
		for (int i = 0; i < n; ++i){
			sql = "INSERT writes(isbn, author_id) VALUES (\'"+isbn+"\', \'"+authors.get(i)+"\')";
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
		
		String sql = "SELECT DISTINCT B.* " 
				+ "FROM book B, writes W "
				+ "WHERE "
				+ "B.isbn = W.isbn AND "
				+ "W.author_id like \'%" + author + "%\' AND " 
				+ "B.publisher_id like \'%" + publisher + "%\' AND "
				+ "B.title like \'%" + title + "%\' AND "
				+ "B.subject like \'%" + subject + "%\' "
				;
		
		//by year
		if (order == 1){
			sql +=  ("ORDER BY B.year_of_publication");
		}
		//(b) by the average numerical score of the feedbacks
		else if (order == 2){
			sql += ("ORDER BY ("
				+ "SELECT AVG(O.score) "
				+ "FROM opinion O "
				+ "WHERE O.isbn = B.isbn "
				+ "GROUP BY B.isbn)");
		}
		//(c) by the average numerical score of the trusted user feedbacks
		else if(order == 3){
			sql += ("ORDER BY("
					+ "SELECT AVG(O.score) "
					+ "FROM opinion O, user_trust T "
					+ "WHERE "
					+ "O.isbn = B.isbn AND "
					+ "T.u_id1 = \'" + u_id + "\' AND "
					+ "T.u_id2 = O.u_id AND "
					+ "T.is_trust = \'1\'"
					+ "GROUP BY B.isbn)");
		}else{
			System.out.println("Please choose the order from 1 to 3");
		}
		
		int res = 0;
		
		printQueryResult(sql);
		
		return res;
	}
	
	/**<strong>Useful feedbacks:</strong>
	 * <p> For a given book, 
	 * a user could ask for the top n most `useful' feedbacks.
	 * The value of n is user-specified (say, 5, or 10).
	 * The `usefulness' of a feedback is its average `usefulness' score.
	 * */
	public static int displayUsefulFeedback(String isbn, int n) {
		int res = -1;
		return res;
	}
	
	/**<strong>Arrival of more copies:</strong>
	 * <p>The store manager increases the appropriate counts.</p>*/
	public static int addCopies(String isbn, int num_copy) throws SQLException{
		String sql = "UPDATE book SET num_copy = numCopy + " + num_copy;
		int res = executeUpdate(sql);
		return res;
	}
	
	/**<strong>Feedback recordings: </strong>
	 * <p>Users can record their feedback for a book. 
	 * We should record the date, the numerical score (0= terrible, 10= masterpiece), and an optional short text.
	 * No changes are allowed; only one feedback per user per book is allowed.</p>*/
	public static int giveFeedback(String isbn, int u_id, int score, String comment, String date) throws SQLException{
		String sql = "INSERT INTO opinion(isbn, u_id, score, comment, date) VALUES (\'" 
			+ isbn + "\', \'"
			+ u_id + "\', \'"
			+ score + "\', \'"
			+ comment + "\', \'"
			+ date + "\')";
		return executeUpdate(sql);
	}

	/**<p>The user can only give one book one feedback once</p>*/
	public static boolean haveGivenFeedback(String isbn, int u_id) throws Exception{
		String sql = "SELECT COUNT(*) FROM opinion WHERE isbn = \'"+isbn+"\' AND u_id = \'"+ u_id + "\'";
		int num = Integer.parseInt(getQueryWithOneResult(sql));
		if(num == 0) return false;
		return true;
	}

	/**<strong>Usefulness ratings:</strong> 
	 * <p>Users can assess a feedback record, giving it a numerical score 0, 1, or 2
	 * ('useless', 'useful', 'very useful' respectively).
	 * A user should not be allowed to provide a usefulness-rating for his/her own feedbacks.</p>*/
	public static int usefulnessRating(int u_id, String isbn, int u_id2, int score) throws SQLException{
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
	public static void giveSeparationDegree(String author1, String author2) throws Exception {
		// TODO Auto-generated method stub
		
		String sql = "CREATE OR REPLACE VIEW degree AS"
				+ "SELECT W1.author_id AS a1, W2.author_id AS a2, W1.isbn AS isbn "
				+ "FROM writes W1, writes W2 "
				+ "WHERE"
				+ "W1.isbn = W2.isbn AND "
				+ "a1 <> a2 ";
		if (executeUpdate(sql) == -1){
			System.err.println("failed to create view");
			return;
		}
		
		sql = "SELECT COUNT(*) FROM degree"
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
			+ "WHERE D1.isbn = D2.isbn AND "
			+ "D1.a2 = D2.a1 AND "
			+ "D1.a1 = \'" + author1 + "\' AND "
			+ "D2.a2 = \'" + author2 + "\'";
		
		cnt = Integer.parseInt(getQueryWithOneResult(sql));
		if(cnt > 0){
			System.out.println("Authors " + author1 + " and " + author2 + " are 2-degree away");
			return;
		}
		
		System.out.println("Authors " + author1 + " and " + author2 + " are neither 1-degree nor 2-degree away");
	}
	

	/**<strong>Buying suggestions:</strong> 
	 * Like most e-commerce websites, when a user orders a copy of book `A', 
	 * your system should give a list of other suggested books.
	 * Book `B' is suggested, if there exist a user `X' that bought both `A' and `B'.
	 * The suggested books should be sorted on decreasing sales count
	 * (i.e., most popular first); count only sales to users like `X'.
	 * @throws SQLException */
	public static void giveSuggestBooks(int u_id, String isbn) throws SQLException {
		String sql = "SELECT B.* "
				+ "FROM Book B, order O1, order O2 "
				+ "WHERE O1.u_id = \'" + u_id + "\' AND "
				+ "O1.isbn = O2.isbn "
				+ "GROUP BY B.isbn "
				+ "ORDER BY SUM(O2.num_copy) DESC";
		printQueryResult(sql);
	}

	
	
	public static void printQueryResult(String sql) throws SQLException{
		System.err.println("DEBUG CHECK : "+ sql);
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
