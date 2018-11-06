package ca.dal.acs.book.service.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ca.dal.acs.book.service.exceptions.BookException;
import ca.dal.acs.book.service.exceptions.DBConnectionException;
import ca.dal.acs.book.service.model.Book;
import ca.dal.acs.book.service.model.User;

public class BookUtil {
	private static Connection connection;

	public BookUtil() throws DBConnectionException {
		// TODO Auto-generated constructor stub
		connection = ConnectionManager.getConnection();
	}

	private static void checkConnection() throws DBConnectionException {
		// TODO Auto-generated method stub
		try {
			
			if (connection == null || connection.isClosed())
			{
				connection = ConnectionManager.getConnection();
		} 
		}
			catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DBConnectionException("Error Connecting to database: " + e.getMessage());
		} 
	}

	/**
	 * @param bookId
	 * @return the details of the book specified by bookId
	 * @throws BookException
	 */
	public static Book getBookDetails(String bookId) throws BookException {
		// TODO Auto-generated method stub
		Book bookDetails = null;
		try {
			checkConnection();
			Statement statement = connection.createStatement();
			String query = "select * from books where book_id = '" + bookId + "';";
			ResultSet rs = statement.executeQuery(query);
			if (rs.next())
				bookDetails = new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5),
						rs.getString(6),rs.getString(7),rs.getString(8));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading book details.");
		}
		 catch (DBConnectionException e) {
		 // TODO Auto-generated catch block
		 throw new BookException("Error reading book details: "
		 + e.getMessage());
		 }
		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return bookDetails;
	}

	/**
	 * @return details of all the books available
	 * @throws BookException
	 */
	public static ArrayList<Book> getAllBooksDetails() throws BookException {
		// TODO Auto-generated method stub
		ArrayList<Book> booksDetails = new ArrayList<>();
		try {
			checkConnection();
			Statement statement = connection.createStatement();
			String query = "select * from books;";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
				booksDetails.add(new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4),
						rs.getInt(5), rs.getString(6),rs.getString(7),rs.getString(8)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading books details.");
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading books details: " + e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return booksDetails;
	}

	/**
	 * @param bookId
	 * @param title
	 * @param authors
	 * @param publisher
	 * @param book_price
	 * @param book_quantity
	 * @param availability
	 * @param book_period
	 * @param category
	 * @param description
	 * @param uname 
	 * @return true if book is added successfully otherwise false
	 * @throws BookException
	 */
	public static boolean addBook(String bookId, String title, String authors, float book_price, int duration,
			String category, String uname, String description) throws BookException {
		// TODO Auto-generated method stub
		boolean insertStatus = false;
		try {
			checkConnection();
			Statement stmt = connection.createStatement();
			String query = "INSERT into books(book_id,title,authors,price,uname,duration,category, description) values('" + bookId + "','"
					+ title + "','" + authors + "'," + book_price + ",'" + uname  + "'," + duration + ",'" + category + "','" + description + "')";

			int records = stmt.executeUpdate(query);
			if (records > 0) {
				insertStatus = true;
			}
		}
		 catch (DBConnectionException e) {
		 // TODO Auto-generated catch block
		 throw new BookException("Error adding book details: "
		 + e.getMessage());
		 }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BookException("Error adding book details.");
		} finally {
			// TODO Auto-generated catch block
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Problem in closing database connection");
			}
		}

		return insertStatus;
	}
	
	/**
	 * @return details of all the books available
	 * @throws BookException
	 */
	public static ArrayList<Book> getUserUploadedBooksDetails(String uname) throws BookException {
		// TODO Auto-generated method stub
		ArrayList<Book> booksDetails = new ArrayList<>();
		try {
			checkConnection();
			Statement statement = connection.createStatement();
			String query = "select * from books where uname = '"+uname+"';";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
				booksDetails.add(new Book(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4),
						rs.getInt(5), rs.getString(6),rs.getString(7),rs.getString(8)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading books details.");
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading books details: " + e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return booksDetails;
	}

	public static ArrayList<Book> getUserBorrowedBooks(String uname) throws BookException {
		// TODO Auto-generated method stub
		ArrayList<Book> booksDetails = new ArrayList<>();
		try {
			StringBuilder str = new StringBuilder();
			checkConnection();
			Statement statement = connection.createStatement();
			String query = "select * from books_borrow where uname = '"+uname+"';";
			ResultSet rs = statement.executeQuery(query);
			List<String> bookIdList = new ArrayList<>();
			while (rs.next())
				bookIdList.add(rs.getString(2));
			
			int size = bookIdList.size();
			if(size>0) {
				str.append("'"+bookIdList.get(0)+"'");
			for(int i=1;i<size;i++) {
				str.append(",");
				str.append("'"+bookIdList.get(i)+"'");
			}
			Statement statement1 = connection.createStatement();
			String query1 = "select * from books where book_id in ("+str+");";
			ResultSet rs1 = statement1.executeQuery(query1);
			while(rs1.next())
				booksDetails.add(new Book(rs1.getString(1), rs1.getString(2), rs1.getString(3), rs1.getFloat(4),
						rs1.getInt(5), rs1.getString(6),rs1.getString(7),rs1.getString(8)));
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading books details.");
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading books details: " + e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return booksDetails;
	}

	public static boolean paymentSubmit(String bookid, String uname)  throws BookException {
		// TODO Auto-generated method stub
		boolean insertStatus = false;
		try {
			checkConnection();
			Statement stmt = connection.createStatement();
			String query = "INSERT into books_borrow(book_id,uname) values('" + bookid + "','"
					+ uname +"'"+ ");";

			int records = stmt.executeUpdate(query);
			if (records > 0) {
				insertStatus = true;
			}
		}
		 catch (DBConnectionException e) {
		 // TODO Auto-generated catch block
		 throw new BookException("Error adding book details: "
		 + e.getMessage());
		 }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BookException("Error adding book details.");
		} finally {
			// TODO Auto-generated catch block
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Problem in closing database connection");
			}
		}

		return insertStatus;
	}

	public static boolean addReview(String uname, String touser, String feedback, String date) throws BookException {
		// TODO Auto-generated method stub
		boolean insertStatus = false;
		try {
			checkConnection();
			Statement stmt = connection.createStatement();
			String query = "INSERT into user_reviews(fromuser,touser,feedback,date) values('" + uname + "','"
					+ touser + "','" + feedback + "','" + date+"'"+ ");";

			int records = stmt.executeUpdate(query);
			if (records > 0) {
				insertStatus = true;
			}
		}
		 catch (DBConnectionException e) {
		 // TODO Auto-generated catch block
		 throw new BookException("Error adding book details: "
		 + e.getMessage());
		 }
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BookException("Error adding book details.");
		} finally {
			// TODO Auto-generated catch block
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Problem in closing database connection");
			}
		}

		return insertStatus;
	}

	public static ArrayList<UserReview> getUserReviews(String touser) throws BookException {
		// TODO Auto-generated method stub
		ArrayList<UserReview> userReviewDetails = new ArrayList<>();
		try {
			checkConnection();
			Statement statement = connection.createStatement();
			String query = "select * from user_reviews where touser = '"+touser+"';";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
				userReviewDetails.add(new UserReview(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading user review details.");
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading user review details: " + e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return userReviewDetails;
	}

	public static ArrayList<String> getAllBorrowedBooksDetails() {
		// TODO Auto-generated method stub
		ArrayList<String> bookIdList = new ArrayList<>();
		try {
			checkConnection();
			Statement statement;
			
				statement = connection.createStatement();
			
			String query = "select * from books_borrow;";
			ResultSet rs = statement.executeQuery(query);
			
			while (rs.next())
				bookIdList.add(rs.getString(2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally {
				try {
					if (connection != null)
						connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
				}
			}
		return bookIdList;
	}

	public static List<User> getContactDetails(String uname) throws Exception {
		List<User> userList = new ArrayList<>();
		// TODO Auto-generated method stub
		try {
			checkConnection();
			Statement statement = connection.createStatement();
			String query = "select * from user_profile where email = '"+uname+"';";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
				userList.add(new User(null, rs.getString(2), rs.getString(1),null,rs.getString(3)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading user details.");
		} catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			throw new BookException("Error reading user details: " + e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
		return userList;
	}
}
