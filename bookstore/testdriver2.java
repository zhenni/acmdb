package bookstore;


//import java.lang.*;
import java.sql.*;
import java.io.*;

public class testdriver2 {

	/**
	 * @param args
	 */
	public static void displayMenu()
	{
		 System.out.println("        Video Store Management System     ");
    	 System.out.println("1. search a course by cname and dname:");
    	 System.out.println("2. enter your onw query:");
    	 System.out.println("3. enter your update operation (such like create table):");
    	 System.out.println("4. exit:");
    	 System.out.println("pleasse enter your choice:");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Example for cs5530");
		Connector con=null;
		String choice;
        String cname;
        String dname;
        String sql=null;
        int c=0;
         try
		 {
			//remember to replace the password
			 	 con= new Connector();
	             System.out.println ("Database connection established");
	         
	             BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	             
	             while(true)
	             {
	            	 displayMenu();
	            	 while ((choice = in.readLine()) == null && choice.length() == 0);
	            	 try{
	            		 c = Integer.parseInt(choice);
	            	 }catch (Exception e)
	            	 {
	            		 
	            		 continue;
	            	 }
	            	 
	            	 if (c<1 | c>4)
	            		 continue;
	            	 if (c==1)
	            	 {
	            		 System.out.println("please enter a cname:");
	            		 while ((cname = in.readLine()) == null && cname.length() == 0);
	            		 System.out.println("please enter a dname:");
	            		 while ((dname = in.readLine()) == null && dname.length() == 0);
	            		// Course course=new Course();
	            		// System.out.println(course.getCourse(cname, dname, con.stmt));
	            	 }
	            	 else if (c==2)
	            	 {	 
	            		 System.out.println("please enter your query below:");
	            		 while ((sql = in.readLine()) == null && sql.length() == 0)
	            			 System.out.println(sql);
	            		 ResultSet rs=con.stmt.executeQuery(sql);
	            		 ResultSetMetaData rsmd = rs.getMetaData();
	            		 int numCols = rsmd.getColumnCount();
	            		 while (rs.next())
	            		 {
	            			 //System.out.print("cname:");
	            			 for (int i=1; i<=numCols;i++)
	            				 System.out.print(rs.getString(i)+"  ");
	            			 System.out.println("");
	            		 }
	            		 System.out.println(" ");
	            		 rs.close();
	            	 }
	            	 else if (c==3){
	            		 System.out.println("please enter your operation below:");
	            		 while ((sql = in.readLine()) == null && sql.length() == 0)
	            			 System.out.println(sql);
	            		 	 int res =con.stmt.executeUpdate(sql);
	            		 	 if (res != -1) {
	            		 		 System.out.println("Update suscess"); 
	            		 	 }
	            	 }	            	 
	            	 else
	            	 {   
	            		 System.out.println("Remeber to pay us!");
	            		 con.stmt.close(); 
	        
	            		 break;
	            	 }
	             }
		 }
         catch (Exception e)
         {
        	 if (e instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException){
        		System.out.println("You have an error in your SQL syntax"); 
        	 }else{
        		 e.printStackTrace();
            	 System.err.println ("Cannot connect to database server");
        	 }
         }
         finally
         {
        	 if (con != null)
        	 {
        		 try
        		 {
        			 con.closeConnection();
        			 System.out.println ("Database connection terminated");
        		 }
        	 
        		 catch (Exception e) { /* ignore close errors */ }
        	 }	 
         }
	}
}