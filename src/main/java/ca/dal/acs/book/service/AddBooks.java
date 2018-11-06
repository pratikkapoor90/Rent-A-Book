package ca.dal.acs.book.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
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
import ca.dal.acs.book.service.util.BookUtil;
import ca.dal.acs.book.service.util.LoginUtil;

@Path("/book")
public class AddBooks {
	
	@Context
	ServletContext context;

	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response bookAddService(@FormParam("title") String title,
			@FormParam("authors") String authors,
			@FormParam("price") String price,
			@FormParam("period") String period,
			@FormParam("category") String category,
			@FormParam("description") String description,
			@FormParam("cover_page") String encodedCoverPage,
			@FormParam("token") String token) throws JSONException {
		boolean status = false;
		JSONObject json = new JSONObject();
		String bookId = generateBookID();
		try {

			String uname = token.split(":")[0];
			if (LoginUtil.validateToken(uname, token)) {
				float book_price = Float.parseFloat(price);
				int duration = Integer.parseInt(period);
				status = BookUtil.addBook(bookId, title, authors, book_price,
						duration, category,uname, description);
				boolean coverPageStatus = storeCoverImage(context,
						encodedCoverPage, bookId + ".jpg");
				if (status && coverPageStatus) {
					json.put("status", Constants.SUCCESS);
					json.put("msg", "Book is added successfully.");
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
	private String generateBookID() {
		// TODO Auto-generated method stub
		return "book" + (int) (1 + Math.random() * 10000);
	}

	private boolean storeCoverImage(ServletContext context, String encoded,
			String fileName) {
		String folderName = "cover_pages";
		String basePath = context.getRealPath(folderName);
		System.out.println(basePath);
		String imagePath = "";
		if (basePath.endsWith("/cover_pages")) { // Linux based systems
			imagePath = basePath + "/" + fileName;
		} else { // Windows
			imagePath = basePath + "\\" + fileName;
		}
		System.out.println(imagePath);
		byte[] fileBytes = Base64.getDecoder().decode(encoded.getBytes());
		ByteArrayInputStream bis = new ByteArrayInputStream(fileBytes);
		BufferedImage image;
		try {
			image = ImageIO.read(bis);

			bis.close();
			File outputFile = new File(imagePath);
			if (!outputFile.isFile()) {
				outputFile.getParentFile().mkdirs();
				outputFile.createNewFile();
			}

			ImageIO.write(image, "jpg", outputFile);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	
	@POST
	@Path("/payment")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response paymentService(@FormParam("book_id") String bookid,
			@FormParam("token") String token) throws JSONException {
		boolean status = false;
		JSONObject json = new JSONObject();
		try {
			String uname = token.split(":")[0];
			if (LoginUtil.validateToken(uname, token)) {
				status = BookUtil.paymentSubmit(bookid,uname);
				if (status) {
					json.put("status", Constants.SUCCESS);
					json.put("msg", "Payment is done successfully.");
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

	
	

}
