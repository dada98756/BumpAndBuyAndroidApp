package com.pennapps.bumpandbuy;

import java.io.IOException;
import java.net.MalformedURLException;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class Server {
	
	private static enum Method {POST,GET};
	
	final public static String SERVER_URL="";
	
	final private static int POST = 0;
	

	private static void post(String endpoint, Map<String, String> params)
            throws IOException {
		send(endpoint,params,Method.POST);
	}
	private static void get(String endpoint, Map<String, String> params) 
			throws IOException {
		send(endpoint,params,Method.GET);
	}
	private static HttpResponse send(String endpoint, Map<String, String> params, Method method)
			throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
       
        HttpResponse res=null;
        HttpClient client= new DefaultHttpClient();
        if(method==Method.GET){
        	HttpGet get= new HttpGet();
        	HttpParams httpParams=new BasicHttpParams();
            Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
            	Entry<String, String> param = iterator.next();
            	httpParams.setParameter(param.getKey(), param.getValue());
            }
        	get.setParams(httpParams);
        	res=client.execute(get);
        }
        else if (method==Method.POST){
        	HttpPost post= new HttpPost();
        	List<NameValuePair> nameValuePairs= new ArrayList<NameValuePair>();
        	Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
            	Entry<String, String> param = iterator.next();
            	nameValuePairs.add(new BasicNameValuePair(param.getKey(),
            			param.getValue()));
            }
        	post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	res=client.execute(post);
        }
        return res;
      }
}
