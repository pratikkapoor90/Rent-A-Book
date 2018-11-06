package ca.dal.acs.book.service;

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

import ca.dal.acs.book.service.exceptions.UserException;
import ca.dal.acs.book.service.util.UserUtil;
import ca.dal.acs.book.service.util.Utility;

@Path("/user")
public class Register {

	@Context
	private ServletContext context;

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response userRegisterService(@FormParam("reg_email") String email,
			@FormParam("reg_password") String pwd,
			@FormParam("reg_fullname") String name,
			@FormParam("reg_number") String number) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			createUserLogin(email, pwd);
			createUserProfile(email, name, number);
			json.put("status", Constants.SUCCESS);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			json.put("status", Constants.FAILURE);
			json.put("errorMsg", e.getMessage());
		}
		return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
	}

	private void createUserLogin(String email, String pwd) throws UserException {
		if (Utility.isNotNull(email) && Utility.isNotNull(pwd)) {
			UserUtil.addUser(email, pwd);
		}
	}

	private void createUserProfile(String email, String name, String number)
			throws UserException {
		if (Utility.isNotNull(email) && Utility.isNotNull(name)
				&& Utility.isNotNull(email) && Utility.isNotNull(number)) {
			UserUtil.addUserProfile(email, name, number);
		}
	}

}
