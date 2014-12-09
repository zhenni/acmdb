README:

### File:

Our ER-diagram and schema is in doc/doc.pdf

### Compile:

1. javac acmdb/*.java
2. In windows system: java -cp "./mysql.jar;." acmdb.Driver
   In Linux/Unix system: java -cp ./mysql.jar:. acmdb.Driver
3. Or you can use an IDE (such as Ecilipse). Remeber to add mysql.jar as an external jar to your project's Java build/library path.
4. When you run the program, you may have to login with the root authority to do something, such that add a new book, add some new copies. The login_name and the password is shown in the followings.

### Admin Login:
	login_name : admin
	password   : admin

### Assumption:

1.	The name of author and publisher is unique, since there are no other information about them. 
	Since there will be some duplicate columns((author_id, name) (publisher_id, publiser)) if we use the schema in the doc.pdf, we modified the tables to have no duplicate columns.

2.	If the book have no feedback (with a score) then it will not displayed when user choose the second and third choice of "Book Browsing".

3.	If the books of the author are ordered by no one, then the author will not appears in the most popular author list.

4.	The user can change the trust(untrust) if he/she have already trust(untrust) someone.

### Author:

	-- Wei Zhen
	-- Sheng Ying