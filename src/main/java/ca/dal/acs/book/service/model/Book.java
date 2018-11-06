package ca.dal.acs.book.service.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Book {
	private String bookId;
	private String title;
	private String author;
	private float price;
	private int period;
	private String category;
	private String uname;
	private String description;

	public Book(String bookId, String title, String author,
			float price, int period,
			String category,String uname, String description) {
		this.bookId = bookId;
		this.title = title;
		this.author = author;
		this.price = price;
		this.period = period;
		this.category = category;
		this.uname = uname;
		this.description = description;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}


	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String uname) {
		this.category = category;
	}
	
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson.toJson(this);
	}

}
