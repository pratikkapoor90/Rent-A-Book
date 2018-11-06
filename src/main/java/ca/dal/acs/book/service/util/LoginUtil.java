package ca.dal.acs.book.service.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

import ca.dal.acs.book.service.exceptions.UserException;
import ca.dal.acs.book.service.security.TokenMap;

public class LoginUtil {

	public static boolean checkLogin(String username, String password)
			throws UserException {
		return BCrypt.checkpw(password, UserUtil.getPassword(username));
	}

	public static boolean validateToken(String username, String token) {
		return TokenMap.getInstance().match(username, token);
	}

	public static void setUserToken(String uname, String token) {
		// TODO Auto-generated method stub
		TokenMap.getInstance().setToken(uname, token);
	}

}
