package acmdb;

public class Driver {
	
	public static final String[] tableNames= { "user","user_trust", "author", "publisher", "book", "writes", "orders", "opinion", "feedback"};
	
	public static void createTables() {
		String sql = "CREATE TABLE IF NOT EXISTS user ( "
					+"u_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,"
					+"name CHAR(30), "
					+"login_name CHAR(30) UNIQUE, "
					+"password CHAR(30) NOT NULL, "
					+"address CHAR(100), "
					+"phone_num CHAR(20) "
					+");";
		Tables.create(tableNames[0], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS user_trust ( "
			+ "u_id1 INTEGER, "
			+ "u_id2 INTEGER, "
			+ "is_trust TINYINT(1), "
			+ "PRIMARY KEY (u_id1, u_id2), "
			+ "FOREIGN KEY (u_id1) REFERENCES user(u_id), "
			+ "FOREIGN KEY (u_id2) REFERENCES user(u_id)"
			+ ");";
		Tables.create(tableNames[1], sql);
		
		/*
		sql = "CREATE TABLE IF NOT EXISTS author ( "
			+ "author_id CHAR(30) NOT NULL PRIMARY KEY, "
			+ "name CHAR(30) UNIQUE"
			+ ");";
		Tables.create(tableNames[2], sql);

		sql = "CREATE TABLE IF NOT EXISTS publisher ( "
			+ "publisher_id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY, "
			+ "name CHAR(30) "
			+ ");";
		Tables.create(tableNames[3], sql);
		*/
		
		sql = "CREATE TABLE IF NOT EXISTS book ( "
			+ "isbn CHAR(30) PRIMARY KEY, "
			+ "title CHAR(30), "
			+ "year_of_publication INTEGER, "
			+ "copy_num INTEGER, "
			+ "price REAL, "
			+ "format CHAR(30), "
			+ "subject CHAR(100), "
			+ "keywords CHAR(100), "
			+ "publisher_id INTEGER "
			+ "); ";
		Tables.create(tableNames[4], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS writes ( "
			+ "isbn CHAR(30), "
			+ "author_id INTEGER, "
			+ "PRIMARY KEY (isbn, author_id), "
			+ "FOREIGN KEY (isbn) REFERENCES book(isbn) "
			+ ");";
		Tables.create(tableNames[5], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS orders ( "
			+ "time TIMESTAMP, "
			+ "copy_num INTEGER, "
			+ "u_id INTEGER, "
			+ "isbn CHAR(30), "
			+ "PRIMARY KEY (time, u_id, isbn), "
			+ "FOREIGN KEY (u_id) REFERENCES user (u_id), "
			+ "FOREIGN KEY (isbn) REFERENCES book (isbn)"
			+ ");";
		Tables.create(tableNames[6], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS opinion ( "
			+ "time TIMESTAMP, "
			+ "short_text VARCHAR(200), "
			+ "score INTEGER, "
			+ "u_id INTEGER NOT NULL, "
			+ "isbn CHAR(30) NOT NULL, "
			+ "PRIMARY KEY (u_id, isbn),"
			+ "FOREIGN KEY (u_id) REFERENCES user(u_id), "
			+ "FOREIGN KEY (isbn) REFERENCES book(isbn)"
			+ ");";
		Tables.create(tableNames[7], sql);
		
		sql = "CREATE TABLE IF NOT EXISTS feedback ( "
			+ "score INTEGER, "
			+ "u_id INTEGER, "
			+ "isbn CHAR(30),"
			+ "u_id2 INTEGER, "
			+ "PRIMARY KEY (u_id, isbn, u_id2), "
			+ "FOREIGN KEY (u_id) REFERENCES user(u_id), "
			+ "FOREIGN KEY (isbn) REFERENCES book(isbn),"
			+ "FOREIGN KEY (u_id2) REFERENCES user(u_id) "
			+ ");";
		Tables.create(tableNames[8], sql);
	}
	
	public static void clearTables(){
		int len = tableNames.length;
		for (int i = len-1; i >= 0; --i){
			Tables.clear(tableNames[i]);
		}
	}
	
	public static void main(String[] args) {
		System.out.println("Welcome to our Bookstore");
		Connector con = null;
		try {
			con = new Connector();
			System.out.println("Database connection established");
			
			Tables.setConfiguration(con.stmt);
			Book.setConfiguration(con.stmt);
			BookStore.setConfiguration(con.stmt);
			Order.setConfiguration(con.stmt);
			User.setConfiguration(con.stmt);
			UserInterface.setConfiguration(con.stmt);
			
			createTables();
			if (!User.exists("admin"))
				User.newUser("admin", "admin", "admin", "", "");
			
			UserInterface.run();
		} catch (Exception e) {
			if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException) {
				System.out.println("You have an error in your SQL syntax");
			} else {
				e.printStackTrace();
				System.err.println("Cannot connect to database server");
			}
		} finally {
			if (con != null) {
				try {
					con.closeConnection();
					System.out.println("Database connection terminated");
				} catch (Exception e) {
					/* No action for close errors */
				}
			}
		}
	}	
}
