package ca.dal.acs.book.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import ca.dal.acs.book.service.exceptions.BookException;
import ca.dal.acs.book.service.model.Book;
import ca.dal.acs.book.service.util.BookUtil;
import ca.dal.acs.book.service.util.LoginUtil;

@Path("/book")
public class GetBooks {

	@GET
	@Path("/getAllBooks")
	@Produces(MediaType.APPLICATION_JSON)
    public Response getAllAvailebleBooks(@Context HttpServletRequest req) throws JSONException {
		JSONObject json = new JSONObject();
        try {
        		ArrayList<Book> allBooks = BookUtil.getAllBooksDetails();
        		ArrayList<String> allBorrowedBooks = BookUtil.getAllBorrowedBooksDetails();
        		ArrayList<Book> updatedList =new ArrayList<>();
        		for(Book book: allBooks) {
        			if(!allBorrowedBooks.contains(book.getBookId())) {
        				updatedList.add(book);
        			}
        		}
        		json.put("allBooks",updatedList);
        } catch (BookException e) {
            // TODO Auto-generated catch block
            json.put("status", Constants.FAILURE);
            json.put("errorMsg", e.getMessage());
        }
        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }
	
	@GET
	@Path("/getUserBorrowedBooks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBorrowedBooks( @QueryParam("uname") String uname) throws JSONException {
        JSONObject json = new JSONObject();
         try {

          //  String uname = token.split(":")[0];
//            if (LoginUtil.validateToken(uname, token)) {
                    json.put("borrowedBooks", BookUtil.getUserBorrowedBooks(uname));

         //   }
////         else {
//                json.put("status", Constants.FAILURE);
//                json.put("errorMsg", "Session TimeOut! Please login again.");
//       //     }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BookException e) {
            // TODO Auto-generated catch block
            json.put("status",Constants.FAILURE);
            json.put("errorMsg", e.getMessage());
        }

        return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
    }

	
	@GET
  	@Path("/getUserUploadedBooks")
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response getUserUploadedBooks(@QueryParam("uname") String uname) throws JSONException {
        JSONObject json = new JSONObject();
  		try {
  				json.put("allBooks", BookUtil.getUserUploadedBooksDetails(uname));
  		} catch (BookException e) {
  			// TODO Auto-generated catch block
  			json.put("status", "failed");
  			json.put("errorMsg", e.getMessage());
  		}
  		return Response.ok(json.toString(), MediaType.APPLICATION_JSON).build();
  	}

	
}