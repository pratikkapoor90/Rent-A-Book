package ca.dal.acs.book.service;

import java.sql.Timestamp;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ca.dal.acs.book.service.exceptions.BookException;
import ca.dal.acs.book.service.util.BookUtil;
import ca.dal.acs.book.service.util.LoginUtil;

@Path("/book")
public class UserReviews {
	

	@POST
	@Path("/addreview")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addReview(@FormParam("touser") String touser,
			@FormParam("feedback") String feedback,
			@FormParam("token") String token) throws JSONException {
		boolean status = false;
		JSONObject json = new JSONObject();
		try {

			String uname = token.split(":")[0];
			Timestamp timestampStart = new Timestamp(System.currentTimeMillis());
			if (LoginUtil.validateToken(uname, token)) {
				status = BookUtil.addReview(uname
						, touser, feedback, timestampStart.toString());
				if (status) {
					json.put("status", Constants.SUCCESS);
					json.put("msg", "Review is added successfully.");
				} else {
					json.put("status", Constants.FAILURE);
					json.put("errorMsg", Constants.UNEXPECTED_ERROR);
				}
			} else {
				json.put("status", Constants.FAILURE);
				json.put("errorMsg", "Session TimeOut! Please login again.");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BookException e) {
			// TODO Auto-generated catch block
			json.put("status", Constants.FAILURE);
			json.put("errorMsg", e.getMessage());
		}
		return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
	}	
	
	
	@GET
	@Path("/getreviews")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserReviews(@QueryParam("touser") String touser) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("userReviews", BookUtil.getUserReviews(touser));
		} catch (BookException e) {
			// TODO Auto-generated catch block
			json.put("status", Constants.FAILURE);
			json.put("errorMsg", e.getMessage());
		}
		return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/contactdetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContactDetails(@QueryParam("uname") String uname) throws Exception {
		JSONObject json = new JSONObject();
		try {
			json.put("contactdetails", BookUtil.getContactDetails(uname));
		} catch (BookException e) {
			// TODO Auto-generated catch block
			json.put("status", Constants.FAILURE);
			json.put("errorMsg", e.getMessage());
		}
		return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
	}

}
