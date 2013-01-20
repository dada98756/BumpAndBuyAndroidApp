package com.pennapps.bumpandbuy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Server {

	public static enum Method {
		POST, GET
	};

	final public static String SERVER_URL = "http://158.130.110.227:8080";


	public JSONObject post(String endpoint, Map<String, String> params)
			throws IOException, URISyntaxException, IllegalStateException, JSONException {
		return printToErr(send(endpoint, params, Method.POST));
	}

	public JSONObject get(String endpoint, Map<String, String> params)
			throws IOException, URISyntaxException, IllegalStateException, JSONException {
		return printToErr(send(endpoint, params, Method.GET));
	}

	private HttpResponse send(String endpoint,
			Map<String, String> params, Method method) throws IOException,
			URISyntaxException {
		URL url;
		try {
			url = new URL(SERVER_URL + endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + SERVER_URL
					+ endpoint);
		}

		HttpResponse res = null;
		HttpClient client = new DefaultHttpClient();
		if (method == Method.GET) {
			HttpGet get = new HttpGet();
			if (params != null) {
				StringBuilder bodyBuilder = new StringBuilder();
				Iterator<Entry<String, String>> iterator = params.entrySet()
						.iterator();
				// constructs the POST body using the parameters
				while (iterator.hasNext()) {
					Entry<String, String> param = iterator.next();
					bodyBuilder.append(param.getKey()).append('=')
							.append(param.getValue());
					if (iterator.hasNext()) {
						bodyBuilder.append('&');
					}
				}
				System.err.println(SERVER_URL + endpoint + "?"
						+ bodyBuilder.toString());
				get.setURI(new URI(SERVER_URL + endpoint + "?"
						+ bodyBuilder.toString()));
			}
			res = client.execute(get);
		} else if (method == Method.POST) {
			HttpPost post = new HttpPost();
			post.setURI(new URI(SERVER_URL + endpoint));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (params != null) {
				Iterator<Entry<String, String>> iterator = params.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<String, String> param = iterator.next();
					nameValuePairs.add(new BasicNameValuePair(param.getKey(),
							param.getValue()));
				}

				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}
			res = client.execute(post);
		}
		return res;
	}

	private JSONObject printToErr(HttpResponse res)
			throws IllegalStateException, IOException, JSONException {
		if (res != null) {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					res.getEntity().getContent()));
			System.err.println("length:" + res.getEntity().getContentLength());
//			String newline = null;
//			StringBuilder builder = new StringBuilder();
//			while ((newline = reader.readLine()) != null) {
//				System.err.println(newline);
//				builder.append(newline).append("\n");
//			}
//			JSONTokener tokener = new JSONTokener(builder.toString());
			JSONObject finalResult = new StreamToJSON(res.getEntity().getContent()).getJSONObject();
			JSONArray names=finalResult.names();
			for(int i=0;i<names.length();i++){
				System.err.println(names.getString(i)+" : "+finalResult.get(names.getString(i)));
			}
			return finalResult;
		} else {
			System.err.println("response is null");
			return null;
		}
	}
}
