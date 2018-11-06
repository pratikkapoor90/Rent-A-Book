package ca.dal.acs.book.service.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.jcraft.jsch.JSchException;

import ca.dal.acs.book.service.exceptions.BookException;
import ca.dal.acs.book.service.exceptions.DBConnectionException;
import ca.dal.acs.book.service.exceptions.UserException;
import ca.dal.acs.book.service.model.User;

public class UserUtil {
	private static Connection connection = null;

	public static String getPassword(String username) throws UserException {
		String pwd = null;
		Statement stmt;
		ResultSet rs = null;
		try {
			checkConnection();
			stmt = connection.createStatement();
			String query = "SELECT * FROM logins WHERE email = '" + username
					+ "'";
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				pwd = rs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new UserException("Error fetching user details!");
		} 
		catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(
					"Error fetching user details! Unable to connect to database.");
		} 
		finally {
			try {
				rs.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return pwd;
	}

	public static boolean addUser(String email, String password)
			throws UserException {
		boolean insertStatus = false;
		try {
			checkConnection();
			Statement stmt = connection.createStatement();
			String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
			String query = "INSERT into logins(email, password) values('"
					+ email + "','" + hash + "')";
			// System.out.println(query);
			int records = stmt.executeUpdate(query);
			// System.out.println(records);
			// When record is successfully inserted
			if (records > 0) {
				insertStatus = true;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
			if (sqle.getErrorCode() == 1062)
				throw new UserException(
						"Username is already taken. Please give another one.");
			else
				throw new UserException(
						"Error occured while creating user login. Pleae try again later.");
		}
		catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(
					"Error creating user details! Unable to connect to database.");
		} 
		finally {
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

	public static boolean addUserProfile(String email, String name,
			String number) throws UserException {
		boolean insertStatus = false;
		try {
			checkConnection();
			Statement stmt = connection.createStatement();
			String query = "INSERT into user_profile(email, name,  number) values('"
					+ email + "','" + name + "','" + number + "')";
			int records = stmt.executeUpdate(query);
			// System.out.println(records);
			// When record is successfully inserted
			if (records > 0) {
				insertStatus = true;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle);
			throw new UserException(
					"Error in creting user profile. Please update profile later.");
		} 
			catch (DBConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new UserException(
					"Error in creting user profile. Please update profile later.");
		} 
			finally {
			// TODO Auto-generated catch block
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Problem in closing database connection");
			}
		}

		return insertStatus;
	}

	private static void checkConnection() throws DBConnectionException {
		// TODO Auto-generated method stub
		try {
			if (connection == null)
			{
				connection = ConnectionManager.getConnection();
		}
			else if(connection.isClosed()) {
				connection = ConnectionManager.getConnection();
			}
		}
			catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("error connecting to database");
			throw new DBConnectionException("Error Connecting to database: " + e.getMessage());
	
		} 
	}

}
