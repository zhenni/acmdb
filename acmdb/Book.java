package acmdb;

import java.sql.*;

public class Book {
	private static Statement stmt;
	
	public Book(Statement _stmt) throws SQLException {
		stmt = _stmt;
	}
	
	public int newBook(String isbn, String title, String year, String copy_num, String price, String format, String subject, String keywords, String publisher, String[] authors) throws SQLException{
		//TODO insert publisher
		int res;
		String sql;
		if (publisher != null){
			sql = "INSERT INTO ";
		}
		
		sql = "INSERT INTO user() VALUES (\'"
				+ isbn + "\', \'"
				+ year + "\')"
				;
		res = stmt.executeUpdate(sql);
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
			//TODO
			sql += ("");
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
	
	
	public static void printQueryResult(String sql) throws SQLException{
		System.out.println("DEBUG CHECK : "+ sql);
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
}
