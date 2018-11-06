package ca.dal.acs.book.service;

import java.security.SecureRandom;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ca.dal.acs.book.service.exceptions.BookException;
import ca.dal.acs.book.service.exceptions.UserException;
import ca.dal.acs.book.service.model.Book;
import ca.dal.acs.book.service.model.User;
import ca.dal.acs.book.service.util.BookUtil;
import ca.dal.acs.book.service.util.LoginUtil;
import ca.dal.acs.book.service.util.UserUtil;
import ca.dal.acs.book.service.util.Utility;

@Path("/login")
public class Login {

	@Context
	ServletContext context;

	@POST
	@Path("/doLogin")
	// Produces JSON as response
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doLogin(@FormParam("lg_username") String uname,
			@FormParam("lg_password") String pwd) throws JSONException {
		JSONObject json = null;
		String response = "";
		System.out.println("checking login details");
		if (checkCredentials(uname, pwd)) {
			String token = generateToken();
			try {
				token = uname + ":" + token;
				LoginUtil.setUserToken(uname, token);
				json = Utility.constructTokenJSON("login", true);
				json.put("token", token);
				ArrayList<Book> allBooks = BookUtil.getAllBooksDetails();
				json.put("allBooks", allBooks);

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (BookException e) {
				// TODO Auto-generated catch block
				json.put("status", Constants.FAILURE);
				json.put("errorMsg", e.getMessage());
			}
		} else {
			response = Utility.constructJSON("login", false,
					"Incorrect Username or Password");
			return Response.ok(response, MediaType.APPLICATION_JSON).build();
		}
		return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
	}

	private String generateToken() {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[100];
		random.nextBytes(bytes);
		return bytes.toString();
	}

	private boolean checkCredentials(String uname, String pwd) {
		boolean result = false;
		if (Utility.isNotNull(uname) && Utility.isNotNull(pwd)) {
			try {
				result = LoginUtil.checkLogin(uname, pwd);
			} catch (Exception e) {
				result = false;
			}
		} else {
			result = false;
		}

		return result;
	}

}
