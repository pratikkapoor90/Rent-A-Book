package ca.dal.acs.book.service.security;

import java.util.HashMap;

public class TokenMap {
	private static TokenMap instance = new TokenMap();
	private HashMap<String, String> map;

	private TokenMap() {
		// TODO Auto-generated constructor stub
		map = new HashMap<>();
	}

	public static TokenMap getInstance() {
		return instance;
	}

	public void setToken(String username, String token) {
		map.put(username, token);
	}

	public boolean match(String username, String token) {
		// TODO Auto-generated method stub
		if (map.get(username) != null)
			return map.get(username).equals(token);
		else
			return false;

	}
}
