package com.pennapps.bumpandbuy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class StreamToJSON {
	private JSONTokener tokener; 
	public StreamToJSON(InputStream stream) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				stream));
		String newline = null;
		StringBuilder builder = new StringBuilder();
		while ((newline = reader.readLine()) != null) {
			System.err.println(newline);
			builder.append(newline).append("\n");
		}
		tokener = new JSONTokener(builder.toString());
	}
	
	public JSONArray getJSONArray() throws JSONException{
		return new JSONArray(tokener);
	}
	
	public JSONObject getJSONObject() throws JSONException{
		return new JSONObject(tokener);
	}
}
