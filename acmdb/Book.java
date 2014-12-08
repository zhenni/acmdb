package acmdb;

import java.sql.*;

public class Book {
	private static Statement stmt;
	
	public Book(Statement _stmt) throws SQLException {
		stmt = _stmt;
	}
	
	public static int newBook(String isbn, String title, String year, String copy_num, String price, String format, String subject,
			String keywords, String publisher_id, int n, String[] authors) throws SQLException{
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
			sql = "INSERT writes(isbn, author_id) VALUES (\'"+isbn+"\', \'"+authors[i]+"\')";
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
	public static int find(String author, String publisher, String title, String subject, int order) throws SQLException {		
		if (author == null) author = "";
		if (publisher == null) publisher = "";
		if (title == null) title = "";
		if (subject == null) subject = "";
		
		String sql = "SELECT DISTINCT B.* " 
				+ "FROM book B, author A, writes W "
				+ "WHERE "
				+ "B.isbn = W.isbn AND "
				+ "A.author_id = W.author_id AND "
				+ "A.name like \'%" + author + "%\' AND " 
				+ "B.publisher like \'%" + publisher + "%\' AND "
				+ "B.title like \'%" + title + "%\' AND "
				+ "B.subject like \'%" + subject + "%\' "
				;
		
		//by year
		if (order == 1){
			sql +=  ("ORDER BY B.year_of_publication");
		}
		//(b) by the average numerical score of the feedbacks
		else if (order == 2){
			//FIXME
			sql += ("ORDER BY ("
				+ "SELECT AVG(F.score) "
				+ "FROM feedback F, opinion O "
				+ "WHERE F.u_id = O.u_id AND F.isbn = O.isbn"
				+ "GROUP BY B.isbn)");
		}else if(order == 3){
			//TODO
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
	
	
	public static int addCopies(String isbn, int num_copy) throws SQLException{
		String sql = "UPDATE book SET num_copy = numCopy + " + num_copy;
		int res = executeUpdate(sql);
		return res;
	}

	public static int giveFeedback(String isbn, String u_id, int score, String comment, String date) throws SQLException{
		String sql = "INSERT INTO opinion(isbn, u_id, score, comment, date) VALUES (\'" 
			+ isbn + "\', \'"
			+ u_id + "\', \'"
			+ score + "\', \'"
			+ comment + "\', \'"
			+ date + "\')";
		return executeUpdate(sql);
	}

	public static boolean haveGivenFeedback(String isbn, String u_id) throws Exception{
		String sql = "SELECT COUNT(*) FROM opinion WHERE isbn = \'"+isbn+"\' AND u_id = \'"+ u_id + "\'";
		int num = Integer.parseInt(getQueryWithOneResult(sql));
		if(num == 0) return false;
		return true;
	}

	public static int usefulnessRating(String u_id, String isbn, String u_id2, int score) throws SQLException{
		String sql = "INSERT INTO feedback(u_id, isbn, u_id2, score) VALUES (\'"
			+ u_id + "\', \'"
			+ isbn + "\', \'"
			+ u_id2 + "\', \'"
			+ score + "\')";
		return executeUpdate(sql);
	}

	public static int setTrustOrNot(String u_id1, String u_id2, int trust) throws SQLException{
		String sql = "INSERT INTO user_trust (u_id1, u_id2, is_trust) VALUES (\'"
			+ u_id1 + "\', \'"
			+ u_id2 + "\', \'"
			+ trust + "\')";
		return executeUpdate(sql);
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
