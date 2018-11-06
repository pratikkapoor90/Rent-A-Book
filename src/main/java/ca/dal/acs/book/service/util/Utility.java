package ca.dal.acs.book.service.util;

import java.util.ArrayList;

import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utility {

	private static GsonBuilder gsonBuilder = new GsonBuilder();
	public static Gson gson = gsonBuilder.create();

	public static boolean isNotNull(String txt) {
		// System.out.println("Inside isNotNull");
		return txt != null && txt.trim().length() >= 0 ? true : false;
	}

	public static JSONObject constructTokenJSON(String tag, boolean status) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(tag, new Boolean(status));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return obj;
	}

	public static String constructJSON(String tag, boolean status,
			String err_msg) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(tag, new Boolean(status));
			obj.put("error_msg", err_msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return obj.toString();
	}

	public static String constructJSON(String tag, boolean status) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(tag, new Boolean(status));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return obj.toString();
	}

	public static String constructJSON(String name, ArrayList<String> list) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(name, list);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return obj.toString();
	}

	public static String getRandomNumber() {
		return String.format("%04d", new Random().nextInt(10000));
	}

}
