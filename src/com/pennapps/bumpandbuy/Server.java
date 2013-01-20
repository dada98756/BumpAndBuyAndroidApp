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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class Server {

	public static enum Method {
		POST, GET
	};

	final public static String SERVER_URL = "http://158.130.110.227:8080";

	final private static int POST = 0;

	public static void post(String endpoint, Map<String, String> params)
			throws IOException, URISyntaxException {
		printToErr(send(endpoint, params, Method.POST));
	}

	public static void get(String endpoint, Map<String, String> params)
			throws IOException, URISyntaxException {
		printToErr(send(endpoint, params, Method.GET));
	}

	private static HttpResponse send(String endpoint,
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

	private static void printToErr(HttpResponse res)
			throws IllegalStateException, IOException {
		if (res != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					res.getEntity().getContent()));
			Log.d("content", reader.readLine());
			System.err.println("length:" + res.getEntity().getContentLength());
			String newline = null;
			while ((newline = reader.readLine()) != null) {
				System.err.println(newline);
			}
		} else {
			System.err.println("response is null");
		}
	}
}
