package ca.dal.acs.book.service.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class User {
	private String username;
	private String name;
	private String email;
	private String gender;
	private String phone;
	public User(String username, String name, String email, String gender,String phone) {
		this.username = username;
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}

}
