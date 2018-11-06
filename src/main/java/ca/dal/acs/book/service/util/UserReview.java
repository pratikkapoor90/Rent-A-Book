package ca.dal.acs.book.service.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserReview {

	private String fromuser;
	private String touser;
	private String feedback;
	private String date;
	
	
	public UserReview(String fromuser, String touser, String feedback, String date) {
		super();
		this.fromuser = fromuser;
		this.touser = touser;
		this.feedback = feedback;
		this.date = date;
	}
	public String getFromuser() {
		return fromuser;
	}
	public void setFromuser(String fromuser) {
		this.fromuser = fromuser;
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}
	
}
