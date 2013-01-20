package com.pennapps.bumpandbuy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	public static HashMap<String,String> userAccount;
	private EditText user;
	private EditText pwd;
	private Context myContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		user=(EditText) findViewById(R.id.editText1);
		pwd= (EditText) findViewById(R.id.passwordText);
		myContext=this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	public void onEnterButtonClick(View view){
		Toast.makeText(getApplicationContext(),
				"GO!",
				Toast.LENGTH_LONG).show();
		try {
			String email=user.getText().toString().trim();
			String password=pwd.getText().toString();
			String fakeToken="123";
			Map<String, String> map=new HashMap<String, String>();
			map.put("email", email);
			map.put("pwd", password);
			map.put("access_token",fakeToken);
			Network.executeRequest(this, new LoginTask(), Server.Method.POST, map);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class LoginTask extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			try {
				Server server = new Server();
				JSONObject obj = server.post("/user", (Map)params[0]);
				userAccount = JSONConverter.JSONObjectToMap(obj);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return params[0];
		}
		protected void onPostExecute(Object result){
			Intent myIntent = new Intent(myContext,FeedListActivity.class);
			startActivityForResult(myIntent, 0);
		}
		
	}
	
	
	public void onGetOutButtonClick(View view){
		Toast.makeText(getApplicationContext(),
				"Bump You!",
				Toast.LENGTH_LONG).show();
	}
}
