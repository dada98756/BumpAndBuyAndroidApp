package com.pennapps.bumpandbuy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;


public class Network {
	public static void executeRequest(Context context, AsyncTask task, Server.Method method, Object ... args) throws InstantiationException, IllegalAccessException{
		ConnectivityManager connMgr = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
 	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
 	        if (networkInfo != null && networkInfo.isConnected()) {
 	        	//context.getS
 	        	if(args.length>0)
 	        		task.execute(args);
 	        	else
 	        		task.execute();
 	        } else {
 	            //textView.setText("No network connection available.");
 	        }
	}
}
